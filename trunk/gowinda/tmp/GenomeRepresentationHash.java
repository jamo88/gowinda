package gowinda.tmp;

import java.util.logging.Logger;
import java.util.*;

import gowinda.analysis.AnnotationEntry;
import gowinda.analysis.IGenomeRepresentation;
import gowinda.analysis.Snp;

import gowinda.io.IBulkAnnotationReader;

/*
 * Very fast access to SNP information but may require a lot of memory
 */
public class GenomeRepresentationHash implements IGenomeRepresentation {
	
	private Logger logger;

	private HashMap<String,HashMap<Integer,GeneidSet>> genRep;
	private ArrayList<String> geneids;
	
	public GenomeRepresentationHash(IBulkAnnotationReader reader, Logger logger)
	{
		
		this.logger=logger;
		ArrayList<AnnotationEntry> entries=reader.readAnnotation();
		setGeneids(entries);
		setGenomeHash(entries);
	}
	
	public GenomeRepresentationHash(ArrayList<AnnotationEntry> entries)
	{
		// More for debugging
		this.logger=java.util.logging.Logger.getLogger("Gowinda Logger");
		logger.setUseParentHandlers(false);
		setGeneids(entries);
		setGenomeHash(entries);
	}
	
	private void setGeneids(ArrayList<AnnotationEntry> entries)
	{
		geneids=new ArrayList<String>();
		for(AnnotationEntry e:entries)
		{
			geneids.add(e.geneid());
		}
	}
	
	private void setGenomeHash(ArrayList<AnnotationEntry> entries)
	{
		logger.info("Building a hash based representation of the reference genome");
		genRep=new HashMap<String,HashMap<Integer,GeneidSet>>();	
		for(AnnotationEntry e: entries)
		{
			if(!genRep.containsKey(e.chromosome())) genRep.put(e.chromosome(), new HashMap<Integer,GeneidSet>());
			HashMap<Integer,GeneidSet> temph=genRep.get(e.chromosome());
			for(int i=e.start(); i<=e.end();i++)
			{
				ArrayList<String> currentGenes=new ArrayList<String>();
				// First add the geneid of this exon
				currentGenes.add(e.geneid());
				if(temph.containsKey(i))
				{
					// if already other exons are present update the set
					currentGenes.addAll(temph.get(i).getGenelist());
				}
				temph.put(i,GeneidSet.getGeneidSet(currentGenes));
			}
		}
		logger.fine("Count novel key returned: "+GeneidSet.countKeyNovel());
		logger.fine("Count existing key returned: "+ GeneidSet.countKeyContained());
		logger.fine("Final cache size: "+ GeneidSet.size());
		logger.fine("Final size of genome hash: "+this.entryCount());
		logger.info("Finished building representation of the reference genome");
	}
	
	@Override
	public ArrayList<String> getGeneidsForSnps(ArrayList<Snp> snps)
	{
		ArrayList<String> toret=new ArrayList<String>();
		for(Snp s: snps)
		{
			toret.addAll(this.getGeneidsForSnp(s));
		}
		return toret;
	}
	
	@Override
	public ArrayList<String> getGeneidsForSnp(Snp snp)
	{
		if(!genRep.containsKey(snp.chromosome())) return new ArrayList<String>();
		if(!genRep.get(snp.chromosome()).containsKey(snp.position())) return new ArrayList<String>();
		ArrayList<String> toret= genRep.get(snp.chromosome()).get(snp.position()).getGenelist();
		assert(toret !=null);
		return toret;
	}
	
	@Override
	public ArrayList<String> allGeneids()
	{
		return new ArrayList<String>(new HashSet<String>(this.geneids));
	}
	
	public int entryCount()
	{
		
		int count=0;
		for(HashMap<Integer,GeneidSet> tmp: genRep.values())
		{
			count+=tmp.values().size();
		}
		return count;
	}
}




/*
 * Represents a set of GeneIDs,
 * Performs a buffering of the geneids
 */
class GeneidSet
{
	private ArrayList<String> geneidlist;
	
	// instances can only be obtained with the static factory method, which performs a caching
	private GeneidSet(ArrayList<String> geneNames)
	{

		HashSet<String> hs=new HashSet<String>(geneNames);
		geneidlist=new ArrayList<String>(hs);
		Collections.sort(geneidlist);
	}
	
	
	
	@Override
	public boolean equals(Object o)
	{
        if(!(o instanceof GeneidSet)){return false;}
        GeneidSet tc=(GeneidSet)o;
        if(tc.geneidlist.size()!=this.geneidlist.size()) return false;
        
        for(int i=0; i<this.geneidlist.size(); i++)
        {
        	String thise=this.geneidlist.get(i);
        	String thate=tc.geneidlist.get(i);
        	if(!thise.equals(thate)) return false;
        }
        return true;
	}
	
	@Override
	public int hashCode(){
		int toret=17;
		for(String s: geneidlist)
		{
			toret=31*toret + s.hashCode();
		}
		return toret;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getGenelist()
	{
		return (ArrayList<String>)this.geneidlist.clone();
	}
	
	
	
	// Caching with a static constructor
	private static HashMap<GeneidSet,GeneidSet> geneids=new HashMap<GeneidSet,GeneidSet>();
	//Cache of Geneidsets - avoids excessive memory usage by returning the same reference for an equal immutable element
	public static GeneidSet getGeneidSet(ArrayList<String> geneNames)
	{
		GeneidSet novel=new GeneidSet(geneNames);
		if(geneids.containsKey(novel))
		{
			contained++;
			return geneids.get(novel);

		}
		else
		{
			novelc++;
			geneids.put(novel, novel);
			return novel;
		}
	}
	
	public static int countKeyContained()
	{
		return contained;
	}
	public static int countKeyNovel()
	{
		return novelc;
	}
	public static int size()
	{
		return geneids.size();
	}
	
	private static int contained=0;
	private static int novelc=0;
	
	
	
	
	
	
	
}

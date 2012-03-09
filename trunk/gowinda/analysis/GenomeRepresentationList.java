package gowinda.analysis;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;
import gowinda.analysis.AnnotationEntry;

/**
 * Class allowing for retrieval of the genes in which a certain SNP is located.
 * Flexible with respect to 'gene' definition
 * Memory friendly implementation, but CPU demanding
 */
public class GenomeRepresentationList implements IGenomeRepresentation {
	private   Logger logger;
	private   ArrayList<AnnotationEntry> allentries;
	private   HashMap<String,ArrayList<AnnotationEntry>> genrep;
	private   HashMap<String,Integer> longestAnnotation;
	private   ArrayList<String> geneids;
	
	public static class AnnotationStartComparator implements Comparator<AnnotationEntry>
	{
		@Override
		public int compare(AnnotationEntry e1,AnnotationEntry e2)
		{
			if(e1.start() < e2.start()) return -1;
			if(e1.start() > e2.start()) return 1;
			return 0;
		}
	}
	
	public static AnnotationStartComparator comp=new AnnotationStartComparator();
	
	public GenomeRepresentationList(ArrayList<AnnotationEntry> entries, Logger logger)
	{
		this.logger=logger;
		this.allentries=entries;
		setGeneids(entries);
		setGenomeRep(entries);
	}
	public GenomeRepresentationList(ArrayList<AnnotationEntry> entries)
	{
		this.logger=java.util.logging.Logger.getLogger("Gowinda Logger");
		logger.setUseParentHandlers(false);
		this.allentries=entries;
		setGeneids(entries);
		setGenomeRep(entries);
	}
	
	private void setGeneids(ArrayList<AnnotationEntry> entries)
	{
		geneids=new ArrayList<String>();
		for(AnnotationEntry e:entries)
		{
			geneids.add(e.geneid());
		}
	}
	
	private void setGenomeRep(ArrayList<AnnotationEntry> entries)
	{


		genrep=new HashMap<String,ArrayList<AnnotationEntry>>();
		for(AnnotationEntry e: entries)
		{
			if(!genrep.containsKey(e.chromosome())) genrep.put(e.chromosome(), new ArrayList<AnnotationEntry>());
			genrep.get(e.chromosome()).add(e);
		}
		
		//
		longestAnnotation=new HashMap<String,Integer>();
		for(Entry<String, ArrayList<AnnotationEntry>> entry: genrep.entrySet())
		{
			int longest=getLongest(entry.getValue());
			longestAnnotation.put(entry.getKey(), longest);
			Collections.sort(entry.getValue(),comp);
		}
		
	}
	private int getLongest(ArrayList<AnnotationEntry> entries)
	{
		int longest=-1;
		for(AnnotationEntry e : entries)
		{
			int eleng=e.end()-e.start()+1;
			assert(eleng > 0);
			if(longest==-1)longest=eleng;
			if(eleng>longest)longest=eleng;
		}
		return longest;
	}



	/**
	 * Returns a unique collection of the Gene IDs present in the genome representation
	 */
	@Override
	public ArrayList<String> allGeneids()
	{
		return new ArrayList<String>(new HashSet<String>(this.geneids));
	}
	
	
	/**
	 * Returns the genes overlapping with the given SNPs. 
	 * A gene_id may be present several times if several SNPs are overlapping with the given gene
	 */
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
	public IGenomeRepresentation filterForGOTerms(GOCategoryContainer gocatcont)
	{
		ArrayList<AnnotationEntry> goOverlap=new ArrayList<AnnotationEntry>();
		this.logger.info("Starting to filter the annotation for entries that have an associated gene set (e.g.: GO category); Feature count "+ this.allentries.size());
		for(AnnotationEntry e : this.allentries)
		{
			if(gocatcont.contains(e.geneid()))
			{
				goOverlap.add(e);
			}
		}
		this.logger.info("Finished filtering for features with an associated gene set; Final feature count: "+ goOverlap.size());
		return new GenomeRepresentationList(goOverlap,this.logger);
	}
	
	/**
	 * Returns the genes overlapping with the given SNP
	 */
	@Override
	public ArrayList<String> getGeneidsForSnp(Snp s)
	{
		
		
		if(!genrep.containsKey(s.chromosome()))return new ArrayList<String>();
		assert(longestAnnotation.containsKey(s.chromosome()));
		
		// position-length+1 would actually be enough (position-length=conservative)
		int toSearch=s.position()-longestAnnotation.get(s.chromosome());
		ArrayList<AnnotationEntry> toScan=genrep.get(s.chromosome());
		int startindex=Collections.binarySearch(toScan, new AnnotationEntry(s.chromosome(),"",toSearch,toSearch+1,AnnotationEntry.Strand.Plus,null,""),comp);
		
		// TESTCODE collections.binarysearch
		// 0,1,2,3,4,5,6,7
    	//{2,3,4,7,8,9,9} 
    	//System.out.println(Collections.binarySearch(ilist,2)); 	//  0
    	//System.out.println(Collections.binarySearch(ilist,1)); 	// -1
    	//System.out.println(Collections.binarySearch(ilist,5)); 	// -4
    	//System.out.println(Collections.binarySearch(ilist,7)); 	//  3
    	//System.out.println(Collections.binarySearch(ilist,9)); 	//  5
    	//System.out.println(Collections.binarySearch(ilist,10)); 	// -8
		if(startindex<0) startindex = Math.abs(startindex)-2;
		if(startindex<0) startindex=0;
		
		
		HashSet<String> geneids=new HashSet<String>();
		for(int i=Math.abs(startindex); i<toScan.size(); i++)
		{
			AnnotationEntry e=toScan.get(i);
			if(e.start()<=s.position() && e.end()>=s.position())
			{
				geneids.add(e.geneid());
			}
			// stop iteration if larger than the snp position
			if(e.start()>s.position())break;
		}
		return new ArrayList<String>(geneids);
	}
	
	
	/*
	int binarySearch(int[] array, int value, int left, int right) {
	      if (left > right)
	            return -1;
	      int middle = (left + right) / 2;
	      if (array[middle] == value)
	            return middle;
	      else if (array[middle] > value)
	            return binarySearch(array, value, left, middle - 1);
	      else
	            return binarySearch(array, value, middle + 1, right); 
	*/
	
//	private binarySearch(ArrayList<>)
	
}

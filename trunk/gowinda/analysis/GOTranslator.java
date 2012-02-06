package gowinda.analysis;

import gowinda.io.GOEntryBulkReader;
import java.util.*;

/*
 * Tranlsates a list of Geneids into counts of GO categories
 * Two modes, when multiple repeats of the same Geneid are present
 * a.) Counting.unit Gene: every Gene ID only counts once 
 * b.) Counitng.unit SNP: every Gene ID counts several times (the number of times being present)
 */

public class GOTranslator {
	private final HashMap<String,ArrayList<GOEntry>> goh;
	private final gowinda.misc.CountingUnit cu;
	private final java.util.logging.Logger logger;
	
	public GOTranslator(GOEntryBulkReader goreader,gowinda.misc.CountingUnit cu, java.util.logging.Logger logger)
	{
		goh=goreader.readGOEntries();
		this.cu=cu;
		this.logger=logger;
	}
	
	public GOTranslator(HashMap<String,ArrayList<GOEntry>> golist, gowinda.misc.CountingUnit cu)
	{
		goh=new HashMap<String,ArrayList<GOEntry>>(golist);
		this.cu=cu;
		this.logger=java.util.logging.Logger.getLogger("Gowinda Logger");
		logger.setUseParentHandlers(false);
	}
	
	
	/*
	 * Translates a list of geneids into counts for all GOcategories
	 */
	public HashMap<GOEntry,Integer> translate(ArrayList<String> geneids)
	{
		ArrayList<String> towork;
		if(this.cu==gowinda.misc.CountingUnit.Snp)
		{
			towork=geneids;
		}
		else if(this.cu==gowinda.misc.CountingUnit.Gene)
		{
			// make them unique
			towork=new ArrayList<String>(new HashSet<String>(geneids));
		}
		else
		{
			throw new IllegalArgumentException("Do not recognise Counting unit"+this.cu);
		}
		
		HashMap<GOEntry,Integer> toret=new HashMap<GOEntry,Integer>();
		for(String geneid: towork)
		{
			// CRITICAL - if no go entry is found for a given gene, an empty list will be used
			ArrayList<GOEntry> goterms=new ArrayList<GOEntry>();
			if(this.goh.containsKey(geneid))goterms=this.goh.get(geneid);
			
			for(GOEntry ge: goterms)
			{
				if(!toret.containsKey(ge))toret.put(ge,0);
				toret.put(ge, toret.get(ge)+1);
			}
		}
		return toret;
	}
	
	/*
	 *	Checks the number of geneids for which no Go entry is found
	 */
	public void validateGeneids(ArrayList<String> genelist)
	{
		this.logger.info("Start validating geneid with GO terms - most genes should have at least one associated GO category");

		int genecount=genelist.size();
		int genewithgo=0;
		
		ArrayList<String> orphans=new ArrayList<String>();
		for(String geneid: genelist)
		{
			ArrayList<GOEntry> go=this.goh.get(geneid);
			if(go==null || go.size()==0)
			{
				orphans.add(geneid);
			}
			else
			{
				genewithgo++;
			}
		}
		
		this.logger.info("Found GO categories for "+genewithgo +" out of "+genecount+ " genes");
		this.logger.fine("Gene IDs without GO category:");
		for(String s: orphans)
		{
			this.logger.fine("Gene_id: "+s);
		}
		
		// stop if not a single association between GO term and GeneID was found 
		if(genewithgo==0) throw new IllegalArgumentException("Did not find a GO category for a single gene. Unable to perform simulations. Maybee GO file uses different gene names than the annotation file");
	}
	
	public ArrayList<GOEntry> goEntryList()
	{
		HashSet<GOEntry> all=new HashSet<GOEntry>();
		for(Map.Entry<String, ArrayList<GOEntry>> en: goh.entrySet())
		{
			for(GOEntry ge:en.getValue())
			{
				all.add(ge);
			}
		}
		ArrayList<GOEntry> toret=new ArrayList<GOEntry>(all);
		Collections.sort(toret);
		return toret;
	}
	
	public int goEntryCount()
	{
		return this.goEntryList().size();
	}
	
}

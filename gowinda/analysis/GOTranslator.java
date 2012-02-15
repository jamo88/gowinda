package gowinda.analysis;

import java.util.*;

/*
 * Tranlsates a list of Geneids into counts of GO categories
 * Two modes, when multiple repeats of the same Geneid are present
 * a.) Counting.unit Gene: every Gene ID only counts once 
 * b.) Counitng.unit SNP: every Gene ID counts several times (the number of times being present)
 */

public class GOTranslator {
	private final GOCategoryContainer gocategories;

	public GOTranslator(GOCategoryContainer gocategories)
	{
		this.gocategories=gocategories;
	}
	
	
	/*
	 * Translates a list of geneids into counts of GO-categories
	 */
	public HashMap<GOEntry,Integer> translateToCount(ArrayList<String> geneids)
	{
		HashMap<GOEntry,Integer> toret=new HashMap<GOEntry,Integer>();
		for(String geneid: geneids)
		{
			// CRITICAL - if no go entry is found for a given gene, an empty list will be used
			ArrayList<GOEntry> goterms=new ArrayList<GOEntry>();
			if(this.gocategories.contains(geneid))goterms=this.gocategories.get(geneid);
			
			for(GOEntry ge: goterms)
			{
				if(!toret.containsKey(ge))toret.put(ge,0);
				toret.put(ge, toret.get(ge)+1);
			}
		}
		return toret;
	}
	
	
	public HashMap<GOEntry,ArrayList<String>> translateToGeneids(ArrayList<String> geneids)
	{
		ArrayList<String> towork=new ArrayList<String>(new HashSet<String>(geneids));
		HashMap<GOEntry,ArrayList<String>> toret=new HashMap<GOEntry,ArrayList<String>>();
		for(String geneid: towork)
		{
			// CRITICAL - if no go entry is found for a given gene, an empty list will be used
			ArrayList<GOEntry> goterms=new ArrayList<GOEntry>();
			if(this.gocategories.contains(geneid))goterms=this.gocategories.get(geneid);
			
			for(GOEntry ge: goterms)
			{
				if(!toret.containsKey(ge))toret.put(ge,new ArrayList<String>());
				toret.get(ge).add(geneid);
			}
		}
		return toret;
	}
	
}

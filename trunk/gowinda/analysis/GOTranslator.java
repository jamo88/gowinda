package gowinda.analysis;

import java.util.*;

/**
 * Tranlsates a list of gene_ids into counts of GO categories
 */

public class GOTranslator {
	private final GOCategoryContainer gocategories;

	public GOTranslator(GOCategoryContainer gocategories)
	{
		this.gocategories=gocategories;
	}
	
	
	/**
	 * Translates a list of geneids into counts of GO-categories; Gene_ids being present multiple times are also considered multiple times
	 */
	
	/**
	 * Translates a list of gene_ids into counts of every GO-category.
	 * Gene_ids being present multiple times are also considered multiple times.
	 * Minimum count is '1', i.e.: GO categories with no corresponding genes are not considered
	 * @param geneids a collection of gene_ids
	 * @return
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
	
	/**
	 * Assigns a list of gene_IDs to the corresponding GO categories.
	 * If a gene_id is present multiple times, it will also be  considered multiple times.
	 * GO categories with no associated gene will not be displayed. 
	 * @param geneids a collection of gene_ids
	 * @return a map from the GO category to the corresponding list of genes
	 */
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

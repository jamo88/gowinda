package gowinda.analysis;

import java.util.*;

public class GOCategoryContainer {
	private final HashMap<String,ArrayList<GOEntry>> goentries;
	public GOCategoryContainer(HashMap<String,ArrayList<GOEntry>> goentries)
	{
		this.goentries=new HashMap<String,ArrayList<GOEntry>>(goentries);
	}
	
	/**
	 * returns a unique list of GO categories associated with the given gene_id
	 * @param geneid 
	 * @return a list of unique GO categories
	 */
	public ArrayList<GOEntry> get(String geneid)
	{
		if(!this.goentries.containsKey(geneid)) return new ArrayList<GOEntry>();
		return new ArrayList<GOEntry>(this.goentries.get(geneid));
	}
	
	/**
	 * is the given gene_id part of the collection
	 * @param geneid
	 * @return true if the gene_id is part of the collection
	 */
	public boolean contains(String geneid)
	{
		return this.goentries.containsKey(geneid);
	}
	
	/**
	 * Returns the total number of GO categories found
	 * @return total number of GO categories
	 */
	public int size()
	{
		return this.goentries.size();
	}
	
	/**
	 * Returns a unique list of all GO categories
	 * @return a list of unique GO categories
	 */
	public ArrayList<GOEntry> getAllGOEntries()
	{
		HashSet<GOEntry> toret=new HashSet<GOEntry>();
		for(Map.Entry<String, ArrayList<GOEntry>> me: this.goentries.entrySet())
		{
			toret.addAll(me.getValue());
		}
		return new ArrayList<GOEntry>(toret);
	}
	
	/**
	 * Returns the count of associated genes for every GO category
	 * @return
	 */
	public HashMap<GOEntry,Integer> getGOcategoryAbundance()
	{
		HashMap<GOEntry,HashSet<String>> col=new HashMap<GOEntry,HashSet<String>>(); 
		for(Map.Entry<String,ArrayList<GOEntry>> me: this.goentries.entrySet())
		{
			String geneid=me.getKey();
			for(GOEntry ge: me.getValue())
			{
				if(!col.containsKey(ge))col.put(ge, new HashSet<String>());
				col.get(ge).add(geneid);
			}
		}
		
		HashMap<GOEntry,Integer> toret=new HashMap<GOEntry,Integer>();
		for(Map.Entry<GOEntry, HashSet<String>> ge: col.entrySet())
		{
			toret.put(ge.getKey(), ge.getValue().size());
		}
		return toret;
	}
	

	/**
	 * Returns a list of gene_ids with at least a single associated GO category
	 * @return a unique list of gene_ids with at least a single associated GO category
	 */
	public ArrayList<String> getAllGeneids()
	{
		return new ArrayList<String>(this.goentries.keySet());
	}
	
	
	public GOCategoryContainer subsetMinGenes(int minGenes)
	{
		// Get Abundance
		HashMap<GOEntry,Integer> abundance=getGOcategoryAbundance();
		
		
		HashMap<String,ArrayList<GOEntry>> toret=new HashMap<String,ArrayList<GOEntry>>();		
		for(Map.Entry<String,ArrayList<GOEntry>> me: this.goentries.entrySet())
		{
			ArrayList<GOEntry> novelList=new ArrayList<GOEntry>();
			for(GOEntry ge: me.getValue())
			{
				if(abundance.get(ge)>=minGenes)novelList.add(ge);
			}
			
			if(novelList.size()>0) toret.put(me.getKey(), novelList);
		}
		
		return new GOCategoryContainer(toret);
	}
	
	
	
	
}

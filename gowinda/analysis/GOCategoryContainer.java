package gowinda.analysis;

import java.util.*;

public class GOCategoryContainer {
	private final HashMap<String,ArrayList<GOEntry>> goentries;
	public GOCategoryContainer(HashMap<String,ArrayList<GOEntry>> goentries)
	{
		this.goentries=new HashMap<String,ArrayList<GOEntry>>(goentries);
	}
	
	
	public ArrayList<GOEntry> get(String geneid)
	{
		if(!this.goentries.containsKey(geneid)) return new ArrayList<GOEntry>();
		return new ArrayList<GOEntry>(this.goentries.get(geneid));
	}
	
	public boolean contains(String geneid)
	{
		return this.goentries.containsKey(geneid);
	}
	
	public int size()
	{
		return this.goentries.size();
	}
	
	/*
	 * Returns a list of all GO categories
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
	
	/*
	 * Return all the Geneids for which any GO category was found
	 */
	public ArrayList<String> getAllGeneids()
	{
		return new ArrayList<String>(this.goentries.keySet());
	}
	
	
	
	
}

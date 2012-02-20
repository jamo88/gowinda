package gowinda.analysis;

import java.util.*;
/*
 * Immutable container
 */
public class GOResultForCandidateSnp {

	private GOEntry goe;
	private int maxCount;
	private double expectedCount;
	private int observedCount;
	private double significance;
	private double adjustedSignificance=1.0;
	private double minSignificance;
	private ArrayList<String> geneids;
	private ArrayList<String> maxGeneids;
	
	public GOResultForCandidateSnp(GOEntry entry, double significance, int observedCount, double expectedCount)
	{
		this.goe=entry;
		this.observedCount=observedCount;
		this.expectedCount=expectedCount;
		this.significance=significance;
	}

	public GOEntry goEntry()
	{
		return this.goe;
	}
	public double significance()
	{
		return this.significance;
	}
	public double expectedCount()
	{
		return this.expectedCount;
	}
	public int observedCount()
	{
		return this.observedCount;
	}
	public int maxCount()
	{
		return this.maxCount;
	}
	public void setMaxCount(int maxCount)
	{
		this.maxCount=maxCount;
	}
	public double adjustedSignificance()
	{
		return this.adjustedSignificance;
	}
	public void setAdjustedSignificance(double adjSignificance)
	{
		this.adjustedSignificance=adjSignificance;
	}
	public double minSignificance()
	{
		return this.minSignificance;
	}
	public void setMinSignificance(double minSignificance)
	{
		this.minSignificance=minSignificance;
	}
	public ArrayList<String> geneids()
	{
		return new ArrayList<String>(this.geneids);
	}
	public void setGeneids(ArrayList<String> geneids)
	{
		this.geneids=geneids;
	}
	public ArrayList<String> maxGeneids()
	{
		return this.maxGeneids;
	}
	public void setMaxGeneids(ArrayList<String> geneids)
	{
		this.maxGeneids=geneids;
	}
}

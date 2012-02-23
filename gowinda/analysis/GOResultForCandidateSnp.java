package gowinda.analysis;

import java.util.*;
/*
 * Immutable container
 */
public class GOResultForCandidateSnp {

	private GOEntry goe;
	private double expectedCount;
	private int observedCount;
	private double significance;
	private double adjustedSignificance=1.0;
	private ArrayList<String> geneids;
	private int maxsnpGeneids;
	private int maxGeneids;
	
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
	public double adjustedSignificance()
	{
		return this.adjustedSignificance;
	}
	public void setAdjustedSignificance(double adjSignificance)
	{
		this.adjustedSignificance=adjSignificance;
	}
	public ArrayList<String> geneids()
	{
		return new ArrayList<String>(this.geneids);
	}
	public void setGeneids(ArrayList<String> geneids)
	{
		this.geneids=geneids;
	}
	public int maxsnpGeneids()
	{
		return this.maxsnpGeneids;
	}
	public void setMaxsnpGeneids(int maxsnpgeneids)
	{
		this.maxsnpGeneids=maxsnpgeneids;
	}
	public int maxGeneids()
	{
		return this.maxGeneids;
	}
	public void setMaxGeneids(int maxGeneids)
	{
		this.maxGeneids=maxGeneids;
	}
}

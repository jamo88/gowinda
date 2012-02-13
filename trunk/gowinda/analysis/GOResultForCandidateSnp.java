package gowinda.analysis;

import java.util.*;
/*
 * Immutable container
 */
public class GOResultForCandidateSnp {
	private final double significance;
	private final GOEntry goe;
	private final int observedCount;
	private final double expectedCount;
	private final double adjustedSignificance;
	private final ArrayList<String> geneids;
	public GOResultForCandidateSnp(GOEntry entry, double significance, double adjustedSignificance,int observed, double expected)
	{
		this(entry,significance,adjustedSignificance,observed,expected,new ArrayList<String>());
	}
	
	public GOResultForCandidateSnp(GOEntry entry, double significance, double adjustedSignificance,int observed, double expected,ArrayList<String> geneids)
	{
		this.geneids=new ArrayList<String>(geneids);
		this.goe=entry;
		this.observedCount=observed;
		this.expectedCount=expected;
		this.adjustedSignificance=adjustedSignificance;
		this.significance=significance;
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
	public GOEntry goEntry()
	{
		return this.goe;
	}

	public double adjustedSignificance()
	{
		return this.adjustedSignificance;
	}
	public ArrayList<String> geneids()
	{
		return new ArrayList<String>(this.geneids);
	}
	
	public GOResultForCandidateSnp setGeneids(ArrayList<String> geneids)
	{
		return new GOResultForCandidateSnp(this.goe,this.significance,this.adjustedSignificance,this.observedCount,this.expectedCount,geneids);
	}
	
	public GOResultForCandidateSnp setAdjustedSignificance(double adjustedSignificance)
	{
		return new GOResultForCandidateSnp(this.goe,this.significance,adjustedSignificance,this.observedCount,this.expectedCount);
	}

	
}

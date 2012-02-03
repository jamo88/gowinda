package gowinda.analysis;

/*
 * Immutable container
 */
public class GOResultForCandidateSnp {
	private double significance;
	private GOEntry goe;
	private int observedCount;
	private double expectedCount;
	private double adjustedSignificance;
	public GOResultForCandidateSnp(GOEntry entry, double significance, double adjustedSignificance,int observed, double expected)
	{
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
	
}

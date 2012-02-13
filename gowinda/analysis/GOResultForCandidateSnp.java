package gowinda.analysis;

/*
 * Immutable container
 */
public class GOResultForCandidateSnp {
	private final double significance;
	private final GOEntry goe;
	private final int observedCount;
	private final double expectedCount;
	private final double adjustedSignificance;
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
	
	public GOResultForCandidateSnp setAdjustedSignificance(double adjustedSignificance)
	{
		return new GOResultForCandidateSnp(this.goe,this.significance,adjustedSignificance,this.observedCount,this.expectedCount);
	}
	
}

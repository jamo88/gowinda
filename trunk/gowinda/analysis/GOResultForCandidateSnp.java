package gowinda.analysis;

public class GOResultForCandidateSnp {
	private double significance;
	private GOEntry goe;
	private int observedCount;
	private double expectedCount;
	public GOResultForCandidateSnp(GOEntry entry, double significance, int observed, double expected)
	{
		this.goe=entry;
		this.observedCount=observed;
		this.expectedCount=expected;
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
	@Override
	public String toString()
	{
		return String.format("%s\t%.2f\t%d\t%f\t%s", goe.goID(),this.expectedCount,this.observedCount,this.significance,goe.description());
	}
}

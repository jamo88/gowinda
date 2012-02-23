package gowinda.analysis.fdr;



import java.util.ArrayList;

public class FdrSimulatedAdjuster implements IMultipleTestingAdjuster {
	private final FdrSimulationContainer fdrSim;
	
	/*
	private static class PVal{
		public final int index;
		public final double pvalue;
		public PVal(int index, double pvalue)
		{
			this.index=index;
			this.pvalue=pvalue;
		}
	}
	*/
	
	public FdrSimulatedAdjuster(FdrSimulationContainer fdrSim)
	{
		this.fdrSim=fdrSim;	
	}
	
	@Override
	public ArrayList<Double> getAdjustedSignificance(ArrayList<Double> uncorrected)
	{
		
		ArrayList<Double> toret =new ArrayList<Double>();
		for(double d: uncorrected)
		{	
			double expectedCount=fdrSim.getExpectedCount(d);
			double observedCount=0;
			for (double temp: uncorrected)
			{
				if(temp<=d)observedCount++;
			}
			double fdr=expectedCount/observedCount;
			if(fdr>1.0)fdr=1.0;
			toret.add(fdr);
		}
		return toret;
	}
}

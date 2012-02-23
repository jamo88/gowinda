package gowinda.analysis.fdr;



public class FdrSimulationContainer {
	private double[] cumulativePvalDistri;
	private int simulations;
	
	public FdrSimulationContainer(double[] averagePvalueDistribution,int simulations)
	{
		this.simulations=simulations;
		this.cumulativePvalDistri=new double[averagePvalueDistribution.length];
		this.cumulativePvalDistri[0]=averagePvalueDistribution[0];
		for(int i=1; i < averagePvalueDistribution.length; i++)
		{
			this.cumulativePvalDistri[i]=this.cumulativePvalDistri[i-1]+averagePvalueDistribution[i];
		}
	}
	
	public double getExpectedCount(double uncorrectedPvalue)
	{
		int key=(int)(uncorrectedPvalue*((double)this.simulations));
		return cumulativePvalDistri[key];

	}
	
}




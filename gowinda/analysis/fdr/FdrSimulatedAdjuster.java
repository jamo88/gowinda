package gowinda.analysis.fdr;



import java.util.ArrayList;

public class FdrSimulatedAdjuster implements IMultipleTestingAdjuster {
	private final FdrSimulationContainer fdrSim;
	
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
			toret.add(fdrSim.translateToFdr(d));
		}
		return toret;
	}
	
	

}

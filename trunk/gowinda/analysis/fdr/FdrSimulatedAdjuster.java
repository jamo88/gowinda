package gowinda.analysis.fdr;




import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FdrSimulatedAdjuster implements IMultipleTestingAdjuster {
	private final FdrSimulationContainer fdrSim;
	
	
	private static class PVal{
		public int index;
		public double fdr;
		public double pvalue;
		public PVal(int index, double pvalue, double fdr)
		{
			this.index=index;
			this.pvalue=pvalue;
			this.fdr=fdr;
		}
	}
	
	
	public FdrSimulatedAdjuster(FdrSimulationContainer fdrSim)
	{
		this.fdrSim=fdrSim;	
	}
	
	@Override
	public ArrayList<Double> getAdjustedSignificance(ArrayList<Double> uncorrected)
	{
		
	
		ArrayList<PVal> tempList=new ArrayList<PVal>();
		for(int i=0; i<uncorrected.size(); i++)
		{	
			double uncorPval=uncorrected.get(i);
			double expectedCount=fdrSim.getExpectedCount(uncorPval);
			double observedCount=0;
			for (int k=0; k<uncorrected.size(); k++)
			{
				double temp=uncorrected.get(k);
				if(temp<=uncorPval)observedCount++;
			}
			double fdr = expectedCount / observedCount;
			tempList.add(new PVal(i,uncorPval,fdr));
		}
		ArrayList<PVal> cummin=cummin(tempList);
		Collections.sort(cummin,new Comparator<PVal>(){
			public int compare(PVal v1, PVal v2)
			{
				if(v1.index > v2.index) return 1;
				if(v1.index < v2.index) return -1;
				return 0;
			}
		});
		
		ArrayList<Double> toret =new ArrayList<Double>();
		for(PVal pv: cummin)
		{
			toret.add(pv.fdr);
		}
		return toret;
	}
	
	private ArrayList<PVal> cummin(ArrayList<PVal> tocummin)
	{
		Collections.sort(tocummin,new Comparator<PVal>(){
			public int compare(PVal v1, PVal v2)
			{
				if(v1.pvalue < v2.pvalue) return 1;
				if(v1.pvalue > v2.pvalue) return -1;
				return 0;
			}
		});

		// Cummin 10 5 6 7 4 3 2 1
		double previous=1.0;
		for(PVal ip: tocummin)
		{
			// Set largest to 1.0;
			if(ip.fdr>1.0) ip.fdr=1.0;
		
			// Calculate the cummulative minimum; no value 
			if(ip.fdr > previous) ip.fdr=previous;
			else if(ip.fdr<previous) previous=ip.fdr;
		}
		return tocummin;
	}
}

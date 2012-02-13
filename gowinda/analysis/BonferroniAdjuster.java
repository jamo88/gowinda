package gowinda.analysis;

import java.util.*;
/*
 * Adjustement of p-value for multiple sampling
 * Concrete implementation uses the Bonferroni correction, which is by many considered to stringent
 */
public class BonferroniAdjuster implements IMultipleTestingAdjuster{
	

	private int goCategoryCount;
	public BonferroniAdjuster(int goCategoryCount)
	{
		this.goCategoryCount=goCategoryCount;
	}

	
	@Override
	public ArrayList<Double> getAdjustedSignificance(ArrayList<Double> pval)
	{

		ArrayList<Double> toret=new ArrayList<Double>();
		for(double f: pval)
		{
			double novel=f*goCategoryCount;
			if(novel>1.0) novel=1.0;
			toret.add(novel);
		}
		return toret;
	}

}

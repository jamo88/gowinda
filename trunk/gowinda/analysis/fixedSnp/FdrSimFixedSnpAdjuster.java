package gowinda.analysis.fixedSnp;

import java.util.ArrayList;

import gowinda.analysis.fdr.IMultipleTestingAdjuster;

public class FdrSimFixedSnpAdjuster implements IMultipleTestingAdjuster{
	
	@Override
	public ArrayList<Double> getAdjustedSignificance(ArrayList<Double> uncorrected)
	{
		return new ArrayList<Double>();
	}

}

package gowinda.analysis.fixedGene;

import gowinda.analysis.*;
import gowinda.analysis.fdr.IMultipleTestingAdjuster;

import java.util.*;

public class FdrSimFixedGeneAdjuster implements IMultipleTestingAdjuster {
	
	public FdrSimFixedGeneAdjuster(GOSimulationContainer gosimres,ArrayList<Snp> snps, int candCount,int threads, int simulations)
	{
		
	}
	
	@Override
	public ArrayList<Double> getAdjustedSignificance(ArrayList<Double> uncorrected)
	{
		return new ArrayList<Double>();
	}

}

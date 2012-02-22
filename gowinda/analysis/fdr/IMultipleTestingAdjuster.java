package gowinda.analysis.fdr;

import java.util.ArrayList;

public interface IMultipleTestingAdjuster {
	public abstract ArrayList<Double> getAdjustedSignificance(ArrayList<Double> pval);

}

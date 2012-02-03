package gowinda.analysis;

import java.util.ArrayList;

public interface IMultipleTestingAdjuster {
	public abstract ArrayList<GOResultForCandidateSnp> getAdjustedSignificance(ArrayList<GOResultForCandidateSnp> candidates);

}

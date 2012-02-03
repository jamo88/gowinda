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
	public ArrayList<GOResultForCandidateSnp> getAdjustedSignificance(ArrayList<GOResultForCandidateSnp> candidates)
	{
		ArrayList<GOResultForCandidateSnp> res=new ArrayList<GOResultForCandidateSnp>();
		for(GOResultForCandidateSnp cand:candidates)
		{
			double adj=cand.significance()*this.goCategoryCount;
			if(adj>1.0) adj=1.0;
			res.add(new GOResultForCandidateSnp(cand.goEntry(),cand.significance(),adj,cand.observedCount(),cand.expectedCount()));
		}
		return res;
	}

}

package gowinda.analysis;

import java.util.*;

public class GOResultContainer {
	private final ArrayList<GOResultForCandidateSnp> gores;
	public GOResultContainer(ArrayList<GOResultForCandidateSnp> gores)
	{
		this.gores=new ArrayList<GOResultForCandidateSnp>(gores);
	}
	
	public GOResultContainer updateMultipleTesting(IMultipleTestingAdjuster adj)
	{
		ArrayList<Double> pvals=new ArrayList<Double>();
		for(GOResultForCandidateSnp res:this.gores)
		{
			pvals.add(res.significance());
		}
		
		
		ArrayList<Double> adjPvals=adj.getAdjustedSignificance(pvals);
		ArrayList<GOResultForCandidateSnp> toret = new ArrayList<GOResultForCandidateSnp>();
		for(int i=0; i<adjPvals.size(); i++)
		{
			GOResultForCandidateSnp active=this.gores.get(i);
			double adjusteP=adjPvals.get(i);
			toret.add(active.setAdjustedSignificance(adjusteP));
			
		}
		return new GOResultContainer(toret);
	}
	
	public ArrayList<GOResultForCandidateSnp> getCollection()
	{
		return new ArrayList<GOResultForCandidateSnp>(this.gores);
	}
	
	public int size()
	{
		return this.gores.size();
	}
	
	public GOResultForCandidateSnp get(int index)
	{
		return this.gores.get(index);
	}

	

}

package gowinda.analysis;

import java.util.*;

public class GOResultContainer {
	private final ArrayList<GOResultForCandidateSnp> gores;
	private final GOSimulationContainer gosim;
	public GOResultContainer(ArrayList<GOResultForCandidateSnp> gores, GOSimulationContainer gosim)
	{
		this.gores=new ArrayList<GOResultForCandidateSnp>(gores);
		this.gosim=gosim;
	}
	
	public GOResultContainer updateGeneids(HashMap<GOEntry,ArrayList<String>> genids)
	{
		ArrayList<GOResultForCandidateSnp> toret=new ArrayList<GOResultForCandidateSnp>();
		
		for(GOResultForCandidateSnp gr: this.gores)
		{
			ArrayList<String> geneids =new ArrayList<String>();
			if(genids.containsKey(gr.goEntry()))
			{
				geneids=genids.get(gr.goEntry());
			}
			toret.add(gr.setGeneids(geneids));
		}
		return new GOResultContainer(toret,this.gosim);
		
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
		return new GOResultContainer(toret,this.gosim);
	}
	
	
	public ArrayList<GOResultForCandidateSnp> getCollection()
	{
		return new ArrayList<GOResultForCandidateSnp>(this.gores);
	}
	
	public int size()
	{
		return this.gores.size();
	}
	
	public GOSimulationContainer getSimulationContainer()
	{
		return this.gosim;
	}
	
	public GOResultForCandidateSnp get(int index)
	{
		return this.gores.get(index);
	}

	

}

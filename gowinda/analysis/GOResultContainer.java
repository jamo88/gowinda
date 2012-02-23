package gowinda.analysis;

import gowinda.analysis.fdr.*;

import java.util.*;

public class GOResultContainer {
	private final ArrayList<GOResultForCandidateSnp> gores;
	private final GOSimulationContainer gosim;
	private final HashMap<GOEntry,GOResultForCandidateSnp> goresmapping;
	
	public GOResultContainer(ArrayList<GOResultForCandidateSnp> gores, GOSimulationContainer gosim)
	{
		this.gores=new ArrayList<GOResultForCandidateSnp>(gores);
		this.gosim=gosim;
		goresmapping=new HashMap<GOEntry,GOResultForCandidateSnp>();
		for(GOResultForCandidateSnp gres: this.gores)
		{
			goresmapping.put(gres.goEntry(), gres);
		}	
	}
	
	public void updateGeneids(HashMap<GOEntry,ArrayList<String>> genids)
	{
		for(GOResultForCandidateSnp gr: this.gores)
		{
			ArrayList<String> geneids =new ArrayList<String>();
			if(genids.containsKey(gr.goEntry()))
			{
				geneids=genids.get(gr.goEntry());
			}
			gr.setGeneids(geneids);
		}
	}
	
	public void updateMaxGeneids(GOCategoryContainer goCont)
	{
		
		HashMap<GOEntry,Integer> mc=goCont.getGOcategoryAbundance();
		for(GOResultForCandidateSnp gr:this.gores)
		{
			gr.setMaxGeneids(mc.get(gr.goEntry()));
		}
	}
	
	public void updateMaxsnpGeneids(HashMap<GOEntry,ArrayList<String>> genids)
	{
		for(GOResultForCandidateSnp gr: this.gores)
		{
			ArrayList<String> geneids =new ArrayList<String>();
			if(genids.containsKey(gr.goEntry()))
			{
				geneids=genids.get(gr.goEntry());
			}
			gr.setMaxGeneids(geneids.size());
		}
	}
	
	public void updateMultipleTesting(IMultipleTestingAdjuster adj)
	{
		// get the adjusted p-values
		ArrayList<Double> pvals=new ArrayList<Double>();
		for(GOResultForCandidateSnp res:this.gores)
		{
			pvals.add(res.significance());
		}
		ArrayList<Double> adjPvals=adj.getAdjustedSignificance(pvals);
		
		// update the underlying collection
		for(int i=0; i<adjPvals.size(); i++)
		{
			GOResultForCandidateSnp active=this.gores.get(i);
			double adjusteP=adjPvals.get(i);
			active.setAdjustedSignificance(adjusteP);
		}
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
	
	public GOResultForCandidateSnp get(GOEntry goe)
	{
		return this.goresmapping.get(goe);
	}
}

package gowinda.analysis;
import java.util.*;


public class GOSimulationContainer {
	private int simulations;
	private HashMap<GOEntry,HashMap<Integer,Integer>> simres;
	private static int pseudocount=1;
	public GOSimulationContainer(int simulations)
	{
		this.simulations=simulations;
		simres=new HashMap<GOEntry,HashMap<Integer,Integer>>();
	}
	
	/*
	 * Update the simulation container with the results of a single novel simulation
	 * Results of a single simulation are the GOEntry and the counts of significant hits
	 */
	public synchronized void addSimulation(HashMap<GOEntry,Integer> singleSimulation)
	{
		for(Map.Entry<GOEntry,Integer> en: singleSimulation.entrySet())
		{
			// update with default values
			// Key is GO entry; value is the number of counts obtained for the given GO category
			if(!this.simres.containsKey(en.getKey())) simres.put(en.getKey(), new HashMap<Integer,Integer>());
			if(!this.simres.get(en.getKey()).containsKey(en.getValue())) simres.get(en.getKey()).put(en.getValue(),0);
			// increase count
			this.simres.get(en.getKey()).put(en.getValue(),this.simres.get(en.getKey()).get(en.getValue())+1);
		}
	}
	
	public ArrayList<GOResultForCandidateSnp> estimateSignificance(HashMap<GOEntry,Integer> candidateResults, IMultipleTestingAdjuster adjuster)
	{
		ArrayList<GOResultForCandidateSnp> res=new ArrayList<GOResultForCandidateSnp>();
		
		for(Map.Entry<GOEntry, Integer> candGO: candidateResults.entrySet())
		{
			double sign=singleSignificance(candGO.getValue(),this.simres.get(candGO.getKey()));
			double expected=expectedCount(candGO.getValue(),this.simres.get(candGO.getKey()));
			res.add(new GOResultForCandidateSnp(candGO.getKey(),sign,1.0,candGO.getValue(),expected));
		}
		return adjuster.getAdjustedSignificance(res);
	}
	
	
	private double expectedCount(int candCount,HashMap<Integer,Integer> candSimres)
	{
		int sumCount=0;
		for(Map.Entry<Integer, Integer> cat: candSimres.entrySet())
		{
			sumCount+=(cat.getKey()*cat.getValue());
		}
		return ((double)sumCount)/((double)simulations);
	}

	
	private double singleSignificance(int candCount,HashMap<Integer,Integer> candSimres)
	{
		if(candSimres.size()<1)return ((double)pseudocount)/((double)simulations);
		
		int sumCount=0;
		for(Map.Entry<Integer, Integer> cat: candSimres.entrySet())
		{
			if(cat.getKey()>=candCount)sumCount+=cat.getValue();
		}
		if(sumCount==0)sumCount=1;
		return ((double)sumCount)/((double)simulations);
	}

}

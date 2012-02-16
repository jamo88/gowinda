package gowinda.analysis;
import java.util.*;

/*
 * Contains the results of the GO simulations. 
 * May also be used to calculate the significance of all GO categories for a given SNP set.
 */
public class GOSimulationContainer {
	
	
	public static class GOSimulationContainerBuilder
	{
		private HashMap<GOEntry,HashMap<Integer,Integer>> simres;
		private int simulations=0;
		public GOSimulationContainerBuilder()
		{
			simres=new HashMap<GOEntry,HashMap<Integer,Integer>>();
		}
		/*
		 * Update the simulation container with the results of a single novel simulation
		 * Results of a single simulation are the GOEntry and the counts of significant hits
		 */
		public synchronized void addSimulation(HashMap<GOEntry,Integer> singleSimulation)
		{
			this.simulations++;
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
		
		public synchronized GOSimulationContainer getSimulationResults()
		{
			return new GOSimulationContainer(this.simres,this.simulations);
		}
		
	}
	
		/*	
		 * END GOSimulationContainerBuilder
		 */
	
	
	
	private final static int pseudocount=1;
	private final int simulations;
	private final HashMap<GOEntry,HashMap<Integer,Integer>> simres;

	private GOSimulationContainer(HashMap<GOEntry,HashMap<Integer,Integer>> simres,int simulations)
	{
		this.simulations=simulations;
		this.simres=simres;
	}
	

	
	/*
	 * Estimate the significance for a given Set of GOterms
	 */
	public GOResultContainer estimateSignificance(HashMap<GOEntry,Integer> candidateResults)
	{
		ArrayList<GOResultForCandidateSnp> res=new ArrayList<GOResultForCandidateSnp>();
		
		for(Map.Entry<GOEntry, Integer> candGO: candidateResults.entrySet())
		{
			double sign=singleSignificance(candGO.getValue(),this.simres.get(candGO.getKey()));
			double expected=expectedCount(candGO.getValue(),this.simres.get(candGO.getKey()));
			res.add(new GOResultForCandidateSnp(candGO.getKey(),sign,1.0,candGO.getValue(),expected));
		}
		return new GOResultContainer(res);
	}
	
	public int size()
	{
		return this.simres.size();
	}
	
	
	/*
	 * Calculate the average number of genes having the given go category
	 */
	private double expectedCount(int candCount,HashMap<Integer,Integer> candSimres)
	{
		int sumCount=0;
		for(Map.Entry<Integer, Integer> cat: candSimres.entrySet())
		{
			sumCount+=(cat.getKey()*cat.getValue());
		}
		return ((double)sumCount)/((double)simulations);
	}

	
	/*
	 * Calculate the significance for a single GO category
	 */
	private double singleSignificance(int candCount,HashMap<Integer,Integer> candSimres)
	{
		if(candSimres.size()<1)return ((double)pseudocount)/((double)simulations);
		
		int sumCount=0;
		for(Map.Entry<Integer, Integer> cat: candSimres.entrySet())
		{
			if(cat.getKey()>=candCount)sumCount+=cat.getValue();
		}
		if(sumCount==0) sumCount=pseudocount;
		return ((double)sumCount)/((double)simulations);
	}

}

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
			res.add(new GOResultForCandidateSnp(candGO.getKey(),sign,candGO.getValue(),expected));
		}
		return new GOResultContainer(res,this);
	}
	
	
	
	/*
	 * Calculate the average number of genes having the given go category
	 */
	private double expectedCount(int candCount,HashMap<Integer,Integer> candSimres)
	{
		long sumCount=0;
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
		
		long sumCount=0;
		for(Map.Entry<Integer, Integer> cat: candSimres.entrySet())
		{
			if(cat.getKey()>=candCount)sumCount+=cat.getValue();
		}
		if(sumCount==0) sumCount=pseudocount;
		return ((double)sumCount)/((double)simulations);
	}
	
	/**
	 * Returns the number of GO categories identified in the simulations 
	 * @return
	 */
	public int size()
	{
		return simres.size();
	}
	
	/**
	 * Get the average pvalue counts;
	 * double[] c[i]
	 * i/simulations= pvalue 
	 * c[i] average number of occurences of this pvalue for a single simulation (averaged over all simulations) 
	 */
	public double[] getAveragePvalueDistribution()
	{
		double[] pvalsum=new double[simulations+1];
		for(int i=0; i<pvalsum.length; i++)
		{
			pvalsum[i]=0;
		}
		for(Map.Entry<GOEntry, HashMap<Integer,Integer>> me: this.simres.entrySet())
		{
			double[] pvalDistriSingleGoCat=getPvalueDistributionForSingleGOEntry(me.getValue());
			for(int i=0; i<pvalDistriSingleGoCat.length; i++)
			{
				pvalsum[i]+=pvalDistriSingleGoCat[i];
			}
		}
		return pvalsum;
 	}
	
	/**
	 * Converts the Count statistics (Key=genes found for GO category; Value=number of occurences) into
	 * a pvalue distribution (index=(int)pvalue*simulations element=frequency of pvalue (in counts))
	 * @param toaggregate
	 * @return
	 */
	private double[] getPvalueDistributionForSingleGOEntry(HashMap<Integer,Integer> toaggregate)
	{
		int maxCount=0;
		for(Map.Entry<Integer,Integer> me: toaggregate.entrySet())
		{
			if(me.getKey()>maxCount)maxCount=me.getKey();
		}
		
		// Initialize the pvalue distribution
		long[] pvalueDistribution=new long[this.simulations+1];
		for(int i=0; i<pvalueDistribution.length; i++)
		{
			pvalueDistribution[i]=0;
		}
		
		int checksum=0;
 		for(int i=0; i<=maxCount; i++)
		{
 			Integer tmp=toaggregate.get(i);
 			int thisPvalueFrequency=tmp==null?0:tmp;
			
 			checksum+=thisPvalueFrequency;
			
			int counterSum=0;
			for(Map.Entry<Integer, Integer>me : toaggregate.entrySet())
			{
				if(me.getKey() >= i)counterSum+=me.getValue();
			}
			// double pvalue=((double)counterSum)/((double)simulations);
			// int encodedpvalue=pvalue*simulations
			int encodedPvalue=counterSum;
			pvalueDistribution[encodedPvalue]+=thisPvalueFrequency;
		}
 		assert(checksum==this.simulations);
 		
		double[] averagePvalForGoCat=new double[this.simulations+1];
		for(int i=0; i<averagePvalForGoCat.length; i++)
		{
			averagePvalForGoCat[i]=((double)pvalueDistribution[i])/((double)this.simulations);
		}
		return averagePvalForGoCat;
	}


}





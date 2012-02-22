package gowinda.analysis.fdr;

import gowinda.analysis.*;
import java.util.*;

public class FdrSimulationContainer {
	/**
	 * Class for constructing FDR simulation Results
	 * Synchronized (thread safe)
	 * @author robertkofler
	 *
	 */
	public static class FdrSimulationContainerBuilder
	{
		private ArrayList<Double> pvalues;
		
		public FdrSimulationContainerBuilder()
		{
			pvalues=new ArrayList<Double>();
		}
		
		/**
		 * Update the FdrSimulationBuilder with the results of a single simulation
		 * Thread safe
		 * @param gores
		 */
		public void addGOResults(GOResultContainer gores)
		{
			ArrayList<Double> tmpPval=new ArrayList<Double>();
			for(GOResultForCandidateSnp gr:gores.getCollection())
			{
				tmpPval.add(gr.significance());
			}
			synchronized(this)
			{
				this.pvalues.addAll(tmpPval);
			}
		}
		/**
		 * Get the results of the Fdr Simulation
		 * @return
		 */
		public synchronized FdrSimulationContainer getFdrSimulationContainer()
		{
			return new FdrSimulationContainer(this.pvalues);
		}
	}
	
	
	/*
	 * FDR Simulation Container
	 */
	private final ArrayList<Double> pvalues;
	
	public FdrSimulationContainer(ArrayList<Double> pvalues)
	{
		Collections.sort(pvalues);
		this.pvalues=pvalues;
	}
	
	public double translateToFdr(double uncorrectedPVal)
	{
		int counter=0;
		int totSize=pvalues.size();
		for(double p: this.pvalues)
		{
			if(p<=uncorrectedPVal)
			{
				counter++;
			}
			else
			{
				break;
			}
		}
		return ((double)counter)/((double)totSize);
	}
	
	
	
	

}

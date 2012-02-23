package gowinda.analysis.fixedSnp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gowinda.analysis.GOEntry;
import gowinda.analysis.GOResultContainer;
import gowinda.analysis.GOSimulationContainer;
import gowinda.analysis.GOTranslator;
import gowinda.analysis.IGenomeRepresentation;
import gowinda.analysis.Snp;
import gowinda.analysis.fdr.FdrSimulationContainer;



public class FdrFixedSnpSimulator{
	
	private final GOSimulationContainer gosimres;
	private final IGenomeRepresentation genrep;
	private final GOTranslator gotrans;
	private final ArrayList<Snp> snps;
	private final int candSnpCount;
	private final int threads;
	private final int simulation;
	
	public FdrFixedSnpSimulator(GOSimulationContainer gosimres, IGenomeRepresentation genrep, GOTranslator gotrans, ArrayList<Snp> snps, int candSnpCount,
			int threads, int simulations)
	{
		this.gosimres=gosimres;
		this.genrep=genrep;
		this.gotrans=gotrans;
		this.snps=snps;
		this.candSnpCount=candSnpCount;
		this.threads=threads;
		this.simulation=simulations;
	}
	
	public FdrSimulationContainer getFdrSimulations()
	{
		FdrSimulationContainer.FdrSimulationContainerBuilder fdrbuilder=new FdrSimulationContainer.FdrSimulationContainerBuilder();
		ExecutorService executor=Executors.newFixedThreadPool(threads);
		ArrayList<Callable<Object>> call=new ArrayList<Callable<Object>>();
		for(int i=0; i<this.simulation; i++)
		{
			
			call.add(Executors.callable(new SingleFdrFixedSnpSimulation(fdrbuilder,this.gosimres,this.gotrans,this.genrep,this.snps,this.candSnpCount,i)));
		}
		try
		{	
			// Run them all!
			executor.invokeAll(call);	
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		return fdrbuilder.getFdrSimulationContainer();
	}

}

class SingleFdrFixedSnpSimulation implements Runnable
{
	private final FdrSimulationContainer.FdrSimulationContainerBuilder fdrbuilder;
	private final ArrayList<Snp> snps;
	private final int candCount;
	private final GOSimulationContainer gosimres;
	private final GOTranslator gotrans;
	private final Random rand;
	private final IGenomeRepresentation genrep;
	
	public SingleFdrFixedSnpSimulation(FdrSimulationContainer.FdrSimulationContainerBuilder fdrbuilder, GOSimulationContainer gosimres,
			GOTranslator gotrans, IGenomeRepresentation genrep, ArrayList<Snp> snps, int candCount, int instanceCount)
	{
		this.fdrbuilder=fdrbuilder;
		this.snps=snps;
		this.candCount=candCount;
		this.gosimres=gosimres;
		this.gotrans=gotrans;
		this.genrep=genrep;
		rand=new Random(System.currentTimeMillis()+instanceCount);
	}
	
	@Override
	public void run()
	{
		HashSet<Snp> simsnps=new HashSet<Snp>();
		int snpcount=snps.size();
		while(simsnps.size()<candCount)
		{
			int index = rand.nextInt(snpcount);
			Snp s=snps.get(index);
			simsnps.add(s);
		}
		ArrayList<String> snpgeneids=genrep.getGeneidsForSnps(new ArrayList<Snp>(simsnps));
		HashMap<GOEntry,Integer> gocatfound=gotrans.translateToCount(new ArrayList<String>(snpgeneids));
		GOResultContainer fdrres=gosimres.estimateSignificance(gocatfound);
		fdrbuilder.addGOResults(fdrres);
	}
}

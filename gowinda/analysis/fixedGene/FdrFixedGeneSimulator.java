package gowinda.analysis.fixedGene;

import gowinda.analysis.*;
import gowinda.analysis.fdr.FdrSimulationContainer;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FdrFixedGeneSimulator{

	private final GOSimulationContainer gosimres;
	private final IGenomeRepresentation genrep;
	private final GOTranslator gotrans;
	private final ArrayList<Snp> snps;
	private final int candGeneCount;
	private final int threads;
	private final int simulation;
	
	public FdrFixedGeneSimulator(GOSimulationContainer gosimres, IGenomeRepresentation genrep, GOTranslator gotrans, ArrayList<Snp> snps, int candGeneCount,
			int threads, int simulations)
	{
		this.gosimres=gosimres;
		this.genrep=genrep;
		this.gotrans=gotrans;
		this.snps=snps;
		this.candGeneCount=candGeneCount;
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
			
			call.add(Executors.callable(new SingleFdrFixedGeneSimulation(fdrbuilder,this.gosimres,this.gotrans,this.genrep,this.snps,this.candGeneCount,i)));
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

class SingleFdrFixedGeneSimulation implements Runnable
{
	private final FdrSimulationContainer.FdrSimulationContainerBuilder fdrbuilder;
	private final ArrayList<Snp> snps;
	private final int candCount;
	private final GOSimulationContainer gosimres;
	private final GOTranslator gotrans;
	private final Random rand;
	private final IGenomeRepresentation genrep;
	
	public SingleFdrFixedGeneSimulation(FdrSimulationContainer.FdrSimulationContainerBuilder fdrbuilder, GOSimulationContainer gosimres,
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
		HashSet<String> geneids=new HashSet<String>();
		int snpcount=snps.size();
		while(geneids.size()<candCount)
		{
			int index = rand.nextInt(snpcount);
			Snp s=snps.get(index);
			ArrayList<String> snpgeneids=genrep.getGeneidsForSnp(s);
			geneids.addAll(snpgeneids);
		}
		HashMap<GOEntry,Integer> gocatfound=gotrans.translateToCount(new ArrayList<String>(geneids));
		GOResultContainer fdrres=gosimres.estimateSignificance(gocatfound);
		fdrbuilder.addGOResults(fdrres);
	}
}
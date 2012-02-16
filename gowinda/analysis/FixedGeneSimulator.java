package gowinda.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import gowinda.io.SnpGenicFilterReader;



public class FixedGeneSimulator implements IGOSimulator{
	
	private final IGenomeRepresentation genrep;
	private final GOCategoryContainer goentries;
	private final ArrayList<Snp> snps;
	private final ArrayList<Snp> candidateSnps;
	private java.util.logging.Logger logger;
	
	public FixedGeneSimulator(IGenomeRepresentation genrep, GOCategoryContainer goentries, ArrayList<Snp> snps, ArrayList<Snp> candidateSnps, java.util.logging.Logger logger)
	{
		this.logger=logger;
		this.genrep=genrep;
		this.goentries=goentries;
		this.snps=new SnpGenicFilterReader( snps,genrep,logger).getSnps();
		this.candidateSnps=new SnpGenicFilterReader(candidateSnps,genrep,logger).getSnps();
	}
	

	@Override
	public GOResultContainer getSimulationResults(int simulations, int threads)
	{
        // SIMULATIONS
        // Get simulation results
        HashSet<String> candGeneids= new HashSet<String>(genrep.getGeneidsForSnps(candidateSnps));
        int candGeneCount=candGeneids.size();
        int candSnpCount=this.candidateSnps.size();
		this.logger.info("Starting " +simulations+ " simulations for " +candSnpCount+ " candidate SNPs using " + threads +" threads");
		this.logger.info("The candidate SNPs are overlapping with "+ candGeneCount+ " genes; Will randomly draw SNPs unless the random SNPs are overlapping with the same nubmer of genes");
		this.logger.info("This may take a while. Switch to the detailed log mode if you want to see the progress");
		
		GOTranslator gotrans=new GOTranslator(this.goentries);
		gowinda.misc.GowindaProgressReporter progReporter=new gowinda.misc.GowindaProgressReporter(10000,this.logger);
		GOSimulationContainer.GOSimulationContainerBuilder simbuilder=new GOSimulationContainer.GOSimulationContainerBuilder();

		ExecutorService executor=Executors.newFixedThreadPool(threads);
		
		ArrayList<Callable<Object>> call=new ArrayList<Callable<Object>>();
		for(int i=0; i<simulations; i++)
		{
			
			call.add(Executors.callable(new SingleSimulationFixedSnp(simbuilder,gotrans,snps,genrep,candGeneCount,progReporter)));
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
		
		
		this.logger.info("Finished simulations");
		GOSimulationContainer simcont= simbuilder.getSimulationResults();
       
        
        // estimate significance of candidates: simres.estimateSignificance(candres);
        this.logger.info("Estimating significance of the candidate SNPs");

        HashMap<GOEntry,Integer> candidateGOcategories=gotrans.translateToCount(new ArrayList<String>(candGeneids));
        GOResultContainer gores=simcont.estimateSignificance(candidateGOcategories);
        return gores;
	}

}

class SingleSimulationFixedGene implements Runnable
{
	private final GOSimulationContainer.GOSimulationContainerBuilder simbuilder;
	private final GOTranslator gotrans;
	private final Random randgen=new Random();
	private final ArrayList<Snp> snps;
	private final IGenomeRepresentation genrep;

	private final int candGeneCount;
	private final gowinda.misc.GowindaProgressReporter progReporter;
	public SingleSimulationFixedGene(GOSimulationContainer.GOSimulationContainerBuilder simbuilder,GOTranslator gotrans, ArrayList<Snp> snps, IGenomeRepresentation genrep, int candGeneCount,gowinda.misc.GowindaProgressReporter progReporter)
	{
		this.simbuilder=simbuilder;
		this.gotrans=gotrans;
		this.snps=snps;
		this.candGeneCount=candGeneCount;
		this.genrep=genrep;
		this.progReporter=progReporter;
	}
	
	@Override
	public void run()
	{
		int snpcount=this.snps.size();
		HashSet<String> geneids=new HashSet<String>();
		while(geneids.size()<candGeneCount)
		{
			int index=randgen.nextInt(snpcount);
			Snp s=snps.get(index);
			ArrayList<String> snpgeneids=genrep.getGeneidsForSnp(s);
			geneids.addAll(snpgeneids);
		}
		
		HashMap<GOEntry,Integer> gocatfound=gotrans.translateToCount(new ArrayList<String>(geneids));
		simbuilder.addSimulation(gocatfound);
		progReporter.increment();
	}
}

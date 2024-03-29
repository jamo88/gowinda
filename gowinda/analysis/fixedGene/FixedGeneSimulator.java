package gowinda.analysis.fixedGene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import gowinda.analysis.GOCategoryContainer;
import gowinda.analysis.GOEntry;
import gowinda.analysis.GOResultContainer;
import gowinda.analysis.GOSimulationContainer;
import gowinda.analysis.GOTranslator;
import gowinda.analysis.IGOSimulator;
import gowinda.analysis.IGenomeRepresentation;
import gowinda.analysis.Snp;
import gowinda.io.SnpGenicFilterReader;
import gowinda.analysis.fdr.*;



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
		this.candidateSnps=candidateSnps;
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
		this.logger.info("The candidate SNPs are overlapping with "+ candGeneCount+ " genes; Will randomly draw SNPs unless the random SNPs are overlapping with the same number of genes");
		this.logger.info("This may take a while. Switch to the detailed log mode if you want to see the progress");
		
		GOTranslator gotrans=new GOTranslator(this.goentries);
		gowinda.misc.GowindaProgressReporter progReporter=new gowinda.misc.GowindaProgressReporter(10000,this.logger);
		GOSimulationContainer.GOSimulationContainerBuilder simbuilder=new GOSimulationContainer.GOSimulationContainerBuilder();

		ExecutorService executor=Executors.newFixedThreadPool(threads);
		
		ArrayList<Callable<Object>> call=new ArrayList<Callable<Object>>();
		for(int i=0; i<simulations; i++)
		{
			
			call.add(Executors.callable(new SingleSimulationFixedGene(simbuilder,gotrans,snps,genrep,candGeneCount,progReporter,i)));
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
        this.logger.info("Estimating significance of the candidate SNPs");
        HashMap<GOEntry,Integer> candidateGOcategories=gotrans.translateToCount(new ArrayList<String>(candGeneids));
        GOResultContainer gores=simcont.estimateSignificance(candidateGOcategories);
        
        // FDR simulation

        FdrSimulationContainer fdrsim=new FdrSimulationContainer(simcont.getAveragePvalueDistribution(),simulations);
        this.logger.info("Starting FDR correction");
        FdrSimulatedAdjuster fdrCorrector=new FdrSimulatedAdjuster(fdrsim);
        gores.updateMultipleTesting(fdrCorrector);
        
        
        // Max results
        /*
        ArrayList<String> allGenes= new ArrayList<String>(new HashSet<String>(genrep.getGeneidsForSnps(snps)));
        HashMap<GOEntry,Integer> maxGOcatCount=gotrans.translateToCount(allGenes,candidateSnps.size());
        GOResultContainer maxres=simcont.estimateSignificance(maxGOcatCount);
        gores.updateMaxResult(maxres);        
        */
        return gores;
        
	}

}

class SingleSimulationFixedGene implements Runnable
{
	private final GOSimulationContainer.GOSimulationContainerBuilder simbuilder;
	private final GOTranslator gotrans;
	private final ArrayList<Snp> snps;
	private final IGenomeRepresentation genrep;
	private final Random randgen;


	private final int candGeneCount;
	private final gowinda.misc.GowindaProgressReporter progReporter;
	public SingleSimulationFixedGene(GOSimulationContainer.GOSimulationContainerBuilder simbuilder,GOTranslator gotrans, ArrayList<Snp> snps, IGenomeRepresentation genrep, int candGeneCount,
			gowinda.misc.GowindaProgressReporter progReporter, int instanceCount)
	{
		this.simbuilder=simbuilder;
		this.gotrans=gotrans;
		this.snps=snps;
		this.candGeneCount=candGeneCount;
		this.genrep=genrep;
		this.progReporter=progReporter;
		this.randgen=new Random(System.currentTimeMillis()+instanceCount);

	}
	
	@Override
	public void run()
	{
		int snpcount=this.snps.size();
		HashSet<String> geneids=new HashSet<String>();
		while(geneids.size()<candGeneCount)
		{
			int index = randgen.nextInt(snpcount);
			Snp s=snps.get(index);
			ArrayList<String> snpgeneids=genrep.getGeneidsForSnp(s);
			geneids.addAll(snpgeneids);
		}
		
		HashMap<GOEntry,Integer> gocatfound=gotrans.translateToCount(new ArrayList<String>(geneids));
		simbuilder.addSimulation(gocatfound);
		progReporter.increment();
	}
}

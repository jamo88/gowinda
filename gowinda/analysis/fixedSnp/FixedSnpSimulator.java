package gowinda.analysis.fixedSnp;

import gowinda.analysis.GOCategoryContainer;
import gowinda.analysis.GOEntry;
import gowinda.analysis.GOResultContainer;
import gowinda.analysis.GOSimulationContainer;
import gowinda.analysis.GOTranslator;
import gowinda.analysis.IGOSimulator;
import gowinda.analysis.IGenomeRepresentation;
import gowinda.analysis.Snp;
import gowinda.analysis.fdr.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;

public class FixedSnpSimulator implements IGOSimulator {
	
	private final IGenomeRepresentation genrep;
	private final GOCategoryContainer goentries;
	private final ArrayList<Snp> snps;
	private final ArrayList<Snp> candidateSnps;
	private java.util.logging.Logger logger;
	
	public FixedSnpSimulator(IGenomeRepresentation genrep, GOCategoryContainer goentries, ArrayList<Snp> snps, ArrayList<Snp> candidateSnps, java.util.logging.Logger logger)
	{
		this.logger=logger;
		this.genrep=genrep;
		this.goentries=goentries;
		this.snps=snps;
		this.candidateSnps=candidateSnps;

	}
	
	@Override
	public GOResultContainer getSimulationResults(int simulations, int threads)
	{
        // SIMULATIONS
        // Get simulation results
		int candidateCount=this.candidateSnps.size();
		this.logger.info("Starting " +simulations+ " simulations for " +candidateCount+ " candidate SNPs using " + threads +" threads");
		this.logger.info("Will randomly draw SNPs unless the number of random SNPs equals the number of candidate SNPs");
		this.logger.info("This may take a while. Switch to the detailed log mode if you want to see the progress");
		
		GOTranslator gotrans=new GOTranslator(this.goentries);
		gowinda.misc.GowindaProgressReporter progReporter=new gowinda.misc.GowindaProgressReporter(10000,this.logger);
		GOSimulationContainer.GOSimulationContainerBuilder simbuilder=new GOSimulationContainer.GOSimulationContainerBuilder();
		ExecutorService executor=Executors.newFixedThreadPool(threads);
		
		ArrayList<Callable<Object>> call=new ArrayList<Callable<Object>>();
		for(int i=0; i<simulations; i++)
		{
			
			call.add(Executors.callable(new SingleSimulationFixedSnp(simbuilder,gotrans,snps,genrep,candidateCount,progReporter,i)));
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
        ArrayList<String> candGeneids= genrep.getGeneidsForSnps(candidateSnps);
        HashMap<GOEntry,Integer> candidateGOcategories=gotrans.translateToCount(candGeneids);
        GOResultContainer gores=simcont.estimateSignificance(candidateGOcategories);
        
        //Fdr correction
        FdrSimulationContainer fdrsim=new FdrSimulationContainer(simcont.getAveragePvalueDistribution(),simulations);
        this.logger.info("Starting FDR correction");
        gores.updateMultipleTesting(new FdrSimulatedAdjuster(fdrsim));
   
        
        // Max results
        /*
        ArrayList<String> allGenes= genrep.getGeneidsForSnps(snps);
        HashMap<GOEntry,Integer> maxGOcatCount=gotrans.translateToCount(allGenes,candidateCount);
        GOResultContainer maxres=simcont.estimateSignificance(maxGOcatCount);
        gores.updateMaxResult(maxres); 
        */
        return gores;
	}

}

class SingleSimulationFixedSnp implements Runnable
{
	private final GOSimulationContainer.GOSimulationContainerBuilder simbuilder;
	private final GOTranslator gotrans;

	private final ArrayList<Snp> snps;
	private final IGenomeRepresentation genrep;
	private final int snpcount;
	private final int candCount;
	private final gowinda.misc.GowindaProgressReporter progReporter;
	private final Random randgen;
	public SingleSimulationFixedSnp(GOSimulationContainer.GOSimulationContainerBuilder simbuilder,GOTranslator gotrans, ArrayList<Snp> snps,
			IGenomeRepresentation genrep, int candCount, gowinda.misc.GowindaProgressReporter progReporter, int instanceCount)
	{
		this.simbuilder=simbuilder;
		this.gotrans=gotrans;
		this.snps=snps;
		this.snpcount=this.snps.size();
		this.candCount=candCount;
		this.genrep=genrep;
		this.progReporter=progReporter;
		this.randgen=new Random(System.currentTimeMillis()+instanceCount);
	}
	
	@Override
	public void run()
	{
		HashSet<Integer> randSnpPos=new HashSet<Integer>();
		while(randSnpPos.size()<candCount)
		{
			int index=randgen.nextInt(snpcount);
			randSnpPos.add(index);
		}
		
		assert(randSnpPos.size()==candCount);
		
		ArrayList<Snp> randSnps=new ArrayList<Snp>(randSnpPos.size());
		for(Integer i: randSnpPos)
		{
			randSnps.add(snps.get(i));
		}
		
		ArrayList<String> geneids=genrep.getGeneidsForSnps(randSnps);
		HashMap<GOEntry,Integer> gocatfound=gotrans.translateToCount(geneids);
		simbuilder.addSimulation(gocatfound);
		progReporter.increment();
	}
	

}

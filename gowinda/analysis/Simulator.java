package gowinda.analysis;

import java.util.*;
import java.util.concurrent.*;


public class Simulator {
	private int threads=8;
	private int simulations;
	private int candidateCount;
	private ArrayList<Snp> snps;
	private SnpsToGeneidTranslator snptrans;
	private GOTranslator gotrans;
	private java.util.logging.Logger logger;
	private TaskCounter progres;
	
	public Simulator( ArrayList<Snp> snps, SnpsToGeneidTranslator snptrans, int threads, int simulations, int candidateCount, GOTranslator gotrans,
			java.util.logging.Logger logger)
	{
		this.candidateCount=candidateCount;
		this.logger=logger;
		this.threads=threads;
		this.simulations=simulations;
		this.snps=snps;
		this.snptrans=snptrans;
		this.gotrans=gotrans;
		this.logger=logger;
		this.progres=new TaskCounter(this.logger,10000);
	}
	
	
	public GOSimulationContainer simulate()
	{
		this.logger.info("Starting " +this.simulations+ " simulations for " +this.candidateCount+ " candidate SNPs using " + this.threads +" threads");
		this.logger.info("This may take a while. Switch to the detailed log mode if you want to see the progress");
		GOSimulationContainer.GOSimulationContainerBuilder simbuilder=new GOSimulationContainer.GOSimulationContainerBuilder();
		ExecutorService executor=Executors.newFixedThreadPool(this.threads);
		
		ArrayList<Callable<Object>> call=new ArrayList<Callable<Object>>();
		for(int i=0; i<this.simulations; i++)
		{
			
			call.add(Executors.callable(new SingleSimulation(simbuilder,this.gotrans,snps,this.snptrans,this.candidateCount,this.progres)));
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
		return simbuilder.getSimulationResults();
	}
	//Math.random() is thread safe, can be called from many threads at the same time
	// However, if many threads require random numbers in large numbers it may be more appropriate if every thread
	// has it's own random number generator Random rand=new Random() rand.nextInt(111)
}


class SingleSimulation implements Runnable
{
	private final GOSimulationContainer.GOSimulationContainerBuilder simbuilder;
	private final GOTranslator gotrans;
	private final Random randgen=new Random();
	private final ArrayList<Snp> snps;
	private final SnpsToGeneidTranslator snptrans;
	private final int snpcount;
	private final int candCount;
	private final TaskCounter progres;
	public SingleSimulation(GOSimulationContainer.GOSimulationContainerBuilder simbuilder,GOTranslator gotrans, ArrayList<Snp> snps, SnpsToGeneidTranslator snptrans, int candCount,TaskCounter progres)
	{
		this.simbuilder=simbuilder;
		this.gotrans=gotrans;
		this.snps=snps;
		this.snpcount=this.snps.size();
		this.candCount=candCount;
		this.snptrans=snptrans;
		this.progres=progres;
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
		
		ArrayList<String> geneids=snptrans.translate(randSnps);
		HashMap<GOEntry,Integer> gocatfound=gotrans.translate(geneids);
		simbuilder.addSimulation(gocatfound);
		progres.increment();
	}
}

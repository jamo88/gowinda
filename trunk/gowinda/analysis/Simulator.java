package gowinda.analysis;

import java.util.*;


public class Simulator {
	private int threads=8;
	private int simulations;
	private int candidateCount;
	private ArrayList<Snp> snps;
	private SnpsToGeneidTranslator snptrans;
	private GOTranslator gotrans;
	private java.util.logging.Logger logger;
	
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
	}
	
	
	public GOSimulationContainer simulate()
	{
		this.logger.info("Starting " +this.simulations+ " simulations for " +this.candidateCount+ " candidate SNPs using " + this.threads +" threads");
		this.logger.info("This may take a while. Switch to the detailed log mode if you want to see the progress");
		GOSimulationContainer simres=new GOSimulationContainer(this.simulations);

	
		
		for(int i=0; i<this.simulations; i++)
		{
			try
			{
				if(Thread.activeCount()>=this.threads) {
					Thread.sleep(1);
				}
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
				System.exit(0);
			}
			Thread t=new Thread(new SingleSimulation(simres,this.gotrans,snps,this.snptrans,this.candidateCount));
			t.start();
			if(i>0 && i%10000==0) logger.fine("Finished "+i+" simulations");
		}

		
		try
		{
			if(Thread.activeCount()>1) {
				Thread.sleep(1);
			}
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		this.logger.info("Finished simulations");
		return simres;
	}
	//Math.random() is thread safe, can be called from many threads at the same time
	// However, if many threads require random numbers in large numbers it may be more appropriate if every thread
	// has it's own random number generator Random rand=new Random() rand.nextInt(111)
}


class SingleSimulation implements Runnable
{
	private GOSimulationContainer gores;
	private GOTranslator gotrans;
	private Random randgen=new Random();
	private ArrayList<Snp> snps;
	private SnpsToGeneidTranslator snptrans;
	int snpcount;
	private int candCount;
	public SingleSimulation(GOSimulationContainer gores,GOTranslator gotrans, ArrayList<Snp> snps, SnpsToGeneidTranslator snptrans, int candCount)
	{
		this.gores=gores;
		this.gotrans=gotrans;
		this.snps=snps;
		this.snpcount=this.snps.size();
		this.candCount=candCount;
		this.snptrans=snptrans;
	}
	
	@Override
	public void run()
	{
		// Randomly draw a number of SNPs
		HashSet<Snp> randSnps=new HashSet<Snp>();
		while(randSnps.size()<candCount)
		{
			int index=randgen.nextInt(snpcount);
			randSnps.add(snps.get(index));
		}
		
		ArrayList<String> geneids=snptrans.translate(new ArrayList<Snp>(randSnps));
		HashMap<GOEntry,Integer> gocatfound=gotrans.translate(geneids);
		gores.addSimulation(gocatfound);
	}
}

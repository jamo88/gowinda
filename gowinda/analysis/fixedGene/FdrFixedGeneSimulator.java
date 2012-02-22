package gowinda.analysis.fixedGene;

import gowinda.analysis.*;
import gowinda.analysis.fdr.IMultipleTestingAdjuster;
import gowinda.analysis.fdr.FdrSimulationContainer;

import java.util.*;

public class FdrFixedGeneSimulator{

	private final FdrSimulationContainer fdrSim;
	
	public FdrFixedGeneSimulator(GOSimulationContainer gosimres, IGenomeRepresentation genrep, GOTranslator gotrans, ArrayList<Snp> snps, int candCount,
			int threads, int simulations)
	{

		this.fdrSim=getFdrSimulationContainer(gosimres, genrep, gotrans,snps,candCount,threads,simulations);
		
	}
	

	
	@Override
	public ArrayList<Double> getAdjustedSignificance(ArrayList<Double> uncorrected)
	{
		ArrayList<Double>() toret =new ArrayList<Double>();
		for
		
	}
	
	
	private FdrSimulationContainer getFdrSimulationContainer(GOSimulationContainer gosimres, IGenomeRepresentation genrep, GOTranslator gotrans, ArrayList<Snp> snps, int candCount,
			int threads, int simulations)
	{
		
	}

}

class SingleFdrFixedGeneSimulation implements Runnable
{
	private final FdrSimulationContainer.FdrSimulationContainerBuilder fdrbuilder;
	private final ArrayList<Snp> snps;
	private final int candCount;
	private final GOSimulationContainer gosimres;
	private final GOTranslator gotrans;
	private Random rand;
	
	public SingleFdrFixedGeneSimulation(FdrSimulationContainer.FdrSimulationContainerBuilder fdrbuilder, GOSimulationContainer gosimres,
			GOTranslator gotrans, ArrayList<Snp> snps, int candCount, int instanceCount)
	{
		this.fdrbuilder=fdrbuilder;
		this.snps=snps;
		this.candCount=candCount;
		this.gosimres=gosimres;
		this.gotrans=gotrans;
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
	}
}

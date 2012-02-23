/*
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda;

import java.util.*;

import gowinda.io.*;
import gowinda.analysis.*;
import gowinda.analysis.fixedGene.FixedGeneSimulator;
import gowinda.analysis.fixedSnp.FixedSnpSimulator;

import java.io.*;

/**
 *
 * @author robertkofler
 */
public class SimulationAnalyzer implements IAnalyze {

	private final String outputFile;
    private final String annotationFile;
    private final String snpFile;
    private final String candidateSnpFile;
    private final String goAssociationFile;
    private final int simulations;
    private final int threads;
    private final double minsignificance;
    private final gowinda.misc.SimulationMode simulationMode;
    private final gowinda.misc.GeneDefinition geneDefinition;
    private java.util.logging.Logger logger;
    public SimulationAnalyzer(String outputFile, String annotationFile, String snpFile, String candidateSnpFile, String goAssociationFile, int simulations, 
            int threads, float significance, gowinda.misc.SimulationMode simulationMode,gowinda.misc.GeneDefinition geneDefinition, 
            java.util.logging.Logger logger)
    {
        if(!new File(annotationFile).exists()){throw new IllegalArgumentException("Annotation file does not exist");}
        if(!new File(snpFile).exists()){throw new IllegalArgumentException("File with all SNPs of the species does not exist");}
        if(!new File(candidateSnpFile).exists()){throw new IllegalArgumentException("Candidate SNP file does not exist");}
        if(!new File(goAssociationFile).exists()){throw new IllegalArgumentException("GO association file does not exist");}
        if(simulations<1){throw new IllegalArgumentException("Number of simulations must be larger than 0");}
        if(significance<=0 || significance>1){throw new IllegalArgumentException("Significance must be larger than 0 and smaller or equal than 1; Provided: "+significance);}
        if(threads<1) throw new IllegalArgumentException("Number of threads needs to be larger than zero");
        try{
        	new FileWriter(outputFile);
        }
        catch(IOException e)
        {
        	throw new IllegalArgumentException("Can not create output file:" +outputFile);
        }
        
        
        
        // set the variables
        this.threads=threads;
        this.outputFile=outputFile;
        this.annotationFile=annotationFile;
        this.snpFile=snpFile;
        this.candidateSnpFile=candidateSnpFile;
        this.goAssociationFile=goAssociationFile;
        this.simulations=simulations;
        this.minsignificance=significance;
        this.logger=java.util.logging.Logger.getAnonymousLogger();
        this.logger.setUseParentHandlers(false);
        this.simulationMode=simulationMode;
        this.geneDefinition=geneDefinition;
        this.logger=logger;
    }
    

    
    @Override
    public int startAnalysis()
    {
        
                
        // PREPARATION reading files
        // Read the genome annotation
        IGenomeRepresentation genrep=(new GenomeRepresentationBuilder(this.annotationFile,this.geneDefinition,this.logger)).getGenomeRepresentation();
        // Read GO file and obtain a GO translator (geneid -> GO term)
        GOCategoryContainer goentries= new GOEntryBulkReader(this.goAssociationFile,this.logger).readGOEntries();
        // Read the SNPs
        ArrayList<Snp> snps= new SnpBulkReader(this.snpFile,this.logger).getSnps();
        ArrayList<Snp> candidateSnps=new SnpBulkReader(this.candidateSnpFile,this.logger).getSnps();
        
        // Validate content of the files
        new GeneidCrossValidator(genrep,goentries,this.logger).validate();
        new SnpCrossValidator(snps,candidateSnps,this.logger).validate();
        SnpCoverageValidator covVal=new SnpCoverageValidator(genrep,snps,goentries,this.logger);
        covVal.validate();
        int goCatPossible=covVal.getCoveredGOCount();
        
        //Simulate
        IGOSimulator gosimulator=getGOSimulator(this.simulationMode,genrep,goentries,snps,candidateSnps,this.logger);
        GOResultContainer gores  =gosimulator.getSimulationResults(this.simulations,this.threads);
    
        logger.info("Simulations detected SNPs in genes corresponding to "+gores.getSimulationContainer().size() +" GO categories, out of " + goCatPossible+ " possible ones (corresponding to genes having at least one SNP)");
        logger.info("Candidate SNPs show an overlap with "+ gores.size() + " GO categories");
        
        //gene_ids
        ArrayList<String> candGeneids=new ArrayList<String>(new HashSet<String>(genrep.getGeneidsForSnps(candidateSnps)));
        gores.updateGeneids(new GOTranslator(goentries).translateToGeneids(candGeneids));
        ArrayList<String> maxGeneids=new ArrayList<String>(new HashSet<String>(genrep.getGeneidsForSnps(snps)));
        gores.updateMaxsnpGeneids(new GOTranslator(goentries).translateToGeneids(maxGeneids));
        gores.updateMaxGeneids(goentries);
        
        // Write results to output file
        new GOResultWriter(this.outputFile,gores,this.minsignificance,this.logger).writeAll();
        this.logger.info("FINISHED - Thank you for using Gowinda");
        
        return 1;
        
        




    }
    
    private IGOSimulator getGOSimulator(gowinda.misc.SimulationMode mode, IGenomeRepresentation genrep, GOCategoryContainer goentries, ArrayList<Snp> snps, ArrayList<Snp> candidateSnps,
    		java.util.logging.Logger logger)
    {
    	if(mode==gowinda.misc.SimulationMode.FixGene)
    	{
    		return new FixedGeneSimulator(genrep,goentries,snps,candidateSnps,logger);
    	}
    	else if(mode==gowinda.misc.SimulationMode.FixSnp)
    	{
    		return new FixedSnpSimulator(genrep,goentries,snps,candidateSnps,logger);
    	}
    	else
    	{
    		throw new IllegalArgumentException("Do not recognise Gowinda simulation mode "+mode);
    	}
    	
    }
        
    
    



    
}

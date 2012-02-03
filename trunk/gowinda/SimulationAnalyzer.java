/*
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import gowinda.io.*;
import gowinda.analysis.*;

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
    private final String statOutputFile;
    private final int simulations;
    private final int threads;
    private final double minsignificance;
    private final gowinda.misc.CountingUnit unit;
    private final gowinda.misc.GeneDefinition geneDefinition;
    private final boolean geneDefinitionSampling;
    private java.util.logging.Logger logger;
    private gowinda.misc.GenomeRepOptimize toOptimize;
    public SimulationAnalyzer(String outputFile, String annotationFile, String snpFile, String candidateSnpFile, String goAssociationFile, String statOutputFile, int simulations, 
            int threads, float significance, gowinda.misc.CountingUnit unit,gowinda.misc.GeneDefinition geneDefinition, boolean geneDefinitionSampling, 
            gowinda.misc.GenomeRepOptimize toOptimize, java.util.logging.Logger logger)
    {
        if(!new File(annotationFile).exists()){throw new IllegalArgumentException("Annotation file does not exist");}
        if(!new File(snpFile).exists()){throw new IllegalArgumentException("File with all SNPs of the species does not exist");}
        if(!new File(candidateSnpFile).exists()){throw new IllegalArgumentException("Candidate SNP file does not exist");}
        if(!new File(goAssociationFile).exists()){throw new IllegalArgumentException("GO association file does not exist");}
        if(simulations<1){throw new IllegalArgumentException("Number of simulations must be larger than 0");}
        if(significance<0 || significance>1){throw new IllegalArgumentException("Significance must be between 0 and 1");}
        if(threads<1) throw new IllegalArgumentException("Number of threads needs to be larger than zero");

        
        // set the variables
        this.threads=threads;
        this.outputFile=outputFile;
        this.annotationFile=annotationFile;
        this.snpFile=snpFile;
        this.candidateSnpFile=candidateSnpFile;
        this.goAssociationFile=goAssociationFile;
        this.statOutputFile=statOutputFile;
        this.simulations=simulations;
        this.minsignificance=significance;
        this.logger=java.util.logging.Logger.getAnonymousLogger();
        this.logger.setUseParentHandlers(false);
        this.unit=unit;
        this.geneDefinition=geneDefinition;
        this.geneDefinitionSampling=geneDefinitionSampling;
        this.logger=logger;
        this.toOptimize=toOptimize;
    }
    

    
    @Override
    public int startAnalysis()
    {
        
        
        logger.info("No file with precomputed statistics has been provided. Starting 'de novo' simulations"); 
        
        // PREPARATION reading files
        // Read the genome annotation
        IGenomeRepresentation genrep=(new GenomeRepresentationBuilder(this.annotationFile,this.geneDefinition,this.toOptimize,this.logger)).getGenomeRepresentation();
        // Read GO file and obtain a GO translator (geneid -> GO term)
        GOTranslator gotrans=new GOTranslator(new GOEntryBulkReader(this.goAssociationFile,this.logger),this.unit,this.logger);
        // Crosscheck GO-IDs and Geneids
        gotrans.validateGeneids(genrep.allGeneids());
        
        // Read the SNPs
        ISnpBulkReader allSnpReader=new SnpBulkReader(this.snpFile,this.logger);
        ISnpBulkReader candidateSnpReader=new SnpBulkReader(this.candidateSnpFile,this.logger);
        ArrayList<Snp> snps= geneDefinitionSampling?(new SnpGenicFilterReader(allSnpReader,genrep,this.logger)).getSnps() :allSnpReader.getSnps();
        ArrayList<Snp> candidateSnps=geneDefinitionSampling?(new SnpGenicFilterReader(candidateSnpReader,genrep,this.logger)).getSnps():candidateSnpReader.getSnps();
        

        // SIMULATIONS
        // Get simulation results
        SnpsToGeneidTranslator snptrans=new SnpsToGeneidTranslator(genrep);
        GOSimulationContainer simres =new Simulator(snps,snptrans,this.threads,this.simulations,candidateSnps.size(),gotrans,this.logger).simulate();
        
        // estimate significance of candidates: simres.estimateSignificance(candres);
        this.logger.info("Estimating significance of the candidate SNPs");
        HashMap<GOEntry,Integer> candidateGOcategories=gotrans.translate(snptrans.translate(candidateSnps));
        ArrayList<GOResultForCandidateSnp> significance=simres.estimateSignificance(candidateGOcategories);
        new GOResultWriter(this.outputFile,significance,this.minsignificance,this.logger).writeAll();
        this.logger.info("FINISHED - Thank you for using Gowinda");
        
        return 1;
    }
        
    
    



    
}

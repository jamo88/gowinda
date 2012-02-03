package gowinda.misc;

import java.util.*;

/**
 *
 * @author robertkofler
 */
public class CommandLineParser {
        
    public static CommandLineArguments getCommandLineArguments(String[] arguments)
    {
        LinkedList<String> args=new LinkedList<String>(Arrays.asList(arguments));
        String snpFile="";
        String goFile="";
        String candidateFile="";
        String gtfFile="";
        String statInputFile="";
        String statOutputFile="";
        String genedefstr="";
        String unitString="";
        String genRepString="";
        String outputFile="";
        int simulations=0;
        int threads=1;
        float significance=1;
        boolean displayHelp=false;
        boolean onlyGenedefSnps=false;
        boolean debugmode=false;
        gowinda.misc.CountingUnit countunit=gowinda.misc.CountingUnit.Gene;
        gowinda.misc.GeneDefinition geneDef=gowinda.misc.GeneDefinition.Exon;
        gowinda.misc.GenomeRepOptimize toOptimize=gowinda.misc.GenomeRepOptimize.Memory;
        
        // Parse the command line arguments
        // order does not matter
        while(args.size()>0)
        {
            String cu=args.remove(0);
            if(cu.equals("--snp-file"))
            {
                snpFile=args.remove(0);
            }
            else if(cu.equals("--candidate-snp-file"))
            {
                candidateFile=args.remove(0);
            }        
            else if(cu.equals("--go-association-file"))
            {
                goFile=args.remove(0);
            }
            else if(cu.equals("--gtf-file"))
            {
                gtfFile=args.remove(0);
            }
//            else if(cu.equals("--statistic-input-file"))
//            {
//                statInputFile=args.remove(0);
//            }
//            else if(cu.equals("--statistic-output-file"))
//            {
//                statOutputFile=args.remove(0);
//            }
            else if(cu.equals("--simulations"))
            {
                simulations=Integer.parseInt(args.remove(0));
            }
            else if(cu.equals("--min-significance"))
            {
                significance=Float.parseFloat(args.remove(0));
            }
            else if(cu.equals("--gene-definition"))
            {
                genedefstr=args.remove(0);
            }
            else if(cu.equals("--unit"))
            {
                unitString=args.remove(0);
            }
            else if(cu.equals("--help"))
            {
               displayHelp=true;
            }
            else if(cu.equals("--gene-definition-sampling"))
            {
                onlyGenedefSnps=true;
            }
            else if(cu.equals("--detailed-log"))
            {
            	debugmode=true;
            }
            else if(cu.equals("--output-file"))
            {
            	outputFile=args.remove(0);
            }
            else if(cu.equals("--threads"))
            {
            	threads=Integer.parseInt(args.remove(0));
            }
            else
            {
                throw new IllegalArgumentException("Do not recognize command line option "+cu);
            }
            
        }
        if(!genRepString.equals(""))toOptimize=getGenomeRepresentation(genRepString);
        if(!unitString.equals(""))countunit=getCountingUnit(unitString);
        if(!genedefstr.equals(""))geneDef=getGeneDefinition(genedefstr);
        return new CommandLineArguments(outputFile, statInputFile, statOutputFile, gtfFile,snpFile,
        		candidateFile,goFile,simulations,threads,significance,countunit,geneDef,toOptimize,displayHelp,debugmode,onlyGenedefSnps);
    }
    
    
    
    public static String helpMessage()
    {
        StringBuilder sb=new StringBuilder();
        sb.append("Gowinda: GeneOntology analysis for Genome Wide Association Studies\n");
        sb.append("--output-file            the output file for the GO enrichment analysis\n");
        sb.append("--snp-file               a file containing all SNPs found in the species\n");
        sb.append("--candidate-snp-file     a file containing only the candidate SNPs\n");
        sb.append("--go-association-file    a file containing the association between gene-id and GO terms\n");
        sb.append("--gtf-file               a file containing the annotation of the species in the .gtf format\n");
        sb.append("                         only the gtf-entries 'CDS' and 'exon' will be used\n");
//        sb.append("--statistic-input-file   a file containing precomputed compressed simulation results\n");
//        sb.append("--statistic-output-file  compressed output file for the simulation results\n");
        sb.append("--simulations            the number of simulations\n");
        sb.append("--min-significance       the minimum significance of GO terms to report\n");
        sb.append("--unit                   the unit of the tests; gene | snp'\n");
        sb.append("--gene-definition        this is a major feature which allows to specify how a gene should\n");
        sb.append("		                    be defined for GO analysis: exon | cds | exonintron\n");
        sb.append("--gene-definition-sampling\n");
        sb.append("                         sampling of the SNPs will only be done for genic SNPs according to\n");
        sb.append("                         the '--gene-definition'\n");
        sb.append("--detailed-log           switch to detailed log messages");
        sb.append("--help                   Display a help message\n");
        
        return sb.toString();
    }
    
    private static gowinda.misc.GenomeRepOptimize getGenomeRepresentation(String genrepstr)
    {
    	if(genrepstr.toLowerCase().equals("cpu"))
    	{
    		return gowinda.misc.GenomeRepOptimize.Cpu;
    	}
    	else if(genrepstr.toLowerCase().equals("memory"))
    	{
    		return gowinda.misc.GenomeRepOptimize.Memory;
    	}
    	else
    	{
    		throw new IllegalArgumentException("Do not recognise optimization " +genrepstr);
    	}
    	
    }
    
    private static gowinda.misc.CountingUnit getCountingUnit(String unitString)
    {
           if((unitString.toLowerCase()).equals("gene"))
           {
               return gowinda.misc.CountingUnit.Gene;
           }
           else if((unitString.toLowerCase()).equals("snp"))
           {
                return gowinda.misc.CountingUnit.Snp;
           }
           else
           { 
                 throw new IllegalArgumentException("Do not recognise counting unit "+unitString);
           }
    }
    
    private static gowinda.misc.GeneDefinition getGeneDefinition(String geneDefString)
    {
        if(geneDefString.toLowerCase().equals("exon"))
        {
            return gowinda.misc.GeneDefinition.Exon;
        }
        else if(geneDefString.toLowerCase().equals("cds"))
        {
            return gowinda.misc.GeneDefinition.CDS;
        }
        else if(geneDefString.toLowerCase().equals("exonintron"))
        {
            return gowinda.misc.GeneDefinition.ExonIntron;
        }
        else
        {
            throw new IllegalArgumentException("Do not recognise Gene Definition "+geneDefString);
        }
        
    }
    

    
    
}

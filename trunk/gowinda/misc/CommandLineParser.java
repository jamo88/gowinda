package gowinda.misc;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.*;

import gowinda.misc.GeneDefinition;

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
        String outputFile="";
        int simulations=0;
        int threads=1;
        float significance=1;
        boolean displayHelp=false;
        boolean onlyGenedefSnps=false;
        boolean debugmode=false;
        gowinda.misc.SimulationMode countunit=gowinda.misc.SimulationMode.FixGene;
        GeneDefinition geneDef=GeneDefinition.Exon;

        
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
            else if(cu.equals("--annotation-file"))
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
            else if(cu.equals("--mode"))
            {
                unitString=args.remove(0);
            }
            else if(cu.equals("--help"))
            {
               displayHelp=true;
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

        if(!unitString.equals(""))countunit=getMode(unitString);
        if(!genedefstr.equals(""))geneDef=getGeneDefinition(genedefstr);
        return new CommandLineArguments(outputFile, statInputFile, statOutputFile, gtfFile,snpFile,
        		candidateFile,goFile,simulations,threads,significance,countunit,geneDef,displayHelp,debugmode,onlyGenedefSnps);
    }
    
    
    
    public static String helpMessage()
    {
        StringBuilder sb=new StringBuilder();
        sb.append("Gowinda: GeneOntology analysis for Genome Wide Association Studies\n");
        sb.append("--output-file            the output file for the GO enrichment analysis\n");
        sb.append("--snp-file               a file containing all SNPs found in the species\n");
        sb.append("--candidate-snp-file     a file containing only the candidate SNPs\n");
        sb.append("--go-association-file    a file containing the association between gene-id and GO terms\n");
        sb.append("--annotation-file        a file containing the annotation of the species in the .gtf or .gff format\n");
        sb.append("                         only the gtf-entries 'CDS' and 'exon' will be used\n");
        sb.append("--simulations            the number of simulations\n");
        sb.append("--min-significance       the minimum significance of GO terms to report\n");
        sb.append("--mode                   the simulation mode; gene | snp; default: gene'\n");
        sb.append("							'snp': the same number of SNPs as candidate SNPs will be sampled per simulation\n");
        sb.append("							'gene': the same number of genes as genes that are overlapping with a candidate SNP will be sampled per simulation\n");
        sb.append("--gene-definition        allows to specify which feature needs to overlap with a given SNP\n");
        sb.append("							in order to assign the SNP to a gene ID (thus later on to a GO category)\n");	
        sb.append("		                    'cds' SNPs overlapping with CDS\n");
        sb.append("							'exon' SNPs overlapping with exons (including UTRs)\n");
        sb.append("							'utr' SNPs overlapping with UTR regions\n");
        sb.append("							'gene' SNPs overlapping with exons and introns\n");	
        sb.append("							'upstream5000; like gene plus 5000bp upstream of gene, any number may be provided\n");
        sb.append("							'downsteam5000: like gene plus 5000bp downstream of gene, any number may be provided\n");
        sb.append("							'updownstream5000: like gene plus 5000bp downstream and upstream of the gene, any number may be provided\n");
        sb.append("--threads                number of threads to be used\n");
        sb.append("--detailed-log           switch to detailed log messages\n");
        sb.append("--help                   Display a help message\n");
        
        return sb.toString();
    }
   
    private static gowinda.misc.SimulationMode getMode(String unitString)
    {
           if((unitString.toLowerCase()).equals("gene"))
           {
               return gowinda.misc.SimulationMode.FixGene;
           }
           else if((unitString.toLowerCase()).equals("snp"))
           {
                return gowinda.misc.SimulationMode.FixSnp;
           }
           else
           { 
                 throw new IllegalArgumentException("Do not recognise counting unit "+unitString);
           }
    }
    
    private static GeneDefinition getGeneDefinition(String geneDefString)
    {
    	
    	String lowGD=geneDefString.toLowerCase();
        if(lowGD.equals("exon"))
        {
            return GeneDefinition.Exon;
        }
        else if(lowGD.equals("cds"))
        {
            return GeneDefinition.CDS;
        }
        else if(lowGD.equals("gene"))
        {
            return GeneDefinition.Gene;
        }
        else if(lowGD.equals("utr"))
        {
        	return GeneDefinition.UTR;
        }
        else if(lowGD.startsWith("upstream"))
        {
        	GeneDefinition gd=GeneDefinition.Upstream;
           	Pattern p=Pattern.compile("upstream(\\d+)");
        	Matcher m=p.matcher(lowGD);
        	if(!m.find()) throw new IllegalArgumentException("Please also provide the upstream distance, eg.: upstream500");
        	int distance=Integer.parseInt(m.group(1));
        	gd.setLength(distance);
        	return gd;
        }
        else if(lowGD.startsWith("downstream"))
        {
        	GeneDefinition gd=GeneDefinition.Downstream;
        	Pattern p=Pattern.compile("downstream(\\d+)");
        	Matcher m=p.matcher(lowGD);
        	if(!m.find()) throw new IllegalArgumentException("Please also provide the downstream distance, eg.: downstream500");
        	int distance=Integer.parseInt(m.group(1));
        	gd.setLength(distance);
        	return gd;
        }
        else if(lowGD.startsWith("updownstream"))
        {
        	GeneDefinition gd=GeneDefinition.UpDownstream;
        	Pattern p=Pattern.compile("updownstream(\\d+)");
        	Matcher m=p.matcher(lowGD);
          	if(!m.find()) throw new IllegalArgumentException("Please also provide the updownstream distance, eg.: updownstream500");
        	int distance=Integer.parseInt(m.group(1));
        	gd.setLength(distance);
        	return gd;
        }
        else
        {
            throw new IllegalArgumentException("Do not recognise Gene Definition "+geneDefString);
        }
        
    }
    

    
    
}

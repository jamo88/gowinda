/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda.analysis;


import gowinda.misc.GeneDefinition;
import gowinda.io.*;
import gowinda.misc.GenomeRepOptimize;
/**
 *
 * @author robertkofler
 */
public class GenomeRepresentationBuilder {
    private final String annotationFile;
    private final GeneDefinition geneDef;
    private final GenomeRepOptimize toOptimize;
    private java.util.logging.Logger logger;
    
    public GenomeRepresentationBuilder(String annotationFile, GeneDefinition geneDef, GenomeRepOptimize toOptimize, java.util.logging.Logger logger)
    {
        this.annotationFile=annotationFile;
        this.geneDef=geneDef;
        this.logger=logger;
        this.toOptimize=toOptimize;
    }
    
    public IGenomeRepresentation getGenomeRepresentation()
    {
    	// Read the gtf or the gff file
        IBulkAnnotationReader reader=getAnnotationFileReader();
        
        // add an decorator for the requested gene definition
        reader=getGenomeDefinitionReader(reader);
  
        // now obtain the Genome representation, pick either hash-based or list-based	
        IGenomeRepresentation genrep=getGenomeRepresentation(reader);
        return genrep;
    }
    private IGenomeRepresentation getGenomeRepresentation(IBulkAnnotationReader reader)
    {
    	if(this.toOptimize==GenomeRepOptimize.Cpu)
    	{
    		return new GenomeRepresentationHash(reader,this.logger);
    	}
    	else if(this.toOptimize==GenomeRepOptimize.Memory)
    	{
    		return new GenomeRepresentationList(reader,this.logger);
    	}
    	else
    	{
    		throw new IllegalArgumentException("Do not recognize otpimiziation "+this.toOptimize);
    	}
    }

    
    private IBulkAnnotationReader getAnnotationFileReader()
    {
    	this.logger.info("Estimating the file format of the annotation file (allowed extensions: .gtf and .gff)");
    	if(this.annotationFile.toLowerCase().endsWith("gtf"))
    	{
    		return new GtfReader(this.annotationFile,this.logger);
    	}	
    	else if(this.annotationFile.toLowerCase().endsWith("gff"))
    	{
    		return new GffReader(this.annotationFile, this.logger);
    	}
    	else
    	{
    		throw new IllegalArgumentException("Do not recognise file type: "+this.annotationFile+"; valid extensions are .gtf and .gff");
    	}
    }
    
    private IBulkAnnotationReader getGenomeDefinitionReader(IBulkAnnotationReader reader)
    {
        if(geneDef==GeneDefinition.Exon)
        {
        	return new ExonDecorator(reader,this.logger);
        	
        }
        else if(geneDef==GeneDefinition.CDS)
        {
        	return new CdsDecorator(reader,this.logger);
        }
        else if(geneDef==GeneDefinition.ExonIntron)
        {
        	return new ExIntDecorator(reader,this.logger);
        }
        else
        {
        	throw new IllegalArgumentException("Gene definition "+ geneDef + " not supported");
        }
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda.analysis;


import gowinda.misc.GeneDefinition;
import gowinda.io.*;
/**
 *
 * @author robertkofler
 */
public class GenomeRepresentationBuilder {
    private final String annotationFile;
    private final GeneDefinition geneDef;
    private java.util.logging.Logger logger;
    
    public GenomeRepresentationBuilder(String annotationFile, GeneDefinition geneDef, java.util.logging.Logger logger)
    {
        this.annotationFile=annotationFile;
        this.geneDef=geneDef;
        this.logger=logger;
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

    	return new GenomeRepresentationList(reader,this.logger);

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
        else if(geneDef==GeneDefinition.Gene)
        {
        	return new GeneDecorator(reader,this.logger);
        }
        else if(geneDef==GeneDefinition.UTR)
        {
        	return new UtrDecorator(reader,this.logger);
        }
        else if(geneDef==GeneDefinition.Upstream)
        {
        	return new UpstreamDecorator(reader,geneDef.getLength(),this.logger);
        }
        else if(geneDef==GeneDefinition.Downstream)
        {
        	return new DownstreamDecorator(reader,geneDef.getLength(),this.logger);
        }
        else if(geneDef==GeneDefinition.UpDownstream)
        {
        	return new UpDownstreamDecorator(reader,geneDef.getLength(),this.logger);
        }
        else
        {
        	throw new IllegalArgumentException("Gene definition "+ geneDef + " not supported");
        }
    }
    
}

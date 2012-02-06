package gowinda.io;

import java.util.*;
import gowinda.analysis.AnnotationEntry;
import java.util.logging.Logger;
/*
 * Obtains the region from the start of the first exon until the end of the last exon
 * This should contain the whole region of a gene spanning all exons and introns
 */
public class GeneDecorator implements IBulkAnnotationReader {

	private IBulkAnnotationReader bar;
	private Logger logger;
	public GeneDecorator(IBulkAnnotationReader bar,Logger logger)
	{
		// Filter only for exons; 
		//
		this.logger=logger;
		this.bar=new ExonDecorator(bar,logger);
	}
	
	@Override
	public ArrayList<AnnotationEntry> readAnnotation(){
	
		// obtain all exons (using the ExonDecorator)
		ArrayList<AnnotationEntry> exons=bar.readAnnotation();
		
		logger.info("Starting to build 'gene' entries, ranging from the start position of the first exon to the end position of the last exon");
		// group all exons with the same geneid
		HashMap<String,ArrayList<AnnotationEntry>> geneExons=new HashMap<String,ArrayList<AnnotationEntry>>();
		for(AnnotationEntry exon: exons)
		{
			String gn=exon.geneid();
			if(!geneExons.containsKey(gn))
			{
				geneExons.put(gn,new ArrayList<AnnotationEntry>());
			}
			geneExons.get(gn).add(exon);
		}
		
		// Create a new entry spanning the position from the start of the first exon to the end of the last exon
		ArrayList<AnnotationEntry> genes=new ArrayList<AnnotationEntry>();
		for (ArrayList<AnnotationEntry> exonList:geneExons.values())
		{
			AnnotationEntry gene=getGene(exonList);
			if(gene!=null)genes.add(gene);
		}
		// voila a list of genes
		logger.info("Finished - obtained " + genes.size()+" 'gene' entries");
		return genes;
	}
	
	private AnnotationEntry getGene(ArrayList<AnnotationEntry> exonList)
	{
		assert exonList.size()>0 :"Exon List must have non zero size";
		
		int start=-1;
		int end=-1;
		String geneID=null;
		String chromosome=null;
		AnnotationEntry.Strand strand=getStrand(exonList);
		if(strand==null) return null;
		for(AnnotationEntry e : exonList)
		{
			if(start==-1) start =e.start();
			if(end==-1)end=e.end();
			if(geneID==null) geneID=e.geneid();
			if(chromosome==null)chromosome=e.chromosome();
			if(strand==null)strand=e.strand();
			
			if(e.start()<start)start=e.start();
			if(e.end()>end)end=e.end();
			// perform some security checks, we can not group exons from different chromosomes or genes
			if(!geneID.equals(e.geneid())) throw new IllegalArgumentException("All exons of the same gene must have the same geneid"+e.toString());
			if(!chromosome.equals(e.chromosome())) throw new IllegalArgumentException("All exons of the same gene must be on the same chromosome:" +e.toString());
		}
		// Create the new Annotation entry
		return new AnnotationEntry(chromosome,"gene",start,end,strand,null,geneID);
	}
	
	/*
	 * Thank you Annotators!! Some Genes have exons from two strands some exons are on the plus some on the minus
	 * This should be two different genes!!! We take the majority vote..
	 */
	private AnnotationEntry.Strand getStrand(ArrayList<AnnotationEntry> exonList)
	{
		int plusCount=0;
		int minusCount=0;
		for(AnnotationEntry e: exonList)
		{
			if(e.strand()==AnnotationEntry.Strand.Minus) minusCount++;
			else if(e.strand()==AnnotationEntry.Strand.Plus) plusCount++;
			else throw new IllegalArgumentException("Invalid strand: "+e);
		}
		if(plusCount>minusCount) return AnnotationEntry.Strand.Plus;
		else if(plusCount<minusCount) return AnnotationEntry.Strand.Minus;
		else return null;
		
	}
	
	
}

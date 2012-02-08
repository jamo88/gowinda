package gowinda.io;

import java.util.*;
import gowinda.analysis.AnnotationEntry;
import java.util.logging.Logger;

public class UpstreamDecorator implements IBulkAnnotationReader {


	private ArrayList<AnnotationEntry> genes;
	private Logger logger;
	private int distance;
	public UpstreamDecorator(IBulkAnnotationReader bar,int distance, Logger logger)
	{
		// Filter only for exons; 
		//
		this.logger=logger;
		GeneDecorator ed=new GeneDecorator(bar,logger);
		this.genes=ed.readAnnotation();
		this.distance=distance;
	}
	public UpstreamDecorator(ArrayList<AnnotationEntry> genes,int distance)
	{
		this.genes=genes;
		this.logger=java.util.logging.Logger.getLogger("Gowinda Logger");
		logger.setUseParentHandlers(false);
		this.distance=distance;
	}
	

	
	@Override
	public ArrayList<AnnotationEntry> readAnnotation(){
	
		// obtain all exons (using the ExonDecorator)
		
		
		logger.info("Starting to build 'upstream+gene' regions, using a distance of " +this.distance+"bp");
		
		ArrayList<AnnotationEntry> geneUpstream=new ArrayList<AnnotationEntry>();
		for(AnnotationEntry gene: genes)
		{
			int start=gene.start();
			int end=gene.end();
			if(gene.strand()==AnnotationEntry.Strand.Plus)
			{
				start=start-this.distance;
				if(start<1) start=1;
			}
			else if(gene.strand()==AnnotationEntry.Strand.Minus)
			{
				end=end + this.distance;
			}
			else
			{
				throw new IllegalArgumentException("Do not recognise strand"+gene.strand());
			}
			geneUpstream.add(new AnnotationEntry(gene.chromosome(),"upstream+gene",start,end,gene.strand(),gene.frameshift(),gene.geneid()));
		}
		logger.info("Finished - obtained " + geneUpstream.size()+" 'upstream+gene' entries");
		return geneUpstream;
	}
	

}

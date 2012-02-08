package gowinda.io;

import java.util.*;
import gowinda.analysis.AnnotationEntry;
import java.util.logging.Logger;

public class DownstreamDecorator implements IBulkAnnotationReader {


	private ArrayList<AnnotationEntry> genes;
	private Logger logger;
	private int distance;
	public DownstreamDecorator(IBulkAnnotationReader bar,int distance, Logger logger)
	{
		// Filter only for exons; 
		//
		this.logger=logger;
		GeneDecorator ed=new GeneDecorator(bar,logger);
		this.genes=ed.readAnnotation();
		this.distance=distance;
	}
	public DownstreamDecorator(ArrayList<AnnotationEntry> genes,int distance)
	{
		this.genes=genes;
		this.distance=distance;
		this.logger=java.util.logging.Logger.getLogger("Gowinda Logger");
		logger.setUseParentHandlers(false);
	}
	

	
	@Override
	public ArrayList<AnnotationEntry> readAnnotation(){
	
		
		
		logger.info("Starting to build 'downstream+gene' regions, using a distance of " +this.distance+"bp");
		
		ArrayList<AnnotationEntry> geneUpstream=new ArrayList<AnnotationEntry>();
		for(AnnotationEntry gene: genes)
		{
			int start=gene.start();
			int end=gene.end();
			if(gene.strand()==AnnotationEntry.Strand.Plus)
			{
				end=end + this.distance;
			}
			else if(gene.strand()==AnnotationEntry.Strand.Minus)
			{
				start=start-this.distance;
				if(start<1) start=1;
			}
			else
			{
				throw new IllegalArgumentException("Do not recognise strand"+gene.strand());
			}
			geneUpstream.add(new AnnotationEntry(gene.chromosome(),"downstream+gene",start,end,gene.strand(),gene.frameshift(),gene.geneid()));
		}
		logger.info("Finished - obtained " + geneUpstream.size()+" 'downstream+gene' entries");
		return geneUpstream;
	}
	

}

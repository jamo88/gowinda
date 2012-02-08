package gowinda.io;

import java.util.*;
import gowinda.analysis.AnnotationEntry;
import java.util.logging.Logger;

public class UpDownstreamDecorator implements IBulkAnnotationReader {


	private ArrayList<AnnotationEntry> genes;
	private Logger logger;
	private int distance;
	public UpDownstreamDecorator(IBulkAnnotationReader bar,int distance, Logger logger)
	{
		// Filter only for exons; 
		//
		this.logger=logger;
		GeneDecorator ed=new GeneDecorator(bar,logger);
		this.genes=ed.readAnnotation();
		this.distance=distance;
	}
	public UpDownstreamDecorator(ArrayList<AnnotationEntry> genes, int distance)
	{
		this.genes=genes;
		this.distance=distance;
		this.logger=java.util.logging.Logger.getLogger("Gowinda Logger");
		logger.setUseParentHandlers(false);
	}
	

	
	@Override
	public ArrayList<AnnotationEntry> readAnnotation(){
	
		// obtain all exons (using the ExonDecorator)
		
		
		logger.info("Starting to build 'upstream+downstream+gene' regions, using a distance of " +this.distance+"bp");
		
		ArrayList<AnnotationEntry> geneUpstream=new ArrayList<AnnotationEntry>();
		for(AnnotationEntry gene: genes)
		{
			int start=gene.start();
			int end=gene.end();

				start=start-this.distance;
				if(start<1) start=1;
				end=end + this.distance;
			
			geneUpstream.add(new AnnotationEntry(gene.chromosome(),"upstream+downstream+gene",start,end,gene.strand(),gene.frameshift(),gene.geneid()));
		}
		logger.info("Finished - obtained " + geneUpstream.size()+" 'upstream+downstream+gene' entries");
		return geneUpstream;
	}
	

}

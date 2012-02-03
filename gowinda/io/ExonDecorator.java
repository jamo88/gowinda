package gowinda.io;
import java.util.*;
import gowinda.analysis.AnnotationEntry;
import java.util.logging.Logger;

public class ExonDecorator implements IBulkAnnotationReader {

	private static String toFilter="exon";
	private Logger logger;
	private IBulkAnnotationReader bar;
	public ExonDecorator(IBulkAnnotationReader bar,Logger logger)
	{
		this.bar=bar;
		this.logger=logger;
	}
	@Override
	public ArrayList<AnnotationEntry> readAnnotation(){
	

		ArrayList<AnnotationEntry> aes=bar.readAnnotation();
		ArrayList<AnnotationEntry> filterset=new ArrayList<AnnotationEntry>();

		logger.info("Start filtering for annotation entries with the feature: "+toFilter);
		for(AnnotationEntry ae : aes)
		{
			if(ae.feature().equals(toFilter))
			{
				filterset.add(ae);
			}
		}
		logger.info("Finished - obtained " + filterset.size() +" annotation entries");
		return filterset;
	}
}

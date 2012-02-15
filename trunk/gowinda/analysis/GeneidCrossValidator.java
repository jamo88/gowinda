package gowinda.analysis;

public class GeneidCrossValidator {
	private final IGenomeRepresentation genrep;
	private final GOCategoryContainer gocategories;
	private java.util.logging.Logger logger;
	
	public GeneidCrossValidator(IGenomeRepresentation genrep, GOCategoryContainer gocategories, java.util.logging.Logger logger)
	{
		this.genrep=genrep;
		this.gocategories=gocategories;
		this.logger=logger;
	}
	
	
	public void validate()
	{
		this.logger.info("Start crossvalidating the GeneIDs found in the annotation with the GeneIDs found in the GO association file");
		this.logger.info("Found "+genrep.allGeneids().size()+" genes in the annotation file");
		this.logger.info("Found "+gocategories.getAllGeneids().size()+ " genes in the GO association file");
		
		int overlap=0;
		for(String s:genrep.allGeneids())
		{
			if(gocategories.contains(s)) overlap++;
		}
		this.logger.info("Number of geneids found in the annotation and in the GO association file " + overlap);
		if(overlap==0) throw new IllegalArgumentException("Can not perform analysis when annotation is not consistent with GO associations");
	}
}

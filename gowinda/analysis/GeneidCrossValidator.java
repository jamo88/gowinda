package gowinda.analysis;

import java.util.*;

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
		this.logger.info("Start crossvalidating the gene IDs from the annotation with the ones from the gene set file");
		this.logger.info("Found "+genrep.allGeneids().size()+" genes in the annotation file");
		this.logger.info("Found "+gocategories.getAllGeneids().size()+ " genes in the gene set file");
		HashSet<String> annGenes=new HashSet<String>(genrep.allGeneids());
		HashSet<String> goGenes= new HashSet<String>(gocategories.getAllGeneids());
		
		int overlap=0;
		logger.info("Switch to detailed log mode if you want to see the list of genes for which no corresponding entry in the annotation was found");
		logger.fine("Genes present in the 'gene set' file but not present in annotation file");
		for(String s:goGenes)
		{
			if(annGenes.contains(s)) overlap++;
			else logger.fine("gene set file, gene_id: "+s);
		}
		this.logger.info("Number of gene IDs found in the annotation and in the gene set file (union): " + overlap);
		if(overlap==0) throw new IllegalArgumentException("Can not perform analysis when gene IDs are not consisten");
		
	}
}

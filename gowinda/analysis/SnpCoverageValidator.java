package gowinda.analysis;

import java.util.*; 

public class SnpCoverageValidator {
	private final IGenomeRepresentation genrep;
	private final ArrayList<Snp> snps;
	private final GOCategoryContainer gocat;
	private final java.util.logging.Logger logger;
	
	public SnpCoverageValidator(IGenomeRepresentation genrep, ArrayList<Snp> snps, GOCategoryContainer gocat, java.util.logging.Logger logger)
	{
		this.genrep	=genrep;
		this.snps	=snps;
		this.gocat 	=gocat;
		this.logger	=logger;
	}
	
	/**
	 * Start validating
	 */
	public void validate()
	{
		this.logger.info("Computing the number of genes and GO categories available for testing");
		int annotationGeneCount= genrep.allGeneids().size();
		ArrayList<String> genesCoveredBySnps=new ArrayList<String>(new HashSet<String>(genrep.getGeneidsForSnps(snps)));
		int snpCoveredGeneCount=genesCoveredBySnps.size();
		this.logger.info("Out of "+annotationGeneCount+ " genes present, " + snpCoveredGeneCount+" genes contain a SNP and are thus available for testing");
		
		
		GOTranslator gotrans=new GOTranslator(gocat);
		int gocatCount=gocat.getAllGOEntries().size();
		int gocatPresentCount=gotrans.translateToGeneids(genesCoveredBySnps).size();
		this.logger.info("Out of "+gocatCount+" GO categories, "+gocatPresentCount +" GO categories are available for testing, as they contain to at least one gene that contains a SNP"); 
	}
	
	/**
	 * Calculates the number of GO categories that are available for testing.
	 * First obtains the gene_ids of all genes that are overlapping with any SNP and finally computes the 
	 * GO categories that are corresponding to these genes.
	 * @return the number of GO categories overlapping with any SNP
	 */
	public int getCoveredGOCount()
	{
		GOTranslator gotrans=new GOTranslator(gocat);
		ArrayList<String> genesCoveredBySnps=new ArrayList<String>(new HashSet<String>(genrep.getGeneidsForSnps(snps)));
		return gotrans.translateToGeneids(genesCoveredBySnps).size();
		
	}
	
	
}

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
		this.logger.info("Computing the number of genes and gene sets available for testing");
		int annotationGeneCount= genrep.allGeneids().size();
		ArrayList<String> genesCoveredBySnps=new ArrayList<String>(new HashSet<String>(genrep.getGeneidsForSnps(snps)));
		int snpCoveredGeneCount=genesCoveredBySnps.size();
		this.logger.info("Total genes in annotation: "+annotationGeneCount+ "; Annotated genes with SNP: " + snpCoveredGeneCount+" (genes without SNP are ignored).");
		
		
		GOTranslator gotrans=new GOTranslator(gocat);
		int gocatCount=gocat.getAllGOEntries().size();
		int gocatPresentCount=gotrans.translateToGeneids(genesCoveredBySnps).size();
		this.logger.info("Total gene sets: "+gocatCount+"; Gene sets with at least one gene having a SNP: "+gocatPresentCount +"  (gene sets without a single gene having a SNP are ignored)"); 
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

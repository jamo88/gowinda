package gowinda.analysis;

import java.util.*;

public class SnpCrossValidator {
	private ArrayList<Snp> snps;
	private ArrayList<Snp> candidateSnps;
	private java.util.logging.Logger logger;
	public SnpCrossValidator(ArrayList<Snp> snps, ArrayList<Snp> candidateSnps, java.util.logging.Logger logger)
	{
		this.snps=snps;
		this.candidateSnps=candidateSnps;
		this.logger=logger;
	}
	
	public void validate()
	{
		this.logger.info("Start validating the SNP files; Candidate SNPs must be a subset of all SNPs");
		HashSet<Snp> allsnps=new HashSet<Snp>(this.snps);
		
		for(Snp s: this.candidateSnps)
		{
			if(!allsnps.contains((s))) throw new IllegalArgumentException("SNP "+s+ " is not part of all SNPs; Invalid candidate SNPs must be a subset of all SNPs");
		}
		
		this.logger.info("Finished validating SNP files; Candidate SNPs are a subset of all SNPs");
	}
}

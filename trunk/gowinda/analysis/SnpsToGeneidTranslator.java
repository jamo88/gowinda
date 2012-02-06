package gowinda.analysis;
import java.util.*;

public class SnpsToGeneidTranslator {
	private final IGenomeRepresentation genres;
	public SnpsToGeneidTranslator(IGenomeRepresentation genres)
	{
		this.genres=genres;
	}
	
	/*
	 * Translate SNP positions into a list of Gene_id's 
	 * Note that this list will not be unique, thus the same gene_id may be present several times,
	 * This is necessary as the counting unit of the GO enrichment test will only be decided at a later level
	 */
	public ArrayList<String> translate(ArrayList<Snp> snps)
	{
		ArrayList<String> toret=new ArrayList<String>();
		for(Snp s :snps)
		{
			toret.addAll(genres.getGeneidsForSnp(s));
		}
		return toret;
	}
}

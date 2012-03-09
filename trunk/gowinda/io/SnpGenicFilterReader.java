package gowinda.io;

import gowinda.analysis.Snp;
import java.util.ArrayList;
import gowinda.analysis.IGenomeRepresentation;

public class SnpGenicFilterReader implements ISnpBulkReader
{
	private final ArrayList<Snp> toFilter;
	private IGenomeRepresentation genrep;
	private java.util.logging.Logger logger;
	public SnpGenicFilterReader(ArrayList<Snp> toFilter, IGenomeRepresentation genrep, java.util.logging.Logger logger)
	{
		this.toFilter=toFilter;
		this.genrep=genrep;
		this.logger=logger;
	}

	public ArrayList<Snp> getSnps()
	{


		ArrayList<Snp> filtered=new ArrayList<Snp>();

		logger.info("Starting to filter for 'genic' SNPs; Initial SNP count: "+toFilter.size());
		for(Snp s :toFilter)
		{
			if(genrep.getGeneidsForSnp(s).size()>0) filtered.add(s);
		}
		logger.info("Finished - found " + filtered.size() + " 'genic' SNPs");
		return filtered;
	}

}

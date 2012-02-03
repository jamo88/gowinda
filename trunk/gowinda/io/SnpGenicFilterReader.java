package gowinda.io;

import gowinda.analysis.Snp;
import java.util.ArrayList;
import gowinda.analysis.IGenomeRepresentation;

public class SnpGenicFilterReader implements ISnpBulkReader
{
	private ISnpBulkReader reader;
	private IGenomeRepresentation genrep;
	private java.util.logging.Logger logger;
	public SnpGenicFilterReader(ISnpBulkReader reader, IGenomeRepresentation genrep, java.util.logging.Logger logger)
	{
		this.reader=reader;
		this.genrep=genrep;
		this.logger=logger;
	}

	public ArrayList<Snp> getSnps()
	{

		ArrayList<Snp> unfiltered=reader.getSnps();
		ArrayList<Snp> filtered=new ArrayList<Snp>();

		logger.info("Starting to filter for 'genic' Snps; Initial SNP count: "+unfiltered.size());
		for(Snp s :unfiltered)
		{
			if(genrep.getGeneidsForSnp(s).size()>0) filtered.add(s);
		}
		logger.info("Finished - found " + filtered.size() + " 'genic' SNPs");
		return filtered;
	}

}

package gowinda.analysis;
import java.util.*;

public class SnpsToGeneidTranslator {
	private final IGenomeRepresentation genres;
	public SnpsToGeneidTranslator(IGenomeRepresentation genres)
	{
		this.genres=genres;
	}
	
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

package gowinda.analysis;

import java.util.*;
/*
 * Adjustement of p-value for multiple sampling
 * Concrete implementation uses the Bonferroni correction, which is by many considered to stringent
 */
public class FdrAdjuster implements IMultipleTestingAdjuster{
	
	private int goCategoryCount;
	public FdrAdjuster(int goCategoryCount)
	{
		this.goCategoryCount=goCategoryCount;
	}
	
	private static class IndexPval
	{
		public IndexPval(double pval, int index)
		{
			this.pval=pval;
			this.index=index;
		}
		public double pval;
		public int index;
	}

	
	@Override
	public ArrayList<Double> getAdjustedSignificance(ArrayList<Double> pval)
	{
		ArrayList<IndexPval> tmp =new ArrayList<IndexPval>();
		for(int i=0; i<pval.size(); i++)
		{
			tmp.add(new IndexPval(pval.get(i),i));
		}
		
		tmp=computeAdjusted(tmp);
		
		Collections.sort(tmp,new Comparator<IndexPval>(){
			public int compare(IndexPval v1, IndexPval v2)
			{
				if(v1.index < v2.index) return -1;
				if(v1.index > v2.index) return 1;
				return 0;
			}
		});
		
		ArrayList<Double> toret=new ArrayList<Double>();
		for (IndexPval p: tmp)
		{
			toret.add(p.pval);
		}
		return toret;
	}
	
	
	private ArrayList<IndexPval> computeAdjusted(ArrayList<IndexPval> tonip)
	{
		Collections.sort(tonip,new Comparator<IndexPval>(){
			public int compare(IndexPval v1, IndexPval v2)
			{
				if(v1.pval<v2.pval) return 1;
				if(v2.pval>v2.pval) return -1;
				return 0;
			}
		});

		int iCounter=tonip.size();
		for(IndexPval ip:tonip)
		{
			double multiplicator=((double)this.goCategoryCount)/((double)iCounter);
			ip.pval=ip.pval*multiplicator;
			iCounter--;
		}
		
		// Cummin
		double previous=1.0;
		for(IndexPval ip: tonip)
		{
			// Set largest to 1.0;
			if(ip.pval>1.0) ip.pval=1.0;
		
			// Calculate the cummulative minimum; no value 
			if(ip.pval>previous) ip.pval=previous;
			if(ip.pval<previous) previous=ip.pval;
		}
		return tonip;
	}
	

}

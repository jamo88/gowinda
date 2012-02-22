package gowinda.tmp;

import gowinda.analysis.IMultipleTestingAdjuster;

import java.util.*;
/*
 * Adjustement of p-value for multiple sampling
 * Concrete implementation uses the Bonferroni correction, which is by many considered to stringent
 */
public class FdrAdjuster implements IMultipleTestingAdjuster{
	
	private int n;
	public FdrAdjuster(int goCategoryCount)
	{
		this.n=goCategoryCount;
	}
	
	private static class IndexPval
	{
		public IndexPval(double oriPval, int index)
		{
			this.oriPval=oriPval;
			this.index=index;
		}
		public double oriPval;
		public double adjPval;
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
		
		ArrayList<IndexPval> adj = computeAdjusted(tmp);
		
		Collections.sort(adj,new Comparator<IndexPval>(){
			public int compare(IndexPval v1, IndexPval v2)
			{
				if(v1.index < v2.index) return -1;
				if(v1.index > v2.index) return 1;
				return 0;
			}
		});
		
		ArrayList<Double> toret=new ArrayList<Double>();
		for (IndexPval p: adj)
		{
			toret.add(p.adjPval);
		}
		return toret;
	}
	
	
	private ArrayList<IndexPval> computeAdjusted(ArrayList<IndexPval> tonip)
	{
		Collections.sort(tonip,new Comparator<IndexPval>(){
			public int compare(IndexPval v1, IndexPval v2)
			{
				if(v1.oriPval < v2.oriPval) return 1;
				if(v1.oriPval > v2.oriPval) return -1;
				return 0;
			}
		});

		int iCounter=tonip.size();
		for(IndexPval ip:tonip)
		{
			double multiplicator=((double)this.n)/((double)iCounter);
			ip.adjPval = ip.oriPval*multiplicator;
			iCounter--;
		}
		
		// Cummin 10 5 6 7 4 3 2 1
		double previous=1.0;
		for(IndexPval ip: tonip)
		{
			// Set largest to 1.0;
			if(ip.adjPval>1.0) ip.adjPval=1.0;
		
			// Calculate the cummulative minimum; no value 
			if(ip.adjPval > previous) ip.adjPval=previous;
			else if(ip.adjPval<previous) previous=ip.adjPval;
		}
		return tonip;
	}
	

}

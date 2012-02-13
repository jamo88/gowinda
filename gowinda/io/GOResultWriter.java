package gowinda.io;

import java.util.*;
import java.io.*;
import gowinda.analysis.*;

public class GOResultWriter {
	private String outputFile;
	private GOResultContainer gores;
	private double minSignificance;
	private BufferedWriter bf;
	private java.util.logging.Logger logger;
	public GOResultWriter(String outputFile, GOResultContainer gores, double minSignificance, java.util.logging.Logger logger)
	{
		this.outputFile=outputFile;
		this.gores=gores;
		this.minSignificance=minSignificance;
		this.logger=logger;
		try
		{
			bf=new BufferedWriter(new FileWriter(outputFile));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void writeAll()
	{
		logger.info("Starting to write results to file: "+this.outputFile);
		ArrayList<GOResultForCandidateSnp> res=gores.getCollection();
		Collections.sort(res,new Comparator<GOResultForCandidateSnp>(){
			@Override
			public int compare(GOResultForCandidateSnp r1, GOResultForCandidateSnp r2)
			{
				if(r1.significance()<r2.significance()) return -1;
				if(r1.significance()>r2.significance()) return 1;
				return 0;
			}
		});
		
		
		try
		{
			for(GOResultForCandidateSnp r : res)
			{

				String geneids=this.join(r.geneids());
				
				
				if(r.adjustedSignificance()>minSignificance)continue; 
				String towrite=String.format("%s\t%.3f\t%d\t%f\t%f\t%s\t%s", r.goEntry().goID(),r.expectedCount(),r.observedCount(),r.significance(),r.adjustedSignificance(),r.goEntry().description(),geneids);
				bf.write(towrite+"\n");
			}
			bf.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		logger.info("Finished writing to file");
	}
	
	private String join(ArrayList<String> toJoin)
	{
		if(toJoin.size()==0) return "";
		if(toJoin.size()==1) return toJoin.get(0);
		
		StringBuilder sb=new StringBuilder();
		sb.append(toJoin.get(0));
		for(int i=1; i<toJoin.size(); i++)
		{
			sb.append(",");
			sb.append(toJoin.get(i));
		}
		return sb.toString();
	
	}
}

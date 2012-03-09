package gowinda.io;
import java.util.*;
import gowinda.analysis.AnnotationEntry;
import java.util.logging.Logger;

public class UtrDecorator implements IBulkAnnotationReader {

	private Logger logger;
	private ArrayList<AnnotationEntry> aes;
	
	public UtrDecorator(IBulkAnnotationReader bar,Logger logger)
	{
		aes=bar.readAnnotation();
		this.logger=logger;
	}
	public UtrDecorator(ArrayList<AnnotationEntry> anList)
	{
		this.aes=anList;
		this.logger=java.util.logging.Logger.getLogger("Gowinda Logger");
		logger.setUseParentHandlers(false);
	}
	
	@Override
	public ArrayList<AnnotationEntry> readAnnotation(){
	
		logger.info("Start creating 'UTR' feature (calculated as exons - cds)");
		HashMap<String,ArrayList<AnnotationEntry>> genebase=getFeaturesForGene(this.aes);
		
		ArrayList<AnnotationEntry> toret=new ArrayList<AnnotationEntry>();
		
		for(Map.Entry<String, ArrayList<AnnotationEntry>> e: genebase.entrySet())
		{
			ArrayList<AnnotationEntry> exons 	=this.getFeature(e.getValue(),"exon");
			ArrayList<AnnotationEntry> cds		=this.getFeature(e.getValue(),"cds");
			if(exons.size()==0) throw new IllegalArgumentException("Something is wrong with the annotation, no exons found for gene: "+e.getKey());
			
			ArrayList<AnnotationEntry> utr= this.getUTR(exons,cds);
			toret.addAll(utr);
		}
		
		
		logger.info("Finished - obtained " + toret.size() +" 'UTR' entries");
		return toret;
	}
	
	private ArrayList<AnnotationEntry> getUTR(ArrayList<AnnotationEntry> exons, ArrayList<AnnotationEntry> cds)
	{
		assert exons.size()>0;
		
		String geneid=exons.get(0).geneid();
		String chromosome=exons.get(0).chromosome();
		AnnotationEntry.Strand strand= exons.get(0).strand();
		// get a bitmap where cds are subtracted from the exons
		HashSet<Integer> positions=getIntegerBitmap(exons,cds);
		
		// get start and end position
		int start = -1;
		int end = -1;
		for(Integer i: positions)
		{
			if(start== -1) start=i;
			if(end==-1) end =i;
			if(i<start) start=i;
			if(i>end)end=i;
		}
		
		// parse the bitmap and build new UTR entries
		ArrayList<AnnotationEntry> toret=new ArrayList<AnnotationEntry>();
		int currentStart=-1;
		// (end+1) to capture also the last utr
		for(int i=start; i<=(end+1); i++)
		{
			if(positions.contains(i))
			{
				if(currentStart == -1) currentStart=i;
			}
			else
			{	
				// means a novel  entry was found
				if(currentStart != -1)
				{
					toret.add(new AnnotationEntry(chromosome,"utr",currentStart,i-1,strand,null,geneid));
					currentStart=-1;
				}
			}
		}
		return toret;
	}
	
	
	
	private HashSet<Integer> getIntegerBitmap(ArrayList<AnnotationEntry> exons, ArrayList<AnnotationEntry> cds)
	{
		HashSet<Integer> utrMap=new HashSet<Integer>();
		for(AnnotationEntry ex: exons)
		{
			for(int i=ex.start(); i<=ex.end(); i++) utrMap.add(i);
		}
		
		for(AnnotationEntry c: cds)
		{
			for(int i=c.start(); i<=c.end(); i++)
			{
				if(utrMap.contains(i)) utrMap.remove(i);
			}
		}
		return utrMap;
		
		
	}
	
	
	
	private HashMap<String,ArrayList<AnnotationEntry>> getFeaturesForGene(ArrayList<AnnotationEntry> al)
	{
		HashMap<String,ArrayList<AnnotationEntry>> toret=new HashMap<String,ArrayList<AnnotationEntry>>();	
		for(AnnotationEntry e : al)
		{
			if(!toret.containsKey(e.geneid())) toret.put(e.geneid(),new ArrayList<AnnotationEntry>());
			toret.get(e.geneid()).add(e);
		}
		return toret;
	}
	
	
	private ArrayList<AnnotationEntry> getFeature(ArrayList<AnnotationEntry> al, String toFilter)
	{
		ArrayList<AnnotationEntry> toRet=new ArrayList<AnnotationEntry>();
		for (AnnotationEntry e: al)
		{
			if(e.feature().equals(toFilter)) toRet.add(e);
		}
		return toRet;
	}
	

}

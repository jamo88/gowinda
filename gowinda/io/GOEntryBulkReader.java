package gowinda.io;

import java.util.*;
import gowinda.analysis.GOEntry;
import java.io.*;

/*
 * NOTE: Geneids are converted to lowercase
 */
public class GOEntryBulkReader {
	
	private final String inputFile;
	private java.util.logging.Logger logger;
	private BufferedReader bf;
	
	// Internal class containing the raw file entries
	private class GORaw
	{
		public String goname;
		public String description;
		public String[] geneids;
		public GORaw(String goname, String description, String[] geneids)
		{
			if(geneids.length <1 )throw new IllegalArgumentException("Your GO association file may be corrupt. At least one entry is required for every Gene");
			this.goname=goname;
			this.description=description;
			String[] tl=new String[geneids.length];
			for(int i=0; i<geneids.length; i++)
			{
				tl[i]=geneids[i].toLowerCase();
			}
			this.geneids=tl;
		}

	}
	
	public GOEntryBulkReader(String inputFile, java.util.logging.Logger logger)
	{
		this.inputFile=inputFile;
		this.logger=logger;
		try
		{
			this.bf=new BufferedReader(new FileReader(this.inputFile));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public HashMap<String,ArrayList<GOEntry>> readGOEntries()
	{
		this.logger.info("Starting to read GO association file");
		ArrayList<GORaw> ar= readGOraw();
		this.logger.info("Finished - read "+ar.size() + " GO terms");
		
		this.logger.fine("Starting key conversion: GOTerm -> Geneid");
		// Every GOTerm may only occur once for every gene
		HashMap<String,HashSet<GOEntry>> goe=new  HashMap<String,HashSet<GOEntry>>();
		for(GORaw gr:ar)
		{
			GOEntry current=new GOEntry(gr.goname,gr.description);
			for(String geneid:gr.geneids)
			{
				if(!goe.containsKey(geneid))goe.put(geneid, new HashSet<GOEntry>());
				goe.get(geneid).add(current);
			}
		}
		
		HashMap<String,ArrayList<GOEntry>> toret=new HashMap<String,ArrayList<GOEntry>>();
		for(Map.Entry<String,HashSet<GOEntry>> en: goe.entrySet())
		{
			toret.put(en.getKey(), new ArrayList<GOEntry>(en.getValue()));
		}
		return toret;
	}
	
	
	// Read the content of the GO file 
	private ArrayList<GORaw> readGOraw()
	{
		String line;
		ArrayList<GORaw> ar=new ArrayList<GORaw>();
		try
		{
			while((line=bf.readLine())!=null)
			{
				if(line.startsWith("#"))continue;
				String[] a=line.split("\t");
				String[] b={a[2]};
				if(a[2].contains(" ")) 
				{
					b=a[2].split(" ");
				}
				ar.add(new GORaw(a[0],a[1],b));
			}
			bf.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		return ar;
	}
	
	

}

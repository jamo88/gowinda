/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda.io;

import java.io.*;
import gowinda.analysis.AnnotationEntry;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * GtfReader is a bulk reader; Annotation can only be read as bulk (Gff geneid)
 * @author robertkofler
 */
public class GffReader implements IBulkAnnotationReader {
    

    
    private java.util.logging.Logger logger;
    private String fileName;
    private RawGffReader rr;
    

    
    public GffReader(String fileName,java.util.logging.Logger logger)
    {
        
        this.logger=logger;
        this.fileName=fileName;
        
        this.rr=new RawGffReader(fileName);

        
    }
    
    
    @Override
    public ArrayList<AnnotationEntry> readAnnotation()
    {
        
        logger.info("Start parsing .gff file " + this.fileName);
        ArrayList<RawGff> raw=rr.readGff();
        ArrayList<RawGff> mrna=this.getMrna(raw);
        ArrayList<RawGff> excds=this.getExonCds(raw);
        
        HashMap<String,String> transIdToParent=buildId2ParentMap(mrna);
        
        ArrayList<RawGff> orphans=new ArrayList<RawGff>();
        
        ArrayList<AnnotationEntry> toret= new ArrayList<AnnotationEntry>();
        for(RawGff r: excds)
        {
  
        		String geneid=inferGeneid(r,transIdToParent);
        		if(!(geneid==null))
        		{	
        				toret.add(new AnnotationEntry(r.chromosome,r.feature,r.start,r.end,r.strand,r.frameshift,geneid));
        		}
        		else
        		{
        			orphans.add(r);
        		}
        }
        
        logger.info("Ignored "+orphans.size()+" exons/cds entries; Could not find parent mRNA (e.g.: ncRNA, tRNA)");
        logger.fine("List of exons for which no parent mRNA or gene was found:");
        for(RawGff o: orphans)
        {
        	logger.fine("Orphan: "+o.feature+" "+o.id);
        }
        logger.info("Finished - Read " + toret.size() + " size Annotation entries");
        logger.info("Starting to scan for duplicated gff entries");
        HashSet<AnnotationEntry> tset=new HashSet<AnnotationEntry>(toret);
        logger.info("Finished - Found " + tset.size() + " unique entries");
        return new ArrayList<AnnotationEntry>(tset);
    }
    
    private String inferGeneid(RawGff entry, HashMap<String,String> transIdToParent)
    {
    	String geneid=null;
    	
    	for(String mrnaid: entry.parent)
    	{
    		// for the given exon, get the geneid 
    		String curGeneid=transIdToParent.get(mrnaid);
    		if(curGeneid==null) continue;
    		// if the geneid has not been yet set do so
    		if(geneid==null)geneid=curGeneid;
    		if(!geneid.equals(curGeneid)) throw new IllegalArgumentException("Gff file may be corrupt; given exon belongs to two different genes:"+geneid+" "+curGeneid);
    	}
    	return geneid;
    	
    	
    	
    	
    }
    
    private ArrayList<RawGff> getMrna(ArrayList<RawGff> ann)
    {
    	ArrayList<RawGff> mrna=new ArrayList<RawGff>();
    	for(RawGff r: ann)
    	{
    		if(r.feature.equals("mrna"))mrna.add(r);
    	}
    	return mrna;
    }
    
    private ArrayList<RawGff> getExonCds(ArrayList<RawGff> ann)
    {
    	ArrayList<RawGff> excds=new ArrayList<RawGff>();
    	for(RawGff r: ann)
    	{
    		if(r.feature.equals("cds")|| r.feature.equals("exon")) excds.add(r);
    	}
    	return excds;
    }
    
    private HashMap<String,String> buildId2ParentMap(ArrayList<RawGff> raw)
    {
    	HashMap<String,String> trans=new HashMap<String,String>();
    	// 2L      FlyBase mRNA    94752   102086  .       +       .       ID=FBtr0078103;Name=CG11376-RA;Parent=FBgn0031216;Dbxref=FlyBase_Annotation_IDs:CG11376-RA,REFSEQ:NM_134645;score_text=Strongly Supported
    	for(RawGff r: raw)
    	{
    		if(!r.feature.equals("mrna"))throw new IllegalArgumentException("Can only translate mRNAs into child parent relationship");
    		if(r.parent.length!=1) throw new IllegalArgumentException("A given mRNA can only have single parent gene; .gff file may be corrupted: " +r.id);
    		trans.put(r.id,r.parent[0]);
    	}
    	return trans;
    }
    
}


class RawGff
{
	public String id;
	public String[] parent;
	public String chromosome;
	public String feature;
	public int start;
	public int end;
	public AnnotationEntry.Strand strand;
	public Integer frameshift;
	
	public RawGff(String id, String[] parent, String chromosome, String feature, int start, int end, AnnotationEntry.Strand strand, Integer frameshift)
	{
		this.id=id;
		this.parent=parent;
		this.chromosome=chromosome;
		this.start=start;
		this.feature=feature;
		this.end=end;
		this.strand=strand;
		this.frameshift=frameshift;
	}
	
}



/*
 * Private helper class;
 * Reads the raw gff entries
 * It is still necessary to obtain the id of the parent-genes for all cds,exons
 */
class RawGffReader
{
	private static HashSet<String> acceptedFeatures;
    private File inputFile;
    private java.io.BufferedReader bs; 
    //2L      FlyBase mRNA    94752   102086  .       +       .       ID=FBtr0078103;Name=CG11376-RA;Parent=FBgn0031216;
    private static Pattern pParent= Pattern.compile("Parent=([^;]+);");
    private static Pattern pID=Pattern.compile("ID=([^;]+);");
    
    static 
    {
        acceptedFeatures=new HashSet<String>();
        acceptedFeatures.add("exon");
        acceptedFeatures.add("cds");
        acceptedFeatures.add("mrna");
    }
    
    public RawGffReader(String fileName)
    {
        this.inputFile=new File(fileName);
        try
        {
        	this.bs=new BufferedReader(new FileReader(this.inputFile));
        }
        catch(FileNotFoundException e)
        {
        	e.printStackTrace();
        	System.exit(0);
        }
    }
    
    
    public ArrayList<RawGff> readGff()
    {
    	ArrayList<RawGff> toret =new ArrayList<RawGff>();
    	String line;
    	try
    	{
    		while((line=bs.readLine())!=null)
    		{
    			RawGff parsed=parseLine(line);
    			if(parsed!=null && acceptedFeatures.contains(parsed.feature))toret.add(parsed);
    		}
    		bs.close();
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    		System.exit(0);
    	}
     
    	return toret;
    }
    private RawGff parseLine(String line)
    {
        //0        1        2       3       4     5       6       7       8
		//2L      FlyBase gene    94752   102086  .       +       .       ID=FBgn0031216;Name=CG11376;Alias=FBgn0025528,anon-EST:Posey54,Dm zir,FBgn 31216;Ontology_term=SO:0000010,SO:0000087,GO:0005525,GO:000508
		//    	5,GO:0051020;Dbxref=FlyBase:FBan0011376,FlyBase_Annotation_IDs:CG11376,GB_protein:AAF51561,GB:AA391952,GB:AF083301,GB:AW942162,GB:AZ877629,GB:BT015273,GB_protein:AAT94502,GB:CZ468078,GB:CZ468665,GB:CZ4
		//			69343,GB:CZ470525,GB:CZ470937,GB:CZ480810,GB:CZ480811,GB:CZ483732,UniProt/TrEMBL:Q9VPI9,EntrezGene:33165,INTERPRO:IPR021816,INTERPRO:IPR010703,InterologFinder:33165,BIOGRID:59427,FlyAtlas:CG11376-RA,Ge
		// 			nomeRNAi:33165;gbunit=AE014134;derived_computed_cyto=21B1-21B2
		// 2L      FlyBase mRNA    94752   102086  .       +       .       ID=FBtr0078103;Name=CG11376-RA;Parent=FBgn0031216;Dbxref=FlyBase_Annotation_IDs:CG11376-RA,REFSEQ:NM_134645;score_text=Strongly Supported
		//    			;score=15
    	// 2L      FlyBase CDS     98527   99723   .       +       0       ID=CDS_CG11376:6_785;Name=CG11376-cds;Parent=FBtr0078103;parent_type=mRNA
    	// 2L      FlyBase exon    98527   99723   .       +       .       ID=CG11376:6;Name=CG11376:6;Parent=FBtr0078103;parent_type=mRNA

    	if(line.startsWith("#"))return null;
        String[] a=line.split("\t");
        if(a.length!=9)return null;

        String chromosome=a[0];
        String feature=a[2].toLowerCase();
        if(!acceptedFeatures.contains(feature))return null;
        int start=Integer.parseInt(a[3]);
        int end=Integer.parseInt(a[4]);
        String tstrand=a[6];
        AnnotationEntry.Strand strand=tstrand.equals("+")?AnnotationEntry.Strand.Plus:AnnotationEntry.Strand.Minus;
        Integer frameshift=a[7].equals(".")?null:Integer.parseInt(a[7]);
        Matcher mParent =pParent.matcher(a[8]);
        Matcher mID=pID.matcher(a[8]);
        if(!mParent.find()) throw new IllegalArgumentException("Did not find Parent for gff entry. Illegal entry");
        if(!mID.find()) throw new IllegalArgumentException("Did not find ID for gff entry. Illegal entry");

        String parent=mParent.group(1);
        String id=mID.group(1);
        String[] parentlist;
        if(parent.contains(","))
        {
        	//FBtr0300689,FBtr0300690
        	parentlist=parent.split(",");
        	
        }
        else
        {
        	parentlist=new String[1];
        	parentlist[0]=parent;
        }
   
        
        return new RawGff(id,parentlist,chromosome,feature,start,end,strand,frameshift);
        //AnnotationEntry(chromosome,feature,start,end,strand,frameshift,geneid);
    }
                
}

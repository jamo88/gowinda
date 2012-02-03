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
public class GtfReader implements IBulkAnnotationReader {
    
    private static final Set<String> acceptedFeatures;
    
    private java.util.logging.Logger logger;
    private String fileName;
    private SingleGtfReader gr;
    
    static 
    {
        acceptedFeatures=new HashSet<String>();
        acceptedFeatures.add("exon");
        acceptedFeatures.add("cds");
    }
    
    public GtfReader(String fileName,java.util.logging.Logger logger)
    {
        
        this.logger=logger;
        this.fileName=fileName;
        try
        {
            gr=new SingleGtfReader(fileName);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    
    @Override
    public ArrayList<AnnotationEntry> readAnnotation()
    {
        ArrayList<AnnotationEntry> toret= new ArrayList<AnnotationEntry>();
        logger.info("Start parsing .gtf file " + this.fileName);
        AnnotationEntry entry;
        try
        {
            while((entry=gr.next())!=null)
            {
                if(acceptedFeatures.contains(entry.feature()))
                {
                    toret.add(entry);
                }
            }
            gr.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        logger.info("Finished - Read " + toret.size() + " size Annotation entries");
        logger.info("Starting to scan for duplicated gtf entries");
        HashSet<AnnotationEntry> tset=new HashSet<AnnotationEntry>(toret);
        logger.info("Finished - Found " + tset.size() + " unique entries");
        return new ArrayList<AnnotationEntry>(tset);
    }
    
}


/*
 * Private helper class;
 * Reads single gtf entries;
 */
class SingleGtfReader
{
    private File inputFile;
    private java.io.BufferedReader bs;
    private static Pattern p= Pattern.compile("gene_id\\s+\"([^\"]+)\";");
    
    public SingleGtfReader(String fileName) throws FileNotFoundException
    {
        this.inputFile=new File(fileName);
        this.bs=new BufferedReader(new FileReader(this.inputFile));
    }
    
    
    public AnnotationEntry next() throws IOException
    {
        String line=bs.readLine();
        if(line==null) {return null;}
        String[] a=line.split("\t");
        if(a.length!=9){throw new IllegalArgumentException("Illegal entry in gtf file "+line);}
        
        //0             1        2          3       4       5           6       7       8
        //chr4	dm3_flyBaseGene	exon	251356	251521	0.000000	+	.	gene_id "CG1674-RB"; transcript_id "CG1674-RB"; 
        //chr4	dm3_flyBaseGene	start_codon	252580	252582	0.000000	+	.	gene_id "CG1674-RB"; transcript_id "CG1674-RB"; 
        //chr4	dm3_flyBaseGene	CDS	252580	252603	0.000000	+	0	gene_id "CG1674-RB"; transcript_id "CG1674-RB"; 

        String chromosome=a[0];
        String feature=a[2].toLowerCase();
        int start=Integer.parseInt(a[3]);
        int end=Integer.parseInt(a[4]);
        String tstrand=a[6];
        AnnotationEntry.Strand strand=tstrand.equals("+")?AnnotationEntry.Strand.Plus:AnnotationEntry.Strand.Minus;
        Integer frameshift=a[7].equals(".")?null:Integer.parseInt(a[7]);
        Matcher m =p.matcher(a[8]);
        if(!m.find()){throw new IllegalArgumentException("Illegal entry in gtf file; Does not contain 'gene_id \"genename\";'");}
        String geneid=m.group(1);
      
        return new AnnotationEntry(chromosome,feature,start,end,strand,frameshift,geneid);
    }
    
    public void close() throws IOException
    { 
    	this.bs.close();
    }
                
}

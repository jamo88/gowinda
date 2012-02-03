/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda.analysis;

/**
 * immutable: should be hashable
 * NOTE: Geneids are converted to lowercase
 * @author robertkofler
 */
public class AnnotationEntry {
    public static enum Strand{Plus,Minus};
    private final String chromosome;
    private final String feature;
    private final int start;
    private final int end;
    private final Strand strand;
    private final Integer frameshift;
    private final String geneid;
    public AnnotationEntry(String chromosome, String feature, int start, int end, Strand strand, Integer frameshift, String geneid)
    {
    	if(start > end) throw new IllegalArgumentException("Start position has to be smaller than end position");
        this.chromosome=chromosome;
        this.feature=feature.toLowerCase();
        this.start=start;
        this.end=end;
        this.strand=strand;
        this.frameshift=frameshift;
        this.geneid=geneid.toLowerCase();
    }
    
    public String chromosome()
    {
        return this.chromosome;
    }
    public String feature()
    {
        return this.feature;
    }
    public int start()
    {
        return this.start;
    }
    public int end()
    {
        return this.end;
    }
    public Strand strand()
    {
        return this.strand;
    }
    public Integer frameshift()
    {
        return this.frameshift;
    }
    public String geneid()
    {
        return this.geneid;        
    }
    
    @Override
    public String toString()
    {
        
        String strandstring=this.strand==Strand.Plus?"+":"-";
        return String.format("%s%s:%d-%d,%s,%s,(%d)",this.chromosome,strandstring,this.start,this.end,this.feature,this.geneid,this.frameshift);
    }
    
    @Override
    public boolean equals(Object o)
    {   
        if(!(o instanceof AnnotationEntry)){return false;}
        AnnotationEntry tc=(AnnotationEntry)o;
        if(     tc.chromosome().equals(this.chromosome()) &&
                tc.strand() == this.strand() &&
                tc.start() == this.start() &&
                tc.end() == this.end() &&
                tc.feature().equals(this.feature()) &&
                tc.geneid().equals(this.geneid()) &&
                tc.frameshift() == this.frameshift()
                
                ) return true;
        return false;
    }
    
    @Override 
    public int hashCode()
    {
        int strandint=this.strand()==Strand.Plus?1:0;
        int ret=17;
        ret = ret * 31 + this.start();
        ret = ret * 31 + this.end();
        ret = ret * 31 + (this.frameshift()==null?0:this.frameshift);
        ret = ret * 31 + strandint;
        ret = ret * 31 + this.chromosome().hashCode();
        ret = ret * 31 + this.feature().hashCode();
        ret = ret * 31 + this.geneid().hashCode();
        return ret;
    }
    
}

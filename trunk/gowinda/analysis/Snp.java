package gowinda.analysis;



/**
 * A SNP (Single Nucleotide Polymorphism)
 * Immutable
 * 
 * @author robertkofler
 */
public class Snp implements Comparable<Snp> {
    private final String chromosome;
    private final int position;
    public Snp(String chromosome, int position)
    {
        this.chromosome=chromosome;
        this.position=position;
    }
    /**
     * @return chromosome the chromosome of the SNP 
     */
    public String chromosome()
    {
        return this.chromosome;
    }
    
    /**
     * @return position the position of the SNP on the chromosome 
     */
    public int position()
    {
        return this.position;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s-%d",this.chromosome,this.position);
    }
    
    /**
     * Sort the SNPs by chromosome and than by position
     * @param b the Snp to compare this SNP to
     * @return the sort order
     */
    @Override
    public int compareTo(Snp b)
    {
        int chrcomp=this.chromosome.compareTo(b.chromosome());
        if(chrcomp!=0) return chrcomp;
        if(this.position() < b.position()) return -1;
        if(this.position() > b.position()) return 1;
        return 0;   
    }
    
    @Override
    public int hashCode()
    {
        int hc=17;
        hc=hc*37+position;
        hc=hc*37+chromosome.hashCode();
        return hc;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Snp)){return false;}
        Snp tc=(Snp)o; 
        if(tc.position() == this.position() && tc.chromosome().equals(this.chromosome())){return true;}
        return false;
    }
    
    
}

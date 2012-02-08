/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda.misc;

/**
 *
 * @author robertkofler
 */
public enum GeneDefinition {
    Exon,
    CDS,
    Gene,
    Upstream,
    Downstream,
    UpDownstream;
    
    
    private int length;
    
    public void setLength(int length)
    {
    	this.length=length;
    }
    public int getLength()
    {
    	return this.length;
    }
}

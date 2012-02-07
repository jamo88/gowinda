/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda.io;

import java.io.*;
import gowinda.analysis.Snp;
/**
 *
 * @author robertkofler
 */
public class SnpReader implements ISnpReader{
    
    private File inputFile;
    private BufferedReader bf;
    
    public SnpReader(String inputFile) throws FileNotFoundException
    {
        this.inputFile=new File(inputFile);
        this.bf=new BufferedReader(new FileReader(this.inputFile));
    }
    
    public SnpReader(BufferedReader bf)
    {
    	this.bf=bf;
    }
    
    @Override
    public Snp next() throws IOException
    {
        String line=bf.readLine();
        if(line==null)return null;
        
        // SNP info must be in the following format
        // chromosome   position    ... 
        // X	9121094	T	0:19:16:0:0:0	0:87:54:0:0:0	0:96:8:0:0:0	0:109:0:0:0:0	2.109981e-18	0.0146540545246982
        String[] a=line.split("\t");
        return new Snp(a[0],Integer.parseInt(a[1]));
    }
    
    @Override
    public void close() throws IOException
    { 
        this.bf.close();
    }
    
    
}

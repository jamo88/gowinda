/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda.io;

import java.io.*;
import java.util.*;
import gowinda.analysis.Snp;
/**
 *  Reads all SNPs from a given input file and checks if the are unique
 * @author robertkofler
 */
public class SnpBulkReader implements ISnpBulkReader {
    
    private ISnpReader sr;
    private java.util.logging.Logger logger;
    private String inputFile;
    
    public SnpBulkReader(String inputFile,java.util.logging.Logger logger)
    {
        this.logger=logger;
        this.inputFile=inputFile;
        try
        {
            this.sr=new SnpReader(inputFile);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(0); // exiting with zero is error!
        }
    }
    
    public ArrayList<Snp> getSnps()
    {
        logger.info("Starting to load SNPs from file: "+this.inputFile);
        ArrayList<Snp> snps=new ArrayList<Snp>();
        try
        {
            Snp s;
            while((s=sr.next())!=null)
            {
                snps.add(s);
            }
            sr.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }

        logger.info("Finished - read "+snps.size()+" SNPs");
        logger.info("Searching for duplicated SNP entries");
        Set<Snp> setsnp=new HashSet<Snp>(snps);
        logger.info("Finished - found "+setsnp.size()+ " unique SNPs");
        return new ArrayList<Snp>(setsnp);
        
    }
    
    
}

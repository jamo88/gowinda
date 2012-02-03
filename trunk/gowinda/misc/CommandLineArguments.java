package gowinda.misc;
/**
 *
 * @author robertkofler
 */
public class CommandLineArguments {
    

    private final String annotationFile;
    private final String snpFile;
    private final String candidateSnpFile;
    private final String goAssociationFile;
    private final String statInputFile;
    private final String statOutputFile;
    private final int simulations;
    private final int threads;
    private final String outputFile;
    private final float significance;
    private final boolean onlyGenedefSnps;
    private final boolean debugmode;
    

    private final gowinda.misc.CountingUnit cu;
    private final gowinda.misc.GeneDefinition geneDef;
    private final boolean displayHelp;
    

    
    /**
     * 
     * @param annotationFile
     * @param snpFile
     * @param candidateSnpFile
     * @param goAssociationFile
     * @param simulations
     * @param significance
     * @param countunit 
     */
    public CommandLineArguments(String outputFile, String statInputFile, String statOutputFile, String annotationFile, String snpFile, String candidateSnpFile, String goAssociationFile,
            int simulations, int threads, float significance, gowinda.misc.CountingUnit unit, gowinda.misc.GeneDefinition geneDef, 
             boolean displayHelp, boolean debugmode, boolean  onlyGenedefSnps)
    {
    	this.threads=threads;
    	this.outputFile=outputFile;
        this.cu=unit;
        this.annotationFile=annotationFile;
        this.snpFile=snpFile;
        this.candidateSnpFile=candidateSnpFile;
        this.goAssociationFile=goAssociationFile;
        this.simulations=simulations;
        this.significance=significance;
        this.statInputFile=statInputFile;
        this.statOutputFile=statOutputFile;
        this.displayHelp=displayHelp;
        this.geneDef=geneDef;
        this.debugmode=debugmode;
        this.onlyGenedefSnps=onlyGenedefSnps;

    }
    
    public int threads()
    {
    	return this.threads;
    }
    public boolean debugMode()
    {
    	return this.debugmode;
    }
    public String outputFile()
    {
    	return this.outputFile;
    }
    public String annotationFile()
    {
        return annotationFile;
    }
    public String snpFile()
    {
        return snpFile;
    }
    public String candidateSnpFile()
    {
        return candidateSnpFile;
    }
    public String goAssociationFile()
    {
        return goAssociationFile;
    }
    public int simulations()
    {
        return simulations;
    }
    public float significance()
    {
        return significance;
    }
    public gowinda.misc.CountingUnit unit()
    {
        return cu;
    }
    public gowinda.misc.GeneDefinition geneDefinition()
    {
        return geneDef;
    }
    public String statisticInputFile()
    {
        return this.statInputFile;
    }
    public String statisticOutputFile()
    {
        return this.statOutputFile;
    }
    public boolean displayHelp()
    {
        return this.displayHelp;
    }
    public boolean geneDefSampling()
    {
    	return this.onlyGenedefSnps;
    }
    
           
 
}


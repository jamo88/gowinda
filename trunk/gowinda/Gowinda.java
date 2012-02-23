/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda;

import gowinda.misc.*;
import java.util.logging.Level;

/**
 *
 * @author robertkofler
 */
public class Gowinda {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       // Parse the command line arguments;
        CommandLineArguments arguments= CommandLineParser.getCommandLineArguments(args);
        // Display the help if requested 
        if(arguments.displayHelp())
        {
            System.out.print(gowinda.misc.CommandLineParser.helpMessage());
            System.exit(1); // 1 means that programm exited correctly
        }
      
        // Create a logger to System.err
        java.util.logging.Logger logger=java.util.logging.Logger.getLogger("Gowinda Logger");
        java.util.logging.ConsoleHandler gowhandler =new java.util.logging.ConsoleHandler();
        gowhandler.setLevel(Level.INFO);
        if(arguments.debugMode())gowhandler.setLevel(Level.FINEST);
        gowhandler.setFormatter(new gowinda.misc.GowindaLogFormatter());
        logger.addHandler(gowhandler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        
        // Create 'The Analyzer'
        IAnalyze analyzer;
        // start 'de novo' simulations
        analyzer =new SimulationAnalyzer(arguments.outputFile(),arguments.annotationFile(),arguments.snpFile(),arguments.candidateSnpFile(),arguments.goAssociationFile()
                    ,arguments.minGenes(),arguments.simulations(), arguments.threads(),arguments.significance(),arguments.simulationMode(),arguments.geneDefinition(),logger);

        //Start the analysis
        analyzer.startAnalysis();
        
        //FIN
        System.exit(1);
    }
    

   
}

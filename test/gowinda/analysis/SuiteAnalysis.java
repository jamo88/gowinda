/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.gowinda.analysis;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author robertkofler
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestSnp.class, TestAnnotationEntry.class, TestGenomeRepresentationHash.class,
	TestGenomeRepresentationList.class, TestGenomeRepresentationListMultiSNP.class, TestGOSimulationContainer.class,TestFdrAdjuster.class})
public class SuiteAnalysis {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda.analysis;

import gowinda.analysis.AnnotationEntry.Strand;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

/**
 *
 * @author robertkofler
 */
public class TestAnnotationEntry {
    
    public TestAnnotationEntry() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of chromosome method, of class AnnotationEntry.
     */
    @Test
    public void testChromosome() {
        System.out.println("chromosome");
        AnnotationEntry instance = new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Plus,0,"FBgn007");
        String expResult = "2R";
        String result = instance.chromosome();
        assertEquals(expResult, result);

    }

    /**
     * Test of feature method, of class AnnotationEntry.
     */
    @Test
    public void testFeature() {
        System.out.println("feature");
        AnnotationEntry instance = new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Plus,0,"FBgn007");
        String expResult = "exon";
        String result = instance.feature();
        assertEquals(expResult, result);

    }

    /**
     * Test of start method, of class AnnotationEntry.
     */
    @Test
    public void testStart() {
        System.out.println("start");
        AnnotationEntry instance = new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Plus,0,"FBgn007");
        int expResult = 100;
        int result = instance.start();
        assertEquals(expResult, result);

    }

    /**
     * Test of end method, of class AnnotationEntry.
     */
    @Test
    public void testEnd() {
        System.out.println("end");
        AnnotationEntry instance = new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Plus,0,"FBgn007");
        int expResult = 110;
        int result = instance.end();
        assertEquals(expResult, result);
    }

    /**
     * Test of strand method, of class AnnotationEntry.
     */
    @Test
    public void testStrand() {
        System.out.println("strand");
        AnnotationEntry instance = new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Plus,0,"FBgn007");
        Strand expResult = AnnotationEntry.Strand.Plus;
        Strand result = instance.strand();
        assertEquals(expResult, result);

    }

    /**
     * Test of frameshift method, of class AnnotationEntry.
     */
    @Test
    public void testFrameshift() {
        System.out.println("frameshift");
        AnnotationEntry instance = new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Plus,1,"FBgn007");
        Integer expResult = 1;
        Integer result = instance.frameshift();
        assertEquals(expResult, result);

    }

    /**
     * Test of geneid method, of class AnnotationEntry.
     */
    @Test
    public void testGeneid() {
        System.out.println("geneid");
        AnnotationEntry instance = new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Plus,0,"FBgn007");
        String expResult = "fbgn007";
        String result = instance.geneid();
        assertEquals(expResult, result);

    }


    /**
     * Test of equals method, of class AnnotationEntry.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Plus,0,"fbgn007");
        AnnotationEntry instance = new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Plus,0,"FBgn007");
        boolean expResult = true;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);

    }

    /**
     * Test of hashCode method, of class AnnotationEntry.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Set<AnnotationEntry> al=new HashSet<AnnotationEntry>();
        al.add(new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Plus,0,"FBgn007"));
        al.add(new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Plus,0,"FBgn007"));
        al.add(new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Plus,0,"FBgn007"));
        assertEquals(1,al.size());
        al.add(new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Minus,0,"FBgn007"));
        al.add(new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Minus,0,"FBgn007"));
        assertEquals(2,al.size());
        al.add(new AnnotationEntry("2R","cds",100,110,AnnotationEntry.Strand.Minus,0,"FBgn007"));
        al.add(new AnnotationEntry("2R","cds",100,120,AnnotationEntry.Strand.Minus,0,"FBgn007"));
        al.add(new AnnotationEntry("2R","cds",105,110,AnnotationEntry.Strand.Minus,0,"FBgn007"));
        al.add(new AnnotationEntry("2R","cds",100,110,AnnotationEntry.Strand.Minus,1,"FBgn007"));
        al.add(new AnnotationEntry("2R","cds",100,110,AnnotationEntry.Strand.Minus,1,"FBgn008"));
        al.add(new AnnotationEntry("3R","cds",100,110,AnnotationEntry.Strand.Minus,1,"FBgn007"));
        assertEquals(8,al.size());
    }
}

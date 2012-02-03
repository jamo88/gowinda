/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda.analysis;

import gowinda.analysis.Snp;
import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

/**
 *
 * @author robertkofler
 */
public class TestSnp {
    
    public TestSnp() {
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
     * Test of chromosome method, of class Snp.
     */
    @Test
    public void testChromosome() {
        System.out.println("chromosome");
        Snp instance = new Snp("2R",2);
        String expResult = "2R";
        String result = instance.chromosome();
        assertEquals(expResult, result);

    }

    /**
     * Test of position method, of class Snp.
     */
    @Test
    public void testPosition() {
        System.out.println("position");
        Snp instance = new Snp("2L",200001);
        int expResult = 200001;
        int result = instance.position();
        assertEquals(expResult, result);
 
    }
    
   @Test
    public void testToString() {
        System.out.println("toString");
        Snp instance = new Snp("2L",200001);
        String expResult = "2L-200001";
        String result = instance.toString();
        assertEquals(expResult, result);
 
    }
   
   
    @Test
    public void compareTo() {
        System.out.println("compareTo");
        List<Snp> snplist=new ArrayList<Snp>();
        snplist.add(new Snp("2L",12));
        snplist.add(new Snp("2L",2));
        snplist.add(new Snp("2L",1));
        snplist.add(new Snp("3L",12));
        snplist.add(new Snp("3R",12));
        snplist.add(new Snp("3R",8));
        snplist.add(new Snp("3R",3));
        Collections.sort(snplist);
        

        assertEquals(snplist.get(0).chromosome(),"2L");
        assertEquals(snplist.get(0).position(),1);
        assertEquals(snplist.get(1).chromosome(),"2L");
        assertEquals(snplist.get(1).position(),2);
        assertEquals(snplist.get(2).chromosome(),"2L");
        assertEquals(snplist.get(2).position(),12);
        assertEquals(snplist.get(3).chromosome(),"3L");
        assertEquals(snplist.get(3).position(),12);
        assertEquals(snplist.get(6).chromosome(),"3R");
        assertEquals(snplist.get(6).position(),12);
    }
    
    @Test
    public void hahsAndEquals() {
        System.out.println("hash and equals");
        List<Snp> snplist=new ArrayList<Snp>();
        snplist.add(new Snp("3R",3));
        snplist.add(new Snp("2L",2));
        snplist.add(new Snp("2L",2));
        snplist.add(new Snp("3L",12));
        snplist.add(new Snp("3R",3));
        snplist.add(new Snp("2L",2));
        HashSet<Snp> snpset=new HashSet<Snp>(snplist);
        snplist=new ArrayList<Snp>(snpset);
        Collections.sort(snplist);
        
        assertEquals(snplist.get(0).chromosome(),"2L");
        assertEquals(snplist.get(0).position(),2);
        assertEquals(snplist.get(1).chromosome(),"3L");
        assertEquals(snplist.get(1).position(),12);
        assertEquals(snplist.get(2).chromosome(),"3R");
        assertEquals(snplist.get(2).position(),3);
    }
}

package test.gowinda.analysis;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.*;
import gowinda.analysis.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestGOTranslator {

	
	private static GOEntry g1=new GOEntry("g1","bla1");
	private static GOEntry g2=new GOEntry("g2","bla2");
	private static GOEntry g3=new GOEntry("g3","bla3");
	private static GOEntry g4=new GOEntry("g4","bla4");
	private static GOEntry g5=new GOEntry("g5","bla5");

	private static HashMap<String,ArrayList<GOEntry>> gs2;
	private static GOTranslator gt1;
	private static GOTranslator gt2;

    @BeforeClass
    public static void setUpClass() throws Exception 
    {
    	ArrayList<GOEntry> a1=new ArrayList<GOEntry>();
    	a1.add(g1); a1.add(g2); a1.add(g5);
    	ArrayList<GOEntry> a2=new ArrayList<GOEntry>();
    	a2.add(g3); 
    	ArrayList<GOEntry>a3=new ArrayList<GOEntry>();
    	a3.add(g4);a3.add(g5);
    	ArrayList<GOEntry>a4=new ArrayList<GOEntry>();
    	a4.add(g5);
    	ArrayList<GOEntry> a5=new ArrayList<GOEntry>();
    	a5.add(g5);
    	
    	
    	gs2=new HashMap<String,ArrayList<GOEntry>>();
    	gs2.put("fbgn11",a1);
    	gs2.put("fbgn12",a5 );
    	gs2.put("fbgn13",a2);
    	gt1=new GOTranslator(gs2,gowinda.misc.CountingUnit.Gene);
    	gt2=new GOTranslator(gs2,gowinda.misc.CountingUnit.Snp);

    	
    }
	@Test
	public void test_geneA() {
		ArrayList<String> s1=new ArrayList<String>();
		s1.add("fbgn12");
		HashMap<GOEntry,Integer> res=gt1.translate(s1);
		assertEquals((int)res.get(g5),1);
		assertEquals(res.get(g1),null);
	}
	
	@Test
	public void test_geneB() {
		ArrayList<String> s1=new ArrayList<String>();
		s1.add("fbgn12"); s1.add("fbgn12"); s1.add("fbgn12");
		HashMap<GOEntry,Integer> res=gt1.translate(s1);
		assertEquals((int)res.get(g5),1);
		assertEquals(res.get(g4),null);
	}
	
	@Test
	public void test_geneC() {
		ArrayList<String> s1=new ArrayList<String>();
		s1.add("fbgn12"); s1.add("fbgn12"); s1.add("fbgn12");s1.add("fbgn11");
		HashMap<GOEntry,Integer> res=gt1.translate(s1);
		assertEquals((int)res.get(g5),2);
		assertEquals((int)res.get(g1),1);
		assertEquals((int)res.get(g2),1);
		assertEquals(res.get(g4),null);
	}
	
	@Test
	public void test_geneD() {
		ArrayList<String> s1=new ArrayList<String>();
		s1.add("fbgn11");s1.add("fbgn12"); s1.add("fbgn12"); s1.add("fbgn12");s1.add("fbgn11");
		HashMap<GOEntry,Integer> res=gt1.translate(s1);
		assertEquals((int)res.get(g5),2);
		assertEquals((int)res.get(g1),1);
		assertEquals((int)res.get(g2),1);
		assertEquals(res.get(g4),null);
	}
	
	@Test
	public void test_geneE() {
		ArrayList<String> s1=new ArrayList<String>();
		s1.add("fbgn11");s1.add("fbgn12"); s1.add("fbgn12"); s1.add("fbgn12");s1.add("fbgn11");s1.add("fbgn13");
		HashMap<GOEntry,Integer> res=gt1.translate(s1);
		assertEquals((int)res.get(g5),2);
		assertEquals((int)res.get(g1),1);
		assertEquals((int)res.get(g2),1);
		assertEquals((int)res.get(g3),1);
		assertEquals(res.get(g4),null);
	}
	
	
	@Test
	public void test_geneAs() {
		ArrayList<String> s1=new ArrayList<String>();
		s1.add("fbgn12");
		HashMap<GOEntry,Integer> res=gt2.translate(s1);
		assertEquals((int)res.get(g5),1);
		assertEquals(res.get(g1),null);
	}
	
	@Test
	public void test_geneBs() {
		ArrayList<String> s1=new ArrayList<String>();
		s1.add("fbgn12"); s1.add("fbgn12"); s1.add("fbgn12");
		HashMap<GOEntry,Integer> res=gt2.translate(s1);
		assertEquals((int)res.get(g5),3);
		assertEquals(res.get(g4),null);
	}
	
	@Test
	public void test_geneCs() {
		ArrayList<String> s1=new ArrayList<String>();
		s1.add("fbgn12"); s1.add("fbgn12"); s1.add("fbgn12");s1.add("fbgn11");
		HashMap<GOEntry,Integer> res=gt2.translate(s1);
		assertEquals((int)res.get(g5),4);
		assertEquals((int)res.get(g1),1);
		assertEquals((int)res.get(g2),1);
		assertEquals(res.get(g4),null);
	}
	
	@Test
	public void test_geneDs() {
		ArrayList<String> s1=new ArrayList<String>();
		s1.add("fbgn11");s1.add("fbgn12"); s1.add("fbgn12"); s1.add("fbgn12");s1.add("fbgn11");
		HashMap<GOEntry,Integer> res=gt2.translate(s1);
		assertEquals((int)res.get(g5),5);
		assertEquals((int)res.get(g1),2);
		assertEquals((int)res.get(g2),2);
		assertEquals(res.get(g4),null);
	}
	
	@Test
	public void test_geneEs() {
		ArrayList<String> s1=new ArrayList<String>();
		s1.add("fbgn11");s1.add("fbgn12"); s1.add("fbgn12"); s1.add("fbgn12");s1.add("fbgn11");s1.add("fbgn13");
		HashMap<GOEntry,Integer> res=gt2.translate(s1);
		assertEquals((int)res.get(g5),5);
		assertEquals((int)res.get(g1),2);
		assertEquals((int)res.get(g2),2);
		assertEquals((int)res.get(g3),1);
		assertEquals(res.get(g4),null);
	}

}

package test.gowinda.io;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.*;
import gowinda.analysis.*;

import org.junit.BeforeClass;
import gowinda.io.GOEntryBulkReader;


import org.junit.Test;

public class TestGOEntryBulkReader {
	
	private static  String inputFileFake;

	@BeforeClass
	public static void ini()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("g1	nucl disorption	fbgn1 fbgn2 fbgn3\n");
		sb.append("g2	wing pattern	fbgn4 fbgn2\n");
		sb.append("g3	dorsal ventral	fbgn5 fbgn2\n");
		inputFileFake=sb.toString();
		
	}
	
	@Test
	public void test_a() {
		GOEntryBulkReader gr=new GOEntryBulkReader(new BufferedReader(new StringReader(inputFileFake)));
		HashMap<String,ArrayList<GOEntry>> res=gr.readGOEntries();
		ArrayList<GOEntry> r=res.get("fbgn1");
		assertEquals(r.size(),1);
		assertEquals(r.get(0).goID(),"g1");
		assertEquals(r.get(0).description(),"nucl disorption");
	}

	@Test
	public void test_b() {
		GOEntryBulkReader gr=new GOEntryBulkReader(new BufferedReader(new StringReader(inputFileFake)));
		HashMap<String,ArrayList<GOEntry>> res=gr.readGOEntries();
		ArrayList<GOEntry> r=res.get("fbgn3");
		assertEquals(r.size(),1);
		assertEquals(r.get(0).goID(),"g1");
		assertEquals(r.get(0).description(),"nucl disorption");
	}
	
	@Test
	public void test_c() {
		GOEntryBulkReader gr=new GOEntryBulkReader(new BufferedReader(new StringReader(inputFileFake)));
		HashMap<String,ArrayList<GOEntry>> res=gr.readGOEntries();
		ArrayList<GOEntry> r=res.get("fbgn4");
		assertEquals(r.size(),1);
		assertEquals(r.get(0).goID(),"g2");
		assertEquals(r.get(0).description(),"wing pattern");
	}

	@Test
	public void test_d() {
		GOEntryBulkReader gr=new GOEntryBulkReader(new BufferedReader(new StringReader(inputFileFake)));
		HashMap<String,ArrayList<GOEntry>> res=gr.readGOEntries();
		ArrayList<GOEntry> r=res.get("fbgn5");
		assertEquals(r.size(),1);
		assertEquals(r.get(0).goID(),"g3");
		assertEquals(r.get(0).description(),"dorsal ventral");
	}

	@Test
	public void test_e() {
		GOEntryBulkReader gr=new GOEntryBulkReader(new BufferedReader(new StringReader(inputFileFake)));
		HashMap<String,ArrayList<GOEntry>> res=gr.readGOEntries();
		ArrayList<GOEntry> r=res.get("fbgn2");
		assertEquals(r.size(),3);

	}

}


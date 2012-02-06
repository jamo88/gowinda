package test.gowinda.analysis;

import static org.junit.Assert.*;
import gowinda.analysis.*;


import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

public class TestGenomeRepresentationList {

	private AnnotationEntry e1=new AnnotationEntry("2R","exon",10,20,AnnotationEntry.Strand.Plus,null,"fbgn01");
	private AnnotationEntry e2=new AnnotationEntry("2R","exon",15,25,AnnotationEntry.Strand.Plus,null,"fbgn01");
	private AnnotationEntry e3=new AnnotationEntry("2R","exon",15,25,AnnotationEntry.Strand.Plus,null,"fbgn02");
	private AnnotationEntry a1=new AnnotationEntry("2L","exon",1,1,AnnotationEntry.Strand.Plus,null,"fbgn01");
	private AnnotationEntry a2=new AnnotationEntry("2L","exon",2,2,AnnotationEntry.Strand.Plus,null,"fbgn02");
	private AnnotationEntry a3=new AnnotationEntry("2L","exon",3,3,AnnotationEntry.Strand.Plus,null,"fbgn03");
	private AnnotationEntry a4=new AnnotationEntry("2L","exon",4,4,AnnotationEntry.Strand.Plus,null,"fbgn04");
	private AnnotationEntry a5=new AnnotationEntry("2L","exon",5,5,AnnotationEntry.Strand.Plus,null,"fbgn05");
	private AnnotationEntry a6=new AnnotationEntry("2L","exon",6,6,AnnotationEntry.Strand.Plus,null,"fbgn06");
	private AnnotationEntry a7=new AnnotationEntry("2L","exon",7,7,AnnotationEntry.Strand.Plus,null,"fbgn07");
	private AnnotationEntry a8=new AnnotationEntry("2L","exon",8,8,AnnotationEntry.Strand.Plus,null,"fbgn08");
	private AnnotationEntry a9=new AnnotationEntry("2L","exon",9,9,AnnotationEntry.Strand.Plus,null,"fbgn09");
	private AnnotationEntry a10=new AnnotationEntry("2L","exon",5,5,AnnotationEntry.Strand.Plus,null,"fbgn15");
	
	@Test
	public void testSimple() {
		
		
		ArrayList<AnnotationEntry> al= new ArrayList<AnnotationEntry>();
		al.add(e1);
		GenomeRepresentationList gr=new GenomeRepresentationList(al);
		ArrayList<String> ret=gr.getGeneidsForSnp(new Snp("2R",10));
		assertEquals(ret.get(0),"fbgn01");
		assertEquals(ret.size(),1);
		ret=gr.getGeneidsForSnp(new Snp("2R",9));
		assertEquals(ret.size(),0);
		ret=gr.getGeneidsForSnp(new Snp("2R",21));
		assertEquals(ret.size(),0);
		ret=gr.getGeneidsForSnp(new Snp("2L",10));
		assertEquals(ret.size(),0);
		ret=gr.getGeneidsForSnp(new Snp("2R",20));
		assertEquals(ret.get(0),"fbgn01");
		assertEquals(ret.size(),1);
	}
	
	
	@Test
	public void testTwoIdentical() {
		
		
		ArrayList<AnnotationEntry> al= new ArrayList<AnnotationEntry>();
		al.add(e1);
		al.add(e2);
		GenomeRepresentationList gr=new GenomeRepresentationList(al);
		ArrayList<String> ret=gr.getGeneidsForSnp(new Snp("2R",10));
		assertEquals(ret.get(0),"fbgn01");
		assertEquals(ret.size(),1);
		ret=gr.getGeneidsForSnp(new Snp("2R",9));
		assertEquals(ret.size(),0);
		ret=gr.getGeneidsForSnp(new Snp("2R",15));
		assertEquals(ret.get(0),"fbgn01");
		assertEquals(ret.size(),1);
		ret=gr.getGeneidsForSnp(new Snp("2R",25));
		assertEquals(ret.get(0),"fbgn01");
		assertEquals(ret.size(),1);
		ret=gr.getGeneidsForSnp(new Snp("2R",26));
		assertEquals(ret.size(),0);
		
		ret=gr.getGeneidsForSnp(new Snp("2L",10));
		assertEquals(ret.size(),0);
	}
	
	@Test
	public void testTwoDifferent() {
		
		
		ArrayList<AnnotationEntry> al= new ArrayList<AnnotationEntry>();
		al.add(e1);
		al.add(e3);
		GenomeRepresentationList gr=new GenomeRepresentationList(al);
		ArrayList<String> ret=gr.getGeneidsForSnp(new Snp("2R",10));
		HashSet<String> s;
		assertEquals(ret.get(0),"fbgn01");
		assertEquals(ret.size(),1);
		ret=gr.getGeneidsForSnp(new Snp("2R",9));
		assertEquals(ret.size(),0);
		s=new HashSet<String>(gr.getGeneidsForSnp(new Snp("2R",15)));
		assertTrue(s.contains("fbgn01"));
		assertTrue(s.contains("fbgn02"));
		assertEquals(s.size(),2);
		s=new HashSet<String>(gr.getGeneidsForSnp(new Snp("2R",20)));
		assertTrue(s.contains("fbgn01"));
		assertTrue(s.contains("fbgn02"));
		assertEquals(s.size(),2);
		ret=gr.getGeneidsForSnp(new Snp("2R",21));
		assertEquals(ret.get(0),"fbgn02");
		assertEquals(ret.size(),1);
		ret=gr.getGeneidsForSnp(new Snp("2R",25));
		assertEquals(ret.get(0),"fbgn02");
		assertEquals(ret.size(),1);
		ret=gr.getGeneidsForSnp(new Snp("2L",26));
		assertEquals(ret.size(),0);
	}
	
	@Test
	public void testMultiRow()
	{
		ArrayList<AnnotationEntry> al =new ArrayList<AnnotationEntry>();
		al.add(a5); al.add(a2); al.add(a6);al.add(a1);al.add(a9);al.add(a3);al.add(a8);al.add(a7); al.add(a4);
		GenomeRepresentationList gr=new GenomeRepresentationList(al);
		ArrayList<String> ret=gr.getGeneidsForSnp(new Snp("2L",4));
		assertEquals(ret.size(),1);
		assertEquals(ret.get(0),"fbgn04");

		ret=gr.getGeneidsForSnp(new Snp("2L",5));
		assertEquals(ret.size(),1);
		assertEquals(ret.get(0),"fbgn05");
		
		ret=gr.getGeneidsForSnp(new Snp("2L",9));
		assertEquals(ret.size(),1);
		assertEquals(ret.get(0),"fbgn09");
		
		ret=gr.getGeneidsForSnp(new Snp("2L",1));
		assertEquals(ret.size(),1);
		assertEquals(ret.get(0),"fbgn01");
		
		
	}
	
	@Test
	public void testMultiRow2()
	{
		ArrayList<AnnotationEntry> al =new ArrayList<AnnotationEntry>();
		al.add(a5); al.add(a2); al.add(a6);al.add(a1);al.add(a9);al.add(a3);al.add(a8);al.add(a7); al.add(a4);al.add(a10);
		GenomeRepresentationList gr=new GenomeRepresentationList(al);
		ArrayList<String> ret=gr.getGeneidsForSnp(new Snp("2L",4));
		assertEquals(ret.size(),1);
		assertEquals(ret.get(0),"fbgn04");

		ret=gr.getGeneidsForSnp(new Snp("2L",5));
		assertEquals(ret.size(),2);
		assertEquals(ret.get(0),"fbgn05");
		assertEquals(ret.get(1),"fbgn15");		
		
		ret=gr.getGeneidsForSnp(new Snp("2L",9));
		assertEquals(ret.size(),1);
		assertEquals(ret.get(0),"fbgn09");
		
		ret=gr.getGeneidsForSnp(new Snp("2L",1));
		assertEquals(ret.size(),1);
		assertEquals(ret.get(0),"fbgn01");
		
		
	}

}

package gowinda.analysis;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

public class TestGenomeRepresentationList {

	private AnnotationEntry e1=new AnnotationEntry("2R","exon",10,20,AnnotationEntry.Strand.Plus,null,"FBgn01");
	private AnnotationEntry e2=new AnnotationEntry("2R","exon",15,25,AnnotationEntry.Strand.Plus,null,"FBgn01");
	private AnnotationEntry e3=new AnnotationEntry("2R","exon",15,25,AnnotationEntry.Strand.Plus,null,"FBgn02");
	@Test
	public void testSimple() {
		
		
		ArrayList<AnnotationEntry> al= new ArrayList<AnnotationEntry>();
		al.add(e1);
		GenomeRepresentationList gr=new GenomeRepresentationList(al);
		ArrayList<String> ret=gr.getGeneidsForSnp(new Snp("2R",10));
		assertEquals(ret.get(0),"FBgn01");
		assertEquals(ret.size(),1);
		ret=gr.getGeneidsForSnp(new Snp("2R",9));
		assertEquals(ret.size(),0);
		ret=gr.getGeneidsForSnp(new Snp("2R",21));
		assertEquals(ret.size(),0);
		ret=gr.getGeneidsForSnp(new Snp("2L",10));
		assertEquals(ret.size(),0);
		ret=gr.getGeneidsForSnp(new Snp("2R",20));
		assertEquals(ret.get(0),"FBgn01");
		assertEquals(ret.size(),1);
	}
	
	
	@Test
	public void testTwoIdentical() {
		
		
		ArrayList<AnnotationEntry> al= new ArrayList<AnnotationEntry>();
		al.add(e1);
		al.add(e2);
		GenomeRepresentationList gr=new GenomeRepresentationList(al);
		ArrayList<String> ret=gr.getGeneidsForSnp(new Snp("2R",10));
		assertEquals(ret.get(0),"FBgn01");
		assertEquals(ret.size(),1);
		ret=gr.getGeneidsForSnp(new Snp("2R",9));
		assertEquals(ret.size(),0);
		ret=gr.getGeneidsForSnp(new Snp("2R",15));
		assertEquals(ret.get(0),"FBgn01");
		assertEquals(ret.size(),1);
		ret=gr.getGeneidsForSnp(new Snp("2R",25));
		assertEquals(ret.get(0),"FBgn01");
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
		assertEquals(ret.get(0),"FBgn01");
		assertEquals(ret.size(),1);
		ret=gr.getGeneidsForSnp(new Snp("2R",9));
		assertEquals(ret.size(),0);
		s=new HashSet<String>(gr.getGeneidsForSnp(new Snp("2R",15)));
		assertTrue(s.contains("FBgn01"));
		assertTrue(s.contains("FBgn02"));
		assertEquals(s.size(),2);
		s=new HashSet<String>(gr.getGeneidsForSnp(new Snp("2R",20)));
		assertTrue(s.contains("FBgn01"));
		assertTrue(s.contains("FBgn02"));
		assertEquals(s.size(),2);
		ret=gr.getGeneidsForSnp(new Snp("2R",21));
		assertEquals(ret.get(0),"FBgn02");
		assertEquals(ret.size(),1);
		ret=gr.getGeneidsForSnp(new Snp("2R",25));
		assertEquals(ret.get(0),"FBgn02");
		assertEquals(ret.size(),1);
		ret=gr.getGeneidsForSnp(new Snp("2L",26));
		assertEquals(ret.size(),0);
	}

}

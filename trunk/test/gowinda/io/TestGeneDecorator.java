package test.gowinda.io;

import static org.junit.Assert.*;
import gowinda.analysis.AnnotationEntry;
import java.util.*;
import org.junit.BeforeClass;
import gowinda.io.GeneDecorator;

import org.junit.Test;

public class TestGeneDecorator {

	private static AnnotationEntry a1=new AnnotationEntry("2R","exon",100,110,AnnotationEntry.Strand.Plus,0,"fbgn007");
	private static AnnotationEntry a2=new AnnotationEntry("2R","exon",80,90,AnnotationEntry.Strand.Plus,0,"fbgn007");
	private static AnnotationEntry a3=new AnnotationEntry("2R","exon",120,130,AnnotationEntry.Strand.Plus,0,"fbgn007");
	private static ArrayList<AnnotationEntry> l1=new ArrayList<AnnotationEntry>();
	
	private static AnnotationEntry a5=new AnnotationEntry("2R","exon",1000,1010,AnnotationEntry.Strand.Minus,0,"fbgn008");
	private static AnnotationEntry a6=new AnnotationEntry("2R","exon",1100,1120,AnnotationEntry.Strand.Minus,0,"fbgn008");
	private static AnnotationEntry a7=new AnnotationEntry("2R","exon",1200,1300,AnnotationEntry.Strand.Plus,0,"fbgn008");
	private static AnnotationEntry a8=new AnnotationEntry("2R","exon",200,250,AnnotationEntry.Strand.Minus,0,"fbgn008");	
	private static ArrayList<AnnotationEntry> l2=new ArrayList<AnnotationEntry>();
	private static ArrayList<AnnotationEntry> l3=new ArrayList<AnnotationEntry>();
	
	
	@BeforeClass
	public static void ini()
	{
		l1.add(a1);
		l1.add(a2);
		l1.add(a3);
		
		l2.add(a5);
		l2.add(a6);
		l2.add(a7);
		l2.add(a8);
		
		l3.add(a1);
		l3.add(a2);
		l3.add(a3);
		l3.add(a5);
		l3.add(a6);
		l3.add(a7);
		l3.add(a8);
		
	}
	
	@Test
	public void test_a() {
		ArrayList<AnnotationEntry> r=new GeneDecorator(l1).readAnnotation();
		assertEquals(r.size(),1);
		assertEquals(r.get(0).chromosome(),"2R");
		assertEquals(r.get(0).start(),80);
		assertEquals(r.get(0).end(),130);
		assertEquals(r.get(0).strand(),AnnotationEntry.Strand.Plus);
		assertEquals(r.get(0).geneid(),"fbgn007");
	}
	
	@Test
	public void test_b() {
		ArrayList<AnnotationEntry> r=new GeneDecorator(l2).readAnnotation();
		assertEquals(r.size(),1);
		assertEquals(r.get(0).chromosome(),"2R");
		assertEquals(r.get(0).start(),200);
		assertEquals(r.get(0).end(),1300);
		assertEquals(r.get(0).strand(),AnnotationEntry.Strand.Minus);
		assertEquals(r.get(0).geneid(),"fbgn008");
	}
	
	@Test
	public void test_c() {
		ArrayList<AnnotationEntry> r=new GeneDecorator(l3).readAnnotation();
		assertEquals(r.size(),2);
		assertEquals(r.get(0).geneid(),"fbgn007");
		assertEquals(r.get(1).geneid(),"fbgn008");
	}
	


}

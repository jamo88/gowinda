package test.gowinda.io;

import static org.junit.Assert.*;
import gowinda.analysis.AnnotationEntry;
import java.util.*;
import org.junit.BeforeClass;
import gowinda.io.UtrDecorator;

import org.junit.Test;

public class TestUtrDecorator {

	private static AnnotationEntry a1=new AnnotationEntry("2R","exon",100,400,AnnotationEntry.Strand.Plus,0,"fbgn007");
	private static AnnotationEntry a2=new AnnotationEntry("2R","cds",200,300,AnnotationEntry.Strand.Plus,0,"fbgn007");
	private static ArrayList<AnnotationEntry> l1=new ArrayList<AnnotationEntry>();
	
	private static AnnotationEntry a5=new AnnotationEntry("2R","exon",1000,1100,AnnotationEntry.Strand.Plus,0,"fbgn008");
	private static AnnotationEntry a6=new AnnotationEntry("2R","exon",1200,1300,AnnotationEntry.Strand.Plus,0,"fbgn008");
	private static AnnotationEntry a7=new AnnotationEntry("2R","exon",1400,1500,AnnotationEntry.Strand.Plus,0,"fbgn008");
	private static AnnotationEntry a8=new AnnotationEntry("2R","cds",1050,1100,AnnotationEntry.Strand.Plus,0,"fbgn008");
	private static AnnotationEntry a9=new AnnotationEntry("2R","cds",1200,1300,AnnotationEntry.Strand.Plus,0,"fbgn008");
	private static AnnotationEntry a10=new AnnotationEntry("2R","cds",1400,1450,AnnotationEntry.Strand.Plus,0,"fbgn008");
	private static ArrayList<AnnotationEntry> l2=new ArrayList<AnnotationEntry>();

	
	
	@BeforeClass
	public static void ini()
	{
		l1.add(a1);
		l1.add(a2);
		
		l2.add(a5);
		l2.add(a6);
		l2.add(a7);
		l2.add(a8);
		l2.add(a9);
		l2.add(a10);
	}
	
	@Test
	public void test_a() {
		ArrayList<AnnotationEntry> r=new UtrDecorator(l1).readAnnotation();
		assertEquals(r.size(),2);
		assertEquals(r.get(0).chromosome(),"2R");
		assertEquals(r.get(1).chromosome(),"2R");
		assertEquals(r.get(0).strand(),AnnotationEntry.Strand.Plus);
		assertEquals(r.get(1).strand(),AnnotationEntry.Strand.Plus);		
		assertEquals(r.get(0).feature(),"utr");
		assertEquals(r.get(1).feature(),"utr");		

		assertEquals(r.get(0).start(),100);
		assertEquals(r.get(0).end(),199);
		assertEquals(r.get(1).start(),301);
		assertEquals(r.get(1).end(),400);
	}
	
	@Test
	public void test_b() {
		ArrayList<AnnotationEntry> r=new UtrDecorator(l2).readAnnotation();
		assertEquals(r.size(),2);
		assertEquals(r.get(0).chromosome(),"2R");
		assertEquals(r.get(1).chromosome(),"2R");
		assertEquals(r.get(0).strand(),AnnotationEntry.Strand.Plus);
		assertEquals(r.get(1).strand(),AnnotationEntry.Strand.Plus);		
		assertEquals(r.get(0).feature(),"utr");
		assertEquals(r.get(1).feature(),"utr");		

		assertEquals(r.get(0).start(),1000);
		assertEquals(r.get(0).end(),1049);
		assertEquals(r.get(1).start(),1451);
		assertEquals(r.get(1).end(),1500);
	}
	

}

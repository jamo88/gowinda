package test.gowinda.io;

import static org.junit.Assert.*;
import gowinda.analysis.AnnotationEntry;
import gowinda.io.*;
import org.junit.BeforeClass;

import java.util.ArrayList;

import org.junit.Test;


public class TestUpstreamGeneDecorator {

	private static AnnotationEntry a1=new AnnotationEntry("2R","gene",10000,20000,AnnotationEntry.Strand.Plus,0,"fbgn007");
	private static AnnotationEntry a2=new AnnotationEntry("2R","gene",100000,110000,AnnotationEntry.Strand.Minus,0,"fbgn008");
	private static ArrayList<AnnotationEntry> l1=new ArrayList<AnnotationEntry>();
	private static ArrayList<AnnotationEntry> l2=new ArrayList<AnnotationEntry>();
	
	@BeforeClass
	public static void ini()
	{
		l1.add(a1);
		l2.add(a2);
	}
	
	@Test
	public void test_1() {
		ArrayList<AnnotationEntry> r=new UpstreamDecorator(l1,1000).readAnnotation(); 
		assertEquals(r.size(),1);
		assertEquals(r.get(0).geneid(),"fbgn007");
		assertEquals(r.get(0).start(),9000);
		assertEquals(r.get(0).end(),20000);
		assertEquals(r.get(0).strand(),AnnotationEntry.Strand.Plus);
		
	}
	
	@Test
	public void test_2() {
		ArrayList<AnnotationEntry> r=new UpstreamDecorator(l2,10000).readAnnotation(); 
		assertEquals(r.size(),1);
		assertEquals(r.get(0).geneid(),"fbgn008");
		assertEquals(r.get(0).start(),100000);
		assertEquals(r.get(0).end(),120000);
		assertEquals(r.get(0).strand(),AnnotationEntry.Strand.Minus);
		
	}

}

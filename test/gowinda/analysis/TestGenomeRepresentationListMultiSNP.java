package test.gowinda.analysis;

import org.junit.BeforeClass;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;

import gowinda.analysis.*;

public class TestGenomeRepresentationListMultiSNP {
	
	private static AnnotationEntry a1=new AnnotationEntry("2L","exon",1,1,AnnotationEntry.Strand.Plus,null,"fbgn01");
	private static AnnotationEntry a2=new AnnotationEntry("2L","exon",2,2,AnnotationEntry.Strand.Plus,null,"fbgn02");
	private static AnnotationEntry a3=new AnnotationEntry("2L","exon",3,3,AnnotationEntry.Strand.Plus,null,"fbgn03");
	private static AnnotationEntry a4=new AnnotationEntry("2L","exon",4,4,AnnotationEntry.Strand.Plus,null,"fbgn04");
	private static AnnotationEntry a5=new AnnotationEntry("2L","exon",5,5,AnnotationEntry.Strand.Plus,null,"fbgn05");
	private static AnnotationEntry a6=new AnnotationEntry("2L","exon",6,6,AnnotationEntry.Strand.Plus,null,"fbgn06");
	private static AnnotationEntry a7=new AnnotationEntry("2L","exon",7,7,AnnotationEntry.Strand.Plus,null,"fbgn07");
	private static AnnotationEntry a8=new AnnotationEntry("2L","exon",8,8,AnnotationEntry.Strand.Plus,null,"fbgn08");
	private static AnnotationEntry a9=new AnnotationEntry("2L","exon",9,9,AnnotationEntry.Strand.Plus,null,"fbgn09");
	private static AnnotationEntry a10=new AnnotationEntry("2L","exon",5,5,AnnotationEntry.Strand.Plus,null,"fbgn15");
	private static AnnotationEntry a11=new AnnotationEntry("2L","exon",11,11,AnnotationEntry.Strand.Plus,null,"fbgn01");
	private static IGenomeRepresentation genrep;

	
    @BeforeClass
    public static void setUpClass() throws Exception {
    	ArrayList<AnnotationEntry> al=new ArrayList<AnnotationEntry>();
    	al.add(a1);al.add(a2);al.add(a3);al.add(a4);al.add(a5);al.add(a6);al.add(a7); al.add(a8); al.add(a9);al.add(a10);al.add(a11);
    	genrep=new GenomeRepresentationList(al);
    }
    
    
    @Test
    public void TestSnpA()
    {
    	ArrayList<Snp> s = new ArrayList<Snp>();
    	s.add(new Snp("2L",1));
    	ArrayList<String>res=genrep.getGeneidsForSnps(s);
    	assertEquals(res.get(0),"fbgn01");
    }

    @Test
    public void TestSnpB()
    {
    	ArrayList<Snp> s = new ArrayList<Snp>();
    	s.add(new Snp("2L",2));
    	ArrayList<String>res=genrep.getGeneidsForSnps(s);
    	assertEquals(res.get(0),"fbgn02");
    }
    
    
    @Test
    public void TestSnpC()
    {
    	ArrayList<Snp> s = new ArrayList<Snp>();
    	s.add(new Snp("2L",5));
    	ArrayList<String>res=genrep.getGeneidsForSnps(s);
    	assertEquals(res.get(0),"fbgn05");
    	assertEquals(res.size(),2);
    	assertEquals(res.get(1),"fbgn15");
    }
    
    @Test
    public void TestSnpD()
    {
    	ArrayList<Snp> s = new ArrayList<Snp>();
    	s.add(new Snp("2L",1));
    	s.add(new Snp("2L",9));
    	ArrayList<String>res=genrep.getGeneidsForSnps(s);
    	assertEquals(res.get(0),"fbgn01");
    	assertEquals(res.size(),2);
    	assertEquals(res.get(1),"fbgn09");
    }
    
    @Test
    public void TestSnpE()
    {
    	ArrayList<Snp> s = new ArrayList<Snp>();
    	s.add(new Snp("2L",1));
    	s.add(new Snp("2L",5));
    	s.add(new Snp("2L",9));
    	ArrayList<String>res=genrep.getGeneidsForSnps(s);
    	assertEquals(res.size(),4);
    	assertEquals(res.get(0),"fbgn01");
    	assertEquals(res.get(1),"fbgn05");
    	assertEquals(res.get(2),"fbgn15");
    	assertEquals(res.get(3),"fbgn09");
    }

    @Test
    public void TestSnpF()
    {
    	ArrayList<Snp> s = new ArrayList<Snp>();
    	s.add(new Snp("2L",1));
    	s.add(new Snp("2L",11));
    	ArrayList<String>res=genrep.getGeneidsForSnps(s);
    	assertEquals(res.get(0),"fbgn01");
    	assertEquals(res.size(),2);
    	assertEquals(res.get(1),"fbgn01");
    }
    
    
  
}

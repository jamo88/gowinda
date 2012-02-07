package test.gowinda.io;

import org.junit.BeforeClass;
import gowinda.io.SnpReader;
import gowinda.analysis.Snp;
import java.io.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestSnpIO {
	private static SnpReader sr;
	
	@BeforeClass
	public static void ini()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("X	101	T	0:19:16:0:0:0	0:87:54:0:0:0	0:96:8:0:0:0	0:109:0:0:0:0	2.109981e-18	0.0146540545246982\n");
		sb.append("X	102\n");
		sb.append("2L	1	0.0001.0001.000.100012jdflajdlfkjadslkfjalkdkjflkdfjladkfjlkdfj");
		
		sr=new SnpReader(new BufferedReader(new StringReader(sb.toString())));
	}

	@Test
	public void test_line1() throws IOException {
		Snp r=sr.next();
		assertEquals(r.chromosome(),"X");
		assertEquals(r.position(),101);
	}
	
	@Test
	public void test_line2() throws IOException {
		Snp r=sr.next();
		assertEquals(r.chromosome(),"X");
		assertEquals(r.position(),102);
	}
	
	@Test
	public void test_line3() throws IOException {
		Snp r=sr.next();
		assertEquals(r.chromosome(),"2L");
		assertEquals(r.position(),1);
	
	}
	
	@Test
	public void test_line4() throws IOException {
		Snp r=sr.next();
		assertEquals(r,null);

	
	}

}

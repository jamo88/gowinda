package test.gowinda.analysis;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import java.util.Arrays;
import java.util.*;

import gowinda.analysis.FdrAdjuster;
import org.junit.Test;

public class TestFdrAdjuster {

	private static FdrAdjuster a5=new FdrAdjuster(5);
	private static FdrAdjuster a10=new FdrAdjuster(10);
	private static FdrAdjuster a10000=new FdrAdjuster(10000);
	private static ArrayList<Double> p5s;
	private static ArrayList<Double> p5ns;
	private static ArrayList<Double> p5eq;
	private static ArrayList<Double> p10;
	private static ArrayList<Double> p5rs;
	
	@BeforeClass
	public static void ini()
	{
		Double[] d={0.00001,0.0001,0.001,0.01,0.1};
		Double[] d2={0.01,0.1,0.0001,0.00001,0.001};
		p5s=new ArrayList<Double>(Arrays.asList(d));
		p5ns=new ArrayList<Double>(Arrays.asList(d2));
		
		Double[] d3={0.1, 0.1, 0.1, 0.1, 0.1};
		p5eq=new ArrayList<Double>(Arrays.asList(d3));
		
		Double[] d4={0.01, 0.1 ,0.2 ,0.02 ,0.03 ,0.0004 ,0.0001, 0.01, 1.0, 0.5};
		p10=new ArrayList<Double>(Arrays.asList(d4));
	
		Double[] d5={0.1, 0.01, 0.001, 0.0001, 0.00001};
		p5rs=new ArrayList<Double>(Arrays.asList(d5));
	}
	
	@Test
	public void test_a() {
		ArrayList<Double> res=a5.getAdjustedSignificance(p5s);
		assertEquals(res.get(0),0.00005,0.0000001);
		assertEquals(res.get(1),0.00025,0.0000001);
		assertEquals(res.get(2),0.001666666,0.0000001);
		assertEquals(res.get(3),0.0125, 0.0000001);
		assertEquals(res.get(4),0.1,0.000001);
	}
	
	@Test
	public void test_b() {
		ArrayList<Double> res=a5.getAdjustedSignificance(p5ns);
		assertEquals(res.get(3),0.00005,0.0000001);
		assertEquals(res.get(2),0.00025,0.0000001);
		assertEquals(res.get(4),0.001666666,0.0000001);
		assertEquals(res.get(0),0.0125, 0.0000001);
		assertEquals(res.get(1),0.1,0.000001);
	}
	
	@Test
	public void test_c() {
		ArrayList<Double> res=a5.getAdjustedSignificance(p5eq);
		assertEquals(res.get(0),0.1,0.0000001);
		assertEquals(res.get(1),0.1,0.0000001);
		assertEquals(res.get(2),0.1,0.0000001);
		assertEquals(res.get(3),0.1, 0.0000001);
		assertEquals(res.get(4),0.1,0.000001);
	}
	
	@Test
	public void test_d() {
		ArrayList<Double> res=a10.getAdjustedSignificance(p5eq);
		assertEquals(res.get(0),0.2,0.0000001);
		assertEquals(res.get(1),0.2,0.0000001);
		assertEquals(res.get(2),0.2,0.0000001);
		assertEquals(res.get(3),0.2, 0.0000001);
		assertEquals(res.get(4),0.2,0.000001);
	}
	
	@Test
	public void test_e() {
		ArrayList<Double> res=a10.getAdjustedSignificance(p5s);
		assertEquals(res.get(0),0.0001,0.0000001);
		assertEquals(res.get(1),0.0005,0.0000001);
		assertEquals(res.get(2),0.003333333,0.0000001);
		assertEquals(res.get(3),0.0250, 0.0000001);
		assertEquals(res.get(4),0.2,0.000001);
	}
	
	@Test
	public void test_f() {
		ArrayList<Double> res=a10.getAdjustedSignificance(p10);
		assertEquals(res.get(0),0.025,0.0000001);
		assertEquals(res.get(1),0.14287571,0.0001);
		assertEquals(res.get(2),0.25,0.0000001);
		assertEquals(res.get(3),0.04, 0.0000001);
		assertEquals(res.get(4),0.05,0.000001);
		assertEquals(res.get(5),0.002,0.000001);
		assertEquals(res.get(6),0.001,0.000001);
		assertEquals(res.get(7),0.025,0.000001);
		assertEquals(res.get(8),1.0,0.000001);
		assertEquals(res.get(9),0.555555,0.0001);
	}
	
	@Test
	public void test_g() {
		ArrayList<Double> res=a10000.getAdjustedSignificance(p5s);
		assertEquals(res.get(0),0.1, 0.0000001);
		assertEquals(res.get(1),0.5, 0.0000001);
		assertEquals(res.get(2),1.0, 0.0000001);
		assertEquals(res.get(3),1.0, 0.0000001);
		assertEquals(res.get(4),1.0, 0.000001);
	}
	
	@Test
	public void test_h() {
		ArrayList<Double> res=a5.getAdjustedSignificance(p5rs);
		assertEquals(res.get(0),0.1,0.0000001);
		assertEquals(res.get(1),0.0125,0.0000001);
		assertEquals(res.get(2),0.001666666,0.0000001);
		assertEquals(res.get(3),0.00025, 0.0000001);
		assertEquals(res.get(4),0.00005,0.000001);
	}
	
	

}

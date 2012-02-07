package test.gowinda.analysis;

import gowinda.analysis.*;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import java.util.*;

import org.junit.Test;

public class TestGOSimulationContainer {
	
	private static GOSimulationContainer gosimED;
	private static GOSimulationContainer gosimPK;
	private static GOSimulationContainer gosimMulti;
	private static GOEntry g1=new GOEntry("g1","bla1");
	private static GOEntry g2=new GOEntry("g2","bla2");
	private static IMultipleTestingAdjuster adj =new BonferroniAdjuster(10); 
	
	@BeforeClass
	public static void buildGOContainerED()
	{
		HashMap<GOEntry,Integer> singleSimulation=new HashMap<GOEntry,Integer>();
		singleSimulation.put(g1, 1);
		gosimED=new GOSimulationContainer(40);
		for(int i=0; i<10; i++)
		{
			gosimED.addSimulation(singleSimulation);
		}
		singleSimulation.put(g1, 2);
		for(int i=0; i<10; i++)
		{
			gosimED.addSimulation(singleSimulation);
		}
		singleSimulation.put(g1, 3);
		for(int i=0; i<10; i++)
		{
			gosimED.addSimulation(singleSimulation);
		}
		singleSimulation.put(g1, 4);
		for(int i=0; i<10; i++)
		{
			gosimED.addSimulation(singleSimulation);
		}

		
		// Peak
		singleSimulation=new HashMap<GOEntry,Integer>();
		singleSimulation.put(g1, 10);
		gosimPK=new GOSimulationContainer(100);
		for(int i=0; i<30; i++)
		{
			gosimPK.addSimulation(singleSimulation);
		}
		singleSimulation.put(g1, 20);
		for(int i=0; i<40; i++)
		{
			gosimPK.addSimulation(singleSimulation);
		}
		singleSimulation.put(g1, 30);
		for(int i=0; i<20; i++)
		{
			gosimPK.addSimulation(singleSimulation);
		}
		singleSimulation.put(g1, 40);
		for(int i=0; i<10; i++)
		{
			gosimPK.addSimulation(singleSimulation);
		}
		

		gosimMulti=new GOSimulationContainer(40);
		// Multi
		singleSimulation=new HashMap<GOEntry,Integer>();
		singleSimulation.put(g1, 1);
		singleSimulation.put(g2, 10);
		for(int i=0; i<10; i++)
		{
			gosimMulti.addSimulation(singleSimulation);
		}
		singleSimulation.put(g1, 2);
		singleSimulation.put(g2, 20);
		for(int i=0; i<10; i++)
		{
			gosimMulti.addSimulation(singleSimulation);
		}
		singleSimulation.put(g1, 3);
		singleSimulation.put(g2, 30);
		for(int i=0; i<10; i++)
		{
			gosimMulti.addSimulation(singleSimulation);
		}
		singleSimulation.put(g1, 4);
		singleSimulation.put(g2, 40);
		for(int i=0; i<10; i++)
		{
			gosimMulti.addSimulation(singleSimulation);
		}
	}

	@Test
	public void test_1() {
		HashMap<GOEntry,Integer> candRes=new HashMap<GOEntry,Integer>();
		candRes.put(g1, 1);
		ArrayList<GOResultForCandidateSnp> gr =gosimED.estimateSignificance(candRes,adj);
		assertEquals(gr.size(),1);
		assertEquals(gr.get(0).significance(), 1.0, 0.0000001);
		assertEquals(gr.get(0).observedCount(),1);
		assertEquals(gr.get(0).expectedCount(),2.5,0.0000001);
		assertEquals(gr.get(0).adjustedSignificance(), 1.0, 0.000001);
		assertEquals(gr.get(0).goEntry().goID(),"g1");
	}
	
	@Test
	public void test_2() {
		HashMap<GOEntry,Integer> candRes=new HashMap<GOEntry,Integer>();
		candRes.put(g1, 2);
		ArrayList<GOResultForCandidateSnp> gr =gosimED.estimateSignificance(candRes,adj);
		assertEquals(gr.size(),1);
		assertEquals(gr.get(0).significance(), 0.75, 0.0000001);
		assertEquals(gr.get(0).observedCount(),2);
		assertEquals(gr.get(0).expectedCount(),2.5,0.0000001);
		assertEquals(gr.get(0).adjustedSignificance(), 1.0, 0.000001);
		assertEquals(gr.get(0).goEntry().goID(),"g1");
	}
	
	@Test
	public void test_3() {
		HashMap<GOEntry,Integer> candRes=new HashMap<GOEntry,Integer>();
		candRes.put(g1, 3);
		ArrayList<GOResultForCandidateSnp> gr =gosimED.estimateSignificance(candRes,adj);
		assertEquals(gr.size(),1);
		assertEquals(gr.get(0).significance(), 0.50, 0.0000001);
		assertEquals(gr.get(0).observedCount(),3);
		assertEquals(gr.get(0).adjustedSignificance(), 1.0, 0.000001);
	}
	
	@Test
	public void test_4() {
		HashMap<GOEntry,Integer> candRes=new HashMap<GOEntry,Integer>();
		candRes.put(g1, 4);
		ArrayList<GOResultForCandidateSnp> gr =gosimED.estimateSignificance(candRes,adj);
		assertEquals(gr.size(),1);
		assertEquals(gr.get(0).significance(), 0.25, 0.0000001);
		assertEquals(gr.get(0).observedCount(),4);
		assertEquals(gr.get(0).adjustedSignificance(), 1.0, 0.000001);
	}
	
	@Test
	public void test_5() {
		HashMap<GOEntry,Integer> candRes=new HashMap<GOEntry,Integer>();
		candRes.put(g1, 5);
		ArrayList<GOResultForCandidateSnp> gr =gosimED.estimateSignificance(candRes,adj);
		assertEquals(gr.size(),1);
		assertEquals(gr.get(0).significance(), 0.025, 0.0000001);
		assertEquals(gr.get(0).observedCount(),5);
		assertEquals(gr.get(0).adjustedSignificance(), 0.25, 0.000001);
	}
	
	@Test
	public void test_p1lower() {
		HashMap<GOEntry,Integer> candRes=new HashMap<GOEntry,Integer>();
		candRes.put(g1, 1);
		ArrayList<GOResultForCandidateSnp> gr =gosimPK.estimateSignificance(candRes,adj);
		assertEquals(gr.size(),1);
		assertEquals(gr.get(0).significance(), 1.0,  0.0000001);
		assertEquals(gr.get(0).observedCount(),1);
		assertEquals(gr.get(0).expectedCount(),21.0, 0.0000001);
		assertEquals(gr.get(0).adjustedSignificance(), 1.0, 0.000001);
		assertEquals(gr.get(0).goEntry().goID(),"g1");
	}
	
	
	@Test
	public void test_p1() {
		HashMap<GOEntry,Integer> candRes=new HashMap<GOEntry,Integer>();
		candRes.put(g1, 10);
		ArrayList<GOResultForCandidateSnp> gr =gosimPK.estimateSignificance(candRes,adj);
		assertEquals(gr.size(),1);
		assertEquals(gr.get(0).significance(), 1.0, 0.0000001);
		assertEquals(gr.get(0).observedCount(),10);
		assertEquals(gr.get(0).expectedCount(),21.0,0.0000001);
		assertEquals(gr.get(0).adjustedSignificance(), 1.0, 0.000001);
		assertEquals(gr.get(0).goEntry().goID(),"g1");
	}
	

	@Test
	public void test_p2() {
		HashMap<GOEntry,Integer> candRes=new HashMap<GOEntry,Integer>();
		candRes.put(g1, 20);
		ArrayList<GOResultForCandidateSnp> gr =gosimPK.estimateSignificance(candRes,adj);
		assertEquals(gr.size(),1);
		assertEquals(gr.get(0).significance(), 0.7, 0.0000001);
		assertEquals(gr.get(0).observedCount(),20);
		assertEquals(gr.get(0).expectedCount(),21.0 , 0.0000001);
		assertEquals(gr.get(0).adjustedSignificance(), 1.0, 0.000001);

	}
	
	@Test
	public void test_p3() {
		HashMap<GOEntry,Integer> candRes=new HashMap<GOEntry,Integer>();
		candRes.put(g1, 30);
		ArrayList<GOResultForCandidateSnp> gr =gosimPK.estimateSignificance(candRes,adj);
		assertEquals(gr.size(),1);
		assertEquals(gr.get(0).significance(), 0.30, 0.0000001);
		assertEquals(gr.get(0).observedCount(),30);
		assertEquals(gr.get(0).adjustedSignificance(), 1.0, 0.000001);
	}
	
	
	@Test
	public void test_p4() {
		HashMap<GOEntry,Integer> candRes=new HashMap<GOEntry,Integer>();
		candRes.put(g1, 40);
		ArrayList<GOResultForCandidateSnp> gr =gosimPK.estimateSignificance(candRes,adj);
		assertEquals(gr.size(),1);
		assertEquals(gr.get(0).significance(), 0.10, 0.0000001);
		assertEquals(gr.get(0).observedCount(),40);
		assertEquals(gr.get(0).adjustedSignificance(), 1.0, 0.000001);
	}
	
	
	
	@Test
	public void test_p5() {
		HashMap<GOEntry,Integer> candRes=new HashMap<GOEntry,Integer>();
		candRes.put(g1, 50);
		ArrayList<GOResultForCandidateSnp> gr =gosimPK.estimateSignificance(candRes,adj);
		assertEquals(gr.size(),1);
		assertEquals(gr.get(0).significance(), 0.01, 0.0000001);
		assertEquals(gr.get(0).observedCount(),50);
		assertEquals(gr.get(0).adjustedSignificance(), 0.10, 0.000001);
	}
	
	@Test
	public void test_m1() {
		HashMap<GOEntry,Integer> candRes=new HashMap<GOEntry,Integer>();
		candRes.put(g1, 1);
		candRes.put(g2,10);
		ArrayList<GOResultForCandidateSnp> gr =gosimMulti.estimateSignificance(candRes,adj);
		assertEquals(gr.size(),2);
		assertEquals(gr.get(0).goEntry().goID(),"g2");
		assertEquals(gr.get(1).goEntry().goID(),"g1");
		assertEquals(gr.get(0).observedCount(),10);
		assertEquals(gr.get(1).observedCount(),1);
	}
	
	@Test
	public void test_m2() {
		HashMap<GOEntry,Integer> candRes=new HashMap<GOEntry,Integer>();
		candRes.put(g1, 4);
		candRes.put(g2,10);
		ArrayList<GOResultForCandidateSnp> gr =gosimMulti.estimateSignificance(candRes,adj);
		assertEquals(gr.size(),2);
		assertEquals(gr.get(0).goEntry().goID(),"g2");
		assertEquals(gr.get(1).goEntry().goID(),"g1");
		assertEquals(gr.get(0).observedCount(),10);
		assertEquals(gr.get(1).observedCount(),4);
		assertEquals(gr.get(0).significance(),1.0, 0.0000001);
		assertEquals(gr.get(1).significance(),0.25,0.0000001);
	}
	
	

}

package test.gowinda.analysis;

import gowinda.analysis.GOEntry;
import gowinda.analysis.GOSimulationContainer;
import gowinda.analysis.fdr.FdrSimulationContainer;
import java.util.HashMap;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestPvalueDistribution {


	private static GOSimulationContainer gs1;
	private static GOSimulationContainer gs2;
	private static GOSimulationContainer gs3;
	private static GOEntry g1=new GOEntry("g1","bla1");
	private static GOEntry g2=new GOEntry("g2","bla2");
	private static GOEntry g3=new GOEntry("g3","bla3");

	
	@BeforeClass
	public static void buildGOContainerED()
	{
		HashMap<GOEntry,Integer> singleSimulation=new HashMap<GOEntry,Integer>();
		GOSimulationContainer.GOSimulationContainerBuilder builder;
		builder=new GOSimulationContainer.GOSimulationContainerBuilder();
		// Multi
		singleSimulation=new HashMap<GOEntry,Integer>();
		
		// For three GO categories 60 times zero genes; 40 times 1 gene
		for(int i=0; i<60; i++)
		{
			builder.addSimulation(singleSimulation);
		}
		singleSimulation=new HashMap<GOEntry,Integer>();
		singleSimulation.put(g1, 1);
		singleSimulation.put(g2, 1);
		singleSimulation.put(g3, 1);
		for(int i=0; i<40; i++)
		{
			builder.addSimulation(singleSimulation);
		}
		gs1=builder.getSimulationResults();
		
		

		builder=new GOSimulationContainer.GOSimulationContainerBuilder();
		singleSimulation=new HashMap<GOEntry,Integer>();
		// For three GO categories 60 times zero genes; 40 times 1 gene
		for(int i=0; i<20; i++)
		{
			builder.addSimulation(singleSimulation);
		}
		singleSimulation=new HashMap<GOEntry,Integer>();
		singleSimulation.put(g1, 1);
		singleSimulation.put(g2, 1);
		singleSimulation.put(g3, 1);
		for(int i=0; i<40; i++)
		{
			builder.addSimulation(singleSimulation);
		}
		singleSimulation=new HashMap<GOEntry,Integer>();
		singleSimulation.put(g1, 2);
		singleSimulation.put(g2, 2);
		singleSimulation.put(g3, 2);
		for(int i=0; i<40; i++)
		{
			builder.addSimulation(singleSimulation);
		}
		gs2=builder.getSimulationResults();
		
		
		
		builder=new GOSimulationContainer.GOSimulationContainerBuilder();
		singleSimulation=new HashMap<GOEntry,Integer>();
		// For three GO categories 60 times zero genes; 40 times 1 gene
		singleSimulation.put(g2, 3);
		for(int i=0; i<40; i++)
		{
			builder.addSimulation(singleSimulation);
		}
		singleSimulation=new HashMap<GOEntry,Integer>();
		singleSimulation.put(g1, 1);
		singleSimulation.put(g2, 1);

		for(int i=0; i<30; i++)
		{
			builder.addSimulation(singleSimulation);
		}
		singleSimulation=new HashMap<GOEntry,Integer>();
		singleSimulation.put(g1, 2);
		singleSimulation.put(g2, 2);
		for(int i=0; i<30; i++)
		{
			builder.addSimulation(singleSimulation);
		}
		gs3=builder.getSimulationResults();
	}
	
	
	@Test
	public void test_gs1() {
		double[] pvaldistri=gs1.getAveragePvalueDistribution();
		assertEquals(pvaldistri[100], 1.8, 0.001); 	// A p-value of 1.0 is obtained on the average exactly 1.8 times
		assertEquals(pvaldistri[40],  1.2, 0.001); 	// A p-value of 0.40 is obtained on the average exactly 1.2 times	
	}
	
	@Test
	public void test_gs2() {
		double[] pvaldistri=gs2.getAveragePvalueDistribution();
		assertEquals(pvaldistri[100], 0.6, 0.001); 	
		assertEquals(pvaldistri[80],  1.2, 0.001);
		assertEquals(pvaldistri[40],  1.2, 0.001); 
	}
	
	@Test
	public void test_gs3() {
		double[] pvaldistri=gs3.getAveragePvalueDistribution();
		assertEquals(pvaldistri[100], 0.7, 0.001);
		assertEquals(pvaldistri[70],  0.3, 0.001);
		assertEquals(pvaldistri[60],  0.3, 0.001);
		assertEquals(pvaldistri[40],  0.4, 0.001);
		assertEquals(pvaldistri[30],  0.3, 0.001);
	}
	
	@Test
	public void test_fdr1()
	{
		// testing for expected E(V)
		// expected number of GO categories having a p-value smaller than the given one
		FdrSimulationContainer fsr=new FdrSimulationContainer(gs1.getAveragePvalueDistribution(),100);
		assertEquals(fsr.getExpectedCount(0.4),1.2, 0.01);
		assertEquals(fsr.getExpectedCount(0.1),0.0,   0.01);
		assertEquals(fsr.getExpectedCount(1.0),3.0, 0.01);
	}
	
	@Test
	public void test_fdr2()
	{
		// testing for expected E(V)
		// expected number of GO categories having a p-value smaller than the given one
		FdrSimulationContainer fsr=new FdrSimulationContainer(gs2.getAveragePvalueDistribution(),100);
		assertEquals(fsr.getExpectedCount(0.4),1.2, 0.01);
		assertEquals(fsr.getExpectedCount(0.1),0.0,  0.01);
		assertEquals(fsr.getExpectedCount(1.0),3.0, 0.01);
		assertEquals(fsr.getExpectedCount(0.8),2.4, 0.01);
		assertEquals(fsr.getExpectedCount(0.7),1.2, 0.01);
		assertEquals(fsr.getExpectedCount(0.9),2.4, 0.01);
	}
	
	@Test
	public void test_fdr3()
	{
		// testing for expected E(V)
		// expected number of GO categories having a p-value smaller than the given one
		FdrSimulationContainer fsr=new FdrSimulationContainer(gs3.getAveragePvalueDistribution(),100);
		assertEquals(fsr.getExpectedCount(0.1),0.0, 0.01);
		assertEquals(fsr.getExpectedCount(0.3),0.3, 0.01);
		assertEquals(fsr.getExpectedCount(0.4),0.7, 0.01);
		assertEquals(fsr.getExpectedCount(0.6),1.0, 0.01);
		assertEquals(fsr.getExpectedCount(0.7),1.3, 0.01);
		assertEquals(fsr.getExpectedCount(1.0),2.0, 0.01);
	}

}

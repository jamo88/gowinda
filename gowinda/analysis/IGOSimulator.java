package gowinda.analysis;


public interface IGOSimulator {
	
	public abstract GOResultContainer getSimulationResults(int simulations, int threads);
}

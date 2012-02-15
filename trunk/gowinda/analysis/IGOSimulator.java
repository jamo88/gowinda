package gowinda.analysis;


public interface IGOSimulator {
	
	public abstract GOResultContainer getSimulationResults(int simulations, int threads);
	public abstract void setLogger(java.util.logging.Logger logger);

}

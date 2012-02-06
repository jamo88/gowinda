package gowinda.analysis;

public class TaskCounter {
	private java.util.logging.Logger logger;
	private int runningcount;
	private int makeNoise;
	
	public TaskCounter(java.util.logging.Logger logger, int makeNoise)
	{
		this.logger=logger;
		this.makeNoise=makeNoise;
	}
	
	public synchronized void increment()
	{
		runningcount++;
		if(runningcount%makeNoise==0) this.logger.fine("Finished "+runningcount+ " simulations");
	}
	
	
	
	
	
}

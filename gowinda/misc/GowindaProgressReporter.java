package gowinda.misc;

public class GowindaProgressReporter {

	private int counter=0;
	private java.util.logging.Logger logger;
	private int reportIncrement;
	
	public GowindaProgressReporter(int reportIncrement,java.util.logging.Logger logger)
	{
		this.reportIncrement=reportIncrement;
		this.logger=logger;
	}
	
	public synchronized void increment()
	{
		counter++;
		if(counter % reportIncrement==0) this.logger.fine("Performed " + counter + " simulations");
	}
}

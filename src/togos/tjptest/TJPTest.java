package togos.tjptest;

public abstract class TJPTest
{
	protected int outerIterations = 1000;
	protected int innerIterations = 100000;
	
	/**
	 * Should reset any counters to zero.
	 */
	public void reset() { }
	public void report() { } 
	
	public abstract void iter();
	
	long timer;
	protected void initTimer() { timer = System.currentTimeMillis(); }
	protected long interval() { return System.currentTimeMillis() - timer; }
	
	protected static void printMilliseconds( String label, long amount ) {
		System.out.println( String.format("%20s: % 8dms", label, amount ) );
	}
	
	public void run() {
		reset();
		iter();
		reset();
		for( int i=outerIterations-1; i>=0; --i ) {
			iter();
		}
		report();
	}
	
	public void run(String[] args) {
		run();
	}
}

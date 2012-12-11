package togos.tjptest;


/**
 * Is it faster to new things up a lot or to
 * recycle objects using a pool?
 * 
 * Findings:
 * Java HotSpot(TM) Client VM (build 23.5-b02, mixed mode, sharing)
 * For small, short-term objects, new is about 10 times as fast.
 */
public class RecycleTest extends TJPTest
{
	long newTime;
	long recycleTime;
	int counter;
	
	RecycledThing[] things = new RecycledThing[1024];
	
	@Override public void reset() {
		innerIterations = outerIterations = 300;
		newTime = recycleTime = 0;
	}
	
	protected void countThings() {
		for( int j=0; j<things.length; ++j ) {
			counter += things[j].getPayload();
		}
	}
	
	@Override public void iter() {
		initTimer();
		for( int i=innerIterations-1; i>=0; --i ) {
			for( int j=0; j<things.length; ++j ) {
				things[j] = RecycledThing.takeInstance(i+j);
			}
			countThings(); 
			for( int j=0; j<things.length; ++j ) {
				things[j].recycle();
			}
		}
		recycleTime += interval();
		
		initTimer();
		for( int i=innerIterations-1; i>=0; --i ) {
			for( int j=0; j<things.length; ++j ) {
				things[j] = new RecycledThing(i+j);
			}
			countThings(); 
		}
		newTime += interval();
	}
	
	@Override public void report() {
		printMilliseconds("New", newTime);
		printMilliseconds("Recycle", recycleTime);
	}
	
	public static void main( String[] args ) {
		new RecycleTest().run(args);
	}
}

package togos.tjptest;


/**
 * Is it faster to new things up a lot or to recycle objects using a pool?
 * 
 * Findings:
 * Java HotSpot(TM) Client VM (build 23.5-b02, mixed mode, sharing)
 * with small, short-term objects:
 * 
 * Local, unsynchronized pool (including recycling them) is slightly faster than newing up objects.
 * Newing up objects is about 8 times as fast as using a global, synchronized pool. 
 */
public class RecycleTest extends TJPTest
{
	long newTime, globalPoolTime, localPoolTime;
	int counter;
	
	final RecycledThing[] things = new RecycledThing[1024];
	final RecyclePool<RecycledThing> pool = new RecyclePool<RecycledThing>(1024);;
	
	@Override public void reset() {
		innerIterations = outerIterations = 300;
		newTime = globalPoolTime = localPoolTime = 0;
	}
	
	protected void countThings() {
		for( int j=0; j<things.length; ++j ) {
			counter += things[j].getPayload();
		}
	}
	
	@Override public void iter() {
		initTimer();
		for( int i=innerIterations-1; i>=0; --i ) {
			for( int j=things.length-1; j>=0; --j ) {
				things[j] = StaticRecyclePool.take();
				if( things[j] == null ) things[j] = new RecycledThing(i+j);
				else things[j].payload = i+j;
			}
			countThings(); 
			for( int j=things.length-1; j>=0; --j ) {
				StaticRecyclePool.recycle(things[j]);
			}
		}
		globalPoolTime += interval();
		
		initTimer();
		for( int i=innerIterations-1; i>=0; --i ) {
			for( int j=things.length-1; j>=0; --j ) {
				things[j] = new RecycledThing(i+j);
			}
			countThings(); 
		}
		newTime += interval();
		
		initTimer();
		for( int i=innerIterations-1; i>=0; --i ) {
			for( int j=things.length-1; j>=0; --j ) {
				things[j] = pool.take();
				if( things[j] == null ) things[j] = new RecycledThing(i+j);
				else things[j].payload = i+j;
			}
			countThings();
			for( int j=things.length-1; j>=0; --j ) {
				pool.recycle(things[j]);
			}
		}
		localPoolTime += interval();
	}
	
	@Override public void report() {
		printMilliseconds("New", newTime);
		printMilliseconds("Global pool", globalPoolTime);
		printMilliseconds("Local pool", localPoolTime);
	}
	
	public static void main( String[] args ) {
		new RecycleTest().run(args);
	}
}

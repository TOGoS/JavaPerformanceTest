package togos.tjptest;

import java.util.ArrayList;

/**
 * Is it faster to use an ArrayList or custom functions to
 * deal with simple array objects (that do a binary search to find the end)?
 * 
 * Findings:
 * * ArrayList is much faster at adding objects and does not seem
 *   to take much (if any) more time to instantiate than an array.
 * * Simple arrays are much faster to iterate over.
 * 
 * If you spend most of your time reading and not appending to your arrays,
 * simple arrays might be better!
 */
public class DynamicArrayVsArrayListTJPTest extends TJPTest
{
	long daAddTime;
	long daInstantiateTime;
	long daIterateTime;
	long alAddTime;
	long alInstantiateTime;
	long alIterateTime;
	
	Object obj = new Object(); // Dummy value to add to arrays
	
	@Override public void reset() {
		daAddTime = daInstantiateTime = alAddTime = alInstantiateTime = 0;
	}
	
	@Override public void iter() {
		// Allocate and add a few things!
		
		initTimer();
		for( int i=innerIterations-1; i>=0; --i ) {
			ArrayList<Object> al = new ArrayList<Object>(16);
			for( int j=1; j>=0; --j ) {
				al.add(obj);
			}
		}
		alInstantiateTime += interval();
		
		initTimer();
		for( int i=innerIterations-1; i>=0; --i ) {
			Object[] da = new Object[16];
			for( int j=1; j>=0; --j ) {
				da = DynamicArray.add(da, obj);
			}
		}
		daInstantiateTime += interval();
		
		// Add lots of things!
		
		ArrayList<Object> al = new ArrayList<Object>(16);
		Object[] da = new Object[16];
		
		initTimer();
		for( int i=innerIterations-1; i>=0; --i ) {
			al.add(obj);
		}
		alAddTime += interval();
		
		initTimer();
		for( int i=innerIterations-1; i>=0; --i ) {
			da = DynamicArray.add(da, obj);
		}
		daAddTime += interval();
		
		// Iterate!

		int count = 0; // Do something with objects
		
		initTimer();
		for( Object o : al ) { if(o != null) ++count; }
		alIterateTime += interval();
		
		initTimer();
		for( int j=0; j<da.length && da[j] != null; ++j ) {
			++count;
		}
		daIterateTime += interval();
		
		System.err.print("Count: "+count+"\r");
	}
	
	@Override public void report() {
		printMilliseconds("ArrayList instantiate", alInstantiateTime);
		printMilliseconds("DynamicArray instantiate", daInstantiateTime);
		printMilliseconds("ArrayList add", alAddTime);
		printMilliseconds("DynamicArray add", daAddTime);
		printMilliseconds("ArrayList iterate", alIterateTime);
		printMilliseconds("DynamicArray iterate", daIterateTime);
	}
	
	public static void main( String[] args ) {
		new DynamicArrayVsArrayListTJPTest().run(args);
	}
}

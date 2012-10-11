package togos.tjptest;

import java.util.Random;

public class ThreadLocalVsNew extends TJPTest
{
	static long gorp;
	
	static class SomeReusableThing {
		Random r = new Random();
		public void doStuff() {
			gorp += r.nextLong();
		}
	}
	
	long tlTime;
	long newTime;
	long syncTime;
	
	ThreadLocal<SomeReusableThing> tl = new ThreadLocal<SomeReusableThing>();
	SomeReusableThing thing0, thing1;
	
	@Override public void reset() {
		tlTime = newTime = 0;
	}
	
	@Override public void iter() {
		initTimer();
		for( int i=innerIterations-1; i>=0; --i ) {
			thing0 = tl.get();
			if( thing0 == null ) tl.set(thing0 = new SomeReusableThing());
			thing0.doStuff();
		}
		tlTime += interval();
		
		initTimer();
		for( int i=innerIterations-1; i>=0; --i ) {
			thing1 = new SomeReusableThing();
			thing1.doStuff();
		}
		newTime += interval();
		
		initTimer();
		for( int i=innerIterations-1; i>=0; --i ) {
			synchronized(this) {
				thing1.doStuff();
			}
		}
		syncTime += interval();
	}
	
	@Override public void report() {
		printMilliseconds("ThreadLocal",    tlTime);
		printMilliseconds("New",           newTime);
		printMilliseconds("Synchronized", syncTime);
	}
	
	public static void main(String[] args) {
		new ThreadLocalVsNew().run(args);
	}
}

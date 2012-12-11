package togos.tjptest;

public class RecycledThing {
	private static final int POOL_SIZE = 1024;
	private static final RecycledThing[] POOL = new RecycledThing[POOL_SIZE];
	private static int poolEnd = 0;
	
	private static synchronized void recycle( RecycledThing p ) {
		if( poolEnd == POOL.length ) return;
		
		POOL[poolEnd++] = p;
	}
	
	public static synchronized RecycledThing takeInstance(int payload) {
		if( poolEnd == 0 ) {
			return new RecycledThing(payload);
		} else {
			RecycledThing p = POOL[--poolEnd];
			p.payload = payload;
			return p;
		}
	}
	
	protected int payload;
	
	public RecycledThing(int payload) {
		this.payload = payload;
	}
	
	public int getPayload() {
		return payload;
	}
	
	public void recycle() {
		recycle(this);
	}
}

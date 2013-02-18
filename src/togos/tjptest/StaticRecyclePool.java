package togos.tjptest;

public class StaticRecyclePool
{
	static final RecycledThing[] items = new RecycledThing[1024];
	static int i = 1024;
	public static synchronized final RecycledThing take() {
		return (i > 0) ? items[--i] : null;
	}
	public static synchronized  final void recycle(RecycledThing t) {
		if( i < items.length ) {
			items[i++] = t;
		}
	}
}

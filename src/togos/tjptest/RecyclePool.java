package togos.tjptest;

public final class RecyclePool<Item> {
	final Object[] items;
	int i;
	public RecyclePool(int size) {
		items = new Object[size];
		i = items.length;
	}
	@SuppressWarnings("unchecked")
	public final Item take() {
		return (i > 0) ? (Item)items[--i] : null;
	}
	public final void recycle(Item t) {
		if( i < items.length ) {
			items[i++] = t;
		}
	}
}

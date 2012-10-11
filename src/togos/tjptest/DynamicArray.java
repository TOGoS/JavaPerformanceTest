package togos.tjptest;

import java.lang.reflect.Array;

public class DynamicArray
{
	private DynamicArray() {}
	
	public static final <T extends Object> T[] expand( final T[] arr ) {
		int newLen;
		if( arr.length <= 8 ) {
			newLen = 16;
		} else {
			newLen = arr.length * 3 / 2;
		}
		@SuppressWarnings("unchecked")
		T[] newArr = (T[])Array.newInstance( arr.getClass().getComponentType(), newLen );
		for( int i=arr.length-1; i>=0; --i ) {
			newArr[i] = arr[i];
		}
		return newArr;
	}
	
	public static final <T> int findBlank( final T[] arr ) {
		if( arr.length == 0 ) {
			return 0;
		} else if( arr[0] == null ) {
			return 0;
		} else if( arr[arr.length-1] != null ) {
			return arr.length;
		} else {
			int i = arr.length/2;
			int di = arr.length/4;
			while( di > 8 ) {
				if( arr[i] == null ) {
					i -= di;
				} else {
					i += di;
				}
				di /= 2;
			}
			// Find the last set element
			while( arr[i] == null ) --i;
			// Find the first null after it
			while( arr[i] != null ) ++i;
			return i;
		}
	}
	
	public static final <T> T[] add( T[] arr, final T elem ) {
		int i = findBlank(arr);
		if( i >= arr.length ) arr = expand(arr);
		arr[i] = elem;
		return arr;
	}
}

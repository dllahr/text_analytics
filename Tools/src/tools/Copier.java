package tools;

public class Copier {
	public static <T> void copyArrayVals(T[] source, T[] dest) {
		for (int i = 0; i < source.length; i++) {
			dest[i] = source[i];
		}
	}
}

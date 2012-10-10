package controller.util;

import java.util.Iterator;
import java.util.List;

/**
 * used to calculate the string that is shared at the beginning of a list of strings
 * @author Dave Lahr
 *
 */
public class CommonStringBeginningCalculator {

	/**
	 * calculate the string that is shared in common at the beginning of the provided stringList
	 * @param stringList
	 * @return
	 */
	public String calculateCommonBeginning(final List<String> stringList) {
		final String result;
		if (null == stringList) {
			result = null;
		} else if (stringList.size() == 1) {
			result = stringList.get(0);
		} else {
			int maxLen = 0;
			for (String string : stringList) {
				if (string.length() > maxLen) {
					maxLen = string.length();
				}
			}
			Byte[][] allByteArray = new Byte[stringList.size()][maxLen];
			Iterator<String> iter = stringList.iterator();
			for (int i = 0; iter.hasNext(); i++) {
				byte[] byteArray = iter.next().getBytes();
				for (int j = 0; j < byteArray.length; j++) {
					allByteArray[i][j] = byteArray[j];
				}
			}
			
			boolean doContinue = true;
			int j = 0;
			while (doContinue && j < maxLen) {
				Byte cur = allByteArray[0][j];
				doContinue = cur != null;
				
				int i = 1;
				while (doContinue && i < stringList.size()) {
					doContinue = cur.equals(allByteArray[i][j]);
					i++;
				}
				j++;
			}
			
			final int numMatch = j-1;
			byte[] resultByteArray = new byte[numMatch];
			for (int i = 0; i < numMatch; i++) {
				resultByteArray[i] = allByteArray[0][i];
			}
			
			result = new String(resultByteArray);
		} 
		
		return result;
	}
}

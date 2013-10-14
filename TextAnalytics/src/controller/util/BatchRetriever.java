package controller.util;

import java.util.LinkedList;
import java.util.List;

public class BatchRetriever<T> {
	
	private static final int batchSize = 1000;

	private final GenericRetriever<T> genericRetriever;
	
	public BatchRetriever(GenericRetriever<T> genericRetriever) {
		this.genericRetriever = genericRetriever;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List retrieveAll(List<T> inputList) {
		List result = new LinkedList<>();
		
		final int extra = inputList.size() % batchSize;
		
		final int numBatch = (inputList.size() / batchSize) + (extra > 0 ? 1 : 0);
		
		for (int i = 0; i < numBatch; i++) {
			final int startInd = i*batchSize;
			
			int endInd = (i+1)*batchSize;
			if (endInd > inputList.size()) {
				endInd = inputList.size();
			}
			
			List<T> subList = inputList.subList(startInd, endInd);
			
			result.addAll(genericRetriever.retrieve(subList));
		}
		
		return result;
	}
}

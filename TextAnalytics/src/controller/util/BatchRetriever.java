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
		
		final int numBatch = inputList.size() / batchSize;
		
		for (int i = 0; i < numBatch; i++) {
			final int startInd = i*batchSize;
			final int endInd = (i+1)*batchSize;
			
			result.addAll(genericRetriever.retrieve(inputList.subList(startInd, endInd)));
		}
		
		final int startInd = numBatch*batchSize;
		if (startInd < inputList.size()) {
			result.addAll(genericRetriever.retrieve(inputList.subList(startInd, inputList.size())));
		}
		
		return result;
	}
}

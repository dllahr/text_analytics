package controller.readRawFile;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArticleSectionSplitter {
	
	private final Set<String> bodyLabelSet;
	
	private final Set<String> nonBodyLabelSet;
	
	public ArticleSectionSplitter(Map<String, Boolean> metaDataLabelIsBodyMap) {

		Set<String> buildBodyLabelSet = new HashSet<>();
		for (String label : metaDataLabelIsBodyMap.keySet()) {
			if (metaDataLabelIsBodyMap.get(label)) {
				buildBodyLabelSet.add(label.trim().toLowerCase());
			}
		}
		
		bodyLabelSet = Collections.unmodifiableSet(buildBodyLabelSet);
		
		Set<String> buildNonBodyLabelSet = new HashSet<>(metaDataLabelIsBodyMap.keySet());
		buildNonBodyLabelSet.removeAll(bodyLabelSet);
		nonBodyLabelSet = Collections.unmodifiableSet(buildNonBodyLabelSet);
	}


	public SplitArticle split(RawArticle rawArticle) {
		SplitArticle result = new SplitArticle(rawArticle.file);
		
		Iterator<String> lineIter = rawArticle.lines.iterator();
		
		LinesAndFoundNext beforeBody = readLinesToNextLabel(lineIter, bodyLabelSet);
		
		if (! beforeBody.foundNext) {
			throw new RuntimeException("ArticleSectionSplitter split did not find body of article.  file:  " 
					+ rawArticle.file.getAbsolutePath() + " first line: " + rawArticle.lines.get(0));
		}
		
		LinesAndFoundNext body = readLinesToNextLabel(lineIter, nonBodyLabelSet);
		
		if (! body.foundNext) {
			throw new RuntimeException("ArticleSectionSplitter split did not find field labels after body of article.  file:  " 
					+ rawArticle.file.getAbsolutePath() + " first line: " + rawArticle.lines.get(0));
		}
		
		result.linesBeforeBody.addAll(beforeBody.lineList.subList(0, beforeBody.lineList.size() - 1));
		result.bodyLines.add(beforeBody.lineList.get(beforeBody.lineList.size() - 1));
		result.bodyLines.addAll(body.lineList.subList(0, body.lineList.size() - 1));
		result.linesAfterBody.add(body.lineList.get(body.lineList.size() -1 ));
		
		while (lineIter.hasNext()) {
			result.linesAfterBody.add(lineIter.next());
		}

		return result;
	}
	
	private LinesAndFoundNext readLinesToNextLabel(Iterator<String> lineIter, Set<String> labelSet) {
		List<String> result = new LinkedList<>();

		boolean foundNextLabel = false;
		while (!foundNextLabel && lineIter.hasNext()) {
			final String curLine = lineIter.next();

			result.add(curLine);
			
			foundNextLabel = doesLineStartWithLabel(curLine, labelSet);
		}
		
		return new LinesAndFoundNext(result, foundNextLabel);
	}

	
	private static boolean doesLineStartWithLabel(String curLine, Set<String> labelSet) {
		String curLinePrepped = curLine.trim().toLowerCase();
		
		for (String label : labelSet) {
			if (curLinePrepped.startsWith(label)) {
				return true;
			}
		}
		
		return false;
	}
	
	private class LinesAndFoundNext {
		public final List<String> lineList;
		public final boolean foundNext;

		public LinesAndFoundNext(List<String> lineList, boolean foundNext) {
			this.lineList = lineList;
			this.foundNext = foundNext;
		}
	}
}

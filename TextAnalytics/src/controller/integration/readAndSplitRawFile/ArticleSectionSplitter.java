package controller.integration.readAndSplitRawFile;


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

		Set<String> buildNonBodyLabelSet = new HashSet<>();
		Set<String> buildBodyLabelSet = new HashSet<>();
		for (String label : metaDataLabelIsBodyMap.keySet()) {
			if (metaDataLabelIsBodyMap.get(label)) {
				buildBodyLabelSet.add(label.trim().toLowerCase());
			} else {
				buildNonBodyLabelSet.add(label.trim().toLowerCase());
			}
		}
		
		bodyLabelSet = Collections.unmodifiableSet(buildBodyLabelSet);

		nonBodyLabelSet = Collections.unmodifiableSet(buildNonBodyLabelSet);
	}


	public SplitArticle split(RawArticle rawArticle) {
		SplitArticle result = new SplitArticle(rawArticle.file, rawArticle.startLineNumber);
		
		Iterator<String> lineIter = rawArticle.lines.iterator();
		
		LinesAndFoundNext beforeBody = readLinesToNextLabel(lineIter, bodyLabelSet);
		
		if (! beforeBody.foundNext) {
			System.out.println("ArticleSectionSplitter split did not find body of article.  file:  " 
					+ rawArticle.file.getAbsolutePath() + " first line: " + rawArticle.lines.get(0));
		} else {
			LinesAndFoundNext body = readLinesToNextLabel(lineIter, nonBodyLabelSet);
			
			if (! body.foundNext) {
				System.out.println("ArticleSectionSplitter split did not find field labels after body of article.  file:  " 
						+ rawArticle.file.getAbsolutePath() + " first line: " + rawArticle.lines.get(0));
			} else {
				result.linesBeforeBody.addAll(beforeBody.lineList.subList(0, beforeBody.lineList.size() - 1));
				result.bodyLines.add(beforeBody.lineList.get(beforeBody.lineList.size() - 1));
				result.bodyLines.addAll(body.lineList.subList(0, body.lineList.size() - 1));
				result.linesAfterBody.add(body.lineList.get(body.lineList.size() -1 ));
				
				while (lineIter.hasNext()) {
					result.linesAfterBody.add(lineIter.next());
				}
			}
		}

		return result;
	}
	
	private LinesAndFoundNext readLinesToNextLabel(Iterator<String> lineIter, Set<String> labelSet) {
		List<String> result = new LinkedList<>();

		boolean foundNextLabel = false;
		while (!foundNextLabel && lineIter.hasNext()) {
			final String curLine = lineIter.next();

			result.add(curLine);
			
			if (curLine != null) {
				
			}
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

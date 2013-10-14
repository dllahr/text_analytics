package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import controller.articleIntegration.CompareArticleStemCounts;
import controller.util.Triple;
import controller.util.Utilities;

import orm.Article;

public class MainFindDuplicateArticles {

	
	public static void main(String[] args) throws ParseException, IOException {
		final int articleSourceId = Integer.valueOf(args[0]);
		final Date minArticleDate = Utilities.dateFormat.parse(args[1]);
//		final Date maxArticleDate = args.length >= 3 && args[2] != null ? Utilities.dateFormat.parse(args[2]) : null;
		final Date maxArticleDate = null;
		final File outputFile = new File(args[2]);
		
		List<Integer> articleIdList = 
				Article.getArticleIdsForMinDateAndArticleSource(minArticleDate, maxArticleDate, articleSourceId, false, false);
		
		List<Triple<Article, Article, Double>> articleDistList = (new CompareArticleStemCounts()).compare(articleIdList);
		
		Collections.sort(articleDistList, new Comparator<Triple<Article, Article, Double>>() {
			@Override
			public int compare(Triple<Article, Article, Double> o1,
					Triple<Article, Article, Double> o2) {
				if (o1.v > o2.v) { 
					return 1;
				} else if (o1.v < o2.v) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		
		for (Triple<Article, Article, Double> distTriple : articleDistList) {
//			System.out.println(distTriple.t.getId() + " " + distTriple.u.getId() + " " + distTriple.v);
			writer.write(distTriple.t.getId() + " " + distTriple.u.getId() + " " + distTriple.v);
			writer.newLine();
		}
		
		writer.close();
	}
}

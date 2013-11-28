package main;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;

import orm.Article;
import orm.SessionManager;

public class MainExportArticleStemCount {

	private static final String[] queryArray = {"select artsc.article_id-",
		", artsc.stem_id, artsc.count from article_stem_count artsc " +
			"join stem s on s.id = artsc.stem_id " +
			"where s.is_stop=0 and artsc.article_id in " +
			"(select id from article where article_source_id = ", ")"};
	
	private static final int numCols = 3;
	
	public static void main(String[] args) throws HibernateException, SQLException, FileNotFoundException {

		final int articleSourceId = Integer.valueOf(args[0]);
		final File outputFile = new File(args[1]);
		
		final int minArticleId = Article.getArticlesOrderById(articleSourceId).get(0).getId();
		
		StringBuilder builder = new StringBuilder();
		builder.append(queryArray[0]);
		builder.append(minArticleId-1);
		builder.append(queryArray[1]);
		builder.append(articleSourceId);
		builder.append(queryArray[2]);
		
		builder.append(" and rownum < 10");
		
		System.out.println(builder.toString());
		
		ResultSet rs = SessionManager.resultSetQuery(builder.toString());
		
		PrintStream out = new PrintStream(outputFile);
		
		ResultSetPrinter.print(rs, numCols, out);
		
	}

}

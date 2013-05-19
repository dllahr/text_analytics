package controller.dateExtractionConversion;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;



public class DateOnSingleLineTest {
	
	private static final String[] lines = {"",
		"Locations United States--US Company/Org Asia News Network (NAICS: 519110) Title Sri Mulyani in Forbes' most powerful women list for " +
				"3rd time Authors Anonymous Publication title McClatchy - Tribune Business News Publication year 2011 Publication Date Sep 4, 2011 Year 2011 " +
				"Publisher McClatchy - Tribune Information Services Place of Publication Washington Country of publication United States Journal Subjects " +
				"Business And Economics Source type Wire Feeds Language of Publication English Document type News ProQuest Document ID 887443648 Document " +
				"URL http://search.proquest.com/docview/887443648?accountid=140202 Copyright _(c)2011 the Asia News Network (Hamburg, Germany) Visit the Asia " +
				"News Network (Hamburg, Germany) at www.asianewsnet.net/home/ Distributed by MCT Information Services Last updated 2011-09-04 Database ABI/INFORM Dateline", 
				""};

	@Test
	public void test() {
		List<String> lineList = new LinkedList<>();
		for (String line : lines) {
			lineList.add(line);
		}
		
		DateOnSingleLine dateOnSingleLine = new DateOnSingleLine();
		Date result = dateOnSingleLine.extract(lineList);
		assertNotNull(result);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		assertEquals("2011-09-04", dateFormat.format(result));
	}

}

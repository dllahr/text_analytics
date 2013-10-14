package controller.integration.readAndSplitRawFile;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import controller.articleIntegration.readAndSplitRawFile.BuildMetaDataMap;
import controller.stemCountArticles.StemCounterTest;

public class BuildMetaDataMapTest {

	@Test
	public void testBuild() throws IOException {
		BuildMetaDataMap buildMetaDataMap = new BuildMetaDataMap();
		Map<String, Boolean> result = buildMetaDataMap.build(new File("resources/meta_data_info.txt"));
		
		StemCounterTest.printMap(result);
		
		assertTrue(true);
	}

}

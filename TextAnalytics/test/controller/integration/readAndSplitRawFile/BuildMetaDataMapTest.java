package controller.integration.readAndSplitRawFile;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import controller.integration.CountStemsAndReadDateTest;
import controller.integration.readAndSplitRawFile.BuildMetaDataMap;
import controller.predictFromArticles.StemCounterTest;

public class BuildMetaDataMapTest {

	@Test
	public void testBuild() throws IOException {
		BuildMetaDataMap buildMetaDataMap = new BuildMetaDataMap();
		Map<String, Boolean> result = buildMetaDataMap.build(new File("resources/meta_data_info.txt"));
		
		StemCounterTest.printMap(result);
	}

}
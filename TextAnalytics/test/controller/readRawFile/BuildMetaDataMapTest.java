package controller.readRawFile;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

public class BuildMetaDataMapTest {

	@Test
	public void testBuild() throws IOException {
		BuildMetaDataMap buildMetaDataMap = new BuildMetaDataMap();
		Map<String, Boolean> result = buildMetaDataMap.build(new File("resources/meta_data_info.txt"));
		
		for (String label : result.keySet()) {
			System.out.println(label + " " + result.get(label));
		}
	}

}

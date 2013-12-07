package controller.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import controller.util.FileReaderWhereLineIsEntityId.Pair;

public class FileReaderWhereLineIsEntityIdTest {

	@Test
	public void testReadNext() throws IOException {
		int[] ids = {0,1,2,5,9};
		List<MyHasId> myHasIds = new ArrayList<>(ids.length);
		for (int id : ids) {
			myHasIds.add(new MyHasId(id));
		}
		
		FakeBufferedReader fakeReader = new FakeBufferedReader(ids[ids.length-1] + 1);
		
		FileReaderWhereLineIsEntityId<MyHasId> reader = new FileReaderWhereLineIsEntityId<>(myHasIds, fakeReader);
		
		Iterator<MyHasId> iter = myHasIds.iterator();
		Pair<MyHasId> pair;
		while ((pair = reader.readNext()) != null) {
			MyHasId id = iter.next();
			
			System.out.println(id.getId() + " " + pair.entity.getId() + " " + pair.line);
			
			assertEquals(id, pair.entity);
			
			String[] split = pair.line.split(" ");
			assertEquals(id.getId(), Integer.valueOf(split[0]));
		}
	}
}

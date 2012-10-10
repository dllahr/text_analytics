package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class DjiFracChangeTest {

	@Test
	public void test() {
		List<DjiFracChange> list = UtilitiesForTesting.getFirst10EntititiesNoSetDb("DjiFracChange");
		
		assertTrue(list.size() > 0);
		
		DjiFracChange dfc = list.get(0);
		
		assertNotNull(dfc.getDayIndex());
		
	}

}

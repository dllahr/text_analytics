package orm;

import static org.junit.Assert.*;

import org.junit.Test;

public class ScoringModelTest {

	@Test
	public void test() {
		ScoringModel sm = ScoringModel.getScoringModel(1);
		assertNotNull(sm);
		System.out.println(sm);
	}

}

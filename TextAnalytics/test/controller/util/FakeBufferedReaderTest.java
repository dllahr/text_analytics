package controller.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class FakeBufferedReaderTest {

	@Test
	public void testReadLine() throws IOException {
		FakeBufferedReader reader = new FakeBufferedReader(10);
		
		for (int i = 0; i < 10; i++) {
			assertNotNull(reader.readLine());
		}
		
		for (int i = 0; i < 10; i++) {
			assertNull(reader.readLine());
		}
	}

}

package controller.stockPrices.update;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;

import org.junit.Test;

public class WebReaderBuilderTest {

	@Test
	public void test() throws IOException, WebReaderHeaderMismatchException {
		BufferedReader reader = WebReaderBuilder.build("GE");
		assertNotNull(reader);
		
		for (int i = 0; i < 5; i++) {
			System.out.println(reader.readLine());
		}
		
		reader.close();
	}

}

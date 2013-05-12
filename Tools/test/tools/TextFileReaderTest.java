package tools;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class TextFileReaderTest {

	@Test
	public void test() throws IOException {
		String content = TextFileReader.readFile(new File("test/tools/testForTextFileReader.txt"));
		System.out.println(content);
		
		String[] split = content.split("\r\n");
		assertEquals("line 1", split[0]);
		assertEquals("line 2", split[1]);
		assertEquals("line 3", split[2]);
	}

}

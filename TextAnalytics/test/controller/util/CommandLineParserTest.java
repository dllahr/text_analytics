package controller.util;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class CommandLineParserTest {

	@Test
	public void test() throws CommandLineParserUnrecognizedTokenException {
		String[] args = {null,null,null,"-first", "first value", "-second just option", "-third", "third value"};
		
		Map<String, String> map = CommandLineParser.parse(args, 3);
		assertNotNull(map);
		assertEquals(3, map.size());
		
		assertEquals(args[4], map.get(args[3]));
		
		assertTrue(map.containsKey(args[5]));
		assertNull(map.get(args[5]));
		
		assertEquals(args[7], map.get(args[6]));
	}

	@Test(expected = CommandLineParserUnrecognizedTokenException.class)
	public void testImmediateUnrecognized() throws CommandLineParserUnrecognizedTokenException {
		String[] args = {"my bad"};
		
		CommandLineParser.parse(args, 0);
	}
	
	@Test(expected = CommandLineParserUnrecognizedTokenException.class)
	public void testMiddleUnrecognized() throws CommandLineParserUnrecognizedTokenException {
		String[] args = {null,null,null,"-first", "first value", "middle bad option", "-third", "third value"};
		
		CommandLineParser.parse(args, 3);
	}
	
	@Test(expected = CommandLineParserUnrecognizedTokenException.class)
	public void testLateUnrecognized() throws CommandLineParserUnrecognizedTokenException {
		String[] args = {null,null,null,"-first", "first value", "-second option", "-third", "third value", "late bad option"};
		
		CommandLineParser.parse(args, 3);
	}

}

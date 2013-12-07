package controller.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

class FakeBufferedReader extends BufferedReader {

	private final int numLines;
	
	private int curLine;
	
	public FakeBufferedReader(int numLines) {
		super(new Reader() {
			
			@Override
			public int read(char[] cbuf, int off, int len) throws IOException {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void close() throws IOException {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.numLines = numLines;
		curLine = 0;
	}

	@Override
	public String readLine() throws IOException {
		String val = curLine < numLines ? curLine + " hello world!" : null;
		curLine++;

		return val;
	}
}
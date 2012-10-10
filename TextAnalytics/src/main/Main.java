package main;

import java.text.ParseException;

import javax.swing.JFrame;

import controller.Controller;

import gui.FrameBuilder;



public class Main {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		final Controller controller = new Controller();
		
		FrameBuilder frameBuilder = new FrameBuilder();
		JFrame frame = frameBuilder.buildFrame(controller);
		frame.setVisible(true);
	}

}

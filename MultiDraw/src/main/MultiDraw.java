package main;
import gui.PaintWindow;

import javax.swing.JFrame;
import javax.swing.UIManager;

class MultiDraw {
	public static void main(String[] args) {
		try {
			//Teständring
			// Fix look and feel of GUI.
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		PaintWindow window = new PaintWindow();
	}
}
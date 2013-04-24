package main;
import gui.PaintPanel;
import gui.PaintWindow;

import javax.swing.JFrame;
import javax.swing.UIManager;

import networktest.DrawClient;

class MultiDraw {
	public static void main(String[] args) {
		try {
			// Fix look and feel of GUI.
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		DrawClient mc = new DrawClient("localhost", 30001);
		PaintPanel paintpanel = new PaintPanel(mc);
		mc.setPaintPanel(paintpanel);
		mc.start();
		PaintWindow window = new PaintWindow(paintpanel);
	}
}
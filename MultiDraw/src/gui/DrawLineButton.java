package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;

import javax.swing.JButton;
import javax.swing.JColorChooser;

public class DrawLineButton extends JButton {
	private PaintPanel paintpanel;

	public DrawLineButton(String name, PaintPanel paintpanel) {
		super(name);
		this.paintpanel = paintpanel;
		this.addActionListener(actionListener);
	}

	ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			paintpanel.drawLine();
		}
	};

}

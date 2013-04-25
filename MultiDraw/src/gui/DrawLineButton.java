package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class DrawLineButton extends JButton implements ActionListener {
	private PaintPanel paintpanel;

	public DrawLineButton(String name, PaintPanel paintpanel) {
		super(name);
		this.paintpanel = paintpanel;
		
		addActionListener(this);
	}
	
	// Changed to this, more clear imho
	public void actionPerformed(ActionEvent e) {
		paintpanel.drawLine();
	}
}

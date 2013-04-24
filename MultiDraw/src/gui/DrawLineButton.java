package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class DrawLineButton extends JButton {
	private PaintPanel paintpanel;

	public DrawLineButton(String name, PaintPanel paintpanel) {
		super(name);
		this.paintpanel = paintpanel;
		
		// Changed this to use an anonymous class instead, which do you prefer?
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DrawLineButton src = (DrawLineButton) e.getSource();
				src.getPaintPanel().drawLine();
			}
		});
	}
	
	public PaintPanel getPaintPanel() {
		return paintpanel;
	}
}

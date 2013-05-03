package gui;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

import tools.ClientToolProperties;

public class ColorButton extends JPanel implements MouseListener  {
	private ClientToolProperties tp;

	public ColorButton(ClientToolProperties tp) {
		super();
		this.tp = tp;
		this.addMouseListener(this);
	}

	public void mouseClicked(MouseEvent arg0) {
		Color color = JColorChooser.showDialog(null, "Choose Color", new Color(tp.getColor()));
		if (color != null) {
			this.setBackground(color);
			tp.setColor(color);
		}
	}

	// Ignored
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}

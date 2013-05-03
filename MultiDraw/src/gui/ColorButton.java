package gui;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

import tools.ClientToolProperties;

public class ColorButton extends JPanel implements MouseListener  {
	private ClientToolProperties tp;
	private int typeOfBrush;

	public ColorButton(ClientToolProperties tp, int typeOfBrush) {
		super();
		this.typeOfBrush = typeOfBrush;
		this.tp = tp;
		this.addMouseListener(this);
	}

	public void mouseClicked(MouseEvent arg0) {
		Color color = JColorChooser.showDialog(null, "Choose Color", new Color(tp.getColor(typeOfBrush)));
		if (color != null) {
			this.setBackground(color);
			tp.setColor(color.getRGB(), typeOfBrush);
		}
	}

	// Ignored
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}

package gui;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

public class ColorButton extends JPanel implements MouseListener  {
	private PaintPanel paintPanel;

	public ColorButton(PaintPanel paintpanel) {
		super();
		this.paintPanel = paintpanel;
		this.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		Color color = JColorChooser.showDialog(null, "Choose Color", paintPanel.getBrushColor());
		if (color != null) {
			this.setBackground(color);
			paintPanel.sendChangeBrushcolorCommandToserver(color);
			paintPanel.setBrushColor(color);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

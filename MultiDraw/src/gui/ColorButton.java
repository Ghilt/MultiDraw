package gui;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;

public class ColorButton extends JButton implements ActionListener  {
	private PaintPanel paintPanel;

	public ColorButton(String name, PaintPanel paintpanel) {
		super(name);
		this.paintPanel = paintpanel;
		this.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		Color color = JColorChooser.showDialog(null, "Choose Color", paintPanel.getBrushColor());
		if (color != null) {
			this.setBackground(color);
			paintPanel.sendChangeBrushcolorCommandToserver(color);
			paintPanel.setBrushColor(color);
		}
	}
}

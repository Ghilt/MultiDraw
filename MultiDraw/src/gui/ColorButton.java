package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;

public class ColorButton extends JButton {
	private JColorChooser jcc;
	private PaintPanel paintPanel;

	public ColorButton(String name, PaintPanel paintpanel) {
		super(name);
		this.paintPanel = paintpanel;
		jcc = new JColorChooser(paintPanel.getBrushColor());

		this.addActionListener(actionListener);
	}

	ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Color color =  jcc.showDialog(null, "Choose Color", paintPanel.getBrushColor());
			paintPanel.sendChangeBrushcolorCommandToserver(color);

			
		}
	};
}

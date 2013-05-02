package gui;

import java.awt.Dimension;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BrushSizeSlider extends JSlider implements ChangeListener{
	private PaintPanel paintPanel;
	
	public BrushSizeSlider(PaintPanel paintPanel) {
		super();
		this.paintPanel = paintPanel;
		this.setValue(10);
		this.addChangeListener(this);
		this.setPreferredSize(new Dimension(80,20));
		this.setOpaque(false);
	}
	
	public void stateChanged(ChangeEvent ce) {
		int size = this.getValue();
		paintPanel.setBrushWidth(size);
	}
}

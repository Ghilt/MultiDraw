package gui;

import java.awt.Dimension;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tools.ClientToolProperties;

public class BrushSizeSlider extends JSlider implements ChangeListener{
	private ClientToolProperties tp;
	
	public BrushSizeSlider(ClientToolProperties tp) {
		super();
		this.tp = tp;
		this.setValue(10);
		this.addChangeListener(this);
		this.setPreferredSize(new Dimension(80,20));
		this.setOpaque(false);
	}
	
	public void stateChanged(ChangeEvent ce) {
		int size = this.getValue();
		tp.setBrushWidth(size);
	}
}

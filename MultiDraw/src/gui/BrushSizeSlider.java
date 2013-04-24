package gui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Stroke;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BrushSizeSlider extends JSlider{
	private PaintPanel paintPanel;
	
	public BrushSizeSlider(PaintPanel paintPanel) {
		super();
		this.paintPanel = paintPanel;
		this.setValue(10);
		this.addChangeListener(changeListener);
		this.setPreferredSize(new Dimension(80,20));
	}
	
	private int getSliderValue() {
		return this.getValue();
	}

	ChangeListener changeListener = new ChangeListener() {
		public void stateChanged(ChangeEvent ce){
			 int size = getSliderValue();
			BasicStroke stroke = new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
			paintPanel.setStroke(stroke);	
		}
	};
	

}

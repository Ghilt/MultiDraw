package gui;


import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class IconButtons extends JPanel {
	private ButtonGroup btnGroup;
	private PaintPanel paintpanel;

	// Command sent when corresponding button is pressed: PEN, BRUSH, BUCKET,
	// ERASER, TEXT, SHAPE_LINE, SHAPE_RECTANGLE, SHAPE_ELLIPSE
	public IconButtons(PaintPanel paintpanel) {
		btnGroup = new ButtonGroup();
		this.paintpanel = paintpanel;
		BtnListener btnListener = new BtnListener();

		try {

			// pen button
			JButton penButton = new JButton();
			Image img = ImageIO.read(getClass().getResource(
					"/res/icons/pen.png"));
			penButton.setIcon(new ImageIcon(img));
			penButton.setActionCommand("PEN");
			penButton.setEnabled(false);  // temporary disabled
			btnGroup.add(penButton);

			// brush button
			JButton brushButton = new JButton();
			img = ImageIO.read(getClass().getResource(
					"/res/icons/paintbrush.png"));
			brushButton.setIcon(new ImageIcon(img));
			brushButton.setActionCommand("BRUSH"); 
			btnGroup.add(brushButton);

			// paint bucket button
			JButton bucketButton = new JButton();
			img = ImageIO.read(getClass().getResource(
					"/res/icons/paint_bucket.png"));
			bucketButton.setIcon(new ImageIcon(img));
			bucketButton.setActionCommand("BUCKET");
			bucketButton.setEnabled(false);  // temporary disabled
			btnGroup.add(bucketButton);

			// eraser button
			JButton eraserButton = new JButton();
			img = ImageIO.read(getClass().getResource("/res/icons/erase.png"));
			eraserButton.setIcon(new ImageIcon(img));
			eraserButton.setActionCommand("ERASER");
			btnGroup.add(eraserButton);

			// text button
			JButton textButton = new JButton();
			img = ImageIO.read(getClass().getResource("/res/icons/text.png"));
			textButton.setIcon(new ImageIcon(img));
			textButton.setActionCommand("TEXT");
			textButton.setEnabled(false); // temporary disabled
			btnGroup.add(textButton);

			// line button
			JButton lineButton = new JButton();
			img = ImageIO.read(getClass().getResource("/res/icons/line.png"));
			lineButton.setIcon(new ImageIcon(img));
			lineButton.setActionCommand("SHAPE_LINE");
			btnGroup.add(lineButton);

			// rectangle button
			JButton rectangleButton = new JButton();
			img = ImageIO.read(getClass().getResource(
					"/res/icons/rectangle.png"));
			rectangleButton.setIcon(new ImageIcon(img));
			rectangleButton.setActionCommand("SHAPE_RECTANGLE");  // temporary disabled
			rectangleButton.setEnabled(false);
			btnGroup.add(rectangleButton);

			// ellipse button
			JButton ellipseButton = new JButton();
			img = ImageIO
					.read(getClass().getResource("/res/icons/ellipse.png"));
			ellipseButton.setIcon(new ImageIcon(img));
			ellipseButton.setActionCommand("SHAPE_ELLIPSE"); 
			ellipseButton.setEnabled(false);  // temporary disabled
			btnGroup.add(ellipseButton);

			Enumeration<AbstractButton> buttons = btnGroup.getElements();

			while (buttons.hasMoreElements()) {
				JButton b = (JButton) buttons.nextElement();
				b.setPreferredSize(new Dimension(30, 30));
				b.addActionListener(btnListener);
				this.add(b);
			}

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}

	}

	private class BtnListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton pressedButton = ((JButton) e.getSource());

			// Show visually which button was pressed down
			pressedButton.getModel().setPressed(true);

			String tool = e.getActionCommand();
			paintpanel.changeTool(tool);
			
		}
	}
}

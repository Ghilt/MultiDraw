package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class PaintWindow extends JFrame {
	
	/**
	 * Blabla
	 */
	public PaintWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("AOJA POWER ARTIST CANVAS EDITOR PRO");
		
		PaintPanel paintpanel = new PaintPanel();
		JScrollPane scroller = new JScrollPane(paintpanel);
		scroller.setBorder(BorderFactory.createEmptyBorder());
		
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(80, 800));
		leftPanel.add(new JLabel("Tools"));
		
		//Color button
		ColorButton colorButton = new ColorButton("color", paintpanel);
		leftPanel.add(colorButton, 1);
		//Brush size slider
		BrushSizeSlider brushSizeSlider = new BrushSizeSlider(paintpanel);
		leftPanel.add(brushSizeSlider, 2);
		//Draw line button
		DrawLineButton drawLineButton = new DrawLineButton("Line", paintpanel);
		leftPanel.add(drawLineButton, 3);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(150, 800));
		rightPanel.add(new JLabel("Users"));
		

		JPanel chatPanel = new JPanel();
		chatPanel.add(new JLabel("Chat"));
		chatPanel.setPreferredSize(new Dimension(800, 150));
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(scroller, BorderLayout.CENTER);
		centerPanel.add(chatPanel, BorderLayout.SOUTH);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileItem = new JMenu("File");
		JMenu editItem = new JMenu("Edit");
		JMenu aboutItem = new JMenu("About");
		menuBar.add(fileItem);
		menuBar.add(editItem);
		menuBar.add(aboutItem);

		setLayout(new BorderLayout());
		setJMenuBar(menuBar);
		add(leftPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);

		pack();
		setVisible(true);
	}
}
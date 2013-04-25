package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MultiDrawFrame extends JFrame {
	private PaintPanel paintpanel;
	
	/**
	 * Blabla
	 */

	private static final int RIGHT_PANEL_WIDTH = 150;
	private static final int RIGHT_PANEL_HEIGHT = 800;
	private static final int USERS_LIST_WIDTH = 148;
	private static final int USERS_LIST_HEIGHT = 300;
	private static final int CHATWINDOW_WIDTH = 148;
	private static final int CHATWINDOW_HEIGHT = 500;
	private static final int LEFT_PANEL_HEIGHT = 800;
	private static final int LEFT_PANEL_WIDTH = 80;
	private Component chatWindow;

	public MultiDrawFrame(PaintPanel paintpanel) {
		this.paintpanel = paintpanel;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("AOJA POWER ARTIST CANVAS EDITOR PRO");

		JPanel leftPanel = makeLeftPanel(paintpanel);

		JPanel rightPanel = makeRightPanel(paintpanel);

		JPanel centerPanel = makeCenterPanel(paintpanel);

		JMenuBar menuBar = makeMenuBar();

		this.setLayout(new BorderLayout());
		this.setJMenuBar(menuBar);
		this.add(leftPanel, BorderLayout.WEST);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(rightPanel, BorderLayout.EAST);

		this.pack();
		this.setVisible(true);
	}

	private JMenuBar makeMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileItem = new JMenu("File");
		JMenu editItem = new JMenu("Edit");
		JMenu aboutItem = new JMenu("About");
		menuBar.add(fileItem);
		menuBar.add(editItem);
		menuBar.add(aboutItem);
		return menuBar;
	}

	private JPanel makeCenterPanel(PaintPanel paintpanel) {

		JScrollPane scroller = new JScrollPane(paintpanel);
		scroller.setBorder(BorderFactory.createEmptyBorder());

		JPanel chatPanel = new JPanel();
		chatPanel.add(new JLabel("SOMETHING"));
		chatPanel.setPreferredSize(new Dimension(800, 150));

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(scroller, BorderLayout.CENTER);
		centerPanel.add(chatPanel, BorderLayout.SOUTH);

		return centerPanel;
	}

	private JPanel makeRightPanel(PaintPanel paintpanel) {
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, RIGHT_PANEL_HEIGHT));
		rightPanel.add(new JLabel("Users"));
		rightPanel.setBackground(Color.PINK);
		
		JTextField connectedUsersList = new JTextField();
		connectedUsersList.setPreferredSize(new Dimension(USERS_LIST_WIDTH, USERS_LIST_HEIGHT));
		rightPanel.add(connectedUsersList);
		
		rightPanel.add(new JLabel("Chat"));
		chatWindow = new JTextField();
		chatWindow.setPreferredSize(new Dimension(CHATWINDOW_WIDTH, CHATWINDOW_HEIGHT));
		rightPanel.add(chatWindow);
		
		return rightPanel;
	}

	private JPanel makeLeftPanel(PaintPanel paintpanel) {
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.GREEN);
		leftPanel.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH, LEFT_PANEL_HEIGHT));
		leftPanel.add(new JLabel("Tools"));

		// Color button
		ColorButton colorButton = new ColorButton("color", paintpanel);
		leftPanel.add(colorButton);

		// Brush size slider
		BrushSizeSlider brushSizeSlider = new BrushSizeSlider(paintpanel);
		leftPanel.add(brushSizeSlider);

		// Draw line button
		DrawLineButton drawLineButton = new DrawLineButton("Line", paintpanel);
		leftPanel.add(drawLineButton);
		return leftPanel;
	}

	public PaintPanel getPaintPanel() {
		return paintpanel;
	}
}
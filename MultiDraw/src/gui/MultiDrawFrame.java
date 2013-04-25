package gui;

import interfaces.Protocol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import network.SendBuffer;

public class MultiDrawFrame extends JFrame {
	private SendBuffer buffer;
	private PaintPanel paintpanel;
	
	/**
	 * Blabla
	 */

	private static final int RIGHT_PANEL_WIDTH = 150;
	private static final int RIGHT_PANEL_HEIGHT = 800;
	private static final int USERS_LIST_WIDTH = 148;
	private static final int USERS_LIST_HEIGHT = 200;
	private static final int CHATWINDOW_WIDTH = 148;
	private static final int CHATWINDOW_HEIGHT = 500;
	private static final int LEFT_PANEL_HEIGHT = 800;
	private static final int LEFT_PANEL_WIDTH = 80;
	private JTextArea chatWindow;

	public MultiDrawFrame(SendBuffer buffer) {
		this.buffer = buffer;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("AOJA POWER ARTIST CANVAS EDITOR PRO");

		JPanel centerPanel = makeCenterPanel();
		JPanel leftPanel = makeLeftPanel();
		JPanel rightPanel = makeRightPanel();

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

	private JPanel makeCenterPanel() {
		paintpanel = new PaintPanel(buffer);
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

	private JPanel makeRightPanel() {
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, RIGHT_PANEL_HEIGHT));
		rightPanel.add(new JLabel("Users"));
		rightPanel.setBackground(Color.PINK);
		
		JTextArea connectedUsersList = new JTextArea();
		connectedUsersList.setPreferredSize(new Dimension(USERS_LIST_WIDTH, USERS_LIST_HEIGHT));
		connectedUsersList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		rightPanel.add(connectedUsersList);
		connectedUsersList.setEditable(false);
		
		rightPanel.add(new JLabel("Chat"));
		chatWindow = new JTextArea();
		chatWindow.setPreferredSize(new Dimension(CHATWINDOW_WIDTH, CHATWINDOW_HEIGHT));
		chatWindow.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		rightPanel.add(chatWindow);
		chatWindow.setEditable(false);
		
		JTextField chatInput = new JTextField();
		chatInput.setPreferredSize(new Dimension(CHATWINDOW_WIDTH, 25));
		chatInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		rightPanel.add(chatInput);
		
		chatInput.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_ENTER) {
					JTextField field = (JTextField) e.getSource();
			        buffer.put(Protocol.CHAT_MESSAGE + " " + field.getText());
			        field.setText("");
				}
			}
			public void keyReleased(KeyEvent arg0) { }
			public void keyTyped(KeyEvent arg0) { }
		});
		
		return rightPanel;
	}

	private JPanel makeLeftPanel() {
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
	
	public JTextArea getChatPanel() {
		return chatWindow;
	}
}
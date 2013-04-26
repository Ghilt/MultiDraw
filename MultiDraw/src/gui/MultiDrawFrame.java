package gui;

import gui.buttons.BrushButton;
import gui.buttons.ColorButton;
import gui.buttons.DrawLineButton;
import gui.buttons.EllipseButton;
import gui.buttons.EraseButton;
import gui.buttons.PaintbucketButton;
import gui.buttons.PenButton;
import gui.buttons.RectangleButton;
import gui.buttons.TextButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.text.JTextComponent;

import utils.Protocol;

import network.SendBuffer;

public class MultiDrawFrame extends JFrame {
	private static final int RIGHT_PANEL_WIDTH = 200;
	private static final int RIGHT_PANEL_HEIGHT = 800;
	private static final int USERS_LIST_HEIGHT = 200;
	private static final int CHATWINDOW_HEIGHT = 510;
	private static final int LEFT_PANEL_HEIGHT = 800;
	private static final int LEFT_PANEL_WIDTH = 80;

	private SendBuffer buffer;
	private PaintPanel paintpanel;
	private JTextPane chatWindow;
	private JList connectedUsersList;

	/**
	 * Blabla
	 */
	public MultiDrawFrame(SendBuffer buffer) {
		this.buffer = buffer;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("AOJA POWER ARTIST CANVAS EDITOR PRO");
		this.setMinimumSize(new Dimension(1200, 850));
		this.setResizable(true);

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
		JMenu fileMenu = new JMenu("File");
		JMenuItem importPic = new JMenuItem("Import image...", KeyEvent.VK_T);
		importPic.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.ALT_MASK));
		importPic.getAccessibleContext().setAccessibleDescription(
				"Import image...");
		importPic.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					paintpanel.sendFileForInsertingtoSever(file);
				}
			}
		});
		fileMenu.add(importPic);

		JMenu editMenu = new JMenu("Edit");
		JMenu aboutMenu = new JMenu("About");
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(aboutMenu);
		return menuBar;
	}

	private JPanel makeCenterPanel() {
		paintpanel = new PaintPanel(buffer);
		JScrollPane scroller = new JScrollPane(paintpanel);
		scroller.setBorder(BorderFactory.createEmptyBorder());

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		centerPanel.add(scroller, c);
		return centerPanel;
	}

	private JPanel makeRightPanel() {
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH,
				RIGHT_PANEL_HEIGHT));
		rightPanel.setBackground(new Color(230, 230, 230));
		rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0,
				new Color(200, 200, 200)));
		rightPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JLabel users = new JLabel("Users");
		users.setForeground(new Color(60, 60, 60));

		JLabel chat = new JLabel("Chat");
		chat.setForeground(new Color(60, 60, 60));

		DefaultListModel listModel = new DefaultListModel();

		connectedUsersList = new JList(listModel);
		connectedUsersList.setPreferredSize(new Dimension(
				RIGHT_PANEL_WIDTH - 8, USERS_LIST_HEIGHT));
		connectedUsersList.setBorder(BorderFactory
				.createLineBorder(Color.LIGHT_GRAY));
		connectedUsersList
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		connectedUsersList.setSelectedIndex(0);
		connectedUsersList.setVisibleRowCount(5);

		chatWindow = new JTextPane();
		chatWindow.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH - 8,
				CHATWINDOW_HEIGHT));
		chatWindow.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		chatWindow.setEditable(false);
		chatWindow.setMargin(new Insets(4, 4, 4, 4));

		JTextField chatInput = new JTextField();
		chatInput.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH - 8, 25));
		chatInput.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.insets = new Insets(2, 2, 2, 2);

		c.gridy = 0;
		rightPanel.add(users, c);

		c.gridy = 1;
		rightPanel.add(connectedUsersList, c);

		c.gridy = 2;
		rightPanel.add(chat, c);

		c.gridy = 3;
		rightPanel.add(chatWindow, c);

		c.gridy = 4;
		rightPanel.add(chatInput, c);

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

			public void keyReleased(KeyEvent arg0) {
			}

			public void keyTyped(KeyEvent arg0) {
			}
		});

		return rightPanel;
	}

	private JPanel makeLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH,
				LEFT_PANEL_HEIGHT));
		leftPanel.setBackground(new Color(230, 230, 230));
		leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1,
				new Color(200, 200, 200)));

		// Tools label
		leftPanel.add(new JLabel("Tools"));

		// Brush size slider
		BrushSizeSlider brushSizeSlider = new BrushSizeSlider(paintpanel);
		leftPanel.add(brushSizeSlider);

		// Pen button
		PenButton penButton = new PenButton(paintpanel);
		leftPanel.add(penButton);

		// Brush button
		BrushButton brushButton = new BrushButton(paintpanel);
		leftPanel.add(brushButton);

		// Paint bucket button
		PaintbucketButton paintbucketButton = new PaintbucketButton(paintpanel);
		leftPanel.add(paintbucketButton);
		
		// Erase button
		EraseButton eraseButton = new EraseButton(paintpanel);
		leftPanel.add(eraseButton);

		// Text Button
		TextButton textButton = new TextButton(paintpanel);
		leftPanel.add(textButton);

		// Draw line button
		DrawLineButton drawLineButton = new DrawLineButton(paintpanel);
		leftPanel.add(drawLineButton);
		
		// Rectangle button
		RectangleButton rectangleButton = new RectangleButton(paintpanel);
		leftPanel.add(rectangleButton);
		
		// Ellipse button
		EllipseButton ellipseButton = new EllipseButton(paintpanel);
		leftPanel.add(ellipseButton);
		
		// Color button
		JLabel colorLabel = new JLabel("Color");
		colorLabel.setForeground(new Color(60, 60, 60));
		leftPanel.add(colorLabel);
		ColorButton colorButton = new ColorButton("", paintpanel);
		colorButton.setPreferredSize(new Dimension(50, 50));
		colorButton.setBackground(Color.BLACK);
		colorButton.setOpaque(true);
		leftPanel.add(colorButton);

		return leftPanel;
	}

	public PaintPanel getPaintPanel() {
		return paintpanel;
	}

	public JTextPane getChatPanel() {
		return chatWindow;
	}

	public void updateUsersList(String[] words) {
		DefaultListModel model = (DefaultListModel) connectedUsersList
				.getModel();
		model.clear();
		for (int i = 1; i < words.length; i++) {
			model.addElement(words[i]);
			// System.out.println("Inserting \"" + words[i] + "\" at index " +
			// i);
			// model.insertElementAt(words[i], i-1);
		}
	}
}
package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
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
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(80, 800));
		leftPanel.add(new JLabel("Tools"));
		
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(150, 800));
		rightPanel.add(new JLabel("Users"));
		
		PaintPanel paintpanel = new PaintPanel();
		JScrollPane scroller = new JScrollPane(paintpanel);
		scroller.setBorder(BorderFactory.createEmptyBorder());
		
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
		setTitle("AOJA POWER ARTIST CANVAS EDITOR PRO");
		pack();
	}
}
package spike;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class TestingCanvas {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(400,400));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new MyPanel());
		frame.pack();
		frame.setVisible(true);

	}
}


class MyPanel extends JPanel {
    private int mouseX = 50;
    private int mouseY = 50;
    private int brushSize = 20;
    
    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.WHITE);
    
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                moveSquare(e.getX(),e.getY());
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                moveSquare(e.getX(),e.getY());
            }
        });
    
    }
    
    private void moveSquare(int x, int y) {
        if ((mouseX!=x) || (mouseY!=y)) {
            mouseX=x;
            mouseY=y;
            repaint(mouseX - (brushSize / 2), mouseY - (brushSize / 2), brushSize, brushSize);
        } 
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
//        g.fillRect(mouseX - (brushSize / 2), mouseY - (brushSize / 2), brushSize, brushSize);
//        g.fillRoundRect(mouseX - (brushSize / 2), mouseY - (brushSize / 2), brushSize, brushSize, 10, 10);
        g.fillOval(mouseX - (brushSize / 2), mouseY - (brushSize / 2), brushSize, brushSize);
    }  
}

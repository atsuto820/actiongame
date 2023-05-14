package action.window;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PainterPanel extends JPanel {
	public BufferedImage image;

	public PainterPanel() {
		super();
		this.image = new BufferedImage(960, 720, BufferedImage.TYPE_INT_RGB);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(image,  0,  0,  this);
	}

	public void draw() {
		this.repaint();
	}
}

package action.window;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GameFrame extends JFrame{
	public Keyboard keyboard;
	public GamePanel panel;

	public GameFrame(){
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Super Marlo Bros");
		setSize(966, 755);
		setResizable(false);
		setLocationRelativeTo(null);
		try {
			setIconImage(ImageIO.read(new File("gameData/back_img/icon.png")));
		} catch (IOException e) {
		}
		setLayout(null);

		keyboard = new Keyboard();
		addKeyListener(keyboard);
		panel = new GamePanel();
		panel.setBounds(0, 0, 960, 720);
		add(panel);

		setVisible(true);
	}
}

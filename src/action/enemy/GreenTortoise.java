package action.enemy;

import java.awt.Image;

import action.main.Main;

public class GreenTortoise extends Tortoise {
	public GreenTortoise(double x, double y) {
		super(x, y);
		images = new Image[3];
		images[0] = Main.charaImages[0].getSubimage(192, 0, 40, 40);
		images[1] = Main.charaImages[0].getSubimage(240, 0, 40, 40);
		images[2] = Main.charaImages[0].getSubimage(288, 0, 40, 40);
		imageNumber = 1;
	}
}

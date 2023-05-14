package action.enemy;

import java.awt.Image;

import action.main.Main;

public class MetalTortoise extends Tortoise {
	public MetalTortoise(double x, double y) {
		super(x, y);
		images = new Image[3];
		images[0] = Main.charaImages[0].getSubimage(0, 144, 40, 40);
		images[1] = Main.charaImages[0].getSubimage(48, 144, 40, 40);
		images[2] = Main.charaImages[0].getSubimage(96, 144, 40, 40);
		imageNumber = 1;
	}
}

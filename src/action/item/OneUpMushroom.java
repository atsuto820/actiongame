package action.item;

import action.main.Main;

public class OneUpMushroom extends Mushroom {
	public OneUpMushroom(double x, double y) {
		super(x, y);
		images[0] = Main.charaImages[0].getSubimage(288, 48, 40, 40);
	}
}

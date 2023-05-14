package action.fieldObjects;

import java.awt.Image;

import action.game.HitRange;
import action.main.Main;

public class Hammer extends SpecialObject {
	int vy;

	public Hammer(double x, double y) {
		super(x, y, 40, 40);
		hitRange = HitRange.hitRanges[0];
		images = new Image[1];
		images[0] = Main.charaImages[0].getSubimage(384, 96, 40, 40);
		imageNumber = 0;
		vy = 18;
	}

	@Override
	public void update() {
		x -= 5;
		vy -= 2;
		y += vy;
	}

	@Override
	public SpecialObject touched(SpecialObject so, int direction) {
		return this;
	}

	@Override
	public SpecialObject overlaped(SpecialObject so) {
		return this;
	}
}

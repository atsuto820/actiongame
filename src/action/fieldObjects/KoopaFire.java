package action.fieldObjects;

import java.awt.Image;

import action.game.HitRange;
import action.main.Main;

public class KoopaFire extends SpecialObject {
	public KoopaFire(double x, double y) {
		super(x, y, 40, 40);
		hitRange = HitRange.hitRanges[0];
		images = new Image[1];
		images[0] = Main.charaImages[0].getSubimage(288, 96, 40, 40);
		imageNumber = 0;
	}

	@Override
	public void update() {
		x -= 5;
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

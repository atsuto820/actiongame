package action.fieldObjects;

import java.awt.Image;

import action.game.HitLine;
import action.game.HitRange;
import action.game.Player;
import action.main.Main;

public class SpecialLift extends HitBoxObject {
	public SpecialLift(double x, double y) {
		super(x, y, 192, 48, HitBoxObject.hitPointsNull);
		hitRange = new HitRange(new HitLine[] { new HitLine(0, 48, 144, 48) });
		this.images = new Image[3];
		for (int i = 0; i < 3; i++) {
			this.images[i] = Main.objectImages[0].getSubimage(48, 336 + 48 * i, 144, 48);
		}
		imageNumber = 0;
		suddenlyChange = false;
	}

	@Override
	public void update() {
		moveAndAdjust();

		vx = 0;
		this.imageNumber = 0;
	}

	public SpecialObject touched(SpecialObject so, int direction) {
		if(so instanceof Player) {
			if(this.x > so.getX()) {
				this.vx = -5;
				((Player) so).setDirectryVX(-5);
				this.imageNumber = 1;
			} else {
				this.vx = 5;
				((Player) so).setDirectryVX(5);
				this.imageNumber = 2;
			}
		}
		return this;
	}

	@Override
	public void touched(FieldCell fc, int direction) {
	}

	@Override
	public void overlaped(FieldCell fc) {

	}

	@Override
	public SpecialObject overlaped(SpecialObject so) {
		return this;
	}
}

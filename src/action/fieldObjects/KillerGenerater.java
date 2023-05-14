package action.fieldObjects;

import java.awt.Image;

import action.enemy.Enemy;
import action.enemy.Killer;
import action.game.HitRange;
import action.main.Main;

public class KillerGenerater extends Enemy {
	private int state;

	public KillerGenerater(double x, double y) {
		super(x, y, 48, 48, HitBoxObject.hitPointsNull);
		hitRange = HitRange.hitRanges[15];
		images = new Image[1];
		images[0] = Main.objectImages[0].getSubimage(0, 336, 48, 48);
		imageNumber = 0;
		state = 0;
	}

	@Override
	public void update() {
		if(state == 90) {
			state = 0;
			if(this.x > game.getPlayer().getX()) {
				game.addSo(new Killer(this.x - 48, this.y, true));
			} else {
				game.addSo(new Killer(this.x + 48, this.y, false));
			}
		} else {
			state++;
		}
	}

	@Override
	public SpecialObject touched(SpecialObject so, int direction) {
		return this;
	}

	@Override
	public void touched(FieldCell fc, int direction) {
	}

	@Override
	public SpecialObject overlaped(SpecialObject so) {
		return this;
	}

	@Override
	public void overlaped(FieldCell fc) {
	}
}

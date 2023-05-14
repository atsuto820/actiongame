package action.item;

import java.awt.Image;

import action.fieldObjects.FieldCell;
import action.fieldObjects.HitBoxObject;
import action.fieldObjects.SpecialObject;
import action.game.HitRange;
import action.game.Player;
import action.main.Main;

public class FireFrower extends HitBoxObject {
	public FireFrower(double x, double y) {
		super(x, y, 40, 40, HitBoxObject.hitPoints40_40);
		hitRange = HitRange.hitRanges[0];
		images = new Image[1];
		images[0] = Main.charaImages[0].getSubimage(96, 96, 40, 40);
		imageNumber = 0;
		suddenlyChange = false;
	}

	@Override
	public void update() {
		moveAndAdjust();
		vy -= 3;
	}

	public SpecialObject touched(SpecialObject so, int direction) {
		if(so instanceof Player) {
			return null;
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
		return null;
	}
}

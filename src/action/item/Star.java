package action.item;

import java.awt.Image;

import action.fieldObjects.FieldCell;
import action.fieldObjects.HitBoxObject;
import action.fieldObjects.SpecialObject;
import action.game.HitRange;
import action.game.Player;
import action.main.Main;

public class Star extends HitBoxObject {
	private boolean isLeft;
	private boolean isMidair;

	public Star(double x, double y) {
		super(x, y, 40, 40, HitBoxObject.hitPoints40_40);
		hitRange = HitRange.hitRanges[0];
		images = new Image[1];
		images[0] = Main.charaImages[0].getSubimage(240, 48, 40, 40);
		imageNumber = 0;
		isLeft = true;
		suddenlyChange = false;
	}

	@Override
	public void update() {
		moveAndAdjust();

		vx = isLeft ? -3 : 3;
		if(!isMidair) {
			vy = 22;
		} else {
			vy -= 2;
		}
		isMidair = true;
	}


	@Override
	protected void hitReaction() {
		boolean b = false;

		if(hitCells != null) {
			for (int i = 0; i < hitCells.size(); i++) {
				game.addTouch(this, hitCells.get(i)[0], hitCells.get(i)[1], hitCells.get(i)[2]);
				if(hitCells.get(i)[2] == 1 || hitCells.get(i)[2] == 3) {
					b = true;
				}
			}
		}

		if(hitSos != null) {
			for (Object[] hitSo : hitSos) {
				game.addTouch(this, (SpecialObject)(hitSo[0]), (int)(hitSo[1]));
				if(((int)(hitSo[1]) == 1) || ((int)(hitSo[1]) == 3)) {
					b = true;
				}
			}
		}

		if (b) {
			isLeft = !isLeft;
			suddenlyChange = true;
		}
	}

	public SpecialObject touched(SpecialObject so, int direction) {
		if(so instanceof Player) {
			return null;
		}
		if(direction == 0) {
			isMidair = false;
		}
		return this;
	}

	@Override
	public void touched(FieldCell fc, int direction) {
		if(direction == 0) {
			isMidair = false;
		}
	}

	@Override
	public void overlaped(FieldCell fc) {

	}

	@Override
	public SpecialObject overlaped(SpecialObject so) {
		return null;
	}
}

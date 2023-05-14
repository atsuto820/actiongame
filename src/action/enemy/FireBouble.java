package action.enemy;

import java.awt.Image;

import action.fieldObjects.FieldCell;
import action.fieldObjects.HitBoxObject;
import action.fieldObjects.SpecialObject;
import action.game.HitRange;
import action.main.Main;

public class FireBouble extends Enemy {
	private int state;
	private double gotoY;
	
	/*
	 * 0～60 待ち
	 */
	
	public FireBouble(double x, double y) {
		super(x, y, 40, 40, HitBoxObject.hitPointsNull);
		hitRange = HitRange.hitRanges[0];
		images = new Image[1];
		this.images[0] = Main.charaImages[0].getSubimage(672, 0, 40, 40);
		imageNumber = 0;
		state = -2;
		gotoY = y;
	}

	@Override
	public void update() {
		moveAndAdjust();

		if(state > -1) {
			if(state == 59) {
				state = -1;
				vy = 24;
			} else {
				state++;
			}
		} else {
			switch(state) {
			case -1:
				if(y > gotoY - 144) {
					state = -2;
					}
				break;
			case -2:
				if(vy < -22) {
					state = -3;
					vy = -24;
				}
				vy -= 2;
				break;
			case -3:
				if(y <= -48) {
					y = -48;
					vy = 0;
					state = 0;
				}
				break;
			}
		}
	}

	@Override
	protected void hitReaction() {
		if (hitCells != null) {
			for (int i = 0; i < hitCells.size(); i++) {
				Main.game.game.addTouch(this, hitCells.get(i)[0], hitCells.get(i)[1], hitCells.get(i)[2]);
			}
		}

		if (hitSos != null) {
			for (Object[] hitSo : hitSos) {
				Main.game.game.addTouch(this, (SpecialObject) (hitSo[0]), (int) (hitSo[1]));
			}
		}
	}

	@Override
	protected void overlapReaction() {
		if (overlapCells != null) {
			for (Integer[] overlapCell : overlapCells) {
				Main.game.game.addOverlap(this, overlapCell[0], overlapCell[1]);
			}
		}

		if (overlapSos != null) {
			for (SpecialObject so : overlapSos) {
				Main.game.game.addOverlap(this, so);
			}
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

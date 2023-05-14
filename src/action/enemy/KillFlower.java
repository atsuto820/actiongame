package action.enemy;

import java.awt.Image;

import action.fieldObjects.FieldCell;
import action.fieldObjects.HitBoxObject;
import action.fieldObjects.SpecialObject;
import action.game.HitRange;
import action.main.Main;

public class KillFlower extends Enemy {
	private int state;
	/*
	 * -1 → -60 → -∞ 待機時間
	 * 1 → 90 食事
	 */

	public KillFlower(double x, double y) {
		super(x, y, 80, 80, HitBoxObject.hitPointsNull);
		hitRange = HitRange.hitRanges[0];
		images = new Image[2];
		images[0] = Main.charaImages[0].getSubimage(480, 0, 80, 80);
		images[1] = Main.charaImages[0].getSubimage(576, 0, 80, 80);
		imageNumber = 0;
		state = -1;
	}

	@Override
	public void update() {
		moveAndAdjust();

		if (state > 0) {
			if (state == 90) {
				vy = 0;
				state = -1;
			} else {
				if (state < 30) {
					vy = 2.6;
				} else if (state > 60) {
					vy = -2.6;
				} else {
					if (state == 30) {
						vy = 0;
					}
					if(state % 5 == 0) {
						if(state % 10 == 0) {
							imageNumber = 1;
						} else {
							imageNumber = 0;
						}
					}
				}
				state++;
			}
		} else {
			if ((state < -60) && (game.getPlayer().getX() < this.x - 96 || game.getPlayer().getX() > this.x + 96)) {
				state = 1;
			} else {
				state--;
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

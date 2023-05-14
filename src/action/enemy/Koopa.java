package action.enemy;

import java.awt.Image;

import action.fieldObjects.FieldCell;
import action.fieldObjects.FireBall;
import action.fieldObjects.HitBoxObject;
import action.fieldObjects.KoopaFire;
import action.fieldObjects.SpecialObject;
import action.game.HitRange;
import action.main.Main;

public class Koopa extends Enemy {
	private int state;
	private int fireState;
	private int life;
	/*
	 * 0～29 右移動
	 * 30～59 左移動
	 * 60～89 ジャンプ
	 * 90～99 炎を吐く
	 */

	public Koopa(double x, double y) {
		super(x, y, 80, 80, HitBoxObject.hitPoints80_80);
		hitRange = HitRange.hitRanges[0];
		images = new Image[1];
		images[0] = Main.charaImages[0].getSubimage(480, 96, 80, 80);
		imageNumber = 0;
		state = 60;
		fireState = 120;
		life = 10;
	}

	@Override
	protected void hitReaction() {
		if (hitCells != null) {
			for (int i = 0; i < hitCells.size(); i++) {
				game.addTouch(this, hitCells.get(i)[0], hitCells.get(i)[1], hitCells.get(i)[2]);
			}
		}

		if (hitSos != null) {
			for (Object[] hitSo : hitSos) {
				game.addTouch(this, (SpecialObject) (hitSo[0]), (int) (hitSo[1]));
			}
		}
	}

	@Override
	protected void overlapReaction() {
		if (overlapCells != null) {
			for (Integer[] overlapCell : overlapCells) {
				game.addOverlap(this, overlapCell[0], overlapCell[1]);
			}
		}

		if (overlapSos != null) {
			for (SpecialObject so : overlapSos) {
				game.addOverlap(this, so);
			}
		}
	}

	@Override
	public void touched(FieldCell fc, int direction) {
	}

	@Override
	public void overlaped(FieldCell fc) {
	}

	@Override
	public void update() {
		moveAndAdjust();

		if (state == 29) {
			state = (Math.random() > 0.4) ? 30 : 60;
		} else if (state == 59) {
			state = (Math.random() > 0.5) ? 0 : 60;
		} else if (state == 89) {
			state = (Math.random() > 0.6) ? 0 : 30;
		} else {
			state++;
		}

		if (state == 0) {
			vx = 5;
		} else if (state == 30) {
			vx = -5;
		} else if (state == 60) {
			vx = 0;
			vy = 15;
		}
		
		if(fireState > 119) {
			if(Math.random() < 0.08) {
				game.addSo(new KoopaFire(this.x, this.y));
				fireState = 0;
			}
		}

		vy--;
		fireState++;
	}

	@Override
	public SpecialObject touched(SpecialObject so, int direction) {
		return this;
	}

	@Override
	public SpecialObject overlaped(SpecialObject so) {
		if(so instanceof FireBall) {
			if(this.life == 0) {
				game.addDeletes(this);
				Main.startSound(6);
			} else {
				life--;
			}
			game.addDeletes(so);
		}
		return this;
	}

}

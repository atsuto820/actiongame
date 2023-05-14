package action.fieldObjects;

import java.awt.Image;

import action.enemy.Enemy;
import action.enemy.FireBouble;
import action.enemy.Killer;
import action.enemy.Koopa;
import action.enemy.MetalTortoise;
import action.game.HitRange;
import action.main.Main;

public class FireBall extends HitBoxObject {
	private int state;
	private boolean isMidair;

	public FireBall(double x, double y, boolean isLeft) {
		super(x, y, 40, 40, HitBoxObject.hitPoints40_40);
		hitRange = HitRange.hitRanges[0];
		images = new Image[10];
		for (int i = 0; i < 10; i++) {
			images[i] = Main.animationImage.getSubimage(48 * i, isLeft ? 240 : 192, 40, 40);

		}
		imageNumber = 9;
		vx = isLeft ? -26 : 26;
		suddenlyChange = false;
		state = 10;
		isMidair = true;
	}

	@Override
	public void update() {
		moveAndAdjust();

		if(state < 10) {
			imageNumber = state;
			state++;
		} else {
			vy -= 3;
			imageNumber = 9;
			if(!isMidair) {
				state = 0;
				vy = 0;
			}
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
				} else if(hitCells.get(i)[2] == 0) {
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
			game.addDeletes(this);
		}
	}

	public SpecialObject touched(SpecialObject so, int direction) {
		if(direction == 0) {
			isMidair = false;
		}
		overlaped(so);
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
		if(so instanceof MetalTortoise || so instanceof Killer) {
			game.addDeletes(this);
		} else if(so instanceof Enemy) {
			if(!(so instanceof Koopa || so instanceof FireBouble)) {
				game.addDeletes(so);
				game.addDeletes(this);
				Main.startSound(6);
			}
		}
		return null;
	}
}

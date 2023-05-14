package action.enemy;

import java.awt.Image;

import action.fieldObjects.FieldCell;
import action.fieldObjects.HitBoxObject;
import action.fieldObjects.SpecialObject;
import action.game.HitRange;
import action.game.Player;
import action.main.Main;

public class BounceTortoise extends Enemy {
	private boolean isLeft;
	private boolean isMidair;

	public BounceTortoise(double x, double y) {
		super(x, y, 40, 40, HitBoxObject.hitPoints40_40);
		hitRange = HitRange.up40;
		images = new Image[2];
		images[0] = Main.charaImages[0].getSubimage(336, 48, 40, 40);
		images[1] = Main.charaImages[0].getSubimage(384, 48, 40, 40);
		imageNumber = 1;
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
			imageNumber = (isLeft) ? 0 : 1;
			isLeft = !isLeft;
			suddenlyChange = true;
		}
	}

	@Override
	protected void overlapReaction() {
		if (overlapCells != null) {
			for (Integer[] overlapCell : overlapCells) {
				game.addOverlap(this, overlapCell[0], overlapCell[1]);
			}
		}

		if(overlapSos != null) {
			for (SpecialObject so : overlapSos) {
				game.addOverlap(this, so);
			}
		}
	}

	@Override
	public SpecialObject touched(SpecialObject so, int direction) {
		if (so instanceof Player) {
			if(((Player)so).getStar() != -1) {
				game.addDeletes(this);
				Main.startSound(6);
			} else {
				if (direction == 2) {
					((Player)so).stepJump(-1);
					game.addDeletes(this);
					game.addSo(new GreenTortoise(this.x, this.y));
					Main.startSound(6);
					return this;
				}
				((Player)so).attacked();
			}
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
	public SpecialObject overlaped(SpecialObject so) {
		return this;
	}

	@Override
	public void overlaped(FieldCell fc) {
	}
}

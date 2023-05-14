package action.enemy;

import java.awt.Image;

import action.fieldObjects.FieldCell;
import action.fieldObjects.HitBoxObject;
import action.fieldObjects.SpecialObject;
import action.game.HitRange;
import action.game.Player;
import action.main.Main;

public class Killer extends Enemy {
	private boolean isLeft;

	public Killer(double x, double y, boolean isLeft) {
		super(x, y, 40, 40, HitBoxObject.hitPoints40_40);
		hitRange = HitRange.up40;
		images = new Image[2];
		images[0] = Main.charaImages[0].getSubimage(144, 144, 40, 40);
		images[1] = Main.charaImages[0].getSubimage(192, 144, 40, 40);
		this.isLeft = isLeft;
		imageNumber = isLeft ? 1 : 0;
		suddenlyChange = false;
	}

	@Override
	public void update() {
		if (isLeft) {
			x -= 10;
		} else {
			x += 10;
		}
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
	public SpecialObject touched(SpecialObject so, int direction) {
		if (so instanceof Player) {
			if(((Player) so).getStar() != -1) {
				game.addDeletes(this);
				Main.startSound(6);
			} else {
				if (direction == 2) {
					((Player) so).stepJump(-1);
					game.addDeletes(this);
					Main.startSound(6);
					return this;
				}
				((Player) so).attacked();
			}
		}
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

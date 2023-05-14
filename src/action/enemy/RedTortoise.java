package action.enemy;

import java.awt.Image;
import java.util.ArrayList;

import action.fieldObjects.FieldCell;
import action.fieldObjects.SpecialObject;
import action.main.Main;

public class RedTortoise extends Tortoise {
	private boolean isMidair;
	private ArrayList<Integer[]> hitedCells;

	public RedTortoise(double x, double y) {
		super(x, y);
		images = new Image[3];
		images[0] = Main.charaImages[0].getSubimage(336, 0, 40, 40);
		images[1] = Main.charaImages[0].getSubimage(384, 0, 40, 40);
		images[2] = Main.charaImages[0].getSubimage(432, 0, 40, 40);
		imageNumber = 1;
	}

	@Override
	public void update() {
		moveAndAdjust();

		if((this.x % 48 < 20 || this.x % 20 > 28) && (!isMidair) && getState() == 0) {
			int stepNum = 0;
			if(hitedCells != null) {
				for (Integer[] hitedCell : hitedCells) {
					if(hitedCell[2] == 2) {
						stepNum++;
					}
				}

				if(stepNum < 2) {
					if (isLeft) {
						imageNumber = 0;
					} else {
						imageNumber = 1;
					}
					isLeft = !isLeft;
				}
			}
		}

		if (getState() != 1) {
			if (isLeft) {
				vx = -speed;
			} else {
				vx = speed;
			}
		} else {
			if (Math.abs(vx) < 2) {
				vx = 0;
			} else {
				vx -= 0.5 * Math.signum(vx);
			}
		}
		vy -= 3;

		if (invincibleTime != -1) {
			if (invincibleTime > 1) {
				invincibleTime = -1;
			} else {
				invincibleTime++;
			}
		}

		isMidair = true;
	}

	@Override
	protected void hitReaction() {
		super.hitReaction();
		hitedCells = hitCells;
	}

	@Override
	public SpecialObject touched(SpecialObject so, int direction) {
		if(direction == 0) {
			isMidair = false;
		}
		return super.touched(so, direction);
	}

	@Override
	public void touched(FieldCell fc, int direction) {
		if(direction == 0) {
			isMidair = false;
		}
		super.touched(fc, direction);
	}
}

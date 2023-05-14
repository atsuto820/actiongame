package action.enemy;

import action.fieldObjects.FieldCell;
import action.fieldObjects.HitBoxObject;
import action.fieldObjects.SpecialObject;
import action.game.HitRange;
import action.game.Player;
import action.main.Main;

public abstract class Tortoise extends Enemy {
	protected boolean isLeft;
	private int state;
	protected int invincibleTime;
	protected int speed;
	/*
	 * 0 : 一般
	 * 1 : うずくまり
	 * 2 : うずくまり移動
	 */

	public Tortoise(double x, double y) {
		super(x, y, 40, 40, HitBoxObject.hitPoints40_40);
		hitRange = HitRange.up40;
		isLeft = true;
		suddenlyChange = false;
		invincibleTime = -1;
		speed = 3;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
		switch (state) {
		case 0:
			state = 0;
			speed = 3;
			hitRange = HitRange.up40;
			imageNumber = 1;
			break;
		case 1:
			state = 1;
			speed = 0;
			hitRange = HitRange.range40;
			imageNumber = 2;
			vx = 0;
			break;
		case 2:
			state = 2;
			speed = 10;
			hitRange = HitRange.range40;
			imageNumber = 2;
			invincibleTime = 0;
			break;
		}
	}

	@Override
	protected void hitReaction() {
		boolean b = false;

		if (hitCells != null) {
			for (int i = 0; i < hitCells.size(); i++) {
				game.addTouch(this, hitCells.get(i)[0], hitCells.get(i)[1], hitCells.get(i)[2]);
				if (hitCells.get(i)[2] == 1 || hitCells.get(i)[2] == 3) {
					b = true;
				}
			}
		}

		if (hitSos != null) {
			for (Object[] hitSo : hitSos) {
				game.addTouch(this, (SpecialObject) (hitSo[0]), (int) (hitSo[1]));
				if (((int) (hitSo[1]) == 1) || ((int) (hitSo[1]) == 3)) {
					b = true;
				}
			}
		}

		if (b) {
			if (state == 0) {
				if (isLeft) {
					imageNumber = 0;
				} else {
					imageNumber = 1;
				}
			}
			isLeft = !isLeft;
			suddenlyChange = true;
		}
	}

	@Override
	public void update() {
		moveAndAdjust();

		if (state != 1) {
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
			if(((Player) so).getStar() != -1){
				game.addDeletes(this);
				Main.startSound(6);
			} else{
				if (direction == 2) {
					Main.startSound(6);
					switch (state) {
					case 0:
						setState(1);
						((Player) so).stepJump(-1);
						break;
					case 1:
						setState(2);
						((Player) so).stepJump(-1);
						isLeft = (so.getX() > this.x);
						break;
					case 2:
						setState(1);
						((Player) so).stepJump(-1);
						break;
					}
				} else if (direction != 0 && state == 1) {
					Main.startSound(6);
					isLeft = (direction == 1);
					setState(2);
				} else {
					if ((isLeft && direction == 1) || (!isLeft && direction == 3)) {
						if (speed != 16) {
							speed += 2;
						}
						invincibleTime = 0;
					}
					if (invincibleTime == -1) {
						((Player) so).attacked();
					}
				}
			}
		} else if (so instanceof Enemy) {
			if (this.state == 2) {
				game.addDeletes(so);
				Main.startSound(6);
			}
		}
		return this;

	}

	@Override
	public void touched(FieldCell fc, int direction) {
	}

	@Override
	public SpecialObject overlaped(SpecialObject so) {
		if (so instanceof Goomba || so instanceof GreenTortoise) {
			if (this.state == 2) {
				game.addDeletes(so);
				Main.startSound(6);
			}
		}
		return this;
	}

	@Override
	public void overlaped(FieldCell fc) {
	}

}

package action.game;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import action.enemy.Enemy;
import action.enemy.Goomba;
import action.fieldObjects.FieldCell;
import action.fieldObjects.FireBall;
import action.fieldObjects.FirePart;
import action.fieldObjects.Goal;
import action.fieldObjects.Hammer;
import action.fieldObjects.HitBoxObject;
import action.fieldObjects.KoopaFire;
import action.fieldObjects.SpecialObject;
import action.item.FireFrower;
import action.item.Mushroom;
import action.item.OneUpMushroom;
import action.item.Star;
import action.main.Main;

public class Player extends HitBoxObject {
	private boolean isMidair;
	private int jumpValue;
	private double speed;
	private int invincibleTime;
	private int state;
	private int star;
	/*
	 * state
	 * 0 : 初期
	 * 1 : キノコ
	 * 2 : フラワー
	 */

	public Player(double x, double y, int state) {
		super(x, y, 40, 40, HitBoxObject.hitPoints40_40);
//		hitRange = new HitRange(new HitLine[] {new HitLine(40, 0, 0, 0)});
		hitRange = HitRange.hitRanges[0];
		if(game.getMap().getBack() == 6) {
			speed = 0.6;
		} else {
			speed = 4;
		}
		images = new Image[7];
		images[0] = Main.charaImages[0].getSubimage(0, 0, 40, 40);
		images[1] = Main.charaImages[0].getSubimage(48, 0, 40, 40);
		images[2] = Main.charaImages[0].getSubimage(0, 48, 40, 80);
		images[3] = Main.charaImages[0].getSubimage(48, 48, 40, 80);
		images[4] = Main.charaImages[0].getSubimage(144, 48, 40, 80);
		images[5] = Main.charaImages[0].getSubimage(192, 48, 40, 80);
		images[6] = Main.charaImages[0].getSubimage(240, 96, 40, 80);
		
		jumpValue = 0;
		this.state = state;
		invincibleTime = -1;
		star = -1;

		switch (state) {
		case 0:
			imageNumber = 0;
			break;
		case 1:
			imageNumber = 2;
			this.height = 80;
			hitPoints = HitBoxObject.hitPoints40_80;
			break;
		case 2:
			imageNumber = 4;
			this.height = 80;
			hitPoints = HitBoxObject.hitPoints40_80;
			break;
		}
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getState() {
		return state;
	}

	public int getInvincibleTime() {
		return invincibleTime;
	}

	public int getStar() {
		return star;
	}

	public boolean isMidair() {
		return isMidair;
	}

	public void accelerateLeft() {
		switch (state) {
		case 0:
			imageNumber = 1;
			break;
		case 1:
			imageNumber = 3;
			break;
		case 2:
			imageNumber = 5;
		}
		vx -= speed;
	}

	public void accelerateRight() {
		switch (state) {
		case 0:
			imageNumber = 0;
			break;
		case 1:
			imageNumber = 2;
			break;
		case 2:
			imageNumber = 4;
		}
		vx += speed;
	}

	public void jump() {
		if (!isMidair) {
			if (vx > 18 || vx < -18) {
				vy = 42;
			} else {
				vy = 38;
			}
			jumpValue = 1;
			Main.startSound(0);
		}
	}

	public void stepJump(int soundNumber) {
		Integer keyInfo = Main.isKeyPressed(KeyEvent.VK_SPACE);
		if (keyInfo != null) {
			this.setVy(42);
			if(soundNumber != -1) {
				Main.startSound(soundNumber);
			}
		} else {
			this.setVy(30);
		}
	}

	public void releaseFire() {
		if (this.state == 2) {
			int fireNumber = 0;
			for (SpecialObject so : game.getSos()) {
				if (so instanceof FireBall) {
					fireNumber++;
				}
			}
			if (fireNumber < 2) {
				game.addSo(new FireBall(this.x, this.y, (imageNumber % 2 == 1)));
				Main.startSound(5);
			}
		}
	}

	public void update() {
		isMidair = true;
		if (vx > 20) {
			vx = 20;
		} else if (vx < -20) {
			vx = -20;
		}

		//		if (vy > 48) {
		//			vy = 48;
		//		} else if (vy < -48) {
		//			vy = -48;
		//		}

		moveAndAdjust();
		
		if (jumpValue > 1 && jumpValue < 8) {
			Integer keyInfo = Main.isKeyPressed(KeyEvent.VK_SPACE);
			if (keyInfo == null) {
				vy -= 4;
			}
		}

		double vxGoodBye = Math.signum(vx) * speed / 2.5;
		if (vx < Math.abs(vxGoodBye) && vx > -Math.abs(vxGoodBye)) {
			vx = 0;
		} else {
			vx -= vxGoodBye;
		}

		if (vy < 1 && vy > -1) {
			vy = 0;
		} else {
			vy -= Math.signum(vy);
		}

		if (!isMidair) {
			jumpValue = 0;
		} else if (jumpValue != 0) {
			jumpValue++;
		}
		vy -= 3;

		if (invincibleTime != -1) {
			if (invincibleTime > 29) {
				invincibleTime = -1;
			} else {
				invincibleTime++;
			}
		}

		if (star != -1) {
			if (star > 299) {
				star = -1;
				Main.endBGM();
				Main.startBGM("gameData\\bgm\\" + game.getMap().getBack() + ".wav", true);
			} else {
				if (star % 5 == 0) {
					BufferedImage im = Main.animationImage.getSubimage(0, 336, 768, 48);
					game.addAnimation(new Animation(im, 10, (int) this.x, (int) this.y));
				}
				star++;
			}
		}
		
		if(game.getMap().getBack() == 6) {
			speed = 0.6;
		} else {
			speed = 4;
		}
	}

	@Override
	public void attacked() {
		if (invincibleTime == -1) {
			switch (state) {
			case 0:
				die();
				break;
			case 1:
				state = 0;
				imageNumber -= 2;
				this.height = 40;
				hitPoints = HitBoxObject.hitPoints40_40;
				y -= 20;
				invincibleTime = 0;
				Main.startSound(8);
				break;
			case 2:
				state = 1;
				imageNumber -= 2;
				invincibleTime = 0;
				Main.startSound(8);
				break;
			}
		}
	}

	@Override
	public SpecialObject touched(SpecialObject so, int direction) {
		if (so instanceof Goomba) {
			if (!(direction == 0)) {
				attacked();
			}
		}
		if (direction == 0) {
			isMidair = false;
		}
		return this;
	}

	@Override
	public void touched(FieldCell fc, int direction) {
		if (direction == 0) {
			isMidair = false;
		}
	}

	@Override
	public void overlaped(FieldCell fc) {
		if (fc instanceof Goal) {
			game.goal();
			this.vx = 0;
			if (vy > 0) {
				vy = 0;
			}
		}
	}

	@Override
	public SpecialObject overlaped(SpecialObject so) {
		if (so instanceof Enemy || so instanceof KoopaFire || so instanceof Hammer) {
			if (star != -1) {
				game.addDeletes(so);
				Main.startSound(6);
			} else {
				attacked();
			}
		} else if (so instanceof FireFrower) {
			game.addDeletes(so);
			Main.startSound(7);
			if (state == 0 || state == 1) {
				if (state == 0) {
					imageNumber += 4;
					this.height = 80;
					hitPoints = HitBoxObject.hitPoints40_80;
					y += 20;
				} else {
					imageNumber += 2;
				}
				state = 2;
			}
		} else if (so instanceof Star) {
			game.addDeletes(so);
			star = 0;
			Main.endBGM();
			Main.startBGM("gameData/bgm/star.wav", false);
		} else if (so instanceof OneUpMushroom) {
			game.addDeletes(so);
			Main.game.OneUp();
			Main.startSound(9);
		} else if (so instanceof Mushroom) {
			game.addDeletes(so);
			Main.startSound(7);

			if (state == 0) {
				state = 1;
				imageNumber += 2;
				this.height = 80;
				hitPoints = HitBoxObject.hitPoints40_80;
				y += 20;
			}
		} else if (so instanceof FirePart) {
			if (star != -1) {
				attacked();
			}
		}
		return this;
	}

	public void die() {
		isDead = true;
		imageNumber = 6;
	}
}

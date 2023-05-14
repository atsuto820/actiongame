package action.enemy;

import java.awt.Image;

import action.fieldObjects.FieldCell;
import action.fieldObjects.Hammer;
import action.fieldObjects.HitBoxObject;
import action.fieldObjects.SpecialObject;
import action.game.HitRange;
import action.game.Player;
import action.main.Main;

public class HammerBross extends Enemy {
	private int state;
	private int jumpState;
	private int hammerState;

	public HammerBross(double x, double y) {
		super(x, y, 40, 40, HitBoxObject.hitPoints40_40);
		hitRange = HitRange.up40;
		images = new Image[1];
		images[0] = Main.charaImages[0].getSubimage(336, 96, 40, 40);
		imageNumber = 0;
		state = 0;
		jumpState = 0;
	}

	@Override
	public void update() {
		moveAndAdjust();
		if(state == 29) {
			vx = -2;
		} else if(state == 59) {
			state = 0;
			vx = 2;
		}

		if(jumpState > 89) {
			if(Math.random() < 0.05) {
				jumpState = 0;
				vy = 22;
			}
		}

		if(hammerState == 29) {
			hammerState = 0;
			game.addSo(new Hammer(this.x, this.y));
		}
		state++;
		jumpState++;
		hammerState++;
		vy -= 2;
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

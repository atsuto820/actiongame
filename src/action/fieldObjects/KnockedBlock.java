package action.fieldObjects;

import java.awt.image.BufferedImage;

import action.enemy.Enemy;
import action.enemy.Tortoise;
import action.game.Animation;
import action.game.Player;
import action.item.FireFrower;
import action.item.Mushroom;
import action.item.OneUpMushroom;
import action.item.Star;
import action.main.Main;

public class KnockedBlock extends FieldCell {

	/*
	 * eventNumber
	 * 0 : 何も起こらない
	 * 1 : コイン
	 * 2 : 壊れる
	 * 3 : アイテム
	 * 4 : スター
	 * 5 : 1UPキノコ
	 */
	private int event;
	private int x;
	private int y;

	public KnockedBlock(int skin, int event, int x, int y) {
		super(skin);
		this.event = event;
		this.x = x;
		this.y = y;
	}

	@Override
	public FieldCell touched(SpecialObject so, int direction) {
		if(so instanceof Player) {
			if(direction == 0) {
				return knocked(so, ((Player) so).getState() != 0);
			}
		} else if(so instanceof Tortoise) {
			if(direction % 2 == 1 && ((Tortoise) so).getState() == 2) {
				return knocked(so, true);
			}
		}
		return this;
	}

	private FieldCell knocked(SpecialObject so, boolean isStrong) {
		for (Object[] hboFs : game.getTouchHboFs()) {
			if(game.getField(((int)hboFs[1]), ((int)hboFs[2])) == this && ((int)hboFs[3]) == 2) {
				if(hboFs[0] instanceof Tortoise) {
					Tortoise tortoise = (Tortoise)hboFs[0];
					if(tortoise.getState() == 2) {
						tortoise.setVy(16);
					} else {
						tortoise.setState(1);
						tortoise.setVy(25);
						tortoise.setVx((so.getX() > tortoise.getX()) ? -8 : 8);
					}
				} else if(hboFs[0] instanceof Mushroom) {
					((Mushroom)hboFs[0]).setVy(16);
				} else if(hboFs[0] instanceof Enemy) {
					game.addDeletes((Enemy)hboFs[0]);
				}
			}
		}
		BufferedImage image = null;
		switch(event) {
		case 1 :
			image = Main.animationImage.getSubimage(0, 0, 768, 48);
			game.addAnimation(new Animation(image, 10, x, y + 44));
			Main.game.addCoin();
			Main.startSound(1);
			return new Ground(0x030);
		case 2 :
			if(isStrong) {
				image = Main.animationImage.getSubimage(0, 96, 768, 48);
				game.addAnimation(new Animation(image, 10, x, y));
				Main.startSound(2);
				return null;
			}
			image = Main.animationImage.getSubimage(0, 48, 768, 48);
			game.addAnimation(new Animation(image, 10, x, y));
			Main.startSound(3);
			return this;
		case 3 :
			if(!isStrong){
				game.addSo(new Mushroom(x, y + 44));
				Main.startSound(10);
			} else {
				game.addSo(new FireFrower(x, y + 44));
				Main.startSound(10);
			}
			return new Ground(0x030);
		case 4 :
			game.addSo(new Star(x, y + 44));
			Main.startSound(10);
			return new Ground(0x030);
		case 5 :
			game.addSo(new OneUpMushroom(x, y + 44));
			Main.startSound(10);
			return new Ground(0x030);
		}
		return this;
	}

	@Override
	public FieldCell overlaped(SpecialObject so) {
		return this;
	}
}

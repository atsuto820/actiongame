package action.fieldObjects;

import java.awt.event.KeyEvent;

import action.game.Player;
import action.main.Main;

public class PlayerControlBlock extends FieldCell {
	private double moveX;
	private double moveY;
	boolean isDirectry;

	public PlayerControlBlock(int skin, int hit, double moveX, double moveY, boolean b) {
		super(skin, hit);
		this.moveX = moveX;
		this.moveY = moveY;
		isDirectry = b;
	}

	public PlayerControlBlock(int skin, double moveX, double moveY, boolean b) {
		super(skin);
		this.moveX = moveX;
		this.moveY = moveY;
		isDirectry = b;
	}

	@Override
	public FieldCell touched(SpecialObject so, int direction) {
		if(so instanceof HitBoxObject) {
			if(direction == 2) {
				if(isDirectry) {
					((HitBoxObject)so).setDirectryVX(moveX);
					((HitBoxObject)so).setDirectryVY(moveY);
				} else {
					Integer keyInfo = Main.isKeyPressed(KeyEvent.VK_SPACE);
					if (keyInfo != null) {
						((Player) so).setVy(moveY);
						Main.startSound(11);
					} else {
						((Player) so).setVy(30);
					}
				}
			}
		}
		return this;
	}

	@Override
	public FieldCell overlaped(SpecialObject so) {
		return this;
	}

}

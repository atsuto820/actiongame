package action.fieldObjects;

import java.awt.event.KeyEvent;

import action.game.Player;
import action.main.Main;

public class Pipe extends FieldCell {
	private int info;
	private int[] stage;
	private int x;
	/*
	 * info
	 * 0 上下向き土管の左側
	 * 1 〃右側
	 * 2 左右向き土管の下側
	 */

	public Pipe(int skin, int x, int info, int[] stage) {
		super(skin);
		this.info = info;
		this.stage = stage;
		this.x = x;
	}

	@Override
	public FieldCell touched(SpecialObject so, int direction) {
		if (so instanceof Player) {
			if (info != 2) {
				boolean b = false;
				if (direction == 0) {
					Integer keyInfo = Main.isKeyPressed(KeyEvent.VK_W);
					if (keyInfo != null) {
						if ((info == 0) ? (x - 24 + so.getWidth() < so.getX()) : (x + 24 - so.getWidth() > so.getX())) {
							game.setRug(300);
							b = true;
						}
					}
				} else if (direction == 2) {
					Integer keyInfo = Main.isKeyPressed(KeyEvent.VK_S);
					if (keyInfo != null) {
						if ((info == 0) ? (x - 24 + so.getWidth() < so.getX()) : (x + 24 - so.getWidth() > so.getX())) {
							game.setRug(350);
							b = true;
						}
					}
				}
				if (b) {
					game.setGotoNumber(stage);
					Main.startSound(4);
				}
			} else {
				if (direction == 3) {
					Integer keyInfo = Main.isKeyPressed(KeyEvent.VK_D);
					if (!((Player) so).isMidair()) {
						if (keyInfo != null) {
							game.setRug(450);
							game.setGotoNumber(stage);
							Main.startSound(4);
						}
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

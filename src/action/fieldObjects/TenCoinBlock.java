package action.fieldObjects;

import java.awt.image.BufferedImage;

import action.game.Animation;
import action.main.Main;

public class TenCoinBlock extends FieldCell {
	private int remaining;
	private int x;
	private int y;

	public TenCoinBlock(int x, int y) {
		super(0x020);
		remaining = 10;
		this.x = x;
		this.y = y;
	}

	@Override
	public FieldCell touched(SpecialObject so, int direction) {
		if(direction == 0) {
			BufferedImage image = Main.animationImage.getSubimage(0, 0, 768, 48);
			game.addAnimation(new Animation(image, 10, x, y + 44));
			Main.game.addCoin();
			Main.startSound(1);
			if(remaining == 1) {
				return new Ground(0x030);
			}
			remaining--;
		}
		return this;
	}

	@Override
	public FieldCell overlaped(SpecialObject so) {
		return this;
	}
}

package action.game;

import java.awt.image.BufferedImage;

public class Animation {
	private final BufferedImage animation;
	private final int animationNum;
	private int nowAnimation;
	private int x;
	private int y;

	public Animation(BufferedImage animation, int animationNum, int x, int y) {
		super();
		this.animation = animation;
		this.animationNum = animationNum;
		nowAnimation = 0;
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public BufferedImage update() {
		
		if(nowAnimation == animationNum) {
			return null;
		} else {
			BufferedImage image = animation.getSubimage(48 * nowAnimation, 0, 48, 48);
			nowAnimation++;
			return image;
		}
	}
}

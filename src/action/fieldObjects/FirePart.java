package action.fieldObjects;

import java.awt.Image;

import action.game.HitRange;
import action.game.Player;
import action.main.Main;

public class FirePart extends SpecialObject {
	private double radious;
	private double max;
	private boolean isClock;
	private int direction;
	private double centerX;
	private double centerY;

	public FirePart(double x, double y, double radius, boolean isClock, double max) {
		super(x, y, 40, 40);
		this.radious = radius;
		this.max = max;
		this.isClock = isClock;
		this.direction = 0;
		centerX = x;
		centerY = y;
		hitRange = HitRange.hitRanges[0];
		this.images = new Image[1];
		this.images[0] = Main.charaImages[0].getSubimage(672, 0, 40, 40);
		imageNumber = 0;
	}

	@Override
	public void update() {
		if(isClock) {
			if(direction > 359) {
				direction -= 360;
			}
			direction += (9 - max);
		} else {
			if(direction < 0) {
				direction += 360;
			}
			direction -= (9 - max);
		}

		this.x = centerX + Math.sin(direction / 180.0 * Math.PI) * radious;
		this.y = centerY + Math.cos(direction / 180.0 * Math.PI) * radious;
	}

	@Override
	public SpecialObject touched(SpecialObject so, int direction) {
		return null;
	}

	@Override
	public SpecialObject overlaped(SpecialObject so) {
		if(so instanceof Player) {
			((Player)so).attacked();
		}
		return null;
	}

}

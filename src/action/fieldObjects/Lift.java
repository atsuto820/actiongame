package action.fieldObjects;

import java.awt.Image;

import action.game.HitLine;
import action.game.HitRange;
import action.main.Main;

public abstract class Lift extends SpecialObject{
	public Lift(double x, double y) {
		super(x, y, 144, 48);
		hitRange = new HitRange(new HitLine[] {new HitLine(0, 48, 144, 48)});
		this.images = new Image[1];
		this.images[0] = Main.objectImages[0].getSubimage(48, 288, 144, 48);
		imageNumber = 0;
	}

	@Override
	public SpecialObject touched(SpecialObject so, int direction) {
		return this;
	}

	@Override
	public SpecialObject overlaped(SpecialObject so) {
		return this;
	}
}

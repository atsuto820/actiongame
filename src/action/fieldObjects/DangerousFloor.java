package action.fieldObjects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import action.game.HitLine;
import action.game.HitRange;
import action.main.Main;

public class DangerousFloor extends SpecialObject {

	public DangerousFloor(double x, double y) {
		super(x, y, 13, 48);
		hitRange = new HitRange(new HitLine[] {new HitLine(0, 48, 624, 48)});
		this.images = new Image[1];
		BufferedImage image = Main.objectImages[0].getSubimage(528, 240, 48, 48);
		images[0] = new BufferedImage(624, 48, BufferedImage.TYPE_INT_RGB);
		Graphics gra = this.images[0].getGraphics();
		for (int i = 0; i < 13; i++) {
			gra.drawImage(image, i * 48, 0, Main.frame.panel);
		}
		imageNumber = 0;
	}

	@Override
	public void update() {
		if(game.getRug() != -1 && game.getRug() < 200) {
			game.addDeletes(this);
		}
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

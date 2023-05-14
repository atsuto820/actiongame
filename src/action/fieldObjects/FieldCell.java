package action.fieldObjects;

import java.awt.Image;

import action.game.HitRange;
import action.main.Main;

public abstract class FieldCell extends FieldObject {
	private Image image;

	public FieldCell(int skin, int hit) {
		super();
		hitRange = HitRange.hitRanges[hit];
		image = Main.objectImages[skin - 1];
	}

	public FieldCell(int skin) {
		super();
		try {
			hitRange = HitRange.hitRanges[Main.hitRange[skin / 0x100][skin % 0x10][(skin / 0x10) % 0x10]];
		} catch (Exception e) {
		}
		image = Main.objectImages[skin / 0x100].getSubimage(((skin / 0x10) % 0x10) * 48, (skin % 0x10) * 48, 48, 48);
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public abstract FieldCell touched(SpecialObject so, int direction);

	@Override
	public abstract FieldCell overlaped(SpecialObject so);
}

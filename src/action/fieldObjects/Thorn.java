package action.fieldObjects;

import action.game.Player;

public class Thorn extends FieldCell {
	private boolean isStrong;

	public Thorn(int skin, boolean isStrong) {
		super(skin);
		this.isStrong = isStrong;
	}

	public Thorn(int skin, int hit, boolean isStrong) {
		super(skin, hit);
		this.isStrong = isStrong;
	}

	@Override
	public FieldCell touched(SpecialObject so, int direction) {
		if(so instanceof Player) {
			if(isStrong) {
				((Player)so).die();
			} else {
				((Player)so).attacked();
			}
		}
		return this;
	}

	@Override
	public FieldCell overlaped(SpecialObject so) {
		touched(so, 0);
		return this;
	}
}

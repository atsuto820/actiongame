package action.fieldObjects;

import action.game.Player;
import action.main.Main;

public class Coin extends FieldCell {
	public Coin() {
		super(0x075);
	}

	@Override
	public FieldCell touched(SpecialObject so, int direction) {
		return null;
	}

	@Override
	public FieldCell overlaped(SpecialObject so) {
		if(so instanceof Player) {
			Main.game.addCoin();
			Main.startSound(1);
			return null;
		}
		return this;
	}

}

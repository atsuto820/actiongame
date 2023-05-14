package action.fieldObjects;

public class FireBar extends FieldCell {
	public FireBar(int length, boolean isClock, int x, int y) {
		super(0x085);
		for (int i = 0; i < length; i++) {
			game.addSo(new FirePart(x, y, i * 48, isClock, length));
		}
	}

	public FieldCell touched(SpecialObject so, int direction) {
		return this;
	}

	@Override
	public FieldCell overlaped(SpecialObject so) {
		return this;
	}
}

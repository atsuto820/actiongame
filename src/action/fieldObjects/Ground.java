package action.fieldObjects;

public class Ground extends FieldCell {
	public Ground(int skin) {
		super(skin);
	}

	public Ground(int skin, int hit) {
		super(skin, hit);
	}

	@Override
	public FieldCell touched(SpecialObject so, int direction) {
		return this;
	}

	@Override
	public FieldCell overlaped(SpecialObject so) {
		return this;
	}
}

package action.fieldObjects;

public class Goal extends FieldCell{

	public Goal(int skin, int hit) {
		super(skin, hit);
	}

	public Goal(int skin) {
		super(skin);
	}

	@Override
	public FieldCell touched(SpecialObject so, int direction) {
		return null;
	}

	@Override
	public FieldCell overlaped(SpecialObject so) {
		return this;
	}

}

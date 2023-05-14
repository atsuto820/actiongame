package action.fieldObjects;

import action.game.Game;
import action.game.HitRange;

public abstract class FieldObject {
	protected HitRange hitRange;
	public static Game game;

	public FieldObject() {
		super();
	}

	public HitRange getHitRange() {
		return hitRange;
	}

	public abstract FieldObject touched(SpecialObject so, int direction);

	public abstract FieldObject overlaped(SpecialObject so);
}

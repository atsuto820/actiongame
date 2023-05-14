package action.fieldObjects;

public class SideLift extends Lift {
	private int moveNum;

	public SideLift(double x, double y) {
		super(x, y);
		moveNum = 0;
	}

	@Override
	public void update() {
		x += (Math.sin((moveNum + 1) / 60.0 * Math.PI) - Math.sin(moveNum / 60.0 * Math.PI)) * 100;
		if(moveNum == 119) {
			moveNum = 0;
		} else {
			moveNum++;
		}
	}

	@Override
	public SpecialObject touched(SpecialObject so, int direction) {
		if(so instanceof HitBoxObject) {
			((HitBoxObject) so).setDirectryVX((Math.sin((moveNum + 1) / 60.0 * Math.PI) - Math.sin(moveNum / 60.0 * Math.PI)) * 100);
		}
		return this;
	}
}

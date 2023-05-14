package action.fieldObjects;

public class DownLift extends Lift {
	private int moveNum;

	public DownLift(double x, double y) {
		super(x, y);
		moveNum = 0;
	}

	@Override
	public void update() {
		if(moveNum == 71) {
			moveNum = 0;
			y += 288;
		} else {
			y -= 4;
			moveNum++;
		}
	}
	
	@Override
	public SpecialObject touched(SpecialObject so, int direction) {
		if(so instanceof HitBoxObject) {
			((HitBoxObject) so).setDirectryVY(-4);
		}
		return this;
	}
}

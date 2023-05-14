package action.enemy;

import action.fieldObjects.HitBoxObject;

public abstract class Enemy extends HitBoxObject{
	public Enemy(double x, double y, double width, double height, double[][] hitPoints) {
		super(x, y, width, height, hitPoints);
	}
}

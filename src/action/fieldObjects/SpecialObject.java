package action.fieldObjects;

import java.awt.Image;

public abstract class SpecialObject extends FieldObject {
	protected double x;
	protected double y;
	protected double width;
	protected double height;

	protected Image[] images;
	protected int imageNumber;

	public Image getImage() {
		return images[imageNumber];
	}

	public SpecialObject(double x, double y, double width, double height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public abstract void update();

	@Override
	public abstract SpecialObject touched(SpecialObject so, int direction);

	@Override
	public abstract SpecialObject overlaped(SpecialObject so);
}

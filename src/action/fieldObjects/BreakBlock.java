package action.fieldObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import action.game.Animation;
import action.main.Main;

public class BreakBlock extends FieldCell {
	private int x;
	private int y;
	private int durability;

	public BreakBlock(int durability, int x, int y) {
		super(0x040);
		this.durability = durability;
		this.x = x;
		this.y = y;
		
		
		BufferedImage image = new BufferedImage(48, 48, BufferedImage.TYPE_INT_RGB);
		Graphics gra = image.getGraphics();
		gra.setColor(new Color(128, 64, 0));
		gra.fillRect(0, 0, 48, 48);
		gra.setColor(Color.WHITE);
		gra.setFont(new Font("Sazanami Gothic", Font.PLAIN, 24));
		gra.drawString("" + durability, 0, 36);
		this.setImage(image);
	}

	@Override
	public FieldCell touched(SpecialObject so, int direction) {
		if(Main.getFrameCount() % 5 == 0) {
			if(so instanceof HitBoxObject) {
				if(direction == 2) {
					if(durability == 1) {
						BufferedImage image = Main.animationImage.getSubimage(0, 96, 768, 48);
						game.addAnimation(new Animation(image, 10, x, y));
						return null;
					} else {
						durability--;
						Graphics gra = getImage().getGraphics();
						gra.setColor(new Color(128, 64, 0));
						gra.fillRect(0, 0, 48, 48);
						gra.setColor(Color.WHITE);
						gra.setFont(new Font("Sazanami Gothic", Font.PLAIN, 24));
						gra.drawString("" + durability, 0, 36);
					}
				}
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

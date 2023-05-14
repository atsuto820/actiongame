package action.main;

import java.awt.image.BufferedImage;

public class MapObj {
	private int hit;
	private BufferedImage image;
	private int info;
	/*
	 * course
	 * 0 : コースでない
	 * 正 : 未クリアコース
	 * 負 : クリアコース
	 * 100 : 横
	 * -100 : 縦
	 */

	public MapObj(int info) {
		super();
		this.info = info;
		hit = Main.mapHit[info % 0x10][(info / 0x10) % 0x10];
		image = Main.mapImage.getSubimage(((info / 0x10) % 0x10) * 48, (info % 0x10) * 48, 48, 48);
	}

	public BufferedImage getImage() {
		return image;
	}

	public boolean canGo(int direction) {
		return (hit % Math.pow(2, 4 - direction) >= Math.pow(2, 3 - direction));
	}
	
	public int getInfo() {
		return info;
	}
	
	public void clear() {
		
	}
}

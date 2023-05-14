package action.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import action.enemy.BounceTortoise;
import action.enemy.Enemy;
import action.enemy.FireBouble;
import action.enemy.FlyTortoise;
import action.enemy.Goomba;
import action.enemy.GreenTortoise;
import action.enemy.HammerBross;
import action.enemy.KillFlower;
import action.enemy.Koopa;
import action.enemy.MetalTortoise;
import action.enemy.RedTortoise;
import action.fieldObjects.BreakBlock;
import action.fieldObjects.Coin;
import action.fieldObjects.DangerousFloor;
import action.fieldObjects.DownLift;
import action.fieldObjects.FieldCell;
import action.fieldObjects.FieldObject;
import action.fieldObjects.FireBall;
import action.fieldObjects.FireBar;
import action.fieldObjects.Goal;
import action.fieldObjects.Ground;
import action.fieldObjects.HitBoxObject;
import action.fieldObjects.KillerGenerater;
import action.fieldObjects.KnockedBlock;
import action.fieldObjects.KoopaFire;
import action.fieldObjects.Pipe;
import action.fieldObjects.PlayerControlBlock;
import action.fieldObjects.SideLift;
import action.fieldObjects.SpecialLift;
import action.fieldObjects.SpecialObject;
import action.fieldObjects.TenCoinBlock;
import action.fieldObjects.Thorn;
import action.main.Main;

public class Game {
	private Map map;
	private FieldCell[][] field;
	private ArrayList<SpecialObject> sos;
	private ArrayList<SpecialObject> adds;
	private ArrayList<SpecialObject> removes;
	private ArrayList<Object[]> touchHboFs;
	private ArrayList<Object[]> touchHboHbo;
	private ArrayList<Object[]> overlapHboFs;
	private ArrayList<SpecialObject[]> overlapHboHbo;
	private Player player;
	private double cameraX;
	private double cameraY;
	private int mapX;
	private int mapY;
	private ArrayList<Animation> animations;
	private int rug;
	private int[] gotoStage;
	private int[] stage;

	/*
	 * rug
	 * 0～180
	 * goal
	 * 200～280
	 * miss
	 * 300～330
	 * 上土管
	 * 350～380
	 * 下土管
	 * 400～430
	 * 左土管
	 * 450～480
	 * 右土管
	 */

	public Game(Map map, int state, int[] stage) {
		super();
		FieldObject.game = this;
		this.map = map;
		player = new Player(stage[2], stage[3], state); // 984 168
		cameraX = player.getX() - 480;
		cameraY = player.getY() - 360;
		adjustCamera();
		sos = new ArrayList<SpecialObject>();
		adds = new ArrayList<SpecialObject>();
		removes = new ArrayList<SpecialObject>();
		mapLoad();
		animations = new ArrayList<Animation>();
		rug = -1;
		gotoStage = null;
		this.stage = stage;

		Main.endBGM();
		Main.startBGM("gameData\\bgm\\" + map.getBack() + ".wav", true);
	}

	public Map getMap() {
		return map;
	}

	public FieldCell getField(int line, int row) {
		try {
			return field[line][row];
		} catch (ArrayIndexOutOfBoundsException e) {
			if (line < 0 || line >= 35) {
				return new Ground(0x040);
			} else {
				if (row < 0) {
					return field[line][0];
				} else {
					return field[line][29];
				}
			}
		}
	}

	public void setField(int line, int row, FieldCell fo) {
		try {
			field[line][row] = fo;
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	public ArrayList<SpecialObject> getSos() {
		return sos;
	}

	public void addSo(SpecialObject so) {
		adds.add(so);
	}

	public void addDeletes(SpecialObject so) {
		removes.add(so);
	}

	public ArrayList<Object[]> getTouchHboFs() {
		return touchHboFs;
	}

	public ArrayList<Object[]> getTouchHboHbo() {
		return touchHboHbo;
	}

	public Player getPlayer() {
		return player;
	}

	public double getCameraX() {
		return cameraX;
	}

	public double getCameraY() {
		return cameraY;
	}

	public int getRug() {
		return rug;
	}

	public void setRug(int rug) {
		this.rug = rug;
	}

	public void setGotoNumber(int[] gotoStage) {
		this.gotoStage = gotoStage;
	}

	public Image update() {
		BufferedImage image = new BufferedImage(1008, 720, BufferedImage.TYPE_INT_RGB);
		Graphics gra = image.getGraphics();
		gra.drawImage(map.getBackImage(0), 0, 0, Main.frame.panel);
		gra.drawImage(map.getBackImage(1), -(int) ((cameraX / 5) % 960), 0, Main.frame.panel);
		gra.drawImage(map.getBackImage(1), 960 - (int) ((cameraX / 5) % 960), 0, Main.frame.panel);

		if (!(rug >= 200 && rug < 500)) {
			if (rug == -1) {
				Integer keyInfo = Main.isKeyPressed(KeyEvent.VK_D);
				if (keyInfo != null) {
					player.accelerateRight();
				}
				keyInfo = Main.isKeyPressed(KeyEvent.VK_A);
				if (keyInfo != null) {
					player.accelerateLeft();
				}
				keyInfo = Main.isKeyPressed(KeyEvent.VK_SPACE);
				if (keyInfo != null && keyInfo == Main.getFrameCount() - 1) {
					player.jump();
				}
				keyInfo = Main.isKeyPressed(KeyEvent.VK_SHIFT);
				if (keyInfo != null && keyInfo == Main.getFrameCount() - 1) {
					player.releaseFire();
				}
			} else {
				player.setDirectryVX(5);
			}

			touchHboFs = null;
			touchHboHbo = null;
			overlapHboFs = null;
			overlapHboHbo = null;

			player.update();

			for (SpecialObject so : sos) {
				if (so.getX() < cameraX - 336 ||
						so.getY() < cameraY - 336 || so.getY() > cameraY + 1104) {
					removes.add(so);
				} else {
					so.update();
				}
			}

			for (SpecialObject so : adds) {
				sos.add(so);
			}
			if (adds.size() > 0) {
				adds = new ArrayList<SpecialObject>();
			}

			if (touchHboFs != null) {
				for (Object[] touchHboFs : touchHboFs) {
					if (getField(((int) touchHboFs[1]), ((int) touchHboFs[2])) != null) {
						int unDirection = ((int) touchHboFs[3]) + 2;
						if (unDirection > 3) {
							unDirection -= 4;
						}

						((HitBoxObject) touchHboFs[0]).touched(getField(((int) touchHboFs[1]), ((int) touchHboFs[2])),
								unDirection);
						setField(((int) touchHboFs[1]), ((int) touchHboFs[2]),
								getField(((int) touchHboFs[1]), ((int) touchHboFs[2])).touched(
										((HitBoxObject) touchHboFs[0]),
										((int) touchHboFs[3])));
					}
				}
			}

			if (touchHboHbo != null) {
				for (Object[] touchHboHbo : touchHboHbo) {
					if (touchHboHbo[0] != null && touchHboHbo[1] != null) {
						int unDirection = ((int) touchHboHbo[2]) + 2;
						if (unDirection > 3) {
							unDirection -= 4;
						}

						((HitBoxObject) touchHboHbo[0]).touched(((SpecialObject) touchHboHbo[1]), unDirection);
						((SpecialObject) touchHboHbo[1]).touched(((HitBoxObject) touchHboHbo[0]),
								((int) touchHboHbo[2]));
					}
				}
			}

			if (overlapHboFs != null) {
				for (Object[] overlapHboFs : overlapHboFs) {
					if (getField(((int) overlapHboFs[1]), ((int) overlapHboFs[2])) != null) {
						((HitBoxObject) overlapHboFs[0])
								.overlaped(getField(((int) overlapHboFs[1]), ((int) overlapHboFs[2])));

						setField(((int) overlapHboFs[1]), ((int) overlapHboFs[2]),
								getField(((int) overlapHboFs[1]), ((int) overlapHboFs[2]))
										.overlaped((HitBoxObject) overlapHboFs[0]));
					}
				}
			}

			if (overlapHboHbo != null) {
				for (SpecialObject[] overlapHboHbo : overlapHboHbo) {
					if (overlapHboHbo[0] != null && overlapHboHbo[1] != null) {
						overlapHboHbo[0].overlaped((overlapHboHbo[1]));
						overlapHboHbo[1].overlaped((overlapHboHbo[0]));
					}
				}
			}

			for (SpecialObject so : removes) {
				if(so instanceof Enemy) {
					BufferedImage im = Main.animationImage.getSubimage(0, 144, 768, 48);
					addAnimation(new Animation(im, 10, (int) so.getX(), (int) so.getY()));
				} else if(so instanceof FireBall) {
					BufferedImage im = Main.animationImage.getSubimage(0, 288, 768, 48);
					addAnimation(new Animation(im, 10, (int) so.getX(), (int) so.getY()));
				}
				sos.remove(so);
			}

			if (removes.size() > 0) {
				removes = new ArrayList<SpecialObject>();
			}

			double beforeCameraX = cameraX;
			double beforeCameraY = cameraY;

			if (cameraX < player.getX() - 480) {
				cameraX = player.getX() - 480;
			}
			cameraY = player.getY() - 360;

			adjustCamera();

			if ((int) beforeCameraX / 48 < (int) cameraX / 48) {
				rightFieldMove();
			}
			if ((int) beforeCameraY / 48 < (int) cameraY / 48) {
				upFieldMove();
			} else if ((int) beforeCameraY / 48 > (int) cameraY / 48) {
				downFieldMove();
			}

		}
		int offsetY = -(int) ((player.getY() + 24) % 48);

		if (player.getY() < 360) {
			offsetY = 0;
		} else if (player.getY() > (map.getRow() - 8) * 48 - 24 + 1) {
			offsetY = 0;
		}

		if (rug >= 300) {
			int veerX = 0;
			int veerY = 0;
			;
			if (rug < 350) {
				veerY = (300 - rug) * 2;
			} else if (rug < 400) {
				veerY = (rug - 350) * 2;
			} else if (rug < 450) {
			} else {
				veerX = (rug - 450) * 2;
			}
			gra.drawImage(player.getImage(), (int) (player.getX() - cameraX - player.getWidth() / 2) + veerX,
					720 - (int) (player.getY() - cameraY + player.getHeight() / 2) + veerY, Main.frame.panel);
		}

		for (SpecialObject so : sos) {
			if (so instanceof KillFlower) {
				gra.drawImage(so.getImage(), (int) (so.getX() - cameraX - so.getWidth() / 2),
						720 - (int) (so.getY() - cameraY + so.getHeight() / 2), Main.frame.panel);
			}
		}

		gra.setColor(Color.BLACK);
		for (int i = 0; i < 21; i++) {
			for (int j = 0; j < 16; j++) {
				if (field[i + 7][j + 7] != null) {
					gra.drawImage(field[i + 7][j + 7].getImage(), i * 48 - (int) (cameraX % 48), 672 - j * 48 - offsetY,
							Main.frame.panel);
				}
				//				gra.drawRect(i * 48 - (int) (cameraX % 48), 672 - j * 48 - offsetY, 48, 48);
			}
		}

		for (SpecialObject so : sos) {
			if (!(so instanceof KillFlower)) {
				gra.drawImage(so.getImage(), (int) (so.getX() - cameraX - so.getWidth() / 2),
						720 - (int) (so.getY() - cameraY + so.getHeight() / 2), Main.frame.panel);
			}
		}

		boolean mustPrint = true;
		if (rug == -1) {
			if ((player.getInvincibleTime() != -1 && player.getInvincibleTime() % 2 == 0)) {
				mustPrint = false;
			} else if (player.getStar() != -1 && (player.getStar() < 239 || player.getStar() % 2 == 0)) {
				gra.setColor(Color.YELLOW);
				gra.fillOval((int) (player.getX() - cameraX - player.getWidth() / 2) - 15,
						720 - (int) (player.getY() - cameraY + player.getHeight() / 2) - 15,
						(int) player.getWidth() + 30, (int) player.getHeight() + 30);
			}
		} else {
			if (rug < 200) {
				if (rug >= 65) {
					mustPrint = false;
				}
			} else if (rug < 300) {
				mustPrint = false;
				gra.drawImage(player.getImage(), (int) (player.getX() - cameraX - player.getWidth() / 2),
						(int) (720 - (int) (player.getY() - cameraY + player.getHeight() / 2)
								+ Math.pow(rug - 215, 2) - 225),
						Main.frame.panel);
			} else if (rug < 500) {
				mustPrint = false;
			}
		}
		if (mustPrint) {
			gra.drawImage(player.getImage(), (int) (player.getX() - cameraX - player.getWidth() / 2),
					720 - (int) (player.getY() - cameraY + player.getHeight() / 2), Main.frame.panel);
		}

		ArrayList<Animation> removeAnimations = null;
		for (Animation animation : animations) {
			Image animationImage = animation.update();
			if (animationImage == null) {
				if (removeAnimations == null) {
					removeAnimations = new ArrayList<Animation>();
				}
				removeAnimations.add(animation);
			} else {
				gra.drawImage(animationImage, (int) (animation.getX() - cameraX - 24),
						720 - (int) (animation.getY() - cameraY + 24), Main.frame.panel);
			}
		}

		if (removeAnimations != null) {
			for (Animation animation : removeAnimations) {
				animations.remove(animation);
			}
		}

		if (player.isDead() && rug == -1) {
			rug = 200;
		}

		gra.setColor(Color.BLACK);
		gra.setFont(new Font("Dialog.plain", Font.PLAIN, 20));
		//		gra.drawString("frame : " + Main.getFrameCount(), 0, 20);
		gra.drawString("X : " + player.getX(), 0, 40);
		gra.drawString("Y : " + player.getY(), 0, 60);
		//		gra.drawString("VX : " + player.getVx(), 0, 80);
		//		gra.drawString("VY : " + player.getVy(), 0, 100);
		//		gra.drawString("Key :    " + (Main.key.isKeyPressed(KeyEvent.VK_UP) != null ? "■" : "□"), 300, 20);
		//		gra.drawString("        " + (Main.key.isKeyPressed(KeyEvent.VK_LEFT) != null ? "■" : "□") +
		//				"    " + (Main.key.isKeyPressed(KeyEvent.VK_RIGHT) != null ? "■" : "□"), 300, 40);
		//		gra.drawString("            " + (Main.isKeyPressed(KeyEvent.VK_DOWN) != null ? "■" : "□"), 300, 60);

		if (rug != -1) {
			rug++;
		}

		if (rug == 330 || rug == 380 || rug == 480) {
			int beforeMap = map.getBack();
			try {
				this.map = new Map("gameData/map/" + stage[0] + "/" + stage[1] + "/" + gotoStage[0] + ".txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
			player.setX(gotoStage[1]);
			player.setY(gotoStage[2]);
			cameraX = player.getX() - 480;
			cameraY = player.getY() - 360;
			adjustCamera();
			sos = new ArrayList<SpecialObject>();
			adds = new ArrayList<SpecialObject>();
			removes = new ArrayList<SpecialObject>();
			mapLoad();
			animations = new ArrayList<Animation>();
			rug = -1;
			gotoStage = null;
			player.setVx(0);
			player.setVy(0);

			if (beforeMap != map.getBack()) {
				Main.endBGM();
				Main.startBGM("gameData\\bgm\\" + map.getBack() + ".wav", true);
			}
		}
		return image;
	}

	private void mapLoad() {
		field = new FieldCell[35][30];
		mapX = (int) (player.getX() - player.getX() % 48 - 816) / 48;
		mapY = (int) (player.getY() - player.getY() % 48 - 696) / 48;
		if (mapX < -7) {
			mapX = -7;
		}
		if (mapY < -7) {
			mapY = -7;
		}
		if (mapY > map.getRow() - 23) {
			mapY = map.getRow() - 23;
		}

		for (int i = 0; i < 35; i++) {
			for (int j = 0; j < 30; j++) {
				field[i][j] = mapBlockLoad(i + mapX, j + mapY);
			}
		}
	}

	private void rightFieldMove() {
		mapX++;
		FieldCell[][] newField = new FieldCell[35][30];
		for (int i = 0; i < 30; i++) {
			newField[34][i] = mapBlockLoad(mapX + 34, mapY + i);
		}
		for (int i = 0; i < 34; i++) {
			for (int j = 0; j < 30; j++) {
				newField[i][j] = field[i + 1][j];
			}
		}

		field = newField;
	}

	//	private void leftFieldMove() {
	//		mapX--;
	//		FieldCell[][] newField = new FieldCell[35][30];
	//		for (int i = 0; i < 30; i++) {
	//			newField[0][i] = mapBlockLoad(mapX, mapY + i);
	//		}
	//		for (int i = 0; i < 34; i++) {
	//			for (int j = 0; j < 30; j++) {
	//				newField[i + 1][j] = field[i][j];
	//			}
	//		}
	//
	//		field = newField;
	//	}

	private void upFieldMove() {
		mapY++;
		FieldCell[][] newField = new FieldCell[35][30];
		for (int i = 0; i < 35; i++) {
			newField[i][29] = mapBlockLoad(mapX + i, mapY + 29);
		}
		for (int i = 0; i < 35; i++) {
			for (int j = 0; j < 29; j++) {
				newField[i][j] = field[i][j + 1];
			}
		}

		field = newField;
	}

	private void downFieldMove() {
		mapY--;
		FieldCell[][] newField = new FieldCell[35][30];
		for (int i = 0; i < 35; i++) {
			newField[i][0] = mapBlockLoad(mapX + i, mapY);
		}
		for (int i = 0; i < 35; i++) {
			for (int j = 0; j < 29; j++) {
				newField[i][j + 1] = field[i][j];
			}
		}

		field = newField;
	}

	private FieldCell mapBlockLoad(int x, int y) {
		if (y < 0) {
			y = 0;
		}
		if (y > map.getRow() - 1) {
			y = map.getRow() - 1;
		}
		return changeObj(map.getField(x, y), x, y);
	}

	private FieldCell changeObj(int number, int x, int y) {
		if (number >= 0xc00) {
			switch (number) {
			case 0xc00:
				sos.add(new DownLift(x * 48 + 24, y * 48 + 24));
				break;
			case 0xc01:
				sos.add(new Goomba(x * 48 + 24, y * 48 + 24));
				break;
			case 0xc02:
				sos.add(new GreenTortoise(x * 48 + 24, y * 48 + 24));
				break;
			case 0xc03:
				sos.add(new KillFlower(x * 48 + 48, y * 48 - 40));
				break;
			case 0xc04:
				sos.add(new RedTortoise(x * 48 + 24, y * 48 + 24));
				break;
			case 0xc05:
				sos.add(new SideLift(x * 48 + 24, y * 48 + 24));
				break;
			case 0xc06:
				sos.add(new BounceTortoise(x * 48 + 24, y * 48 + 24));
				break;
			case 0xc07:
				sos.add(new FlyTortoise(x * 48 + 24, y * 48 + 24));
				break;
			case 0xc08:
				sos.add(new Koopa(x * 48 + 24, y * 48 + 24));
				break;
			case 0xc09:
				sos.add(new FireBouble(x * 48 + 24, y * 48 + 24));
				break;
			case 0xc0a:
				sos.add(new KoopaFire(x * 48 + 24, y * 48 + 24));
				break;
			case 0xc0b:
				sos.add(new HammerBross(x * 48 + 24, y * 48 + 24));
				break;
			case 0xc0c:
				sos.add(new MetalTortoise(x * 48 + 24, y * 48 + 24));
				break;
			case 0xc0d:
				sos.add(new KillerGenerater(x * 48 + 24, y * 48 + 24));
				break;
			case 0xc0e:
				sos.add(new SpecialLift(x * 48 + 24, y * 48 + 24));
				break;
			}
			return null;
		} else if (number >= 0x800) {
			switch (number) {
			case 0x800:
				return new KnockedBlock(0x010, 1, x * 48 + 24, y * 48 + 24);
			case 0x801:
				return new KnockedBlock(0x010, 3, x * 48 + 24, y * 48 + 24);
			case 0x802:
				return new KnockedBlock(0x010, 4, x * 48 + 24, y * 48 + 24);
			case 0x803:
				return new KnockedBlock(0x010, 5, x * 48 + 24, y * 48 + 24);
			case 0x804:
				return new KnockedBlock(0x020, 2, x * 48 + 24, y * 48 + 24);
			case 0x805:
				return new KnockedBlock(0x020, 1, x * 48 + 24, y * 48 + 24);
			case 0x806:
				return new KnockedBlock(0x020, 3, x * 48 + 24, y * 48 + 24);
			case 0x807:
				return new KnockedBlock(0x020, 4, x * 48 + 24, y * 48 + 24);
			case 0x808:
				return new KnockedBlock(0x020, 5, x * 48 + 24, y * 48 + 24);
			case 0x80a:
				return new KnockedBlock(0x005, 1, x * 48 + 24, y * 48 + 24);
			case 0x80b:
				return new KnockedBlock(0x005, 3, x * 48 + 24, y * 48 + 24);
			case 0x80d:
				return new KnockedBlock(0x005, 5, x * 48 + 24, y * 48 + 24);
			case 0x80e:
				return new TenCoinBlock(x * 48 + 24, y * 48 + 24);
			case 0x80f:
			case 0x810:
			case 0x811:
			case 0x812:
			case 0x813:
				return new Thorn((number - 0x80a) * 0x10, false);
			case 0x814:
				return new PlayerControlBlock(0x083, 9, 0, true);
			case 0x815:
				return new PlayerControlBlock(0x093, -9, 0, true);
			case 0x816:
				return new PlayerControlBlock(0x084, 18, 0, true);
			case 0x817:
				return new PlayerControlBlock(0x094, -18, 0, true);
			case 0x818:
				return new PlayerControlBlock(0x055, 0, 46, false);
			case 0x819:
				return new PlayerControlBlock(0x065, 0, 57, false);
			case 0x81a:
				return new Goal(0x0a0);
			case 0x81b:
				return new Goal(0x0a1);
			case 0x81c:
				return new Goal(0x0a2);
			case 0x81d:
				return new Coin();
			case 0x831:
				return new Thorn(0x095, true);
			case 0x832:
				sos.add(new DangerousFloor(x * 48, y * 48 + 24));
				return null;
			case 0x833:
				return new Goal(0x0c5);
			case 0x834:
				return new BreakBlock(18, x * 48 + 24, y * 48 + 24);
			case 0x835:
				return new BreakBlock(12, x * 48 + 24, y * 48 + 24);
			case 0x836:
				return new BreakBlock(6, x * 48 + 24, y * 48 + 24);
			case 0x837:
				return new BreakBlock(3, x * 48 + 24, y * 48 + 24);
			}

			if (number >= 0x81e && number <= 0x826) {
				switch ((number - 0x81e) % 3) {
				case 0:
					return new Pipe(0x001, x * 48 + 24, 0, map.getGotoStage((number - 0x81e) / 3));
				case 1:
					return new Pipe(0x011, x * 48 + 24, 1, map.getGotoStage((number - 0x81e) / 3));
				case 2:
					return new Pipe(0x022, x * 48 + 24, 2, map.getGotoStage((number - 0x81e) / 3));
				}
			}
			if (number >= 0x827 && number <= 0x82b) {
				return new FireBar(number - 0x825, true, x * 48 + 24, y * 48 + 24);
			}
			if (number >= 0x82c && number <= 0x830) {
				return new FireBar(number - 0x82a, false, x * 48 + 24, y * 48 + 24);
			}
			return new Ground(0x086);
		}
		return new Ground(number);
	}

	private void adjustCamera() {
		if (cameraX < 0) {
			cameraX = 0;
		} else if (cameraX > (map.getLine() - 21) * 48) {
			cameraX = (map.getLine() - 21) * 48;
		}
		if (cameraY < 0) {
			cameraY = 0;
		} else if (cameraY > (map.getRow() - 16) * 48) {
			cameraY = (map.getRow() - 16) * 48;
		}
	}

	public void addTouch(HitBoxObject hbo, int x, int y, int direction) {
		if (touchHboFs == null) {
			touchHboFs = new ArrayList<Object[]>();
		}
		touchHboFs.add(new Object[] { hbo, x, y, direction });
	}

	public void addTouch(HitBoxObject hbo, SpecialObject so, int direction) {
		if (touchHboHbo == null) {
			touchHboHbo = new ArrayList<Object[]>();
			touchHboHbo.add(new Object[] { hbo, so, direction });
		} else {
			boolean b = true;
			for (Object[] hbos : touchHboHbo) {
				if ((hbos[0] == hbo && hbos[1] == so) || (hbos[0] == so && hbos[1] == hbo)) {
					b = false;
				}
			}
			if (b) {
				touchHboHbo.add(new Object[] { hbo, so, direction });
			}
		}
	}

	public void addOverlap(HitBoxObject hbo, int x, int y) {
		if (overlapHboFs == null) {
			overlapHboFs = new ArrayList<Object[]>();
		}
		overlapHboFs.add(new Object[] { hbo, x, y });
	}

	public void addOverlap(HitBoxObject hbo, SpecialObject so) {
		if (overlapHboHbo == null) {
			overlapHboHbo = new ArrayList<SpecialObject[]>();
			overlapHboHbo.add(new SpecialObject[] { hbo, so });
		} else {
			boolean b = true;
			for (Object[] hbos : overlapHboHbo) {
				if ((hbos[0] == hbo && hbos[1] == so) || (hbos[0] == so && hbos[1] == hbo)) {
					b = false;
				}
			}
			if (b) {
				overlapHboHbo.add(new SpecialObject[] { hbo, so });
			}
		}
	}

	public void addAnimation(Animation animation) {
		animations.add(animation);
	}

	public void goal() {
		rug = 0;
	}

	public int isEnd() {
		if (rug != -1) {
			if (rug == 2) {
				Main.endBGM();
				Main.startBGM("gameData\\bgm\\goal.wav", false);
			}
			if (rug == 201) {
				Main.endBGM();
				Main.startBGM("gameData\\bgm\\miss.wav", false);
			}
			if (rug >= 180 && rug < 200) {
				Main.endBGM();
				if (this.stage[1] == 9) {
					return 2;
				}
				return 0;
			} else if (rug >= 280 && rug < 300) {
				Main.endBGM();
				return 1;
			}
		}
		return -1;
	}
}

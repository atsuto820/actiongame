package action.main;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.IIOException;

public class WorldMap {
	private MapObj[][] map;
	private BufferedImage backImage;
	private BufferedImage playerImage[];
	private int playerX;
	private int playerY;
	private int mapNumber;
	private int[][] startXY;

	public WorldMap(int mapNumber) throws IOException {
		super();
		mapLoad(mapNumber);
		this.mapNumber = mapNumber;
		playerImage = new BufferedImage[3];
		playerImage[0] = Main.mapImage.getSubimage(48, 0, 48, 48);
		playerImage[1] = Main.mapImage.getSubimage(96, 0, 48, 48);
		playerImage[2] = Main.mapImage.getSubimage(144, 0, 48, 48);
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public BufferedImage update(boolean winReturn, int state) {
		BufferedImage image = new BufferedImage(1008, 720, BufferedImage.TYPE_INT_RGB);
		Graphics gra = image.getGraphics();

		Integer keyInfo = Main.isKeyPressed(KeyEvent.VK_Y);
		if (keyInfo != null && keyInfo == Main.getFrameCount() - 1) {
			winReturn = true;
		}

		if(winReturn) {
			if(map[playerX][playerY].getInfo() % 0x10 == 3) {
				map[playerX][playerY] = new MapObj(map[playerX][playerY].getInfo() + 1);

				int x = playerX;
				int y = playerY - 1;
				for (int i = 0; i < 4; i++) {
					int info = map[x][y].getInfo();
					if(info % 0x10 == 0 && (info / 0x10) % 0x10 == 9) {
						map[x][y] = new MapObj(0x21);
					} else if(info % 0x10 == 0 && (info / 0x10) % 0x10 == 10) {
						map[x][y] = new MapObj(0x22);
					}

					switch (i) {
					case 0:
						y += 2;
						break;
					case 1:
						y--;
						x++;
						break;
					case 2:
						x -= 2;
						break;
					}
				}
			}
		}

		keyInfo = Main.isKeyPressed(KeyEvent.VK_W);
		if (keyInfo != null && keyInfo == Main.getFrameCount() - 1) {
			if(map[playerX][playerY - 1].canGo(2)) {
				playerY--;
			}
		}
		keyInfo = Main.isKeyPressed(KeyEvent.VK_D);
		if (keyInfo != null && keyInfo == Main.getFrameCount() - 1) {
			if(map[playerX + 1][playerY].canGo(3)) {
				playerX++;
			}
		}
		keyInfo = Main.isKeyPressed(KeyEvent.VK_S);
		if (keyInfo != null && keyInfo == Main.getFrameCount() - 1) {
			if(map[playerX][playerY + 1].canGo(0)) {
				playerY++;
			}
		}
		keyInfo = Main.isKeyPressed(KeyEvent.VK_A);
		if (keyInfo != null && keyInfo == Main.getFrameCount() - 1) {
			if(map[playerX - 1][playerY].canGo(1)) {
				playerX--;
			}
		}
		keyInfo = Main.isKeyPressed(KeyEvent.VK_SPACE);
		if (keyInfo != null && keyInfo == Main.getFrameCount() - 1) {
			int info = map[playerX][playerY].getInfo();
			if(info % 0x10 == 3 || info % 0x10 == 4) {
				if((info / 0x10) % 0x10 == 10 || (info / 0x10) % 0x10 == 11 ) {
					if(info % 0x10 == 3) {
						Main.game.stage = new int[]{mapNumber, (info / 0x10) % 0x10, 100, 140};
						Main.endBGM();
					}
				} else {
					Main.game.stage = new int[]{mapNumber, (info / 0x10) % 0x10, startXY[(info / 0x10) % 0x10][0], startXY[(info / 0x10) % 0x10][1]};
					Main.endBGM();
				}
			}
		}

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 15; j++) {
				gra.drawImage(backImage, i * 48, j * 48, Main.frame.panel);
				gra.drawImage(map[i][j].getImage(), i * 48, j * 48, Main.frame.panel);
			}
		}

		gra.drawImage(playerImage[state], playerX * 48, playerY * 48, Main.frame.panel);
		return image;
	}

	private void mapLoad(int mapNumber) throws IOException {
		File file = new File("gameData\\world_map\\" + mapNumber + ".txt");
		if (!file.exists()) {
			throw new IIOException("Can't read input file!");
		}
		BufferedReader br = new BufferedReader(new FileReader(file));
		backImage = Main.backMapImages[mapNumber];
		playerX = Integer.parseInt(br.readLine());
		playerY = Integer.parseInt(br.readLine());


		map = new MapObj[20][15];
		for (int i = 0; i < 15; i++) {
			String lineStr = br.readLine();
			for (int j = 0; j < 20; j++) {
				map[j][i] = new MapObj(Integer.parseInt(lineStr.substring(j * 3, j * 3 + 2), 16));
			}
		}

		startXY = new int[10][];
		for (int i = 0; i < 10; i++) {
			int[] a = new int[10];
			a[0] = Integer.parseInt(br.readLine());
			a[1] = Integer.parseInt(br.readLine());
			startXY[i] = a;
		}

		br.close();
	}

	public void ready() {
		Main.startBGM("gameData\\bgm\\" + mapNumber + "w.wav", true);
	}
}

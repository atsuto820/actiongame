package action.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import action.main.Main;
import action.main.WorldMap;

public class AllGame {
	public Game game;
	public WorldMap worldmap;
	public int worldmapNum;
	public int mapNumber;
	public int state;
	public int[] stage;
	public boolean firstChange;
	public boolean win;
	public int playerState;

	private int remaining;
	private int coin;

	public BufferedImage playerImage;
	public BufferedImage coinImage;
	public static final int GAME = 0;
	public static final int MAP = 1;

	public AllGame() {
		super();
		worldmapNum = 0;
		try {
			worldmap = new WorldMap(worldmapNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mapNumber = 0;
		state = MAP;
		stage = null;
		firstChange = true;
		remaining = 3;
		coin = 0;
		playerState = 0;
		playerImage = Main.mapImage.getSubimage(48, 0, 48, 48);
		coinImage = Main.objectImages[0].getSubimage(336, 240, 48, 48);
	}

	public BufferedImage update() {
		BufferedImage image = new BufferedImage(1008, 720, BufferedImage.TYPE_INT_RGB);
		Graphics gra = image.getGraphics();
		if(firstChange) {
			switch(state) {
			case GAME:
				try {
					if(stage[1] == 10) {
						game = new Game(new Map("gameData/map/ItemHouse.txt"), playerState, stage);
					} else if(stage[1] == 11) {
						game = new Game(new Map("gameData/map/OneUpHouse.txt"), playerState, stage);
					} else {
						game = new Game(new Map("gameData/map/" + stage[0] + "/" + stage[1] + "/0.txt"), playerState, stage);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				stage = null;
				break;
			case MAP:
				if(worldmapNum != worldmap.getMapNumber()) {
					try {
						worldmap = new WorldMap(worldmapNum);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				worldmap.ready();
			}
			firstChange = false;
		}

		switch(state) {
		case MAP:
			if(win) {
				gra.drawImage(worldmap.update(true, playerState), 0, 0, Main.frame.panel);
				win = false;
			} else {
				gra.drawImage(worldmap.update(false, playerState), 0, 0, Main.frame.panel);
			}

			if (stage != null) {
				state = GAME;
				firstChange = true;
			}
			break;
		case GAME:
			gra.drawImage(game.update(), 0, 0, Main.frame.panel);

			int end;
			if((end = game.isEnd()) != -1) {
				if(end == 0) {
					win = true;
					playerState = game.getPlayer().getState();
				} else if(end == 1) {
					remaining--;
					playerState = 0;
				} else {
					worldmapNum++;
					playerState = game.getPlayer().getState();
				}
				game = null;
				state = MAP;
				firstChange = true;
			}
			break;
		}

		gra.setColor(Color.WHITE);
		gra.fillRect(720, 0, 240, 50);
		gra.setColor(Color.BLACK);
		gra.setFont(new Font("Dialog.plain", Font.PLAIN, 30));
		gra.drawImage(playerImage, 720, 0, Main.frame.panel);
		gra.drawImage(coinImage, 840, 0, Main.frame.panel);
		gra.drawString("× " + remaining, 770, 30);
		gra.drawString("× " + coin, 890, 30);
		return image;
	}

	public void OneUp() {
		Main.startSound(9);
		if(remaining != 99) {
			remaining++;
		}
	}

	public void addCoin() {
		coin++;
		if(coin == 100) {
			coin = 0;
			remaining++;
		}
	}
	
	public void gameOver() {
		try {
			worldmap = new WorldMap(worldmapNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mapNumber = 0;
		state = MAP;
		stage = null;
		firstChange = true;
		remaining = 3;
		coin = 0;
		playerState = 0;
	}

	public int getRemaining() {
		return remaining;
	}
}

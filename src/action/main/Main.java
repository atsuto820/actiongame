package action.main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import action.game.AllGame;
import action.window.GameFrame;
import action.window.Keyboard;

public class Main {
	public static GameFrame frame;

	public static BufferedImage titleImage;
	public static BufferedImage[] objectImages;
	public static BufferedImage[] charaImages;
	public static BufferedImage animationImage;
	public static BufferedImage[] backMapImages;
	public static BufferedImage mapImage;
	public static byte[][][] hitRange;
	public static byte[][] mapHit;

	public static HashMap<Integer, Integer> pressedButtons;

	private static int frameCount;
	public static final int FPS = 30;
	public static long passTime;
	public static AllGame game;
	public static int state;

	public static AudioClip bgm;
	public static AudioClip[] sounds;

	public static final int MAIN_MENU = 0;
	public static final int GAME = 1;
	public static final int GAME_OVER = 2;

	public static void main(String[] args) {
		frame = new GameFrame();

		Graphics gra = frame.panel.image.getGraphics();
		Keyboard key = frame.keyboard;
		pressedButtons = new HashMap<Integer, Integer>();


		state = MAIN_MENU;
		boolean firstChange = true;
		int selectMenu = 0;

		passTime = 100;

		while (true) {
			long startTime = System.currentTimeMillis();

			pressedButtons = key.getPressedButtons();

			Integer keyInfo;
			if (firstChange) {
				switch (state) {
				case MAIN_MENU:
					game = null;
					try {
						titleImage = ImageIO.read(new File("gameData/back_img/title.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					startBGM("gameData/bgm/title.wav", true);
					break;
				case GAME:
					endBGM();
					objectImages = new BufferedImage[4];
					for (int i = 0; i < objectImages.length; i++) {
						try {
							objectImages[i] = ImageIO.read(new File("gameData/block_img/" + i + ".png"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					charaImages = new BufferedImage[1];
					for (int i = 0; i < charaImages.length; i++) {
						try {
							charaImages[0] = ImageIO.read(new File("gameData/chara_img/chara.png"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					try {
						animationImage = ImageIO.read(new File("gameData/animation_img/animation.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}

					hitRange = new byte[4][16][16];
					try {
						for (int h = 0; h < hitRange.length; h++) {
							File file = new File("gameData/block_img/hitRange" + h + ".txt");
							BufferedReader br = new BufferedReader(new FileReader(file));
							for (int i = 0; i < 16; i++) {
								String lineStr = br.readLine();
								for (int j = 0; j < 16; j++) {
									hitRange[h][i][j] = (byte) Integer.parseInt(lineStr.substring(j * 3, j * 3 + 2));
								}
							}
							br.close();
						}

					} catch (IOException e) {
						e.printStackTrace();
					}

					backMapImages = new BufferedImage[3];
					for (int i = 0; i < backMapImages.length; i++) {
						char c = (char) (48 + i);
						try {
							backMapImages[i] = ImageIO.read(new File("gameData/map_img/" + c + ".png"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					try {
						mapImage = ImageIO.read(new File("gameData/map_img/mapObj.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}

					mapHit = new byte[16][16];
					try {
						File file = new File("gameData/map_img/mapHit.txt");
						BufferedReader br = new BufferedReader(new FileReader(file));
						for (int i = 0; i < 16; i++) {
							String lineStr = br.readLine();
							for (int j = 0; j < 16; j++) {
								mapHit[i][j] = (byte) Integer.parseInt(lineStr.substring(j * 2, j * 2 + 1), 16);
							}
						}
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					sounds = new AudioClip[12];
					for (int i = 0; i < sounds.length; i++) {
						try {
							sounds[i] = Applet.newAudioClip(new File("gameData/bgm/" + i + "s.wav").toURI().toURL());
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
					}

					game = new AllGame();

					break;

				case GAME_OVER:
					try {
						titleImage = ImageIO.read(new File("gameData/back_img/death.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					startBGM("gameData/bgm/death.wav", true);
					game.gameOver();
					break;
				}

				firstChange = false;
			}

			switch (state) {
			case MAIN_MENU:
				gra.drawImage(titleImage, 0, 0, frame.panel);
				gra.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 60));
				drawStringInCenter(gra, Color.RED, "Square Adventure", 100);
				gra.setColor(Color.BLACK);
				gra.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 30));
				gra.drawString("ゲームスタート", 200, 200);
				gra.drawString("設定", 200, 250);
				gra.drawString("終了", 200, 300);
				gra.drawString("▶", 160, 200 + 50 * selectMenu);

				keyInfo = isKeyPressed(KeyEvent.VK_W);
				if (keyInfo != null && keyInfo == getFrameCount() - 1 && selectMenu > 0) {
					selectMenu--;
				}
				keyInfo = isKeyPressed(KeyEvent.VK_S);
				if (keyInfo != null && keyInfo == getFrameCount() - 1 && selectMenu < 2) {
					selectMenu++;
				}
				keyInfo = isKeyPressed(KeyEvent.VK_SPACE);
				if (keyInfo != null && keyInfo == getFrameCount() - 1) {
					switch (selectMenu) {
					case 0:
						state = GAME;
						firstChange = true;
						break;
					case 1:
						break;
					case 2:
						System.exit(0);
						break;
					}
				}
				break;
			case GAME:
				gra.drawImage(game.update(), 0, 0, frame.panel);
				if(game.getRemaining() == 0) {
					state = GAME_OVER;
					firstChange = true;
				}
				break;
			case GAME_OVER:
				gra.drawImage(titleImage, 0, 0, frame.panel);
				gra.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 60));
				drawStringInCenter(gra, Color.RED, "GAME OVER", 100);
				gra.setColor(Color.WHITE);
				gra.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 30));
				gra.drawString("続ける", 200, 200);
				gra.drawString("メインメニューへ", 200, 250);
				gra.drawString("▶", 160, 200 + 50 * selectMenu);

				keyInfo = isKeyPressed(KeyEvent.VK_W);
				if (keyInfo != null && keyInfo == getFrameCount() - 1 && selectMenu > 0) {
					selectMenu--;
				}
				keyInfo = isKeyPressed(KeyEvent.VK_S);
				if (keyInfo != null && keyInfo == getFrameCount() - 1 && selectMenu < 1) {
					selectMenu++;
				}
				keyInfo = isKeyPressed(KeyEvent.VK_SPACE);
				if (keyInfo != null && keyInfo == getFrameCount() - 1) {
					switch (selectMenu) {
					case 0:
						state = GAME;
						firstChange = true;
						break;
					case 1:
						state = MAIN_MENU;
						selectMenu = 0;
						firstChange = true;
						break;
					}
				}
				break;
			}

			//			gra.setColor(Color.BLUE);
			//			gra.fillRect(0, 670, 150, 40);
			//			String a;
			//			if (passTime < 5) {
			//				a = "極軽";
			//			} else if (passTime < 10) {
			//				a = "超軽";
			//			} else if (passTime < 15) {
			//				a = "軽";
			//			} else if (passTime < 20) {
			//				a = "やや軽";
			//			} else if (passTime < 25) {
			//				a = "普通";
			//			} else if (passTime < 30) {
			//				a = "やや重";
			//			} else if (passTime < 35) {
			//				a = "重";
			//			} else if (passTime < 45) {
			//				a = "超重";
			//			} else {
			//				a = "激重";
			//			}
			//
			//			int gb = (int) (passTime * 4);
			//			if (gb > 255) {
			//				gb = 255;
			//			}
			//			gra.setColor(new Color(255, 255 - gb, 255 - gb));
			//			gra.setFont(new Font("Dialog.plain", Font.PLAIN, 20));
			//			if (passTime * 100 / (1000 / FPS) < 10) {
			//				gra.drawString("0" + passTime * 100 / (1000 / FPS), 0, 700);
			//			} else {
			//				gra.drawString(passTime * 100 / (1000 / FPS) + "", 0, 700);
			//			}
			//			gra.drawString("% : " + a, 35, 700);

			frame.panel.draw();

			try {
				passTime = System.currentTimeMillis() - startTime;
				if (passTime < 1000 / FPS) {
					Thread.sleep(1000 / FPS - passTime);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
			}

			frameCount++;
		}
	}

	public static int getFrameCount() {
		return frameCount;
	}

	public static void drawStringInCenter(Graphics gra, Color color, String str, int y) {
		gra.getFontMetrics();
		FontMetrics fontmetrics = gra.getFontMetrics();
		gra.setColor(color);
		gra.drawString(str, (960 - fontmetrics.stringWidth(str)) / 2, y);
	}

	public static Integer isKeyPressed(int keyCode) {
		if (pressedButtons.containsKey(keyCode)) {
			return pressedButtons.get(keyCode);
		}
		return null;
	}

	public static void startBGM(String path, boolean loop) {
		if(bgm != null) {
			endBGM();
		}
		try {
			bgm = Applet.newAudioClip(new File(path).toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if(loop) {
			bgm.loop();
		} else {
			bgm.play();
		}
	}

	public static void endBGM() {
		bgm.stop();
	}

	public static void startSound(int num) {
//		 ジャンプ コイン 破壊 壁 土管 火 殺 パワーアップ パワーダウン 1up アイテム出現 バネ
		sounds[num].play();
	}
}

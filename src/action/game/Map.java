package action.game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

public class Map {
	private int[][] field;
	private int[][] gotoStages;
	private int back;

	/**
	 * line : 縦の行の数 |||
	 * row : 横の列の数  三
	 */
	private int line;
	private int row;

	private BufferedImage[] backImages;

	public Map(String fileName) throws IOException {
		super();
		mapLoad(fileName);
	}

	public int getLine() {
		return line;
	}

	public int getRow() {
		return row;
	}

	public int getField(int line, int row) {
		try {
			return field[line][row];
		} catch (ArrayIndexOutOfBoundsException e) {
			if(line < 0 || line >= this.row) {
				return 0x000;
			} else {
				if(row < 0) {
					return field[line][0];
				} else {
					return field[line][this.row - 1];
				}
			}
		}
	}

	public Image getBackImage(int number) {
		return backImages[number];
	}

	public int[] getGotoStage(int a) {
		return gotoStages[a];
	}
	
	public int getBack() {
		return back;
	}

	private void mapLoad(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new IIOException("Can't read input file!");
		}
		BufferedReader br = new BufferedReader(new FileReader(file));
		back = br.readLine().charAt(0) - 48;
		backImages = new BufferedImage[2];
		try {
			backImages[0] = ImageIO.read(new File("gameData/back_img/" + back + "a.png"));
			backImages[1] = ImageIO.read(new File("gameData/back_img/" + back + "b.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		line = Integer.parseInt(br.readLine());
		row = Integer.parseInt(br.readLine());
		gotoStages = new int[3][];
		for (int i = 0; i < 3; i++) {
			gotoStages[i] = new int[3];
			gotoStages[i][0] = Integer.parseInt(br.readLine().substring(0, 1));
			gotoStages[i][1] = Integer.parseInt(br.readLine());
			gotoStages[i][2] = Integer.parseInt(br.readLine());
		}

		field = new int[line][row];
		for (int i = 0; i < line; i++) {
			String lineStr = br.readLine();
			for (int j = 0; j < row; j++) {
				try {
					field[i][j] = Integer.parseInt(lineStr.substring(j * 4, j * 4 + 3), 16);
				} catch(NumberFormatException e) {
					field[i][j] = 0x086;
				}
			}
		}

		br.close();
	}
}

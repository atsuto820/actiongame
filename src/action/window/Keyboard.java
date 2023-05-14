package action.window;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import action.main.Main;

public class Keyboard extends KeyAdapter{
	public static HashMap<Integer, Integer> pressedButtons = new HashMap<Integer, Integer>();

	public Keyboard() {
		super();
	}

	private Integer isKeyPressed(int keyCode) {
		if(pressedButtons.containsKey(keyCode)) {
			return pressedButtons.get(keyCode);
		}
		return null;
	}


	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);

		if(this.isKeyPressed(e.getKeyCode()) == null) {
			pressedButtons.put(e.getKeyCode(), Main.getFrameCount());
		}
	}

	public void keyReleased(KeyEvent e) {
		super.keyReleased(e);

		pressedButtons.remove(e.getKeyCode());
	}

	public HashMap<Integer, Integer> getPressedButtons(){
		return pressedButtons;
	}
}

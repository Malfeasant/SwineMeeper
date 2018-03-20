package us.malfeasant.swinemeeper;

import java.util.EnumMap;
import java.util.Map;

import javafx.scene.control.Button;

/**
 * A Cell possibly holds a mine, keeps track of its neighbors.  Contains a button.
 * @author mischa
 *
 */

public class Cell {
	private final Map<Direction, Cell> buttonMap = new EnumMap<>(Direction.class);
	private final Button btn = new Button(" ");
	private boolean isMine = false;
	
	Cell() {
		btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	}
	void setMine() {
		isMine = true;
	}
	void setNeighbor(Direction d, Cell c) {
		buttonMap.put(d, c);
		c.buttonMap.put(d.getOpposite(), this);
	}
	Button getButton() {
		return btn;
	}
}
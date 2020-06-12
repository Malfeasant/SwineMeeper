package us.malfeasant.swinemeeper;

import java.util.EnumMap;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * A Cell possibly holds a mine, keeps track of its neighbors.  Contains a button.
 * @author mischa
 *
 */

public class Cell {
	private static String SPACE = "\u2003";	// fat space
	private static String FLAG = "\u2691";	// 1f6a9?
	private static String WRONG = "X";
	private static String BOMB = "*";	// 1f4a3?
	
	private final Map<Direction, Cell> buttonMap = new EnumMap<>(Direction.class);
	private final Button btn = new Button(SPACE);
	private boolean isMine = false;
	private boolean isFlag = false;
	
	Cell() {
		btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btn.setOnMouseClicked(e -> action(e));
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
	
	void action(MouseEvent e) {
		// TODO: signal the gameboard to start timer if this is first click
		switch (e.getButton()) {
		case MIDDLE:	// don't think we care...
			break;
		case NONE:	// shouldn't happen...
			break;
		case PRIMARY:
			if (!isFlag) {	// flag suppresses a click
				if (isMine) {
					btn.setStyle("-fx-background-color: red");
					System.out.println("Boom");	// TODO: end the game
				} else {
					int neighborMines = checkNeighbors();
					if (neighborMines == 0) {
						// TODO: recursively propagate click to all neighbors, avoiding endless loop
					} else {
						btn.setText("" + neighborMines);
					}
				}
			}
			break;
		case SECONDARY:
			isFlag = !isFlag;
			btn.setText(isFlag ? FLAG : SPACE);
			// TODO: update the mine counter
			break;
		default:
			break;
		
		}
	}
	
	int checkNeighbors() {
		int count = 0;
		for (Cell c : buttonMap.values()) {
			if (c.isMine) count++;
		}
		return count;
	}
	
	void endGame() {
		if (isFlag & !isMine) {
			btn.setText(WRONG);
		}
		if (!isFlag & isMine) {
			btn.setText(BOMB);
		}
	}
}

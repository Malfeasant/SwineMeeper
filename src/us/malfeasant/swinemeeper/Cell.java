package us.malfeasant.swinemeeper;

import java.util.EnumMap;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * A Cell possibly holds a mine, keeps track of its neighbors.  Contains a button.
 * @author Malfeasant
 *
 */

public class Cell {
	private static String SPACE = "\u2003";	// fat space
	private static String FLAG = "\u2691";	// 1f6a9?
	private static String WRONG = "X";
	private static String BOMB = "*";	// 1f4a3?
	
	private static String[] COLORS = {
			"purple", "royalblue", "seagreen", "chocolate", "orange", "orangered", "red", "orchid"
	};
	private final Map<Direction, Cell> neighborMap = new EnumMap<>(Direction.class);
	private final Button btn = new Button(SPACE);
	private boolean isMine = false;
	private boolean isFlag = false;
	
	private final Launcher game;	// need to have a reference to notify of clicks and
	
	Cell(Launcher g) {
		game = g;
		btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btn.setOnMouseClicked(e -> action(e));
	}
	void setMine() {
		isMine = true;
	}
	void setNeighbor(Direction d, Cell c) {
		neighborMap.put(d, c);
		c.neighborMap.put(d.getOpposite(), this);
	}
	Button getButton() {
		return btn;
	}
	void action(MouseEvent e) {
		action(e.getButton());
	}
	void action(MouseButton b) {
		switch (b) {
		case MIDDLE:	// don't think we care...
			break;
		case NONE:	// shouldn't happen...
			break;
		case PRIMARY:
			if (!isFlag) {	// flag suppresses a click
				if (isMine) {
					btn.setStyle("-fx-background-color: red");
					game.click(MineAction.KABOOM);
				} else {
					btn.setDisable(true);
					game.click(MineAction.UNCOVER);
					int neighborMines = checkNeighbors();
					if (neighborMines == 0) {
						clickNeighbors();
					} else {
						btn.setText("" + neighborMines);
						btn.setStyle("-fx-text-fill: " + COLORS[neighborMines - 1]);
					}
				}
			}
			break;
		case SECONDARY:
			isFlag = !isFlag;
			btn.setText(isFlag ? FLAG : SPACE);
			game.click(isFlag ? MineAction.MARK : MineAction.UNMARK);
			break;
		}
	}
	
	int checkNeighbors() {
		return (int) neighborMap.values().stream().filter(c -> c.isMine).count();
	}
	
	void clickNeighbors() {	// gets called on each cell for which checkNeighbors() returns 0
		//		neighborMap.values().stream().filter(c -> !c.btn.isDisabled()).forEach(c -> c.btn.fire());
		for (Cell c : neighborMap.values()) {
			if (!c.btn.isDisabled()) {	// avoids the endless loop?
				c.action(MouseButton.PRIMARY);	// can't use fire() method since that fires an Action not a MouseEvent...
			}
		}
	}
	
	void endGame(boolean won) {	// losing the game freezes the mine counter- win reduces it to 0
		// also win or lose the mines are revealed but with flags for a win, bombs for a loss
		btn.setOnMouseClicked(null);	// is there a better way to disallow clicking without changing its appearance?
		if (isFlag & !isMine) {
			btn.setText(WRONG);
		}
		if (!isFlag & isMine) {
			if (won) {
				game.click(MineAction.MARK);
			}
			btn.setText(won ? FLAG : BOMB);
		}
	}
}

package us.malfeasant.swinemeeper;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;

public class MenuBuilder {
	/**
	 * @param game so buttons can call methods on it
	 * @return a populated MenuBar
	 */
	public static MenuBar build(GameBoard game) {
		Menu gameMenu = new Menu("Game");
		Difficulty diff = Persist.loadDifficulty();	// needed so we can set selected on the proper item
		ToggleGroup group = new ToggleGroup();
		
		RadioMenuItem easy = new RadioMenuItem("Easy");
		easy.setToggleGroup(group);
		easy.setSelected(diff == Difficulty.EASY);
		easy.setOnAction(e -> game.setDifficulty(Difficulty.EASY));
		
		RadioMenuItem med = new RadioMenuItem("Medium");
		med.setToggleGroup(group);
		med.setSelected(diff == Difficulty.MEDIUM);
		med.setOnAction(e -> game.setDifficulty(Difficulty.MEDIUM));
		
		RadioMenuItem hard = new RadioMenuItem("Hard");
		hard.setToggleGroup(group);
		hard.setSelected(diff == Difficulty.HARD);
		hard.setOnAction(e -> game.setDifficulty(Difficulty.HARD));
		
		RadioMenuItem cust = new RadioMenuItem("Custom...");
		cust.setToggleGroup(group);
		cust.setSelected(diff == Difficulty.CUSTOM);
		cust.setOnAction(e -> game.setDifficulty(Difficulty.CUSTOM));
		
		MenuItem best = new MenuItem("Best Times...");
		best.setOnAction(e -> BestDialog.showBest());
		
		gameMenu.getItems().addAll(easy, med, hard, cust, new SeparatorMenuItem(), best);
		
		MenuBar bar = new MenuBar();
		bar.getMenus().add(gameMenu);
		return bar;
	}
}

package us.malfeasant.swinemeeper;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public class MenuBuilder {
	/**
	 * Pass in the game board so buttons can call methods on it
	 * @return a populated MenuBar
	 */
	public static MenuBar build(Launcher game) {
		Menu gameMenu = new Menu("Game");
		
		ToggleGroup group = new ToggleGroup();
		
		RadioMenuItem easy = new RadioMenuItem("Easy");
		easy.setToggleGroup(group);
		easy.setOnAction(e -> game.setDifficulty(Difficulty.EASY));
		
		RadioMenuItem med = new RadioMenuItem("Medium");
		med.setToggleGroup(group);
		med.setOnAction(e -> game.setDifficulty(Difficulty.MEDIUM));
		
		RadioMenuItem hard = new RadioMenuItem("Hard");
		hard.setToggleGroup(group);
		hard.setOnAction(e -> game.setDifficulty(Difficulty.HARD));
		
		RadioMenuItem cust = new RadioMenuItem("Custom...");
		cust.setToggleGroup(group);
		cust.setOnAction(e -> game.setDifficulty(Difficulty.CUSTOM));
		
		gameMenu.getItems().addAll(easy, med, hard, cust);
		
		MenuBar bar = new MenuBar();
		bar.getMenus().add(gameMenu);
		return bar;
	}
}

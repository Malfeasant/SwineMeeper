package us.malfeasant.swinemeeper;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public class MenuBuilder {
	/**
	 * Pass in the group in case we later want to add separate buttons- also a convenient place for a listener
	 * @param group the ToggleGroup these items will belong to
	 * @return a populated MenuBar
	 */
	public static MenuBar build(ToggleGroup group) {
		Menu gameMenu = new Menu("Game");
		
		RadioMenuItem easy = new RadioMenuItem("Easy");
		easy.setToggleGroup(group);
		easy.setUserData(Difficulty.EASY);
		RadioMenuItem med = new RadioMenuItem("Medium");
		med.setToggleGroup(group);
		med.setUserData(Difficulty.MEDIUM);
		RadioMenuItem hard = new RadioMenuItem("Hard");
		hard.setToggleGroup(group);
		hard.setUserData(Difficulty.HARD);
		RadioMenuItem cust = new RadioMenuItem("Custom...");
		cust.setToggleGroup(group);
		cust.setUserData(Difficulty.CUSTOM);
		
		gameMenu.getItems().addAll(easy, med, hard, cust);
		
		MenuBar bar = new MenuBar();
		bar.getMenus().add(gameMenu);
		return bar;
	}
}

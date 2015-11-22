package us.malfeasant.swinemeeper;

import java.util.prefs.Preferences;

public enum Difficulty {
	EASY(8, 8, 10), MEDIUM(16, 16, 40), HARD(40, 20, 99), CUSTOM(0, 0, 0) {
		@Override
		public int getHeight() {
			return prefs.getInt("CustomHeight", 5);
		}
		@Override
		public int getWidth() {
			return prefs.getInt("CustomWidth", 5);
		}
		@Override
		public int getMines() {
			return prefs.getInt("CustomMines", 5);
		}
	};
	
	private static Preferences prefs = Preferences.userNodeForPackage(Difficulty.class);
	
	private final int width;
	private final int height;
	private final int mines;
	
	private Difficulty(int w, int h, int m) {
		width = w;
		height = h;
		mines = m;
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getMines() {
		return mines;
	}
	
	public static Difficulty loadDifficulty() {
		return Difficulty.valueOf(prefs.get(Difficulty.class.getSimpleName(), Difficulty.EASY.name()));
	}
	public static void storeDifficulty(Difficulty d) {
		prefs.put(Difficulty.class.getSimpleName(), d.name());
	}
	public static void setCustom(int width, int height, int mines) {
		prefs.putInt("CustomWidth", width);
		prefs.putInt("CustomHeight", height);
		prefs.putInt("CustomMines", mines);
	}
}

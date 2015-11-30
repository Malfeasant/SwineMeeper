package us.malfeasant.swinemeeper;

public enum Difficulty {
	EASY(8, 8, 10), MEDIUM(16, 16, 40), HARD(30, 16, 99), CUSTOM(0, 0, 0) {
		@Override
		public int getWidth() {
			return Persist.getCustomWidth();
		}
		@Override
		public int getHeight() {
			return Persist.getCustomHeight();
		}
		@Override
		public int getMines() {
			return Persist.getCustomMines();
		}
	};
	
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
	
}

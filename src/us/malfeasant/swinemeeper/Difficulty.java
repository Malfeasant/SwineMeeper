package us.malfeasant.swinemeeper;

public enum Difficulty {
	EASY(8, 8, 10), MEDIUM(16, 16, 40), HARD(30, 16, 99), CUSTOM(0, 0, 0) {
		@Override
		public Triple getTriple() {
			return new Triple(Persist.getWidth(), Persist.getHeight(), Persist.getMines());
		}
	};
	
	private final Triple triple;
	
	private Difficulty(int w, int h, int m) {
		triple = new Triple(w, h, m);
	}
	
	public Triple getTriple() {
		return triple;
	}
}

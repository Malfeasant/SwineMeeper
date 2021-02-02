package us.malfeasant.swinemeeper;

import java.util.Optional;

public enum Difficulty {
	EASY(8, 8, 10), MEDIUM(16, 16, 40), HARD(30, 16, 99), CUSTOM(0, 0, 0) {
		@Override
		public Optional<Triple> getTriple() {
			return Optional.empty();
		}
	};
	
	private final Triple triple;
	
	private Difficulty(int w, int h, int m) {
		triple = new Triple(w, h, m);
	}
	
	public Optional<Triple> getTriple() {
		return Optional.of(triple);
	}
}

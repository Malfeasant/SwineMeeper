package us.malfeasant.swinemeeper;

public enum Direction {
	EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST, NORTH, NORTHEAST;
	
	public Direction getOpposite() {
		return values()[(ordinal() + 8) & 7];
	}
}

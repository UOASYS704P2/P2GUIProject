package localization_GUI.org.compsys704.p2.entity;

public class Position {
	
	private int x;
	private static final double DIVIDE = 3.55d;
	private int y;
	private int orientation;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	

	public int getOrientation() {
		return orientation;
	}

	public Position(double x, double y, int orientation) {
		this.x = (int) (x/DIVIDE);
		this.y = (int) (535 - y/DIVIDE);
		this.orientation = -orientation - 90;
	}
}

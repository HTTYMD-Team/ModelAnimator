package math;

public class Vector2i {
	public final int x, y;

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return ("(" + x + "|" + y + ")");
	}
}

package math;

import java.io.Serializable;

public class Vector3i implements Serializable {
	private static final long serialVersionUID = 1L;

	public final int x, y, z;

	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return ("(" + x + "|" + y + "|" + z + ")");
	}

	public Vector3i setX(int value) {
		return new Vector3i(value, y, z);
	}

	public Vector3i setY(int value) {
		return new Vector3i(x, value, z);
	}

	public Vector3i setZ(int value) {
		return new Vector3i(x, y, value);
	}
}

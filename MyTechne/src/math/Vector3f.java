package math;

import java.io.Serializable;

public class Vector3f implements Serializable {
	private static final long serialVersionUID = 1L;

	public final float x, y, z;

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f add(Vector3f v) {
		return new Vector3f(x + v.x, y + v.y, z + v.z);
	}

	@Override
	public String toString() {
		return ("(" + x + "|" + y + "|" + z + ")");
	}

	public Vector3f setX(float value) {
		return new Vector3f(value, y, z);
	}

	public Vector3f setY(float value) {
		return new Vector3f(x, value, z);
	}

	public Vector3f setZ(float value) {
		return new Vector3f(x, y, value);
	}
}

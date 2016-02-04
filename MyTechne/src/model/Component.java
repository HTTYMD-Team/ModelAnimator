package model;

import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.Serializable;

import math.Vector3f;
import math.Vector3i;

import org.json.JSONObject;

import animation.Animation;
import animation.AnimationHandler;

public abstract class Component implements Serializable {
	private static final long serialVersionUID = 1L;

	protected boolean selected = false;
	protected boolean marked = false;

	protected Vector3f rotationPoint = new Vector3f(0, 0, 0);
	protected Vector3i rotation = new Vector3i(0, 0, 0);

	protected String name;

	public Component(String name) {
		this.name = name;
	}

	public Component(JSONObject json) {
		name = json.getString("name");
		JSONObject rotationPointObj = json.getJSONObject("rotationPoint");
		rotationPoint = new Vector3f((float) rotationPointObj.getDouble("x"), (float) rotationPointObj.getDouble("y"), (float) rotationPointObj.getDouble("z"));
	}

	public void select() {
		selected = true;
	}

	public void unselect() {
		selected = false;
	}

	public void mark() {
		marked = true;
	}

	public void unmark() {
		marked = false;
	}

	public void render(AnimationHandler animationHandler, Animation selectedAnimation, float texH, float texW) {
		// XXX maybe change parameters to data array
		float[] data = null;

		if (animationHandler.isActive()) {
			data = animationHandler.getData(this);
		}
		if (data == null) {
			if (selectedAnimation != null)
				data = selectedAnimation.getCurrentFrame().getData(this);
		}
		if (data == null) {
			data = new float[] { rotationPoint.x, rotationPoint.y, rotationPoint.z, rotation.x, rotation.y, rotation.z };
		}

		glTranslatef(data[0], data[1], data[2]);
		glRotatef(data[5], 0, 0, 1);
		glRotatef(data[4], 0, 1, 0);
		glRotatef(data[3], 1, 0, 0);

		if (selected) {
			glDisable(GL_LIGHTING);
			glDisable(GL_LIGHT0);
			glDisable(GL_TEXTURE_2D);

			glBegin(GL_LINES);
			glColor3f(1, 0, 0);
			glVertex3f(-20, 0, 0);
			glVertex3f(20, 0, 0);
			glColor3f(0, 1, 0);
			glVertex3f(0, -20, 0);
			glVertex3f(0, 20, 0);
			glColor3f(0, 0, 1);
			glVertex3f(0, 0, -20);
			glVertex3f(0, 0, 20);
			glEnd();

			glEnable(GL_LIGHT0);
			glEnable(GL_LIGHTING);
			glEnable(GL_TEXTURE_2D);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector3f getRotationPoint() {
		return rotationPoint;
	}

	public void setRotationPoint(Vector3f rotationPoint) {
		this.rotationPoint = rotationPoint;
	}

	public Vector3i getRotation() {
		return rotation;
	}

	public void setRotation(Vector3i rotation) {
		this.rotation = rotation;
	}

	public abstract String toJavaString(ModelHelper modelHelper);

	public float[] getData() {
		return new float[] { rotationPoint.x, rotationPoint.y, rotationPoint.z, rotation.x, rotation.y, rotation.z };
	}
}

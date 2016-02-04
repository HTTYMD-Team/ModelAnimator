package model;

import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import math.Vector3f;
import math.Vector3i;

import org.json.JSONObject;
import org.lwjgl.opengl.GL11;

import animation.Animation;
import animation.AnimationHandler;

public class Cube extends Component {
	private static final long serialVersionUID = 1L;

	private Vector3i size = new Vector3i(1, 1, 1);
	private Vector3f offset = new Vector3f(0, 0, 0);
	private Vector3i textureOffset = new Vector3i(0, 0, 0);

	public Cube(String name) {
		super(name);
	}

	public Cube(JSONObject json) {
		super(json);
		JSONObject size = json.getJSONObject("size");
		this.size = new Vector3i(size.getInt("x"), size.getInt("y"), size.getInt("z"));

		JSONObject rotation = json.getJSONObject("rotation");
		this.rotation = new Vector3i(rotation.getInt("_x"), rotation.getInt("_y"), rotation.getInt("_z"));

		JSONObject offset = json.getJSONObject("offset");
		this.offset = new Vector3f(offset.getInt("x"), offset.getInt("y"), offset.getInt("z"));

		JSONObject textureOffset = json.getJSONObject("textureOffset");
		this.textureOffset = new Vector3i(textureOffset.getInt("x"), textureOffset.getInt("y"), textureOffset.getInt("z"));
	}

	@Override
	public void render(AnimationHandler animationHandler, Animation selectedAnimation, float texH, float texW) {
		super.render(animationHandler, selectedAnimation, texH, texW);

		glTranslatef(offset.x, offset.y, offset.z);
		glScalef(size.x, size.y, size.z);

		glBegin(GL_QUADS);

		float x1 = textureOffset.x / texW;
		float x2 = (textureOffset.x + size.z) / texW;
		float x3 = (textureOffset.x + size.z + size.x) / texW;
		float x4 = (textureOffset.x + size.z + 2 * size.x) / texW;
		float x5 = (textureOffset.x + 2 * size.z + size.x) / texW;
		float x6 = (textureOffset.x + 2 * size.z + 2 * size.x) / texW;

		float y1 = textureOffset.y / texH;
		float y2 = (textureOffset.y + size.z) / texH;
		float y3 = (textureOffset.y + size.z + size.y) / texH;

		// Front
		GL11.glNormal3f(0, 0, -1);
		GL11.glTexCoord2f(x2, y2);
		glVertex3f(0, 0, 0);
		GL11.glTexCoord2f(x2, y3);
		glVertex3f(0, 1, 0);
		GL11.glTexCoord2f(x3, y3);
		glVertex3f(1, 1, 0);
		GL11.glTexCoord2f(x3, y2);
		glVertex3f(1, 0, 0);

		// Back
		GL11.glNormal3f(0, 0, 1);
		GL11.glTexCoord2f(x5, y2);
		glVertex3f(1, 0, 1);
		GL11.glTexCoord2f(x5, y3);
		glVertex3f(1, 1, 1);
		GL11.glTexCoord2f(x6, y3);
		glVertex3f(0, 1, 1);
		GL11.glTexCoord2f(x6, y2);
		glVertex3f(0, 0, 1);

		// Top
		GL11.glNormal3f(0, -1, 0);
		GL11.glTexCoord2f(x2, y1);
		glVertex3f(0, 0, 1);
		GL11.glTexCoord2f(x2, y2);
		glVertex3f(0, 0, 0);
		GL11.glTexCoord2f(x3, y2);
		glVertex3f(1, 0, 0);
		GL11.glTexCoord2f(x3, y1);
		glVertex3f(1, 0, 1);

		// Bottom - Techne
		GL11.glNormal3f(0, 1, 0);
		GL11.glTexCoord2f(x3, y2);
		glVertex3f(0, 1, 0);
		GL11.glTexCoord2f(x3, y1);
		glVertex3f(0, 1, 1);
		GL11.glTexCoord2f(x4, y1);
		glVertex3f(1, 1, 1);
		GL11.glTexCoord2f(x4, y2);
		glVertex3f(1, 1, 0);

		// Left (camera-wise)
		GL11.glNormal3f(-1, 0, 0);
		GL11.glTexCoord2f(x1, y2);
		glVertex3f(0, 0, 1);
		GL11.glTexCoord2f(x1, y3);
		glVertex3f(0, 1, 1);
		GL11.glTexCoord2f(x2, y3);
		glVertex3f(0, 1, 0);
		GL11.glTexCoord2f(x2, y2);
		glVertex3f(0, 0, 0);

		// Right (camera-wise)
		GL11.glNormal3f(1, 0, 0);
		GL11.glTexCoord2f(x3, y2);
		glVertex3f(1, 0, 0);
		GL11.glTexCoord2f(x3, y3);
		glVertex3f(1, 1, 0);
		GL11.glTexCoord2f(x5, y3);
		glVertex3f(1, 1, 1);
		GL11.glTexCoord2f(x5, y2);
		glVertex3f(1, 0, 1);

		glEnd();

		if (marked) {
			glColor3f(1, 1, 1);

			glDisable(GL_LIGHTING);
			glDisable(GL_LIGHT0);
			glDisable(GL_TEXTURE_2D);

			glBegin(GL_LINES);
			glVertex3f(0, 0, 0);
			glVertex3f(1, 0, 0);
			glVertex3f(0, 0, 0);
			glVertex3f(0, 1, 0);
			glVertex3f(0, 0, 0);
			glVertex3f(0, 0, 1);

			glVertex3f(1, 0, 0);
			glVertex3f(1, 1, 0);
			glVertex3f(1, 0, 0);
			glVertex3f(1, 0, 1);
			glVertex3f(0, 1, 0);
			glVertex3f(1, 1, 0);
			glVertex3f(0, 1, 0);
			glVertex3f(0, 1, 1);
			glVertex3f(0, 0, 1);
			glVertex3f(1, 0, 1);
			glVertex3f(0, 0, 1);
			glVertex3f(0, 1, 1);

			glVertex3f(1, 1, 0);
			glVertex3f(1, 1, 1);
			glVertex3f(1, 0, 1);
			glVertex3f(1, 1, 1);
			glVertex3f(0, 1, 1);
			glVertex3f(1, 1, 1);
			glEnd();

			glEnable(GL_LIGHT0);
			glEnable(GL_LIGHTING);
			glEnable(GL_TEXTURE_2D);
		}
	}

	public Vector3i getSize() {
		return size;
	}

	public void setSize(Vector3i size) {
		this.size = size;
	}

	public Vector3f getOffset() {
		return offset;
	}

	public void setOffset(Vector3f offset) {
		this.offset = offset;
	}

	public Vector3i getTextureOffset() {
		return textureOffset;
	}

	public void setTextureOffset(Vector3i textureOffset) {
		this.textureOffset = textureOffset;
	}

	@Override
	public String toJavaString(ModelHelper modelHelper) {
		String java = name + " = new " + (modelHelper.isWing(this) ? "Wing" : "Model") + "Renderer(model, " + textureOffset.x + ", " + textureOffset.y + ");\n";
		java += name + ".addBox(" + offset.x + "F, " + offset.y + "F, " + offset.z + "F, " + size.x + ", " + size.y + ", " + size.z + ");\n";
		java += name + ".setRotationPoint(" + rotationPoint.x + "F, " + rotationPoint.y + "F, " + rotationPoint.z + "F);\n";
		java += name + ".setTextureSize(textureWidth, textureHeight);\n";
		java += name + ".mirror = true;\n";
		java += "setRotation(" + name + ", " + (float) rotation.x / 180 * Math.PI + "F, " + (float) rotation.y / 180 * Math.PI + "F, " + (float) rotation.z / 180 * Math.PI + "F);\n";
		return java;
	}
}

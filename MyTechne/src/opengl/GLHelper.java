package opengl;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import math.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GLHelper {
	private static FloatBuffer lightPosition;

	// Camera settings

	private static int pitch = 0;
	private static int rotation = 0;
	private static Vector3f pos = new Vector3f(0, 0, -100);

	private static long lastFPS;

	private static long updateFPS() {
		long ms = System.currentTimeMillis() - lastFPS;
		ms = ms == 0 ? 1 : ms;
		Display.setTitle("ms: " + ms + " FPS: " + 1000 / ms);
		lastFPS += ms;
		return ms;
	}

	public static void update() {
		float sensitivity = updateFPS() / 1000f * 60f;

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Display.destroy();
			System.exit(0);
		}

		if (Mouse.isButtonDown(2) || Mouse.isButtonDown(1)) {
			rotation += Mouse.getDX();
			pitch -= Mouse.getDY();
			pitch = Math.min(Math.max(pitch, -90), 90);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			pos = pos.add(new Vector3f((float) -Math.sin((float) rotation / 180 * Math.PI) * sensitivity, 0, (float) Math.cos((float) rotation / 180 * Math.PI) * sensitivity));
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			pos = pos.add(new Vector3f((float) Math.sin((float) rotation / 180 * Math.PI) * sensitivity, 0, (float) -Math.cos((float) rotation / 180 * Math.PI) * sensitivity));
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			pos = pos.add(new Vector3f((float) Math.cos((float) rotation / 180 * Math.PI) * sensitivity, 0, (float) Math.sin((float) rotation / 180 * Math.PI) * sensitivity));
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			pos = pos.add(new Vector3f((float) -Math.cos((float) rotation / 180 * Math.PI) * sensitivity, 0, (float) -Math.sin((float) rotation / 180 * Math.PI) * sensitivity));
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			pos = pos.add(new Vector3f(0, sensitivity, 0));
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			pos = pos.add(new Vector3f(0, -sensitivity, 0));

		lightPosition = BufferUtils.createFloatBuffer(4).put(new float[] { -pos.x, pos.y, pos.z, 1 });
		lightPosition.rewind();
	}

	/**
	 * Renders the Scene.
	 */
	public static void preRender() {
		glClearColor(1, 1, 1, 1);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//		glLight(GL_LIGHT0, GL_POSITION, lightPosition);
		glLoadIdentity();

		glRotatef(pitch, 1, 0, 0);
		glRotatef(rotation, 0, 1, 0);
		glTranslatef(pos.x, pos.y, pos.z);

		glRotatef(180, 1, 0, 0);

		float f = 1.0f;
		glColor3f(f, f, f);

		glNormal3f(0, 1, 0);

		glDisable(GL_TEXTURE_2D);
		glEnable(GL_POLYGON_SMOOTH);

		glBegin(GL_QUADS);
		for (int x = -100; x <= 100; x += 2) {
			glVertex3f(x - 0.02f, 24, -100);
			glVertex3f(x + 0.02f, 24, -100);
			glVertex3f(x + 0.02f, 24, 100);
			glVertex3f(x - 0.02f, 24, 100);
		}
		for (int y = -100; y <= 100; y += 2) {
			glVertex3f(-100, 24, y + 0.02f);
			glVertex3f(-100, 24, y - 0.02f);
			glVertex3f(100, 24, y - 0.02f);
			glVertex3f(100, 24, y + 0.02f);
		}
		glEnd();

		glEnable(GL_TEXTURE_2D);
		glDisable(GL_POLYGON_SMOOTH);
	}

	/**
	 * Initializes OpenGL.
	 */
	public static void initGL() {
		// Camera
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		glFrustum(-0.001, 0.001, -0.001 / aspectRatio, 0.001 / aspectRatio, 0.1, 100000);
		glMatrixMode(GL_MODELVIEW);

		// General
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);

		// Transparency
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glAlphaFunc(GL_GREATER, 0.0000001f);
		glEnable(GL_ALPHA_TEST);

		// Light
		FloatBuffer matSpecular = BufferUtils.createFloatBuffer(4).put(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		FloatBuffer matShininess = BufferUtils.createFloatBuffer(4).put(new float[] { 50.0f });
		FloatBuffer matAmbient = BufferUtils.createFloatBuffer(4).put(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		FloatBuffer matDiffuse = BufferUtils.createFloatBuffer(4).put(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });

		lightPosition = BufferUtils.createFloatBuffer(4).put(new float[] { 10, 10, 10, 1 });
		FloatBuffer lightAmbient = BufferUtils.createFloatBuffer(4).put(new float[] { 1, 1, 1, 1 });
		FloatBuffer lightDiffuse = BufferUtils.createFloatBuffer(4).put(new float[] { 1, 1, 1, 1 });
		FloatBuffer lightSpecular = BufferUtils.createFloatBuffer(4).put(new float[] { 1, 1, 1, 1 });

		FloatBuffer lightConstantAttentuation = BufferUtils.createFloatBuffer(4).put(new float[] { 1.0000f });
		FloatBuffer lightLinearAttentuation = BufferUtils.createFloatBuffer(4).put(new float[] { 0.001f });
		FloatBuffer lightQuadraticAttentuation = BufferUtils.createFloatBuffer(4).put(new float[] { 0.0000f });

		matSpecular.rewind();
		matShininess.rewind();
		matAmbient.rewind();
		matDiffuse.rewind();

		lightPosition.rewind();
		lightAmbient.rewind();
		lightDiffuse.rewind();
		lightSpecular.rewind();

		lightConstantAttentuation.rewind();
		lightLinearAttentuation.rewind();
		lightQuadraticAttentuation.rewind();

		glEnable(GL_LIGHTING);
		glEnable(GL_NORMALIZE);
		glEnable(GL_LIGHT0);

		glLight(GL_LIGHT0, GL_CONSTANT_ATTENUATION, lightConstantAttentuation);
		glLight(GL_LIGHT0, GL_LINEAR_ATTENUATION, lightLinearAttentuation);
		glLight(GL_LIGHT0, GL_QUADRATIC_ATTENUATION, lightQuadraticAttentuation);

		glLight(GL_LIGHT0, GL_AMBIENT, lightAmbient);
		glLight(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
		glLight(GL_LIGHT0, GL_SPECULAR, lightSpecular);
	}

	public static void drawQuad() {
		glBegin(GL_QUADS);

		GL11.glNormal3f(0, 0, -1);
		glVertex3f(0, 0, 0);
		glVertex3f(0, 1, 0);
		glVertex3f(1, 1, 0);
		glVertex3f(1, 0, 0);

		GL11.glNormal3f(0, 0, 1);
		glVertex3f(0, 0, 1);
		glVertex3f(1, 0, 1);
		glVertex3f(1, 1, 1);
		glVertex3f(0, 1, 1);

		GL11.glNormal3f(0, -1, 0);
		glVertex3f(0, 0, 0);
		glVertex3f(1, 0, 0);
		glVertex3f(1, 0, 1);
		glVertex3f(0, 0, 1);

		GL11.glNormal3f(0, 1, 0);
		glVertex3f(0, 1, 0);
		glVertex3f(0, 1, 1);
		glVertex3f(1, 1, 1);
		glVertex3f(1, 1, 0);

		GL11.glNormal3f(-1, 0, 0);
		glVertex3f(0, 0, 0);
		glVertex3f(0, 0, 1);
		glVertex3f(0, 1, 1);
		glVertex3f(0, 1, 0);

		GL11.glNormal3f(1, 0, 0);
		glVertex3f(1, 0, 0);
		glVertex3f(1, 1, 0);
		glVertex3f(1, 1, 1);
		glVertex3f(1, 0, 1);

		glEnd();
	}

	public static void drawTetraeder() {
		glBegin(GL_TRIANGLES);

		// upper
		GL11.glNormal3f(1, 1, 1);
		glVertex3f(1, 0, 0);
		glVertex3f(0, 0, 1);
		glVertex3f(0, 1, 0);

		GL11.glNormal3f(-1, 1, 1);
		glVertex3f(-1, 0, 0);
		glVertex3f(0, 1, 0);
		glVertex3f(0, 0, 1);

		GL11.glNormal3f(-1, 1, -1);
		glVertex3f(-1, 0, 0);
		glVertex3f(0, 0, -1);
		glVertex3f(0, 1, 0);

		GL11.glNormal3f(1, 1, -1);
		glVertex3f(1, 0, 0);
		glVertex3f(0, 1, 0);
		glVertex3f(0, 0, -1);

		// lower
		GL11.glNormal3f(1, -1, 1);
		glVertex3f(1, 0, 0);
		glVertex3f(0, -1, 0);
		glVertex3f(0, 0, 1);

		GL11.glNormal3f(-1, -1, 1);
		glVertex3f(-1, 0, 0);
		glVertex3f(0, 0, 1);
		glVertex3f(0, -1, 0);

		GL11.glNormal3f(-1, -1, -1);
		glVertex3f(-1, 0, 0);
		glVertex3f(0, -1, 0);
		glVertex3f(0, 0, -1);

		GL11.glNormal3f(1, -1, -1);
		glVertex3f(1, 0, 0);
		glVertex3f(0, 0, -1);
		glVertex3f(0, -1, 0);

		glEnd();
	}
}
package opengl;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;

import java.awt.Canvas;
import java.awt.Dimension;
import java.io.File;

import main.Controller;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class MyCanvas extends Canvas {
	private static final long serialVersionUID = 1L;

	private final Controller controller;
	private final File texture;
	private Thread thread;
	private MyCanvas thiz = this;

	public MyCanvas(Controller controller, File texture) {
		super();
		this.controller = controller;
		this.texture = texture;
		setIgnoreRepaint(true);
		setPreferredSize(new Dimension(500, 500));
	}

	private void startLWJGL() {
		thread = new Thread() {
			@Override
			public void run() {
				// Create display
				try {
					Display.setDisplayMode(new DisplayMode(100, 100));
					Display.setResizable(true);
					Display.setParent(thiz);
					Display.create();

					// Init Texture
					controller.textureHandler.load(texture);

				} catch (LWJGLException lwjgle) {
					lwjgle.printStackTrace();
					System.exit(0);
				}

				// Initialize GL
				GLHelper.initGL();

				// Game loop
				while (!Display.isCloseRequested()) {
					Display.sync(60);
					thiz.setSize(thiz.getParent().getWidth(), thiz.getParent().getHeight());
					glMatrixMode(GL_PROJECTION);
					glLoadIdentity();
					float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
					glFrustum(-0.01, 0.01, -0.01 / aspectRatio, 0.01 / aspectRatio, 0.01, 100000);
					GL11.glViewport(0, 0, thiz.getWidth(), thiz.getHeight());
					glMatrixMode(GL_MODELVIEW);

					controller.animationHandler.animate();
					GLHelper.update();
					GLHelper.preRender();
					// XXX holy shit
					controller.techneTree.getModel().render(controller.animationHandler, controller.getSelectedAnimation(), controller.textureHandler.getHeight(),
							controller.textureHandler.getWidth(), controller.modelHelper);
					Display.update();
				}

				// Destroy window
				Display.destroy();
				System.exit(0);
			}
		};
		thread.start();
	}

	private void stopLWJGL() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public final void addNotify() {
		super.addNotify();
		startLWJGL();
	}

	@Override
	public final void removeNotify() {
		stopLWJGL();
		super.removeNotify();
	}
}
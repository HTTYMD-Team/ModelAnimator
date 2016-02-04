package animation;

import model.Component;

public class AnimationHandler {

	private Animation animation;
	private int step;

	private long lastTime;

	private boolean active;

	public void animate() {
		if (!active)
			return;

		long time = System.currentTimeMillis();
		step += (time - lastTime);
		lastTime = time;
	}

	public void stop() {
		active = false;
		step = 0;
	}

	public void play() {
		active = !active;
		lastTime = System.currentTimeMillis();
	}

	public void previousFrame() {
		int mpf = animation.getMillisPerFrame();
		step = (step / mpf) * mpf;
	}

	public void previousStep() {
		step--;
	}

	public void nextStep() {
		step++;
	}

	public void nextFrame() {
		int mpf = animation.getMillisPerFrame();
		step = (step / mpf) * (mpf + 1);
	}

	public float[] getData(Component component) {
		if (!animation.containsComponent(component))
			return null;

		float time = (float) step % animation.getDuration() / animation.getMillisPerFrame();
		float[] data = new float[6];
		for (int attribute = 0; attribute < 6; attribute++)
			data[attribute] = animation.getValue(component, attribute, time);
		return data;
	}

	public boolean isActive() {
		return active;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
		step = 0;
		if (animation == null)
			active = false;
	}
}

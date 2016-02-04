package animation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.Component;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Animation implements Serializable {
	private static final long serialVersionUID = 1L;

	private final List<Component> components = new ArrayList<>();
	private final List<Frame> frames = new ArrayList<>();

	private String name;
	private int duration = 1000;

	private int lastShownIndex = 0;

	private transient PolynomialSplineFunction[][] psfs;

	public Animation(String name) {
		this.name = name;
	}

	// Serialization
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		calculateSplines();
	}

	public void addComponent(Component component) {
		if (components.contains(component))
			return;

		components.add(component);

		calculateSplines();
	}

	public void addFrame(Frame frame, int index) {
		if (index == -1)
			frames.add(frame);
		else
			// frames.add(index, frame);
			throw new NotImplementedException();

		lastShownIndex = frames.indexOf(frame);

		calculateSplines();
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getFrameCount() {
		return frames.size();
	}

	public int getMillisPerFrame() {
		return duration / frames.size();
	}

	// XXX following does not really belong here
	// XXX hm.
	public void previousFrame() {
		lastShownIndex--;
		if (lastShownIndex < 0)
			lastShownIndex += frames.size();
	}

	public Frame getCurrentFrame() {
		return frames.get(lastShownIndex);
	}

	public int getCurrentFrameIndex() {
		return lastShownIndex;
	}

	public void nextFrame() {
		lastShownIndex++;
		if (lastShownIndex >= frames.size())
			lastShownIndex -= frames.size();
	}

	public void set(Component component, int attributeId, float value) {
		frames.get(lastShownIndex).set(component, attributeId, value);
		calculateSplines();
	}

	private void calculateSplines() {
		psfs = new PolynomialSplineFunction[components.size()][6];

		SplineInterpolator si = new SplineInterpolator();

		double[] xArr = new double[frames.size() + 5];
		for (int i = 0; i < frames.size() + 5; i++)
			xArr[i] = i - 2;

		for (int component = 0; component < components.size(); component++) {
			double[][] yArr = new double[6][frames.size() + 5];
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < frames.size() + 5; j++) {
					float[] data = frames.get((j + frames.size() - 2) % frames.size()).getData(components.get(component));
					if (data == null)
						data = components.get(component).getData();
					yArr[i][j] = data[i];
				}

				psfs[component][i] = si.interpolate(xArr, yArr[i]);
			}
		}
	}

	public float getValue(Component component, int attribute, double time) {
		return (float) psfs[components.indexOf(component)][attribute].value(time);
	}

	public boolean containsComponent(Component component) {
		return components.contains(component);
	}

	public String toJavaString() {
		String java = "";
		java += "{\n";
		java += "	ModelRenderer[] components = { ";
		for (Component component : components)
			java += component.getName() + ", ";
		java += "};\n";
		java += "int duration = " + duration + ";\n";
		java += "\n";
		java += "float[][][] data = {\n";
		for (Component component : components) {
			java += "{";
			for (int attributeId = 0; attributeId < 6; attributeId++) {
				java += "{";
				for (Frame frame : frames) {
					float[] data = frame.getData(component);
					if (data != null)
						java += data[attributeId] + "f, ";
					else
						java += component.getData()[attributeId] + "f, ";
				}
				java += "},";
			}
			java += "},\n";
		}
		java += "};\n";
		java += "\n";
		java += name + " = new Animation(components, data, duration);\n";
		java += "}\n";

		return java;
	}
}

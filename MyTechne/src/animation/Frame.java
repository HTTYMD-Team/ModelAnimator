package animation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import model.Component;

public class Frame implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Map<Component, float[]> components = new HashMap<Component, float[]>();
	private final Animation animation;

	public Frame(Animation animation) {
		this.animation = animation;
	}

	public Frame(Animation animation, Frame frame) {
		this(animation);
		for (Entry<Component, float[]> entry : frame.components.entrySet())
			this.components.put(entry.getKey(), entry.getValue().clone());
	}

	public void set(Component component, int attributeId, float value) {
		if (!components.containsKey(component)) {
			components.put(component, new float[] { component.getRotationPoint().x, component.getRotationPoint().y, component.getRotationPoint().z, component.getRotation().x,
					component.getRotation().y, component.getRotation().z });
			animation.addComponent(component);
		}

		float[] data = components.get(component);
		data[attributeId] = value;
	}

	public float[] getData(Component component) {
		return components.get(component);
	}
}

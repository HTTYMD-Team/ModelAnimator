package model;

import org.json.JSONObject;

public class Container extends Component {
	private static final long serialVersionUID = 1L;

	// TODO useless class right now
	// XXX except for toJavaString (maybe)
	public Container(String name) {
		super(name);
	}

	public Container(JSONObject json) {
		super(json);
	}

	@Override
	public String toJavaString(ModelHelper modelHelper) {
		String java = name + " = new " + (modelHelper.isWing(this) ? "Wing" : "Model") + "Renderer(model);\n";
		java += name + ".setRotationPoint(" + rotationPoint.x + "F, " + rotationPoint.y + "F, " + rotationPoint.z + "F);\n";
		return java;
	}
}

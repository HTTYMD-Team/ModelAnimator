package tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import model.Component;
import model.Container;
import model.Cube;
import model.ModelHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.opengl.GL11;

import animation.Animation;
import animation.AnimationHandler;

public class TechneTreeNode implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<TechneTreeNode> children = new ArrayList<TechneTreeNode>();
	private final Component component;

	public TechneTreeNode(Component component) {
		this.component = component;
	}

	public TechneTreeNode(JSONObject jObj) {
		if (jObj.has("version")) {
			// root obj
			component = new Container(jObj);
			children.add(new TechneTreeNode(jObj.getJSONObject("head")));
			children.add(new TechneTreeNode(jObj.getJSONObject("body")));
			children.add(new TechneTreeNode(jObj.getJSONObject("legs")));
			children.add(new TechneTreeNode(jObj.getJSONObject("wings")));
		} else if (!jObj.has("children")) {
			component = new Cube(jObj);
		} else {
			component = new Container(jObj);

			JSONArray childrenArr = jObj.getJSONArray("children");
			for (int i = 0; i < childrenArr.length(); i++)
				children.add(new TechneTreeNode(childrenArr.getJSONObject(i)));
		}
	}

	@SuppressWarnings("unchecked")
	public List<TechneTreeNode> getChildren() {
		return (List<TechneTreeNode>) children.clone();
	}

	public int getChildCount() {
		return children.size();
	}

	public TechneTreeNode getChildAt(int index) {
		return children.get(index);
	}

	public boolean addChild(TechneTreeNode child) {
		return children.add(child);
	}

	public void insertChild(TechneTreeNode child, int index) {
		children.add(index, child);
	}

	public boolean removeChild(TechneTreeNode child) {
		return children.remove(child);
	}

	public boolean isFolder() {
		return component instanceof Container;
	}

	public int getIndexAt(TechneTreeNode child) {
		return children.indexOf(child);
	}

	@Override
	public String toString() {
		return component.getName();
	}

	public void setName(String name) {
		component.setName(name);
	}

	public void render(AnimationHandler animationHandler, Animation selectedAnimation, float texH, float texW, ModelHelper modelHelper) {
		GL11.glPushMatrix();
		component.render(animationHandler, selectedAnimation, texH, texW);

		for (TechneTreeNode child : children) {
			if (modelHelper.isWing(child.getComponent())) {
				GL11.glPushMatrix();
				child.render(animationHandler, selectedAnimation, texH, texW, modelHelper);
				GL11.glScalef(-1, 1, 1);
				GL11.glCullFace(GL11.GL_FRONT);
				child.render(animationHandler, selectedAnimation, texH, texW, modelHelper);
				GL11.glScalef(-1, 1, 1);
				GL11.glCullFace(GL11.GL_BACK);
				GL11.glPopMatrix();
			} else {
				child.render(animationHandler, selectedAnimation, texH, texW, modelHelper);
			}
		}
		GL11.glPopMatrix();
	}

	public void select() {
		component.select();
		mark();
	}

	public void unselect() {
		component.unselect();
		unmark();
	}

	private void mark() {
		component.mark();

		for (TechneTreeNode child : children)
			child.mark();
	}

	private void unmark() {
		component.unmark();

		for (TechneTreeNode child : children)
			child.unmark();
	}

	public Component getComponent() {
		return component;
	}

	public String toJavaStringVars() {
		String java = "public final ModelRenderer " + component.getName() + ";\n";
		for (TechneTreeNode child : children)
			java += child.toJavaStringVars();
		return java;
	}

	public String toJavaString(ModelHelper modelHelper) {
		String java = component.toJavaString(modelHelper);

		if (children.size() != 0) {
			java += "{";
			for (TechneTreeNode child : children) {
				java += "\n";
				java += child.toJavaString(modelHelper);
				java += component.getName() + ".addChild(" + child.component.getName() + ");\n";
			}
			java += "}\n";
		}

		return java;
	}

	public void showPopupMenu(TechneTree techneTree, int x, int y, final ModelHelper modelHelper) {
		JPopupMenu menu = new JPopupMenu();

		JMenuItem head = new JMenuItem();
		if (modelHelper.isHead(component)) {
			head.setText("unmark as head");
			head.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					modelHelper.setHead(null);
				}
			});
		} else {
			head.setText("mark as head");
			head.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					modelHelper.setHead(component);
				}
			});
		}
		menu.add(head);

		JMenuItem leg = new JMenuItem();
		if (modelHelper.isLeg(component)) {
			leg.setText("unmark as leg");
			leg.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					modelHelper.removeLeg(component);
				}
			});
		} else {
			leg.setText("mark as leg");
			leg.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					modelHelper.setLeg(component);
				}
			});
		}
		menu.add(leg);

		JMenuItem wing = new JMenuItem();
		if (modelHelper.isWing(component)) {
			wing.setText("unmark as wing");
			wing.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					modelHelper.setWing(null);
				}
			});
		} else {
			wing.setText("mark as wing");
			wing.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					modelHelper.setWing(component);
				}
			});
		}
		menu.add(wing);

		menu.show(techneTree, x, y);
	}
}

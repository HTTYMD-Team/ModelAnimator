package tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import model.ModelHelper;

import org.json.JSONObject;

import animation.Animation;
import animation.AnimationHandler;

public class TechneTreeModel implements TreeModel {

	private final TechneTreeNode root;

	private final List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	public TechneTreeModel(TechneTreeNode root) {
		this.root = root;
	}

	public TechneTreeModel(JSONObject jObj) {
		root = new TechneTreeNode(jObj);
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(l);
	}

	@Override
	public TechneTreeNode getChild(Object parent, int index) {
		if (parent instanceof TechneTreeNode)
			return ((TechneTreeNode) parent).getChildAt(index);
		else
			throw new IllegalArgumentException();
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof TechneTreeNode)
			return ((TechneTreeNode) parent).getChildCount();
		else
			throw new IllegalArgumentException();
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent instanceof TechneTreeNode && child instanceof TechneTreeNode)
			return ((TechneTreeNode) parent).getIndexAt((TechneTreeNode) child);
		else
			throw new IllegalArgumentException();
	}

	@Override
	public TechneTreeNode getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(Object node) {
		if (root instanceof TechneTreeNode)
			return !((TechneTreeNode) node).isFolder();
		else
			throw new IllegalArgumentException();
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		((TechneTreeNode) path.getLastPathComponent()).setName(newValue.toString());

		for (TreeModelListener listener : listeners)
			listener.treeNodesChanged(new TreeModelEvent(this, path));
	}

	public void treeStructureChanged(TreePath path) {
		for (TreeModelListener listener : listeners)
			listener.treeStructureChanged(new TreeModelEvent(this, path));
	}

	public void render(AnimationHandler animationHandler, Animation selectedAnimation, float texH, float texW, ModelHelper modelHelper) {
		root.render(animationHandler, selectedAnimation, texH, texW, modelHelper);
	}
}

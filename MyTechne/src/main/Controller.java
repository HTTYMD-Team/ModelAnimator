package main;

import io.Export;
import io.Import;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreePath;

import model.Component;
import model.Container;
import model.Cube;
import model.ModelHelper;
import opengl.MyCanvas;
import opengl.TextureHandler;
import tree.TechneTree;
import tree.TechneTreeModel;
import tree.TechneTreeNode;
import animation.Animation;
import animation.AnimationHandler;
import animation.Frame;
import display.Display;

public class Controller {
	// Handler etc
	public final AnimationHandler animationHandler = new AnimationHandler();
	public final TextureHandler textureHandler = new TextureHandler();

	public final ModelHelper modelHelper;
	public final TechneTree techneTree;
	public final Display display;

	private Cube selectedCube;
	private Component selectedComponent;
	private Animation selectedAnimation;
	private boolean updating;

	public Controller(File model, File texture) {
		String extension = model.getName().substring(model.getName().lastIndexOf('.') + 1);

		switch (extension) {
		case "xml":
			techneTree = new TechneTree(this, Import.importXml(model));
			modelHelper = new ModelHelper();
			display = new Display(this, techneTree, new MyCanvas(this, texture));
			break;
		case "json":
			techneTree = new TechneTree(this, Import.importJson(model));
			modelHelper = new ModelHelper();
			display = new Display(this, techneTree, new MyCanvas(this, texture));
			break;
		case "ser":
			Object[] oArr = (Object[]) Import.unserialize(model);

			techneTree = new TechneTree(this, new TechneTreeModel((TechneTreeNode) oArr[0]));

			@SuppressWarnings("unchecked")
			List<Animation> animations = (List<Animation>) oArr[1];

			modelHelper = ((ModelHelper) oArr[2]);

			display = new Display(this, techneTree, new MyCanvas(this, texture));

			for (Animation animation : animations)
				display.cbAnimation.addItem(animation);

			break;
		default:
			throw new RuntimeException("file extension " + extension + " not supported");
		}
	}

	public void addQuad() {
		// TODO Auto-generated method stub
	}

	public void addFolder() {
		if (selectedCube != null)
			return;

		TechneTreeNode selectedNode = (TechneTreeNode) techneTree.getSelectionPath().getLastPathComponent();
		selectedNode.addChild(new TechneTreeNode(new Container("new Folder")));
		techneTree.getModel().treeStructureChanged(techneTree.getSelectionPath().getParentPath());
	}

	public void extractToFolder() {
		// TODO Auto-generated method stub
	}

	public void remove() {
		TreePath path = techneTree.getSelectionPath();
		((TechneTreeNode) path.getParentPath().getLastPathComponent()).removeChild(((TechneTreeNode) path.getLastPathComponent()));
		techneTree.getModel().treeStructureChanged(path.getParentPath());
		showComponent(((TechneTreeNode) path.getParentPath().getLastPathComponent()).getComponent());
	}

	public void exportJson() {
		TechneTreeNode root = techneTree.getModel().getRoot();

		List<Animation> animations = new ArrayList<Animation>();
		for (int i = 1; i < display.cbAnimation.getItemCount(); i++)
			animations.add(display.cbAnimation.getItemAt(i));

		Object[] oArr = { root, animations, modelHelper };

		Export.serialize(oArr);
	}

	public void exportJava() {
		if (modelHelper.getHead() == null)
			System.err.println("no head specified");
		if (modelHelper.getWing() == null)
			System.err.println("no wing specified");
		if (modelHelper.getLegs().size() == 0)
			System.err.println("no legs specified");
		if (modelHelper.getTexH() == 0)
			System.err.println("no horizontal texture size specified");
		if (modelHelper.getTexV() == 0)
			System.err.println("no vertical texture size specified");
		if (modelHelper.getIdle() == null)
			System.err.println("no idle animation specified");
		if (modelHelper.getFlying() == null)
			System.err.println("no flying animation specified");

		List<Animation> animations = new ArrayList<Animation>();
		for (int i = 1; i < display.cbAnimation.getItemCount(); i++)
			animations.add(display.cbAnimation.getItemAt(i));
		Export.exportJava(techneTree.getModel().getRoot(), animations, modelHelper);
	}

	public void setSizeX(int value) {
		if (!updating)
			if (selectedCube != null)
				selectedCube.setSize(selectedCube.getSize().setX(value));
	}

	public void setSizeY(int value) {
		if (!updating)
			if (selectedCube != null)
				selectedCube.setSize(selectedCube.getSize().setY(value));
	}

	public void setSizeZ(int value) {
		if (!updating)
			if (selectedCube != null)
				selectedCube.setSize(selectedCube.getSize().setZ(value));
	}

	public void setRotationPointX(float value) {
		if (!updating) {
			if (selectedAnimation == null) {
				if (display.cbxMoveRP.isSelected()) {
					if (selectedCube != null) {
						selectedCube.setOffset(selectedCube.getOffset().setX(selectedCube.getOffset().x + selectedComponent.getRotationPoint().x - value));
					} else {
						TechneTreeNode selectedNode = (TechneTreeNode) techneTree.getSelectionPath().getLastPathComponent();
						for (TechneTreeNode child : selectedNode.getChildren()) {
							child.getComponent().setRotationPoint(
									child.getComponent().getRotationPoint().setX(child.getComponent().getRotationPoint().x + selectedComponent.getRotationPoint().x - value));
						}
					}
				}
				selectedComponent.setRotationPoint(selectedComponent.getRotationPoint().setX(value));
			} else
				selectedAnimation.set(selectedComponent, 0, value);
			updateAttributes();
		}
	}

	public void setRotationPointY(float value) {
		if (!updating) {
			if (selectedAnimation == null) {
				if (display.cbxMoveRP.isSelected()) {
					if (selectedCube != null) {
						selectedCube.setOffset(selectedCube.getOffset().setY(selectedCube.getOffset().y + selectedComponent.getRotationPoint().y - value));
					} else {
						TechneTreeNode selectedNode = (TechneTreeNode) techneTree.getSelectionPath().getLastPathComponent();
						for (TechneTreeNode child : selectedNode.getChildren()) {
							child.getComponent().setRotationPoint(
									child.getComponent().getRotationPoint().setY(child.getComponent().getRotationPoint().y + selectedComponent.getRotationPoint().y - value));
						}
					}
				}
				selectedComponent.setRotationPoint(selectedComponent.getRotationPoint().setY(value));
			} else
				selectedAnimation.set(selectedComponent, 1, value);
			updateAttributes();
		}
	}

	public void setRotationPointZ(float value) {
		if (!updating) {
			if (selectedAnimation == null) {
				if (display.cbxMoveRP.isSelected()) {
					if (selectedCube != null) {
						selectedCube.setOffset(selectedCube.getOffset().setZ(selectedCube.getOffset().z + selectedComponent.getRotationPoint().z - value));
					} else {
						TechneTreeNode selectedNode = (TechneTreeNode) techneTree.getSelectionPath().getLastPathComponent();
						for (TechneTreeNode child : selectedNode.getChildren()) {
							child.getComponent().setRotationPoint(
									child.getComponent().getRotationPoint().setZ(child.getComponent().getRotationPoint().z + selectedComponent.getRotationPoint().z - value));
						}
					}
				}
				selectedComponent.setRotationPoint(selectedComponent.getRotationPoint().setZ(value));
			} else
				selectedAnimation.set(selectedComponent, 2, value);
			updateAttributes();
		}
	}

	public void setRotationX(int value) {
		if (!updating)
			if (selectedAnimation == null)
				selectedComponent.setRotation(selectedComponent.getRotation().setX(value));
			else
				selectedAnimation.set(selectedComponent, 3, value);
	}

	public void setRotationY(int value) {
		if (!updating)
			if (selectedAnimation == null)
				selectedComponent.setRotation(selectedComponent.getRotation().setY(value));
			else
				selectedAnimation.set(selectedComponent, 4, value);
	}

	public void setRotationZ(int value) {
		if (!updating)
			if (selectedAnimation == null)
				selectedComponent.setRotation(selectedComponent.getRotation().setZ(value));
			else
				selectedAnimation.set(selectedComponent, 5, value);
	}

	public void setOffsetX(float value) {
		if (!updating)
			if (selectedCube != null)
				selectedCube.setOffset(selectedCube.getOffset().setX(value));
	}

	public void setOffsetY(float value) {
		if (!updating)
			if (selectedCube != null)
				selectedCube.setOffset(selectedCube.getOffset().setY(value));
	}

	public void setOffsetZ(float value) {
		if (!updating)
			if (selectedCube != null)
				selectedCube.setOffset(selectedCube.getOffset().setZ(value));
	}

	public void setTextureOffsetX(int value) {
		if (!updating)
			if (selectedCube != null)
				selectedCube.setTextureOffset(selectedCube.getTextureOffset().setX(value));
	}

	public void setTextureOffsetY(int value) {
		if (!updating)
			if (selectedCube != null)
				selectedCube.setTextureOffset(selectedCube.getTextureOffset().setY(value));
	}

	public void showComponent(Component component) {
		selectedComponent = component;
		selectedCube = component instanceof Container ? null : (Cube) component;

		updateAttributes();
	}

	private void setContainerEnabled() {
		setSizeEnabled(false);
		setRotationPointEnabled(true);
		setRotationEnabled(true);
		setOffsetEnabled(false);
		setTextureOffsetEnabled(false);
	}

	private void setAllEnabled(boolean b) {
		setSizeEnabled(b);
		setRotationPointEnabled(b);
		setRotationEnabled(b);
		setOffsetEnabled(b);
		setTextureOffsetEnabled(b);
	}

	private void setSizeEnabled(boolean b) {
		display.sSizeX.setEnabled(b);
		display.sSizeY.setEnabled(b);
		display.sSizeZ.setEnabled(b);
	}

	private void setRotationPointEnabled(boolean b) {
		display.sRotationPointX.setEnabled(b);
		display.sRotationPointY.setEnabled(b);
		display.sRotationPointZ.setEnabled(b);
	}

	private void setRotationEnabled(boolean b) {
		display.sRotationX.setEnabled(b);
		display.sRotationY.setEnabled(b);
		display.sRotationZ.setEnabled(b);
	}

	private void setOffsetEnabled(boolean b) {
		display.sOffsetX.setEnabled(b);
		display.sOffsetY.setEnabled(b);
		display.sOffsetZ.setEnabled(b);
	}

	private void setTextureOffsetEnabled(boolean b) {
		display.sTextureOffsetX.setEnabled(b);
		display.sTextureOffsetY.setEnabled(b);
	}

	public void addFrame() {
		selectedAnimation.addFrame(new Frame(selectedAnimation, selectedAnimation.getCurrentFrame()), -1);
		updateFrameLabel();
		updateAttributes();
	}

	public void addAnimation() {
		Animation animation = new Animation("new animation");
		animation.addFrame(new Frame(animation), -1);
		display.cbAnimation.addItem(animation);
		display.cbAnimation.setSelectedItem(animation);
	}

	public void showAnimation(Object object) {
		selectedAnimation = (Animation) object;
		animationHandler.setAnimation(selectedAnimation);
		if (selectedAnimation != null) {
			display.tfName.setText(selectedAnimation.getName());
			display.tfName.setEnabled(true);
			display.sDuration.setValue(selectedAnimation.getDuration());
			display.sDuration.setEnabled(true);
			display.bAddFrame.setEnabled(true);
			display.bPreviousFrame.setEnabled(true);
			display.bNextFrame.setEnabled(true);

			updateFrameLabel();
		} else {
			display.tfName.setText("");
			display.tfName.setEnabled(false);
			display.sDuration.setValue(0);
			display.sDuration.setEnabled(false);
			display.lFrame.setText("0/0");
			display.bAddFrame.setEnabled(false);
			display.bPreviousFrame.setEnabled(false);
			display.bNextFrame.setEnabled(false);
		}
		updateAttributes();
	}

	private void updateFrameLabel() {
		display.lFrame.setText((selectedAnimation.getCurrentFrameIndex() + 1) + "/" + selectedAnimation.getFrameCount());
	}

	private void updateAttributes() {
		updating = true;
		if (selectedAnimation != null) {
			float[] data = selectedAnimation.getCurrentFrame().getData(selectedComponent);
			if (data != null) {
				display.sRotationPointX.setValue(data[0]);
				display.sRotationPointY.setValue(data[1]);
				display.sRotationPointZ.setValue(data[2]);

				display.sRotationX.setValue((int) data[3]);
				display.sRotationY.setValue((int) data[4]);
				display.sRotationZ.setValue((int) data[5]);
				updating = false;
				return;
			}
		}
		display.sRotationPointX.setValue(selectedComponent.getRotationPoint().x);
		display.sRotationPointY.setValue(selectedComponent.getRotationPoint().y);
		display.sRotationPointZ.setValue(selectedComponent.getRotationPoint().z);

		display.sRotationX.setValue(selectedComponent.getRotation().x);
		display.sRotationY.setValue(selectedComponent.getRotation().y);
		display.sRotationZ.setValue(selectedComponent.getRotation().z);

		if (selectedCube == null) {
			setContainerEnabled();

			display.sSizeX.setValue(0);
			display.sSizeY.setValue(0);
			display.sSizeZ.setValue(0);

			display.sOffsetX.setValue(0f);
			display.sOffsetY.setValue(0f);
			display.sOffsetZ.setValue(0f);

			display.sTextureOffsetX.setValue(0);
			display.sTextureOffsetY.setValue(0);
		} else {
			setAllEnabled(true);

			display.sSizeX.setValue(selectedCube.getSize().x);
			display.sSizeY.setValue(selectedCube.getSize().y);
			display.sSizeZ.setValue(selectedCube.getSize().z);

			display.sOffsetX.setValue(selectedCube.getOffset().x);
			display.sOffsetY.setValue(selectedCube.getOffset().y);
			display.sOffsetZ.setValue(selectedCube.getOffset().z);

			display.sTextureOffsetX.setValue(selectedCube.getTextureOffset().x);
			display.sTextureOffsetY.setValue(selectedCube.getTextureOffset().y);
		}
		updating = false;
	}

	public void changeDuration(int duration) {
		if (selectedAnimation != null)
			selectedAnimation.setDuration(duration);
	}

	public void changeName(String name) {
		if (selectedAnimation != null) {
			selectedAnimation.setName(name);
			display.cbAnimation.repaint();
		}
	}

	public void previousFrame() {
		if (selectedAnimation == null)
			return;
		selectedAnimation.previousFrame();
		updateFrameLabel();
		updateAttributes();
	}

	public void nextFrame() {
		if (selectedAnimation == null)
			return;
		selectedAnimation.nextFrame();
		updateFrameLabel();
		updateAttributes();
	}

	public Animation getSelectedAnimation() {
		return selectedAnimation;
	}

	public void animStop() {
		animationHandler.stop();
	}

	public void animPlay() {
		animationHandler.play();
	}

	public void animPreviousFrame() {
		animationHandler.previousFrame();
	}

	public void animPreviousStep() {
		animationHandler.previousStep();
	}

	public void animNextStep() {
		animationHandler.nextStep();
	}

	public void animNextFrame() {
		animationHandler.nextFrame();
	}

	public void setTextureSize() {
		try {
			System.out.println("enter horizontal texture size:");
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String texH = bufferRead.readLine();
			modelHelper.setTexH(Integer.parseInt(texH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			System.out.println("enter vertical texture size:");
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String texV = bufferRead.readLine();
			modelHelper.setTexV(Integer.parseInt(texV));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

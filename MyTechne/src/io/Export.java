package io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import model.Component;
import model.ModelHelper;
import tree.TechneTreeNode;
import animation.Animation;

public class Export {
	public static void exportJava(TechneTreeNode root, List<Animation> animations, ModelHelper modelHelper) {
		String java = "";

		java += "package com.httymd.client.model;\n";
		java += "\n";
		java += "import java.util.ArrayList;\n";
		java += "import java.util.List;\n";
		java += "\n";
		java += "import com.httymd.client.animation.Animation;\n";
		java += "\n";
		java += "import net.minecraft.client.model.ModelRenderer;\n";
		java += "\n";
		java += "public class Model" + root.getComponent().getName() + " extends ModelDragonNew {\n";
		java += "\n";
		java += root.toJavaStringVars();
		java += "\n";
		for (Animation animation : animations)
			java += "public final Animation " + animation.getName() + ";\n";
		java += "\n";
		java += "public Model" + root.getComponent().getName() + "() {\n";
		java += "		textureWidth = " + modelHelper.getTexH() + ";\n";
		java += "		textureHeight = " + modelHelper.getTexV() + ";\n";
		java += "\n";
		java += root.toJavaString(modelHelper);
		for (Animation animation : animations)
			java += animation.toJavaString();
		java += "}\n";
		java += "\n";
		{
			java += "@Override\n";
			java += "public ModelRenderer getRoot() {\n";
			java += "	return " + root.getComponent().getName() + ";\n";
			java += "}\n";
		}
		{
			java += "@Override\n";
			java += "public ModelRenderer getHead() {\n";
			java += "	return " + (modelHelper.getHead() == null ? "null" : modelHelper.getHead().getName()) + ";\n";
			java += "}\n";
		}
		{
			java += "@Override\n";
			java += "public List<ModelRenderer> getLegs() {\n";
			java += "	List<ModelRenderer> legs = new ArrayList<ModelRenderer>();\n";
			for (Component leg : modelHelper.getLegs())
				java += "	legs.add(" + leg.getName() + ");\n";
			java += "	return legs;\n";
			java += "}\n";
		}
		{
			java += "@Override\n";
			java += "public ModelRenderer getWing() {\n";
			java += "	return " + (modelHelper.getWing() == null ? "null" : modelHelper.getWing().getName()) + ";\n";
			java += "}\n";
		}
		{
			java += "@Override\n";
			java += "public Animation getIdle() {\n";
			java += "	return " + (modelHelper.getIdle() == null ? "null" : modelHelper.getIdle().getName()) + ";\n";
			java += "}\n";
		}
		{
			java += "@Override\n";
			java += "public Animation getFlying() {\n";
			java += "	return " + (modelHelper.getFlying() == null ? "null" : modelHelper.getFlying().getName()) + ";\n";
			java += "}\n";
		}

		java += "}";

		System.out.println(java);
	}

	public static void serialize(Object o) {
		try {
			FileOutputStream fileOut = new FileOutputStream("F:////Downloads//test.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(o);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in F://Downloads/test.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
}

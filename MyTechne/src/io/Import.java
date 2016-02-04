package io;

import static io.DocumentHelper.get;
import static io.DocumentHelper.getList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import math.Vector3f;
import math.Vector3i;
import model.Container;
import model.Cube;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tree.TechneTreeModel;
import tree.TechneTreeNode;

public class Import {
	public static TechneTreeModel importXml(File file) {
		Document doc = null;

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			doc = db.parse(file);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return importDocument(doc);
	}

	@SuppressWarnings("unused")
	private static TechneTreeModel importDocument(Document doc) {
		doc.getDocumentElement().normalize();

		Node techne = doc.getElementsByTagName("Techne").item(0);

		Node modelTag = get(techne, "Models", "Model");

		Node name = get(modelTag, "Name");
		Node textureSize = get(modelTag, "TextureSize");

		NodeList shapes = getList(modelTag, "Geometry", "Shape");

		TechneTreeNode root = new TechneTreeNode(new Container(name.getTextContent()));
		TechneTreeNode body = new TechneTreeNode(new Container("body"));
		root.addChild(body);

		for (int i = 0; i < shapes.getLength(); i++) {
			Node shape = shapes.item(i);

			// XXX maybe put this into class TechneTreeNode and call TechneTreeNode(shape);

			String shapeName = shape.getAttributes().getNamedItem("name").getTextContent();
			String[] size = get(shape, "Size").getTextContent().split(",");
			String[] rotationPoint = get(shape, "Position").getTextContent().split(",");
			String[] rotation = get(shape, "Rotation").getTextContent().split(",");
			String[] offset = get(shape, "Offset").getTextContent().split(",");
			String[] textureOffset = get(shape, "TextureOffset").getTextContent().split(",");

			Cube cube = new Cube(shapeName);
			cube.setSize(new Vector3i(Integer.valueOf(size[0]), Integer.valueOf(size[1]), Integer.valueOf(size[2])));
			cube.setRotationPoint(new Vector3f(Float.valueOf(rotationPoint[0]), Float.valueOf(rotationPoint[1]), Float.valueOf(rotationPoint[2])));
			cube.setRotation(new Vector3i(Integer.valueOf(rotation[0]), Integer.valueOf(rotation[1]), Integer.valueOf(rotation[2])));
			cube.setOffset(new Vector3f(Float.valueOf(offset[0]), Float.valueOf(offset[1]), Float.valueOf(offset[2])));
			cube.setTextureOffset(new Vector3i(Integer.valueOf(textureOffset[0]), Integer.valueOf(textureOffset[1]), 0));

			TechneTreeNode node = new TechneTreeNode(cube);
			body.addChild(node);
		}

		return new TechneTreeModel(root);
	}

	public static Object unserialize(File file) {
		Object o = null;
		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			o = in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return o;
	}

	public static TechneTreeModel importJson(File file) {
		JSONObject jObj;

		try {
			byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
			jObj = new JSONObject(new String(encoded));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return new TechneTreeModel(jObj);
	}

}

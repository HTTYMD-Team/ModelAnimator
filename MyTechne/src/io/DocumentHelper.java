package io;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DocumentHelper {

	// TODO maybe remove single Argument methods

	public static Node get(Node node, String tagName) {
		return getList(node, tagName).item(0);
	}

	public static Node get(Node node, String... tagNames) {
		return getList(node, tagNames).item(0);
	}

	public static NodeList getList(Node node, String tagName) {
		return ((Element) node).getElementsByTagName(tagName);
	}

	public static NodeList getList(Node node, String... tagNames) {
		return getList(node, tagNames, 0);
	}

	private static NodeList getList(Node node, String[] tagNames, int index) {
		if (index + 1 == tagNames.length)
			return getList(node, tagNames[index]);
		else
			return getList(get(node, tagNames[index]), tagNames, index + 1);
	}
}

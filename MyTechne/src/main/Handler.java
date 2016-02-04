package main;

import java.io.File;

import javax.swing.JFileChooser;

public class Handler {
	// enable SerializableUID warning

	public static void main(String[] args) {
		JFileChooser jfc = new JFileChooser("F:\\Dropbox\\HTTYMD\\models\\Dragons\\");

		jfc.showOpenDialog(null);
		File model = jfc.getSelectedFile();
		if (model == null)
			return;

		jfc.showOpenDialog(null);
		File texture = jfc.getSelectedFile();
		if (texture == null)
			return;
		// File file = new File("F:\\Dropbox\\HTTYMD\\models\\Dragons\\Night Fury\\NightFuryMultiWing.json");
		// File file = new File("F:////Downloads//test.ser");
		// File file = new File("F:\\Dropbox\\HTTYMD\\models\\Dragons\\Night Fury\\model.xml");
		new Controller(model, texture);
	}
}

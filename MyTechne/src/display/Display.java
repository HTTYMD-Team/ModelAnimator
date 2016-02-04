package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.Controller;
import opengl.MyCanvas;
import tree.TechneTree;
import animation.Animation;

public class Display extends JFrame {
	private static final long serialVersionUID = 1L;

	public final JSpinner sSizeX;
	public final JSpinner sSizeY;
	public final JSpinner sSizeZ;

	public final JSpinner sRotationPointX;
	public final JSpinner sRotationPointY;
	public final JSpinner sRotationPointZ;

	public final JSpinner sRotationX;
	public final JSpinner sRotationY;
	public final JSpinner sRotationZ;

	public final JSpinner sOffsetX;
	public final JSpinner sOffsetY;
	public final JSpinner sOffsetZ;

	public final JSpinner sTextureOffsetX;
	public final JSpinner sTextureOffsetY;

	public final JComboBox<Animation> cbAnimation;
	public final JTextField tfName;
	public final JLabel lFrame;
	public final JButton bAddFrame;
	public final JButton bPreviousFrame;
	public final JButton bNextFrame;
	public final JSpinner sDuration;

	public final JCheckBox cbxMoveRP;

	public Display(final Controller controller, TechneTree techneTree, MyCanvas myCanvas) {
		setResizable(true);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		JMenuItem mntmLoadJson = new JMenuItem("Load JSON");
		mntmLoadJson.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Menu Item clicked");
			}
		});
		mnFile.add(mntmLoadJson);

		JPanel pCanvas = new JPanel();
		pCanvas.setLayout(null);
		pCanvas.setPreferredSize(new Dimension(400, 400));
		pCanvas.add(myCanvas);
		getContentPane().add(pCanvas, BorderLayout.CENTER);

		JPanel pRightMenu = new JPanel();
		getContentPane().add(pRightMenu, BorderLayout.EAST);
		GridBagLayout gbl_pRightMenu = new GridBagLayout();
		gbl_pRightMenu.columnWidths = new int[] { 0, 0 };
		gbl_pRightMenu.rowHeights = new int[] { 500, 0, 0, 0 };
		gbl_pRightMenu.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_pRightMenu.rowWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
		pRightMenu.setLayout(gbl_pRightMenu);
		{
			JScrollPane scrollPane = new JScrollPane();
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 0;
			pRightMenu.add(scrollPane, gbc_scrollPane);

			JPanel pTree = new JPanel();
			FlowLayout flowLayout = (FlowLayout) pTree.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			scrollPane.setViewportView(pTree);
			pTree.add(techneTree);

			JPanel pButtons = new JPanel();
			GridBagConstraints gbc_pButtons = new GridBagConstraints();
			gbc_pButtons.anchor = GridBagConstraints.WEST;
			gbc_pButtons.gridx = 0;
			gbc_pButtons.gridy = 2;
			pRightMenu.add(pButtons, gbc_pButtons);
			GridBagLayout gbl_pButtons = new GridBagLayout();
			gbl_pButtons.columnWidths = new int[] { 77, 0, 81, 0 };
			gbl_pButtons.rowHeights = new int[] { 0, 0, 0, 0 };
			gbl_pButtons.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
			gbl_pButtons.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
			pButtons.setLayout(gbl_pButtons);

			JButton bAddQuad = new JButton("add quad");
			GridBagConstraints gbc_bAddQuad = new GridBagConstraints();
			gbc_bAddQuad.insets = new Insets(0, 0, 5, 5);
			gbc_bAddQuad.gridx = 0;
			gbc_bAddQuad.gridy = 0;
			pButtons.add(bAddQuad, gbc_bAddQuad);

			JButton bAddFolder = new JButton("add folder");
			bAddFolder.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					controller.addFolder();
				}
			});
			GridBagConstraints gbc_bAddFolder = new GridBagConstraints();
			gbc_bAddFolder.anchor = GridBagConstraints.NORTHWEST;
			gbc_bAddFolder.insets = new Insets(0, 0, 5, 5);
			gbc_bAddFolder.gridx = 1;
			gbc_bAddFolder.gridy = 0;
			pButtons.add(bAddFolder, gbc_bAddFolder);

			JButton bExtractToFolder = new JButton("extract to folder");
			GridBagConstraints gbc_bExtractToFolder = new GridBagConstraints();
			gbc_bExtractToFolder.anchor = GridBagConstraints.NORTHWEST;
			gbc_bExtractToFolder.insets = new Insets(0, 0, 5, 0);
			gbc_bExtractToFolder.gridx = 2;
			gbc_bExtractToFolder.gridy = 0;
			pButtons.add(bExtractToFolder, gbc_bExtractToFolder);

			JButton bExportJson = new JButton("export JSON");
			bExportJson.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					controller.exportJson();
				}
			});

			JButton bRemove = new JButton("remove");
			bRemove.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					controller.remove();
				}
			});
			GridBagConstraints gbc_bRemove = new GridBagConstraints();
			gbc_bRemove.insets = new Insets(0, 0, 5, 5);
			gbc_bRemove.gridx = 0;
			gbc_bRemove.gridy = 1;
			pButtons.add(bRemove, gbc_bRemove);
			GridBagConstraints gbc_bExportJson = new GridBagConstraints();
			gbc_bExportJson.insets = new Insets(0, 0, 5, 5);
			gbc_bExportJson.gridx = 1;
			gbc_bExportJson.gridy = 1;
			pButtons.add(bExportJson, gbc_bExportJson);

			JButton bExportJava = new JButton("export Java");
			bExportJava.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					controller.exportJava();
				}
			});
			GridBagConstraints gbc_bExportJava = new GridBagConstraints();
			gbc_bExportJava.insets = new Insets(0, 0, 5, 0);
			gbc_bExportJava.anchor = GridBagConstraints.NORTHWEST;
			gbc_bExportJava.gridx = 2;
			gbc_bExportJava.gridy = 1;
			pButtons.add(bExportJava, gbc_bExportJava);

			cbxMoveRP = new JCheckBox("move RP");
			GridBagConstraints gbc_cbxMoveRP = new GridBagConstraints();
			gbc_cbxMoveRP.insets = new Insets(0, 0, 0, 5);
			gbc_cbxMoveRP.gridx = 0;
			gbc_cbxMoveRP.gridy = 2;
			pButtons.add(cbxMoveRP, gbc_cbxMoveRP);
			
			JButton btnSetTextureSize = new JButton("set texture size");
			btnSetTextureSize.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					controller.setTextureSize();
				}
			});
			GridBagConstraints gbc_btnSetTextureSize = new GridBagConstraints();
			gbc_btnSetTextureSize.insets = new Insets(0, 0, 0, 5);
			gbc_btnSetTextureSize.gridx = 1;
			gbc_btnSetTextureSize.gridy = 2;
			pButtons.add(btnSetTextureSize, gbc_btnSetTextureSize);
		}

		JPanel pLeftMenu = new JPanel();
		getContentPane().add(pLeftMenu, BorderLayout.WEST);
		pLeftMenu.setLayout(new BorderLayout(0, 0));

		JPanel pComponentAttributes = new JPanel();
		pLeftMenu.add(pComponentAttributes, BorderLayout.NORTH);
		GridBagLayout gbl_pComponentAttributes = new GridBagLayout();
		gbl_pComponentAttributes.columnWidths = new int[] { 0, 0 };
		gbl_pComponentAttributes.rowHeights = new int[] { 10, 0, 10, 0, 0, 0 };
		gbl_pComponentAttributes.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_pComponentAttributes.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		pComponentAttributes.setLayout(gbl_pComponentAttributes);
		{
			JPanel pSize = new JPanel();
			pSize.setBorder(new TitledBorder(null, "Size", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_pSize = new GridBagConstraints();
			gbc_pSize.fill = GridBagConstraints.BOTH;
			gbc_pSize.insets = new Insets(0, 0, 5, 0);
			gbc_pSize.gridx = 0;
			gbc_pSize.gridy = 0;
			pComponentAttributes.add(pSize, gbc_pSize);
			GridBagLayout gbl_pSize = new GridBagLayout();
			gbl_pSize.columnWidths = new int[] { 25, 50, 25, 50, 25, 50, 0 };
			gbl_pSize.rowHeights = new int[] { 0, 0 };
			gbl_pSize.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			gbl_pSize.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			pSize.setLayout(gbl_pSize);

			JLabel lSizeX = new JLabel("X:");
			GridBagConstraints gbc_lSizeX = new GridBagConstraints();
			gbc_lSizeX.insets = new Insets(0, 0, 0, 5);
			gbc_lSizeX.gridx = 0;
			gbc_lSizeX.gridy = 0;
			pSize.add(lSizeX, gbc_lSizeX);

			sSizeX = new JSpinner();
			sSizeX.setEnabled(false);
			sSizeX.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
			sSizeX.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setSizeX((Integer) sSizeX.getValue());
				}
			});
			GridBagConstraints gbc_sSizeX = new GridBagConstraints();
			gbc_sSizeX.fill = GridBagConstraints.HORIZONTAL;
			gbc_sSizeX.insets = new Insets(0, 0, 0, 5);
			gbc_sSizeX.gridx = 1;
			gbc_sSizeX.gridy = 0;
			pSize.add(sSizeX, gbc_sSizeX);

			JLabel lSizeY = new JLabel("Y:");
			GridBagConstraints gbc_lSizeY = new GridBagConstraints();
			gbc_lSizeY.insets = new Insets(0, 0, 0, 5);
			gbc_lSizeY.gridx = 2;
			gbc_lSizeY.gridy = 0;
			pSize.add(lSizeY, gbc_lSizeY);

			sSizeY = new JSpinner();
			sSizeY.setEnabled(false);
			sSizeY.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
			sSizeY.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setSizeY((Integer) sSizeY.getValue());
				}
			});
			GridBagConstraints gbc_sSizeY = new GridBagConstraints();
			gbc_sSizeY.fill = GridBagConstraints.HORIZONTAL;
			gbc_sSizeY.insets = new Insets(0, 0, 0, 5);
			gbc_sSizeY.gridx = 3;
			gbc_sSizeY.gridy = 0;
			pSize.add(sSizeY, gbc_sSizeY);

			JLabel lSizeZ = new JLabel("Z:");
			GridBagConstraints gbc_lSizeZ = new GridBagConstraints();
			gbc_lSizeZ.insets = new Insets(0, 0, 0, 5);
			gbc_lSizeZ.gridx = 4;
			gbc_lSizeZ.gridy = 0;
			pSize.add(lSizeZ, gbc_lSizeZ);

			sSizeZ = new JSpinner();
			sSizeZ.setEnabled(false);
			sSizeZ.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
			sSizeZ.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setSizeZ((Integer) sSizeZ.getValue());
				}
			});
			GridBagConstraints gbc_sSizeZ = new GridBagConstraints();
			gbc_sSizeZ.fill = GridBagConstraints.HORIZONTAL;
			gbc_sSizeZ.gridx = 5;
			gbc_sSizeZ.gridy = 0;
			pSize.add(sSizeZ, gbc_sSizeZ);

			JPanel pRotationPoint = new JPanel();
			pRotationPoint.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "RotationPoint", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_pRotationPoint = new GridBagConstraints();
			gbc_pRotationPoint.fill = GridBagConstraints.BOTH;
			gbc_pRotationPoint.insets = new Insets(0, 0, 5, 0);
			gbc_pRotationPoint.gridx = 0;
			gbc_pRotationPoint.gridy = 1;
			pComponentAttributes.add(pRotationPoint, gbc_pRotationPoint);
			GridBagLayout gbl_pRotationPoint = new GridBagLayout();
			gbl_pRotationPoint.columnWidths = new int[] { 25, 50, 25, 50, 25, 50, 0 };
			gbl_pRotationPoint.rowHeights = new int[] { 0, 0 };
			gbl_pRotationPoint.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			gbl_pRotationPoint.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			pRotationPoint.setLayout(gbl_pRotationPoint);

			JLabel lRotationPointX = new JLabel("X:");
			GridBagConstraints gbc_lRotationPointX = new GridBagConstraints();
			gbc_lRotationPointX.insets = new Insets(0, 0, 0, 5);
			gbc_lRotationPointX.gridx = 0;
			gbc_lRotationPointX.gridy = 0;
			pRotationPoint.add(lRotationPointX, gbc_lRotationPointX);

			sRotationPointX = new JSpinner();
			sRotationPointX.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(0.5)));
			sRotationPointX.setEnabled(false);
			sRotationPointX.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setRotationPointX((Float) sRotationPointX.getValue());
				}
			});
			GridBagConstraints gbc_sRotationPointX = new GridBagConstraints();
			gbc_sRotationPointX.fill = GridBagConstraints.HORIZONTAL;
			gbc_sRotationPointX.insets = new Insets(0, 0, 0, 5);
			gbc_sRotationPointX.gridx = 1;
			gbc_sRotationPointX.gridy = 0;
			pRotationPoint.add(sRotationPointX, gbc_sRotationPointX);

			JLabel lRotationPointY = new JLabel("Y:");
			GridBagConstraints gbc_lRotationPointY = new GridBagConstraints();
			gbc_lRotationPointY.insets = new Insets(0, 0, 0, 5);
			gbc_lRotationPointY.gridx = 2;
			gbc_lRotationPointY.gridy = 0;
			pRotationPoint.add(lRotationPointY, gbc_lRotationPointY);

			sRotationPointY = new JSpinner();
			sRotationPointY.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(0.5)));
			sRotationPointY.setEnabled(false);
			sRotationPointY.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setRotationPointY((Float) sRotationPointY.getValue());
				}
			});
			GridBagConstraints gbc_sRotationPointY = new GridBagConstraints();
			gbc_sRotationPointY.fill = GridBagConstraints.HORIZONTAL;
			gbc_sRotationPointY.insets = new Insets(0, 0, 0, 5);
			gbc_sRotationPointY.gridx = 3;
			gbc_sRotationPointY.gridy = 0;
			pRotationPoint.add(sRotationPointY, gbc_sRotationPointY);

			JLabel lRotationPointZ = new JLabel("Z:");
			GridBagConstraints gbc_lRotationPointZ = new GridBagConstraints();
			gbc_lRotationPointZ.insets = new Insets(0, 0, 0, 5);
			gbc_lRotationPointZ.gridx = 4;
			gbc_lRotationPointZ.gridy = 0;
			pRotationPoint.add(lRotationPointZ, gbc_lRotationPointZ);

			sRotationPointZ = new JSpinner();
			sRotationPointZ.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(0.5)));
			sRotationPointZ.setEnabled(false);
			sRotationPointZ.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setRotationPointZ((Float) sRotationPointZ.getValue());
				}
			});
			GridBagConstraints gbc_sRotationPointZ = new GridBagConstraints();
			gbc_sRotationPointZ.fill = GridBagConstraints.HORIZONTAL;
			gbc_sRotationPointZ.gridx = 5;
			gbc_sRotationPointZ.gridy = 0;
			pRotationPoint.add(sRotationPointZ, gbc_sRotationPointZ);

			JPanel pRotation = new JPanel();
			pRotation.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Rotation", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_pRotation = new GridBagConstraints();
			gbc_pRotation.fill = GridBagConstraints.BOTH;
			gbc_pRotation.insets = new Insets(0, 0, 5, 0);
			gbc_pRotation.gridx = 0;
			gbc_pRotation.gridy = 2;
			pComponentAttributes.add(pRotation, gbc_pRotation);
			GridBagLayout gbl_pRotation = new GridBagLayout();
			gbl_pRotation.columnWidths = new int[] { 25, 50, 25, 50, 25, 50, 0 };
			gbl_pRotation.rowHeights = new int[] { 0, 0 };
			gbl_pRotation.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			gbl_pRotation.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			pRotation.setLayout(gbl_pRotation);

			JLabel lRotationX = new JLabel("X:");
			GridBagConstraints gbc_lRotationX = new GridBagConstraints();
			gbc_lRotationX.insets = new Insets(0, 0, 0, 5);
			gbc_lRotationX.gridx = 0;
			gbc_lRotationX.gridy = 0;
			pRotation.add(lRotationX, gbc_lRotationX);

			sRotationX = new JSpinner();
			sRotationX.setEnabled(false);
			sRotationX.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setRotationX((Integer) sRotationX.getValue());
				}
			});
			GridBagConstraints gbc_sRotationX = new GridBagConstraints();
			gbc_sRotationX.fill = GridBagConstraints.HORIZONTAL;
			gbc_sRotationX.insets = new Insets(0, 0, 0, 5);
			gbc_sRotationX.gridx = 1;
			gbc_sRotationX.gridy = 0;
			pRotation.add(sRotationX, gbc_sRotationX);

			JLabel lRotationY = new JLabel("Y:");
			GridBagConstraints gbc_lRotationY = new GridBagConstraints();
			gbc_lRotationY.insets = new Insets(0, 0, 0, 5);
			gbc_lRotationY.gridx = 2;
			gbc_lRotationY.gridy = 0;
			pRotation.add(lRotationY, gbc_lRotationY);

			sRotationY = new JSpinner();
			sRotationY.setEnabled(false);
			sRotationY.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setRotationY((Integer) sRotationY.getValue());
				}
			});
			GridBagConstraints gbc_sRotationY = new GridBagConstraints();
			gbc_sRotationY.fill = GridBagConstraints.HORIZONTAL;
			gbc_sRotationY.insets = new Insets(0, 0, 0, 5);
			gbc_sRotationY.gridx = 3;
			gbc_sRotationY.gridy = 0;
			pRotation.add(sRotationY, gbc_sRotationY);

			JLabel lRotationZ = new JLabel("Z:");
			GridBagConstraints gbc_lRotationZ = new GridBagConstraints();
			gbc_lRotationZ.insets = new Insets(0, 0, 0, 5);
			gbc_lRotationZ.gridx = 4;
			gbc_lRotationZ.gridy = 0;
			pRotation.add(lRotationZ, gbc_lRotationZ);

			sRotationZ = new JSpinner();
			sRotationZ.setEnabled(false);
			sRotationZ.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setRotationZ((Integer) sRotationZ.getValue());
				}
			});
			GridBagConstraints gbc_sRotationZ = new GridBagConstraints();
			gbc_sRotationZ.fill = GridBagConstraints.HORIZONTAL;
			gbc_sRotationZ.gridx = 5;
			gbc_sRotationZ.gridy = 0;
			pRotation.add(sRotationZ, gbc_sRotationZ);

			JPanel pOffset = new JPanel();
			pOffset.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Offset", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_pOffset = new GridBagConstraints();
			gbc_pOffset.fill = GridBagConstraints.BOTH;
			gbc_pOffset.insets = new Insets(0, 0, 5, 0);
			gbc_pOffset.gridx = 0;
			gbc_pOffset.gridy = 3;
			pComponentAttributes.add(pOffset, gbc_pOffset);
			GridBagLayout gbl_pOffset = new GridBagLayout();
			gbl_pOffset.columnWidths = new int[] { 25, 50, 25, 50, 25, 50, 0 };
			gbl_pOffset.rowHeights = new int[] { 0, 0 };
			gbl_pOffset.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			gbl_pOffset.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			pOffset.setLayout(gbl_pOffset);

			JLabel lOffsetX = new JLabel("X:");
			GridBagConstraints gbc_lOffsetX = new GridBagConstraints();
			gbc_lOffsetX.insets = new Insets(0, 0, 0, 5);
			gbc_lOffsetX.gridx = 0;
			gbc_lOffsetX.gridy = 0;
			pOffset.add(lOffsetX, gbc_lOffsetX);

			sOffsetX = new JSpinner();
			sOffsetX.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(0.5)));
			sOffsetX.setEnabled(false);
			sOffsetX.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setOffsetX((Float) sOffsetX.getValue());
				}
			});
			GridBagConstraints gbc_sOffsetX = new GridBagConstraints();
			gbc_sOffsetX.fill = GridBagConstraints.HORIZONTAL;
			gbc_sOffsetX.insets = new Insets(0, 0, 0, 5);
			gbc_sOffsetX.gridx = 1;
			gbc_sOffsetX.gridy = 0;
			pOffset.add(sOffsetX, gbc_sOffsetX);

			JLabel lOffsetY = new JLabel("Y:");
			GridBagConstraints gbc_lOffsetY = new GridBagConstraints();
			gbc_lOffsetY.insets = new Insets(0, 0, 0, 5);
			gbc_lOffsetY.gridx = 2;
			gbc_lOffsetY.gridy = 0;
			pOffset.add(lOffsetY, gbc_lOffsetY);

			sOffsetY = new JSpinner();
			sOffsetY.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(0.5)));
			sOffsetY.setEnabled(false);
			sOffsetY.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setOffsetY((Float) sOffsetY.getValue());
				}
			});
			GridBagConstraints gbc_sOffsetY = new GridBagConstraints();
			gbc_sOffsetY.fill = GridBagConstraints.HORIZONTAL;
			gbc_sOffsetY.insets = new Insets(0, 0, 0, 5);
			gbc_sOffsetY.gridx = 3;
			gbc_sOffsetY.gridy = 0;
			pOffset.add(sOffsetY, gbc_sOffsetY);

			JLabel lOffsetZ = new JLabel("Z:");
			GridBagConstraints gbc_lOffsetZ = new GridBagConstraints();
			gbc_lOffsetZ.insets = new Insets(0, 0, 0, 5);
			gbc_lOffsetZ.gridx = 4;
			gbc_lOffsetZ.gridy = 0;
			pOffset.add(lOffsetZ, gbc_lOffsetZ);

			sOffsetZ = new JSpinner();
			sOffsetZ.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(0.5)));
			sOffsetZ.setEnabled(false);
			sOffsetZ.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setOffsetZ((Float) sOffsetZ.getValue());
				}
			});
			GridBagConstraints gbc_sOffsetZ = new GridBagConstraints();
			gbc_sOffsetZ.fill = GridBagConstraints.HORIZONTAL;
			gbc_sOffsetZ.gridx = 5;
			gbc_sOffsetZ.gridy = 0;
			pOffset.add(sOffsetZ, gbc_sOffsetZ);

			JPanel pTextureOffset = new JPanel();
			pTextureOffset.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "TextureOffset", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_pTextureOffset = new GridBagConstraints();
			gbc_pTextureOffset.fill = GridBagConstraints.BOTH;
			gbc_pTextureOffset.gridx = 0;
			gbc_pTextureOffset.gridy = 4;
			pComponentAttributes.add(pTextureOffset, gbc_pTextureOffset);
			GridBagLayout gbl_pTextureOffset = new GridBagLayout();
			gbl_pTextureOffset.columnWidths = new int[] { 25, 50, 25, 50, 25, 50, 0 };
			gbl_pTextureOffset.rowHeights = new int[] { 0, 0 };
			gbl_pTextureOffset.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			gbl_pTextureOffset.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			pTextureOffset.setLayout(gbl_pTextureOffset);

			JLabel lTextureOffsetX = new JLabel("X:");
			GridBagConstraints gbc_lTextureOffsetX = new GridBagConstraints();
			gbc_lTextureOffsetX.insets = new Insets(0, 0, 0, 5);
			gbc_lTextureOffsetX.gridx = 0;
			gbc_lTextureOffsetX.gridy = 0;
			pTextureOffset.add(lTextureOffsetX, gbc_lTextureOffsetX);

			sTextureOffsetX = new JSpinner();
			sTextureOffsetX.setEnabled(false);
			sTextureOffsetX.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setTextureOffsetX((Integer) sTextureOffsetX.getValue());
				}
			});
			GridBagConstraints gbc_sTextureOffsetX = new GridBagConstraints();
			gbc_sTextureOffsetX.fill = GridBagConstraints.HORIZONTAL;
			gbc_sTextureOffsetX.insets = new Insets(0, 0, 0, 5);
			gbc_sTextureOffsetX.gridx = 1;
			gbc_sTextureOffsetX.gridy = 0;
			pTextureOffset.add(sTextureOffsetX, gbc_sTextureOffsetX);

			JLabel lTextureOffsetY = new JLabel("Y:");
			GridBagConstraints gbc_lTextureOffsetY = new GridBagConstraints();
			gbc_lTextureOffsetY.insets = new Insets(0, 0, 0, 5);
			gbc_lTextureOffsetY.gridx = 2;
			gbc_lTextureOffsetY.gridy = 0;
			pTextureOffset.add(lTextureOffsetY, gbc_lTextureOffsetY);

			sTextureOffsetY = new JSpinner();
			sTextureOffsetY.setEnabled(false);
			sTextureOffsetY.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					controller.setTextureOffsetY((Integer) sTextureOffsetY.getValue());
				}
			});
			GridBagConstraints gbc_sTextureOffsetY = new GridBagConstraints();
			gbc_sTextureOffsetY.fill = GridBagConstraints.HORIZONTAL;
			gbc_sTextureOffsetY.insets = new Insets(0, 0, 0, 5);
			gbc_sTextureOffsetY.gridx = 3;
			gbc_sTextureOffsetY.gridy = 0;
			pTextureOffset.add(sTextureOffsetY, gbc_sTextureOffsetY);
		}

		JPanel pAnimationMenu = new JPanel();
		pLeftMenu.add(pAnimationMenu, BorderLayout.SOUTH);
		GridBagLayout gbl_pAnimationMenu = new GridBagLayout();
		gbl_pAnimationMenu.columnWidths = new int[] { 196, 0 };
		gbl_pAnimationMenu.rowHeights = new int[] { 0, 0, 0, 0, 10, 0 };
		gbl_pAnimationMenu.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_pAnimationMenu.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		pAnimationMenu.setLayout(gbl_pAnimationMenu);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Place title here", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		pAnimationMenu.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JButton bAddAnimation = new JButton("add animation");
		bAddAnimation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.addAnimation();
			}
		});
		bAddAnimation.setToolTipText("add new animation");
		GridBagConstraints gbc_bAddAnimation = new GridBagConstraints();
		gbc_bAddAnimation.insets = new Insets(0, 0, 0, 5);
		gbc_bAddAnimation.gridx = 0;
		gbc_bAddAnimation.gridy = 0;
		panel.add(bAddAnimation, gbc_bAddAnimation);

		bAddFrame = new JButton("add frame");
		bAddFrame.setEnabled(false);
		bAddFrame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.addFrame();
			}
		});
		bAddFrame.setToolTipText("add new frame to current animation");
		GridBagConstraints gbc_bAddFrame = new GridBagConstraints();
		gbc_bAddFrame.gridx = 1;
		gbc_bAddFrame.gridy = 0;
		panel.add(bAddFrame, gbc_bAddFrame);

		JPanel pAnimation = new JPanel();
		pAnimation.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Animation", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_pAnimation = new GridBagConstraints();
		gbc_pAnimation.fill = GridBagConstraints.BOTH;
		gbc_pAnimation.insets = new Insets(0, 0, 5, 0);
		gbc_pAnimation.gridx = 0;
		gbc_pAnimation.gridy = 1;
		pAnimationMenu.add(pAnimation, gbc_pAnimation);
		GridBagLayout gbl_pAnimation = new GridBagLayout();
		gbl_pAnimation.columnWidths = new int[] { 25, 0 };
		gbl_pAnimation.rowHeights = new int[] { 0, 0 };
		gbl_pAnimation.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_pAnimation.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		pAnimation.setLayout(gbl_pAnimation);

		cbAnimation = new JComboBox<Animation>();
		cbAnimation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.showAnimation(cbAnimation.getSelectedItem());
			}
		});
		cbAnimation.addItem(null);
		GridBagConstraints gbc_cbAnimation = new GridBagConstraints();
		gbc_cbAnimation.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbAnimation.gridx = 0;
		gbc_cbAnimation.gridy = 0;
		pAnimation.add(cbAnimation, gbc_cbAnimation);

		JPanel pFrame = new JPanel();
		pFrame.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Frame", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_pFrame = new GridBagConstraints();
		gbc_pFrame.fill = GridBagConstraints.BOTH;
		gbc_pFrame.insets = new Insets(0, 0, 5, 0);
		gbc_pFrame.gridx = 0;
		gbc_pFrame.gridy = 2;
		pAnimationMenu.add(pFrame, gbc_pFrame);
		GridBagLayout gbl_pFrame = new GridBagLayout();
		gbl_pFrame.columnWidths = new int[] { 25, 25, 25, 0 };
		gbl_pFrame.rowHeights = new int[] { 0, 0 };
		gbl_pFrame.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_pFrame.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		pFrame.setLayout(gbl_pFrame);

		bPreviousFrame = new JButton("<<");
		bPreviousFrame.setEnabled(false);
		bPreviousFrame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.previousFrame();
			}
		});
		bPreviousFrame.setToolTipText("previous frame");
		GridBagConstraints gbc_bPreviousFrame = new GridBagConstraints();
		gbc_bPreviousFrame.insets = new Insets(0, 0, 0, 5);
		gbc_bPreviousFrame.gridx = 0;
		gbc_bPreviousFrame.gridy = 0;
		pFrame.add(bPreviousFrame, gbc_bPreviousFrame);

		lFrame = new JLabel("0/0");
		GridBagConstraints gbc_lFrame = new GridBagConstraints();
		gbc_lFrame.insets = new Insets(0, 0, 0, 5);
		gbc_lFrame.gridx = 1;
		gbc_lFrame.gridy = 0;
		pFrame.add(lFrame, gbc_lFrame);

		bNextFrame = new JButton(">>");
		bNextFrame.setEnabled(false);
		bNextFrame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.nextFrame();
			}
		});
		bNextFrame.setToolTipText("next frame");
		GridBagConstraints gbc_bNextFrame = new GridBagConstraints();
		gbc_bNextFrame.gridx = 2;
		gbc_bNextFrame.gridy = 0;
		pFrame.add(bNextFrame, gbc_bNextFrame);

		JPanel pOptions = new JPanel();
		pOptions.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Options", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_pOptions = new GridBagConstraints();
		gbc_pOptions.fill = GridBagConstraints.BOTH;
		gbc_pOptions.insets = new Insets(0, 0, 5, 0);
		gbc_pOptions.gridx = 0;
		gbc_pOptions.gridy = 3;
		pAnimationMenu.add(pOptions, gbc_pOptions);
		GridBagLayout gbl_pOptions = new GridBagLayout();
		gbl_pOptions.columnWidths = new int[] { 25, 75, 25, 50, 0 };
		gbl_pOptions.rowHeights = new int[] { 0, 0 };
		gbl_pOptions.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_pOptions.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		pOptions.setLayout(gbl_pOptions);

		JLabel lDuration = new JLabel("duration (ms):");
		GridBagConstraints gbc_lDuration = new GridBagConstraints();
		gbc_lDuration.insets = new Insets(0, 0, 0, 5);
		gbc_lDuration.gridx = 0;
		gbc_lDuration.gridy = 0;
		pOptions.add(lDuration, gbc_lDuration);

		sDuration = new JSpinner();
		sDuration.setEnabled(false);
		sDuration.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				controller.changeDuration((int) sDuration.getValue());
			}
		});
		sDuration.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_sDuration = new GridBagConstraints();
		gbc_sDuration.fill = GridBagConstraints.HORIZONTAL;
		gbc_sDuration.insets = new Insets(0, 0, 0, 5);
		gbc_sDuration.gridx = 1;
		gbc_sDuration.gridy = 0;
		pOptions.add(sDuration, gbc_sDuration);

		JLabel lName = new JLabel("name:");
		GridBagConstraints gbc_lName = new GridBagConstraints();
		gbc_lName.anchor = GridBagConstraints.EAST;
		gbc_lName.insets = new Insets(0, 0, 0, 5);
		gbc_lName.gridx = 2;
		gbc_lName.gridy = 0;
		pOptions.add(lName, gbc_lName);

		tfName = new JTextField();
		tfName.setEnabled(false);
		tfName.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				controller.changeName(tfName.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				controller.changeName(tfName.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				controller.changeName(tfName.getText());
			}
		});
		GridBagConstraints gbc_tfName = new GridBagConstraints();
		gbc_tfName.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfName.gridx = 3;
		gbc_tfName.gridy = 0;
		pOptions.add(tfName, gbc_tfName);
		tfName.setColumns(10);

		JPanel pControls = new JPanel();
		pControls.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Controls", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_pControls = new GridBagConstraints();
		gbc_pControls.fill = GridBagConstraints.BOTH;
		gbc_pControls.gridx = 0;
		gbc_pControls.gridy = 4;
		pAnimationMenu.add(pControls, gbc_pControls);
		GridBagLayout gbl_pControls = new GridBagLayout();
		gbl_pControls.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_pControls.rowHeights = new int[] { 0, 0 };
		gbl_pControls.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_pControls.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		pControls.setLayout(gbl_pControls);

		JButton bAnimStop = new JButton("\u25A0");
		bAnimStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.animStop();
			}
		});
		bAnimStop.setToolTipText("stop");
		GridBagConstraints gbc_bAnimStop = new GridBagConstraints();
		gbc_bAnimStop.insets = new Insets(0, 0, 0, 5);
		gbc_bAnimStop.gridx = 0;
		gbc_bAnimStop.gridy = 0;
		pControls.add(bAnimStop, gbc_bAnimStop);

		JButton bAnimPlay = new JButton("\u25BA");
		bAnimPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.animPlay();
			}
		});
		bAnimPlay.setToolTipText("start / pause");
		GridBagConstraints gbc_bAnimPlay = new GridBagConstraints();
		gbc_bAnimPlay.insets = new Insets(0, 0, 0, 5);
		gbc_bAnimPlay.gridx = 1;
		gbc_bAnimPlay.gridy = 0;
		pControls.add(bAnimPlay, gbc_bAnimPlay);

		JButton bAnimPreviousFrame = new JButton("<<");
		bAnimPreviousFrame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.animPreviousFrame();
			}
		});
		bAnimPreviousFrame.setToolTipText("previous frame");
		GridBagConstraints gbc_bAnimPreviousFrame = new GridBagConstraints();
		gbc_bAnimPreviousFrame.insets = new Insets(0, 0, 0, 5);
		gbc_bAnimPreviousFrame.gridx = 2;
		gbc_bAnimPreviousFrame.gridy = 0;
		pControls.add(bAnimPreviousFrame, gbc_bAnimPreviousFrame);

		JButton bAnimPreviousStep = new JButton("<");
		bAnimPreviousStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.animPreviousStep();
			}
		});
		bAnimPreviousStep.setToolTipText("previous step");
		GridBagConstraints gbc_bAnimPreviousStep = new GridBagConstraints();
		gbc_bAnimPreviousStep.insets = new Insets(0, 0, 0, 5);
		gbc_bAnimPreviousStep.gridx = 3;
		gbc_bAnimPreviousStep.gridy = 0;
		pControls.add(bAnimPreviousStep, gbc_bAnimPreviousStep);

		JButton bAnimNextStep = new JButton(">");
		bAnimNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.animNextStep();
			}
		});
		bAnimNextStep.setToolTipText("next step");
		GridBagConstraints gbc_bAnimNextStep = new GridBagConstraints();
		gbc_bAnimNextStep.insets = new Insets(0, 0, 0, 5);
		gbc_bAnimNextStep.gridx = 4;
		gbc_bAnimNextStep.gridy = 0;
		pControls.add(bAnimNextStep, gbc_bAnimNextStep);

		JButton bAnimNextFrame = new JButton(">>");
		bAnimNextFrame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.animNextFrame();
			}
		});
		bAnimNextFrame.setToolTipText("next frame");
		GridBagConstraints gbc_bAnimNextFrame = new GridBagConstraints();
		gbc_bAnimNextFrame.gridx = 5;
		gbc_bAnimNextFrame.gridy = 0;
		pControls.add(bAnimNextFrame, gbc_bAnimNextFrame);

		pack();
		// setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
	}
}
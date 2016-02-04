package tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import main.Controller;

public class TechneTree extends JTree implements DragSourceListener, DropTargetListener, DragGestureListener, TreeSelectionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	static DataFlavor localObjectFlavor;
	static {
		try {
			localObjectFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	static final DataFlavor[] supportedFlavors = { localObjectFlavor };

	private final Controller controller;

	private final DragSource dragSource;
	private TechneTreeNode dropTargetNodeForRendering = null;

	private TechneTreeNode selected = null;

	public TechneTree(Controller controller, TechneTreeModel techneTreeModel) {
		super(techneTreeModel);

		this.controller = controller;

		setCellRenderer(new TechneTreeCellRenderer());
		setRootVisible(true);
		setEditable(true);
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);

		new DropTarget(this, this);

		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		addTreeSelectionListener(this);

		setExpandsSelectedPaths(true);

		addMouseListener(this);
	}

	@Override
	public TechneTreeModel getModel() {
		return (TechneTreeModel) super.getModel();
	};

	@Override
	public void setModel(TreeModel treeModel) {
		if (getModel() != null)
			throw new RuntimeException();
		else
			super.setModel(treeModel);
	};

	// DragGestureListener events
	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		// find object at this x,y
		Point clickPoint = dge.getDragOrigin();
		TreePath path = getPathForLocation(clickPoint.x, clickPoint.y);
		if (path == null) {
			return;
		}
		Transferable trans = new RJLTransferable(path);
		dragSource.startDrag(dge, Cursor.getDefaultCursor(), trans, this);
	}

	// DragSourceListener events
	@Override
	public void dragDropEnd(DragSourceDropEvent dsde) {
		dropTargetNodeForRendering = null;
		repaint();
	}

	@Override
	public void dragEnter(DragSourceDragEvent dsde) {
	}

	@Override
	public void dragExit(DragSourceEvent dse) {
	}

	@Override
	public void dragOver(DragSourceDragEvent dsde) {
	}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {
	}

	// DropTargetListener events
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// figure out which cell it's over, no drag to self
		Point dragPoint = dtde.getLocation();
		TreePath path = getPathForLocation(dragPoint.x, dragPoint.y);
		if (path == null)
			dropTargetNodeForRendering = null;
		else
			dropTargetNodeForRendering = (TechneTreeNode) path.getLastPathComponent();
		repaint();
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		Point dropTargetPoint = dtde.getLocation();
		TreePath dropTargetPath = getPathForLocation(dropTargetPoint.x, dropTargetPoint.y);

		dtde.acceptDrop(DnDConstants.ACTION_MOVE);

		Object dragSourceObject;
		try {
			dragSourceObject = dtde.getTransferable().getTransferData(localObjectFlavor);
		} catch (UnsupportedFlavorException | IOException e) {
			throw new RuntimeException(e);
		}

		TreePath dragSourcePath = (TreePath) dragSourceObject;

		if (!dragSourcePath.isDescendant(dropTargetPath)) {
			TechneTreeNode dragSourceNode = (TechneTreeNode) dragSourcePath.getLastPathComponent();
			((TechneTreeNode) dragSourcePath.getParentPath().getLastPathComponent()).removeChild(dragSourceNode);

			TechneTreeNode dropTargetNode = (TechneTreeNode) dropTargetPath.getLastPathComponent();
			if (!dropTargetNode.isFolder()) {
				TechneTreeNode dropTargetParentNode = (TechneTreeNode) dropTargetPath.getParentPath().getLastPathComponent();
				// this inserts before
				int index = dropTargetParentNode.getIndexAt(dropTargetNode);
				dropTargetParentNode.insertChild(dragSourceNode, index);
			} else {
				dropTargetNode.addChild(dragSourceNode);
			}
			updateUI();
		}
		dtde.dropComplete(true);

		updateUI();
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	// TreeSelectionListener events
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (selected != null)
			selected.unselect();

		selected = (TechneTreeNode) e.getPath().getLastPathComponent();
		controller.showComponent(selected.getComponent());
		selected.select();
	}

	class RJLTransferable implements Transferable {
		Object object;

		public RJLTransferable(Object o) {
			object = o;
		}

		@Override
		public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
			if (isDataFlavorSupported(df))
				return object;
			else
				throw new UnsupportedFlavorException(df);
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor df) {
			return (df.equals(localObjectFlavor));
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return supportedFlavors;
		}
	}

	// custom renderer
	class TechneTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 1L;

		boolean isTargetNode;
		boolean isTargetNodeLeaf;
		boolean isLastItem;
		Insets normalInsets, lastItemInsets;
		int BOTTOM_PAD = 30;

		public TechneTreeCellRenderer() {
			super();
			normalInsets = super.getInsets();
			lastItemInsets = new Insets(normalInsets.top, normalInsets.left, normalInsets.bottom + BOTTOM_PAD, normalInsets.right);
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {
			isTargetNode = (value == dropTargetNodeForRendering);
			isTargetNodeLeaf = (isTargetNode && !((TechneTreeNode) value).isFolder());

			if (value instanceof TechneTreeNode) {
				TechneTreeNode node = (TechneTreeNode) value;
				if (node.isFolder())
					if (isExpanded)
						setIcon(UIManager.getIcon("Tree.openIcon"));
					else
						setIcon(UIManager.getIcon("Tree.closedIcon"));
				else
					setIcon(UIManager.getIcon("Tree.leafIcon"));
			}

			return super.getTreeCellRendererComponent(tree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (isTargetNode) {
				g.setColor(Color.black);
				if (isTargetNodeLeaf) {
					g.drawLine(0, 0, getSize().width, 0);
				} else {
					g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
				}
			}
		}
	}

	// MouseListener events

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			TreePath path = getPathForLocation(e.getX(), e.getY());
			Rectangle pathBounds = getUI().getPathBounds(this, path);
			if (pathBounds != null && pathBounds.contains(e.getX(), e.getY())) {
				((TechneTreeNode) path.getLastPathComponent()).showPopupMenu(this, pathBounds.x, pathBounds.y + pathBounds.height, controller.modelHelper);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}

/*
 * Swing Hacks Tips and Tools for Killer GUIs By Joshua Marinacci, Chris Adamson First Edition June 2005 Series: Hacks ISBN: 0-596-00907-0 http://www.oreilly.com/catalog/swinghks/
 */
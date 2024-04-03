
/*
 * ==========================================================================================
 * AnimationViewer.java : Moves shapes around on the screen according to different paths.
 * It is the main drawing area where shapes are added and manipulated.
 * YOUR UPI: hxza301
 * ==========================================================================================
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListDataListener;
import java.lang.reflect.Field;

class AnimationViewer extends JComponent implements Runnable, TreeModel{
	private Thread animationThread = null; // the thread for animation
	private static int DELAY = 120; // the current animation speed
	private ShapeType currentShapeType = Shape.DEFAULT_SHAPETYPE; // the current shape type,
	private PathType currentPathType = Shape.DEFAULT_PATHTYPE; // the current path type
	private Color currentColor = Shape.DEFAULT_COLOR; // the current fill colour of a shape
	private Color currentBorderColor = Shape.DEFAULT_BORDER_COLOR;
	private int currentPanelWidth = Shape.DEFAULT_PANEL_WIDTH, currentPanelHeight = Shape.DEFAULT_PANEL_HEIGHT,currentWidth = Shape.DEFAULT_WIDTH, currentHeight = Shape.DEFAULT_HEIGHT;
	private String currentLabel = Shape.DEFAULT_LABEL;
	protected NestedShape root;
	private ArrayList<TreeModelListener> treeModelListeners = new ArrayList<>();
	protected DefaultListModel<Shape> listModel;
	

	public AnimationViewer() {
		start();
		root = new NestedShape(Shape.DEFAULT_PANEL_WIDTH, Shape.DEFAULT_PANEL_HEIGHT);
		listModel = new DefaultListModel<>();
	}

	
	public final void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Shape currentShape : root.getAllInnerShapes()) {
			currentShape.move();
			currentShape.draw(g);
			currentShape.drawHandles(g);
			currentShape.drawString(g);
		}
	}
	public void resetMarginSize() {
		currentPanelWidth = getWidth();
		currentPanelHeight = getHeight();
		for (Shape currentShape : root.getAllInnerShapes())
			currentShape.resetPanelSize(currentPanelWidth, currentPanelHeight);
	}
	public NestedShape getRoot(){
		return root;
	}
	public boolean isLeaf(Object node){
		return (!(node instanceof NestedShape));
	}
	public boolean isRoot(Shape selectedNode){
		return selectedNode==root;
	}
	public Shape getChild(Object parent, int index){
		if(!(parent instanceof NestedShape)||index<0||index>((NestedShape)parent).getSize()){
			return null;
		}else{
			return ((NestedShape)parent).getInnerShapeAt(index);
		}
	}
	public int getChildCount(Object parent){
		if(!(parent instanceof NestedShape)){
			return 0;
		}else{
			return ((NestedShape)parent).getSize();
		}
	}
	public int getIndexOfChild(Object parent, Object child){
		if(!(parent instanceof NestedShape)){
			return -1;
		}else{
			return ((NestedShape)parent).indexOf((Shape)child);
		}
	}
	public void addTreeModelListener(TreeModelListener listener) {treeModelListeners.add(listener);}
	public void removeTreeModelListener(TreeModelListener listener) {treeModelListeners.remove(listener);}
	public void valueForPathChanged(TreePath path, Object newValue) {  }
	protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children){
		System.out.printf("Called fireTreeNodesInserted: path=%s, childIndices=%s, children=%s\n", Arrays.toString(path), Arrays.toString(childIndices), Arrays.toString(children));
		final TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
		for (final TreeModelListener tml : treeModelListeners){
			tml.treeNodesInserted(event);
		}
	}
	public void addShapeNode(NestedShape selectedNode){
		Shape innerShape = null;
		if(currentShapeType==ShapeType.RECTANGLE){
			 innerShape = new RectangleShape(0, 0, (selectedNode.width)/5, (selectedNode.height)/5, selectedNode.width, selectedNode.height, selectedNode.color, selectedNode.borderColor, currentPathType);
		}else if(currentShapeType==ShapeType.OVAL){
			innerShape = new OvalShape(0, 0, (selectedNode.width)/5, (selectedNode.height)/5, selectedNode.width, selectedNode.height, selectedNode.color, selectedNode.borderColor, currentPathType);
		}else{
			innerShape = new NestedShape(0, 0, (selectedNode.width)/5, (selectedNode.height)/5, selectedNode.width, selectedNode.height, selectedNode.color, selectedNode.borderColor, currentPathType);
		}
		selectedNode.addInnerShape(innerShape);
		listModel.addElement(innerShape);
		int[] index = {selectedNode.indexOf(innerShape)};
		Object[] child = {innerShape};
		fireTreeNodesInserted(this, selectedNode.getPath(), index, child);
	}
	protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices,Object[] children){
		System.out.printf("Called fireTreeNodesRemoved: path=%s, childIndices=%s, children=%s\n", Arrays.toString(path), Arrays.toString(childIndices), Arrays.toString(children));
		final TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
		for (final TreeModelListener tml : treeModelListeners)
			 tml.treeNodesRemoved(event);
	}
	public void removeNodeFromParent(Shape selectedNode){
		NestedShape parent = selectedNode.getParent();
		int[] index = {parent.indexOf(selectedNode)};
		parent.removeInnerShape(selectedNode);
		Object[] child = {selectedNode};
		fireTreeNodesRemoved(this, parent.getPath(), index, child);
	}
	public void reload(Shape selectedNode){
	    if(selectedNode instanceof NestedShape){
	        listModel.clear();
			ArrayList<Shape> innerShapes = ((NestedShape)selectedNode).getAllInnerShapes();
			for(Shape i:innerShapes){
				listModel.addElement(i);
			}
		}
	
	}
	



	// you don't need to make any changes after this line ______________
	public String getCurrentLabel() {return currentLabel;}
	public int getCurrentHeight() { return currentHeight; }
	public int getCurrentWidth() { return currentWidth; }
	public Color getCurrentColor() { return currentColor; }
	public Color getCurrentBorderColor() { return currentBorderColor; }
	public void setCurrentShapeType(ShapeType value) {currentShapeType = value;}
	public void setCurrentPathType(PathType value) {currentPathType = value;}
	public ShapeType getCurrentShapeType() {return currentShapeType;}
	public PathType getCurrentPathType() {return currentPathType;}
	public void update(Graphics g) {
		paint(g);
	}
	public void start() {
		animationThread = new Thread(this);
		animationThread.start();
	}
	public void stop() {
		if (animationThread != null) {
			animationThread = null;
		}
	}
	public void run() {
		Thread myThread = Thread.currentThread();
		while (animationThread == myThread) {
			repaint();
			pause(DELAY);
		}
	}
	private void pause(int milliseconds) {
		try {
			Thread.sleep((long) milliseconds);
		} catch (InterruptedException ie) {}
	}
}

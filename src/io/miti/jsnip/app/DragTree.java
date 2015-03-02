/**
 * This code was derived from source code
 * provided with the book "Swing Hacks".
 * Published by O'Reilly, 2005.  Written
 * by Josh Marinaci and Chris Adamson.
 * ISBN: 0-596-00907-0.
 */

package io.miti.jsnip.app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
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
import java.io.IOException;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Drag'n'drop tree.
 */
public final class DragTree extends JTree
                            implements DragSourceListener,
                                       DropTargetListener,
                                       DragGestureListener
{
  /**
   * The serial version number for this class.
   */
  static final long serialVersionUID = 1L;
  
  /**
   * The local object flavor.
   */
  private static DataFlavor localObjectFlavor;
  
  /**
   * Initialize the local object flavor.
   */
  static
  {
    try
    {
      localObjectFlavor = 
          new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
    }
    catch (ClassNotFoundException cnfe)
    {
      cnfe.printStackTrace();
    }
  }
  
  /**
   * The supported data flavors.
   */
  private static DataFlavor[] supportedFlavors = {localObjectFlavor};
  
  /**
   * The drag source.
   */
  private DragSource dragSource;
  
  /**
   * The drop target.
   */
  @SuppressWarnings("unused")
  private DropTarget dropTarget = null;
  
  /**
   * The target node for the drop.
   */
  private TreeNode dropTargetNode = null;
  
  /**
   * The dragged node.
   */
  private TreeNode draggedNode = null;
  
  /**
   * The tree handler.
   */
  private transient TreeHandler treeHandler = null;
  
  
  /**
   * The drag gesture was recognized.
   * 
   * @param dge the drag gesture event
   */
  public void dragGestureRecognized(final DragGestureEvent dge)
  {
    // Find object at this x,y
    Point clickPoint = dge.getDragOrigin();
    
    // Get the path and check if it's null
    TreePath path = getPathForLocation(clickPoint.x, clickPoint.y);
    if (path == null)
    {
      return;
    }
    
    // Get the dragged node
    draggedNode = (TreeNode) path.getLastPathComponent();
    
    // Create the transfer object
    Transferable trans = new RJLTransferable(draggedNode);
    
    // Start the drag
    dragSource.startDrag(dge, Cursor.getDefaultCursor(),
                         trans, this);
  }
  
  
  /**
   * The drag and drop ended.
   * 
   * @param dsde the event
   */
  public void dragDropEnd(final DragSourceDropEvent dsde)
  {
    dropTargetNode = null;
    draggedNode = null;
    repaint();
  }
  
  
  /**
   * Enter the drag.
   * 
   * @param dsde the event
   */
  public void dragEnter(final DragSourceDragEvent dsde)
  {
    // Nothing to do here
  }
  
  
  /**
   * Exit the drag.
   * 
   * @param dse the event
   */
  public void dragExit(final DragSourceEvent dse)
  {
    // Nothing to do here
  }
  
  
  /**
   * The drag over event.
   * 
   * @param dsde the event
   */
  public void dragOver(final DragSourceDragEvent dsde)
  {
    // Nothing to do here
  }
  
  
  /**
   * The drop action changed.
   * 
   * @param dsde the event
   */
  public void dropActionChanged(final DragSourceDragEvent dsde)
  {
    // Nothing to do here
  }
  
  
  /**
   * Enter the drag.
   * 
   * @param dtde the event
   */
  public void dragEnter(final DropTargetDragEvent dtde)
  {
    dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
  }
  
  
  /**
   * Exit the drag.
   * 
   * @param dte the event
   */
  public void dragExit(final DropTargetEvent dte)
  {
    // Nothing to do here
  }
  
  
  /**
   * Drag over event.
   * 
   * @param dtde the event
   */
  public void dragOver(final DropTargetDragEvent dtde)
  {
    // Figure out which cell it's over, no drag to self
    Point dragPoint = dtde.getLocation();
    TreePath path = getPathForLocation(dragPoint.x, dragPoint.y);
    
    // Check the path
    if (path == null)
    {
      dropTargetNode = null;
    }
    else
    {
      dropTargetNode = (TreeNode) path.getLastPathComponent();
    }
    
    // Repaint the tree
    repaint();
  }
  
  
  /**
   * The drop event.
   * 
   * @param dtde the event
   */
  public void drop(final DropTargetDropEvent dtde)
  {
    // Get the location of the drop
    Point dropPoint = dtde.getLocation();
    
    // Get the path for the location
    TreePath path = getPathForLocation(dropPoint.x, dropPoint.y);
    
    // Default to not dropped
    boolean dropped = false;
    
    // Get the object that was dragged
    Object droppedObject = null;
    try
    {
      droppedObject =
        dtde.getTransferable().getTransferData(localObjectFlavor);
    }
    catch (UnsupportedFlavorException ufe)
    {
      droppedObject = null;
      ufe.printStackTrace();
    }
    catch (IOException ioe)
    {
      droppedObject = null;
      ioe.printStackTrace();
    }
    
    // Check if an error occurred or it's not of
    // the expected type (mutable tree node)
    if ((droppedObject == null) ||
        (!(droppedObject instanceof MutableTreeNode)))
    {
      dtde.rejectDrop();
      dtde.dropComplete(false);
      return;
    }
    
    // Get the node, and check if it has a parent
    MutableTreeNode droppedNode = (MutableTreeNode) droppedObject;
    if (droppedNode.getParent() == null)
    {
      // No parent, so reject the drop (the user is
      // dragging the root node)
      dtde.rejectDrop();
      dtde.dropComplete(dropped);
      return;
    }
    
    // Get the last path component
    DefaultMutableTreeNode dropNode =
        (DefaultMutableTreeNode) path.getLastPathComponent();
    
    // Check if droppedNode is a parent of dropNode
    TreeNode[] parentNodes = dropNode.getPath();
    if (parentNodes != null)
    {
      final int parentCount = parentNodes.length;
      boolean isParent = false;
      for (int i = 0; (i < parentCount) && (!isParent); ++i)
      {
        if (parentNodes[i] == droppedNode)
        {
          isParent = true;
        }
      }
      
      if (isParent)
      {
        dtde.rejectDrop();
        dtde.dropComplete(dropped);
        return;
      }
    }
    
    // Accept the drop
    dtde.acceptDrop(DnDConstants.ACTION_MOVE);
    
    /*
    // dropped on self?
    if (droppedObject == draggedNode) {
        System.out.println ("dropped onto self");
        // can't reject, because we've accepted, so no-op
        return;
    }
    */
    
    // Get the transfer object as a tree node and
    // remove it from its parent
    ((DefaultTreeModel) getModel()).removeNodeFromParent(droppedNode);
    
    // Check if the node is a leaf
    if (dropNode.isLeaf())
    {
      // Get the parent
      DefaultMutableTreeNode parent = 
          (DefaultMutableTreeNode) dropNode.getParent();
      
      // Get the index of the child in the parent
      int index = parent.getIndex(dropNode);
      
      // Move the node to next to the selected node
      ((DefaultTreeModel) getModel()).insertNodeInto(droppedNode,
                                                    parent, index);
    }
    else
    {
      // Move the node to after the last child
      ((DefaultTreeModel) getModel()).insertNodeInto(droppedNode,
                                                     dropNode,
                                                     dropNode.getChildCount());
    }
    
    // The node was dropped
    dropped = true;
    
    // Tell the App class that the tree changed
    if (treeHandler != null)
    {
      treeHandler.treeChanged();
    }
    
    // The drop was completed
    dtde.dropComplete(dropped);
  }
  
  
  /**
   * The drop action changed.
   * 
   * @param dtde the event
   */
  public void dropActionChanged(final DropTargetDragEvent dtde)
  {
    // Nothing to do here
  }
  
  
  /**
   * Default constructor.
   * 
   * @param treeModel the tree model
   * @param handler the tree handler
   */
  public DragTree(final DefaultTreeModel treeModel,
                  final TreeHandler handler)
  {
    super();
    
    // The tree is editable
    setEditable(true);
    
    // Set the editor for each cell
    DefaultTreeCellEditor editor = new DefaultTreeCellEditor(this,
         new DnDTreeCellRenderer());
    setCellEditor(editor);
    
    // Set the cell renderer
    setCellRenderer(new DnDTreeCellRenderer());
    
    // Set the model
    setModel(treeModel);
    
    // Save the handler
    treeHandler = handler;
    
    // Set up the drag source
    dragSource = new DragSource();
    @SuppressWarnings("unused")
	DragGestureRecognizer dgr = dragSource.
      createDefaultDragGestureRecognizer(this,
                                         DnDConstants.ACTION_MOVE,
                                         this);
    
    // Save the drop target
    dropTarget = new DropTarget(this, this);
  }
  
  
  /**
   * Constructor.
   * 
   * 
   */
  public DragTree()
  {
    super();
    
    // Set the cell renderer
    setCellRenderer(new DnDTreeCellRenderer());
    
    // Set the model
    setModel(new DefaultTreeModel(new DefaultMutableTreeNode("default")));
    
    // Set up the drag source
    dragSource = new DragSource();
    @SuppressWarnings("unused")
    DragGestureRecognizer dgr = dragSource.
      createDefaultDragGestureRecognizer(this,
                                         DnDConstants.ACTION_MOVE,
                                         this);
    
    // Save the drop target
    dropTarget = new DropTarget(this, this);
  }
  
  
  /**
   * Class to handle transfer of tree nodes.
   */
  static class RJLTransferable implements Transferable
  {
    /**
     * The object getting transfered.
     */
    private Object object = null;
    
    
    /**
     * Constructor.
     * 
     * @param o the object to save
     */
    public RJLTransferable(final Object o)
    {
      object = o;
    }
    
    
    /**
     * Returns the transfer data for the specified flavor.
     * 
     * @param df the data flavor
     * @return the transfer data
     * @throws UnsupportedFlavorException the flavor is not supported
     * @throws IOException an IO exception occurred
     */
    public Object getTransferData(final DataFlavor df)
        throws UnsupportedFlavorException, IOException
    {
      if (isDataFlavorSupported(df))
      {
        return object;
      }
      else
      {
        throw new UnsupportedFlavorException(df);
      }
    }
    
    
    /**
     * Return whether the specified data flavor is supported.
     * 
     * @param df the specified data flavor
     * @return whether the flavor is supported
     */
    public boolean isDataFlavorSupported(final DataFlavor df)
    {
      return (df.equals(localObjectFlavor));
    }
    
    
    /**
     * Return all transfer data flavors.
     * 
     * @return the array of transfer data flavors
     */
    public DataFlavor[] getTransferDataFlavors()
    {
      return supportedFlavors;
    }
  }
  
  
  /**
   * Custom cell renderer for tree.
   */
  class DnDTreeCellRenderer extends DefaultTreeCellRenderer
  {
    /**
     * The serial version number for this class.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Whether this is the target node.
     */
    private boolean isTargetNode;
    
    /**
     * Whether the target node is a leaf.
     */
    private boolean isTargetNodeLeaf;
    
    /**
     * Whether this is the last item.
     */
    // private boolean isLastItem;
    
    /**
     * Normal insets.
     */
    private Insets normalInsets;
    
    /**
     * Last item insets.
     */
    @SuppressWarnings("unused")
    private Insets lastItemInsets;
    
    /**
     * Bottom padding.
     */
    private static final int BOTTOM_PAD = 30;
    
    /**
     * Default constructor.
     */
    public DnDTreeCellRenderer()
    {
      super();
      
      // Create the insets
      normalInsets = super.getInsets();
      lastItemInsets = new Insets(normalInsets.top,
                                  normalInsets.left,
                                  normalInsets.bottom + BOTTOM_PAD,
                                  normalInsets.right);
    }
    
    
    /**
     * Retrieve the cell renderer component.
     * 
     * @param tree the tree
     * @param value the value
     * @param isSelected whether it is selected
     * @param isExpanded whether it is expanded
     * @param isLeaf whether this is a leaf
     * @param row the row number
     * @param hasFocus whether it has the focus
     * @return a component
     */
    public Component getTreeCellRendererComponent(final JTree tree,
                                                  final Object value,
                                                  final boolean isSelected,
                                                  final boolean isExpanded,
                                                  final boolean isLeaf,
                                                  final int row,
                                                  final boolean hasFocus)
    {
      // Save whether this is the target node
      isTargetNode = (value == dropTargetNode);
      
      // Save whether this is a leaf
      isTargetNodeLeaf = (isTargetNode &&
                          ((TreeNode) value).isLeaf());
      
      // isLastItem = (index == list.getModel().getSize()-1);
      
      // Decide whether to show the selected nodes
      // boolean showSelected = isSelected & (dropTargetNode == null);
      
      // Call the parent method
      return super.getTreeCellRendererComponent(tree, value,
                                                isSelected, isExpanded,
                                                isLeaf, row, hasFocus);
    }
    
    
    /**
     * Paint the component.
     * 
     * @param g the graphics object
     */
    public void paintComponent(final Graphics g)
    {
      // Call the parent paint
      super.paintComponent(g);
      
      // Check if this is the target node
      if (isTargetNode)
      {
        // It is, so set the color to black
        g.setColor(Color.black);
        
        // Check if this is a leaf
        if (isTargetNodeLeaf)
        {
          // It is, so draw a line before it
          g.drawLine(0, 0, getSize().width, 0);
        }
        else
        {
          // It's not a leaf, so draw a rectangle
          g.drawRect(0, 0, getSize().width - 1,
                     getSize().height - 1);
        }
      }
    }
  }
}

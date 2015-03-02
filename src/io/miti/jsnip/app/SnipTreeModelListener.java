/*
 * Written by Mike Wallace (mfwallace at gmail.com).  Available
 * on the web site http://mfwallace.googlepages.com/.
 * 
 * Copyright (c) 2006 Mike Wallace.
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package io.miti.jsnip.app;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 * The tree listener.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class SnipTreeModelListener implements TreeModelListener
{
  /**
   * Default constructor.
   */
  public SnipTreeModelListener()
  {
    super();
  }
  
  
  /**
   * The tree nodes have changed.
   * 
   * @param e the event
   */
  public void treeNodesChanged(final TreeModelEvent e)
  {
    // Get the changed node
    CodeNode node =
      (CodeNode) (e.getTreePath().getLastPathComponent());
    
    /*
     * If the event lists children, then the changed
     * node is the child of the node we've already
     * gotten.  Otherwise, the changed node and the
     * specified node are the same.
     */
    try
    {
      int index = e.getChildIndices()[0];
      node = (CodeNode) (node.getChildAt(index));
    }
    catch (NullPointerException npe)
    {
      System.err.println("Null Pointer: " + npe.getMessage());
    }
  }
  
  
  /**
   * A node has been inserted.
   * 
   * @param e the event
   */
  public void treeNodesInserted(final TreeModelEvent e)
  {
    // Nothing to do here
  }
  
  
  /**
   * A node has been removed.
   * 
   * @param e the event
   */
  public void treeNodesRemoved(final TreeModelEvent e)
  {
    // Nothing to do here
  }
  
  
  /**
   * The tree structure has changed.
   * 
   * @param e the event
   */
  public void treeStructureChanged(final TreeModelEvent e)
  {
    // Nothing to do here
  }
}

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

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Encapsulation of a tree node.
 * 
 * @author Mike Wallace
 * @version 1.0
 */
public final class CodeNode extends DefaultMutableTreeNode
{
  /**
   * The script text.
   */
  private String script = null;
  
  /**
   * Serial version ID.
   */
  private static final long serialVersionUID = 1L;
  
  
  /**
   * Default constructor.
   */
  @SuppressWarnings("unused")
  private CodeNode()
  {
    super();
  }
  
  
  /**
   * Constructor.
   * 
   * @param title the node title
   */
  public CodeNode(final String title)
  {
    super(title);
  }
  
  
  /**
   * Set the script for this node.
   * 
   * @param scriptNew the new value for the script
   */
  public void setScript(final String scriptNew)
  {
    script = scriptNew;
  }
  
  
  /**
   * Return the script for this node.
   * 
   * @return the script
   */
  public String getScript()
  {
    return script;
  }
  
  
  /**
   * Write out the tree, starting with node, and all children.
   * 
   * @return the String with the output
   */
  public String recurseToString()
  {
    // Declare the string builder object
    StringBuilder sb = new StringBuilder(200);
    
    // Iterate over this node and all of its children
    recurseToString(this, sb, 0);
    
    // Return the string
    return sb.toString();
  }
  
  
  /**
   * Recurse over a node and all of its children.  The
   * output string is appended to sb.
   * 
   * @param node the parent node to start with
   * @param sb the string builder to append to
   * @param indent the amount to indent
   */
  private void recurseToString(final CodeNode node,
                               final StringBuilder sb,
                               final int indent)
  {
    // Add leading spaces
    for (int j = 0; j < indent; ++j)
    {
      sb.append(' ');
    }
    
    // Add the node's title
    sb.append(node.getUserObject().toString()).append('\n');
    
    // Get the number of children and iterate over them
    final int childCount = node.getChildCount();
    for (int i = 0; i < childCount; ++i)
    {
      // Get the current child node
      final CodeNode child = (CodeNode) node.getChildAt(i);
      
      // Call this method again and print the child and its children
      recurseToString(child, sb, indent + 2);
    }
  }
}

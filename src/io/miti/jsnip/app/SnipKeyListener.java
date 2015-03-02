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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.nexagis.jsnip.app.ISnippetEvent;

/**
 * Class to handle listening to particular key presses
 * in the tree.
 * 
 * @author Mike Wallace
 * @version 1.0
 */
public final class SnipKeyListener extends KeyAdapter
{
  /**
   * The event handler.
   */
  private ISnippetEvent eventHandler = null;
  
  
  /**
   * Default constructor.
   */
  public SnipKeyListener()
  {
    super();
  }
  
  
  /**
   * Default constructor.
   * 
   * @param handler the event handler
   */
  public SnipKeyListener(final ISnippetEvent handler)
  {
    super();
    eventHandler = handler;
  }
  
  
  /**
   * Override the keyPressed method.
   * 
   * @param e the key event
   */
  @Override
  public void keyPressed(final KeyEvent e)
  {
    // Verify the handler is set
    if (eventHandler == null)
    {
      return;
    }
    
    // Check for key codes of interest
    if (e.getKeyCode() == KeyEvent.VK_INSERT)
    {
      // Add a child node to the tree
      eventHandler.performNodeNew();
    }
    else if (e.getKeyCode() == KeyEvent.VK_DELETE)
    {
      // Delete the node
      eventHandler.performNodeDelete();
    }
  }
}

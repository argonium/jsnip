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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * Handler class for menu and toolbar actions.
 * 
 * @author Mike Wallace
 * @version 1.0
 */
public final class SnippetActionHandler extends AbstractAction
{
  /**
   * Serial ID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Open a file.
   */
  public static final int FILE_OPEN = 1;
  
  /**
   * Save to file.
   */
  public static final int FILE_SAVE = 2;
  
  /**
   * Save to a new file.
   */
  public static final int FILE_SAVE_AS = 3;
  
  /**
   * Close the file.
   */
  public static final int FILE_NEW = 4;
  
  /**
   * Create a node.
   */
  public static final int NODE_NEW = 5;
  
  /**
   * Edit a node.
   */
  public static final int NODE_EDIT = 6;
  
  /**
   * Delete a node.
   */
  public static final int NODE_DELETE = 7;
  
  /**
   * Execute a script.
   */
  public static final int EXEC_SCRIPT = 8;
  
  /**
   * Stop a script.
   */
  public static final int STOP_SCRIPT = 9;
  
  /**
   * Show the About info.
   */
  public static final int ABOUT_INFO = 10;
  
  /**
   * Exit the application.
   */
  public static final int EXIT_APP = 11;
  
  /**
   * Copy the name of the selected node to the clipboard.
   */
  public static final int COPY_NAME_TO_CB = 12;
  
  /**
   * Copy the clipboard string to the name of the node.
   */
  public static final int COPY_NAME_FROM_CB = 13;
  
  /**
   * Rename the node to the current date.
   */
  public static final int RENAME_TO_DATE = 14;
  
  /**
   * Rename the node to the current time.
   */
  public static final int RENAME_TO_TIME = 15;
  
  /**
   * Rename the node to the current date/time.
   */
  public static final int RENAME_TO_DATE_TIME = 16;
  
  /**
   * Constant for renaming a node to the clipboard string.
   */
  public static final int NODE_NAME_CB = 1;
  
  /**
   * Constant for renaming a node to the date.
   */
  public static final int NODE_NAME_DATE = 2;
  
  /**
   * Constant for renaming a node to the time.
   */
  public static final int NODE_NAME_TIME = 3;
  
  /**
   * Constant for renaming a node to the date and time.
   */
  public static final int NODE_NAME_DATE_TIME = 4;
  
  /**
   * The type of handler.
   */
  private int handlerType = 0;
  
  
  /**
   * The handler to call for events.
   */
  private ISnippetEvent handler = null;

  
  /**
   * Default constructor.
   */
  @SuppressWarnings("unused")
  private SnippetActionHandler()
  {
    super();
  }
  
  
  /**
   * Constructor.
   * 
   * @param name the name
   */
  public SnippetActionHandler(final String name)
  {
    super(name);
  }
  
  
  /**
   * Constructor.
   * 
   * @param name the name
   * @param icon the icon
   * @param handlerInstance the handler instance
   * @param buttonType the type of handler
   */
  public SnippetActionHandler(final String name,
                              final Icon icon,
                              final ISnippetEvent handlerInstance,
                              final int buttonType)
  {
    // Call the parent constructor
    super(name, icon);
    
    // Save the additional arguments
    handler = handlerInstance;
    handlerType = buttonType;
  }
  
  
  /**
   * Invoke the correct method.
   * 
   * @param evt perform the action corresponding to the handler type
   */
  public void actionPerformed(final ActionEvent evt)
  {
    // Check the handler type
    switch (handlerType)
    {
      case FILE_OPEN:
        handler.performFileOpen();
        break;
      
      case FILE_SAVE:
        handler.performFileSave();
        break;
      
      case FILE_SAVE_AS:
        handler.performFileSaveAs();
        break;
      
      case FILE_NEW:
        handler.performFileNew();
        break;
      
      case NODE_NEW:
        handler.performNodeNew();
        break;
      
      case NODE_EDIT:
        handler.performNodeEdit();
        break;
      
      case NODE_DELETE:
        handler.performNodeDelete();
        break;
      
      case EXEC_SCRIPT:
        handler.execScript();
        break;
        
      case STOP_SCRIPT:
        handler.stopScript();
        break;
      
      case ABOUT_INFO:
        handler.showAboutInfo();
        break;
        
      case EXIT_APP:
        handler.exitApp();
        break;
        
      case COPY_NAME_TO_CB:
        handler.copyNameToCB();
        break;
      
      case COPY_NAME_FROM_CB:
        handler.renameNodeTo(NODE_NAME_CB);
        break;
        
      case RENAME_TO_DATE:
        handler.renameNodeTo(NODE_NAME_DATE);
        break;
        
      case RENAME_TO_TIME:
        handler.renameNodeTo(NODE_NAME_TIME);
        break;
        
      case RENAME_TO_DATE_TIME:
        handler.renameNodeTo(NODE_NAME_DATE_TIME);
        break;
      
      default:
        throw new RuntimeException("Unhandled event type");
    }
  }
}

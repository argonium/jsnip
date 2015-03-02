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

/**
 * Interface for the menu and toolbar actions.
 * 
 * @author Mike Wallace
 * @version 1.0
 */
public interface ISnippetEvent
{
  /**
   * Open a file.
   */
  void performFileOpen();
  
  /**
   * Save a file.
   */
  void performFileSave();
  
  /**
   * Save a file as.
   */
  void performFileSaveAs();
  
  /**
   * Create a file.
   */
  void performFileNew();
  
  /**
   * Create a node.
   */
  void performNodeNew();
  
  /**
   * Edit a node.
   */
  void performNodeEdit();
  
  /**
   * Delete a node.
   */
  void performNodeDelete();
  
  /**
   * Execute a script.
   */
  void execScript();
  
  /**
   * Stop script execution.
   */
  void stopScript();
  
  /**
   * Show the About box.
   */
  void showAboutInfo();
  
  /**
   * Exit the application.
   */
  void exitApp();
  
  /**
   * Copy the name of the node to the clipboard.
   */
  void copyNameToCB();
  
  /**
   * Rename the node.
   * 
   * @param nodeNameType how to rename the node
   */
  void renameNodeTo(int nodeNameType);
}

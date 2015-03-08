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

import io.miti.ui.component.Factory;
import io.miti.ui.panel.SimpleInternalFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Main application class for JSnip.
 * 
 * @author Mike Wallace
 * @version 1.0
 */
public final class App implements ISnippetEvent, WindowListener,
                                  MouseListener, TreeHandler
{
  /**
   * The name of the properties file.
   */
  private static final String PROP_FILE_NAME = "jsnip.prop";
  
  /**
   * The name of the icon properties file.
   */
  private static final String ICON_PROP_FILE_NAME = "jsnip-icons.prop";
  
  /**
   * The name of the icon properties file, as used to open it.
   */
  private String iconPropFileName = null;
  
  /**
   * The properties object.
   */
  private Properties appProps = null;
  
  /**
   * The application frame.
   */
  private JFrame frame = null;
  
  /**
   * The output text area.
   */
  private JTextPane tpOutput = null;
  
  /**
   * The input text area.
   */
  private JTextArea taInput = null;
  
  /**
   * The tree of snippets.
   */
  private JTree tree = null;
  
  /**
   * Whether the current file contents have changed.
   */
  private boolean fileChanged = false;
  
  /**
   * The name of the currently open filename.
   */
  private String currentFileName = null;
  
  /**
   * The root node of the tree.
   */
  private CodeItemNode rootNode = null;
  
  /**
   * The menu item for creating a new node.
   */
  private JMenuItem itemNewNode = null;
  
  /**
   * The menu item for editing a node.
   */
  private JMenuItem itemEditNode = null;
  
  /**
   * The menu item for deleting a node.
   */
  private JMenuItem itemDeleteNode = null;
  
  /**
   * The popup menu item for creating a new node.
   */
  private JMenuItem popupNewNode = null;
  
  /**
   * The popup menu item for editing a node.
   */
  private JMenuItem popupEditNode = null;
  
  /**
   * The popup menu item for deleting a node.
   */
  private JMenuItem popupDeleteNode = null;
  
  /**
   * The menu item to execute a script.
   */
  private JMenuItem itemExec = null;
  
  /**
   * The menu item to stop a script.
   */
  private JMenuItem itemStop = null;
  
  /**
   * The tree's popup menu.
   */
  private JPopupMenu popupMenuTree = null;
  
  /**
   * The script's popup menu.
   */
  private JPopupMenu popupMenuScript = null;
  
  /**
   * The script's popup menu for cutting text.
   */
  private JMenuItem popupCut = null;
  
  /**
   * The script's popup menu for copying text.
   */
  private JMenuItem popupCopy = null;
  
  /**
   * The script's popup menu for pasting text.
   */
  private JMenuItem popupPaste = null;
  
  /**
   * The child menu for renaming a node.
   */
  private JMenu childMenu = null;
  
  /**
   * The button for creating a new node.
   */
  private JButton btnNewNode = null;
  
  /**
   * The button for editing a node.
   */
  private JButton btnEditNode = null;
  
  /**
   * The button for deleting a node.
   */
  private JButton btnDelNode = null;
  
  /**
   * Execute a script.
   */
  private JButton btnPlay = null;
  
  /**
   * Stop a script.
   */
  private JButton btnStop = null;
  
  /**
   * The tree model.
   */
  private DefaultTreeModel treeModel = null;
  
  /**
   * A copy of the current node's original script.
   */
  private String scriptBackup = null;
  
  /**
   * The current directory.
   */
  private String currentDirectory = ".";
  
  /**
   * Thread that the script runs in.
   */
  private SwingWorker worker = null;
  
  /**
   * Boolean that the worker thread is running.
   */
  private boolean isWorking = false;
  
  /**
   * Save a pointer to standard out.
   */
  private final PrintStream standardOut = System.out;
  
  /**
   * Save a point to standard error.
   */
  private final PrintStream standardErr = System.err;
  
  /**
   * Output stream holder for standard out.
   */
  private PanelOutputStream posOut = null;
  
  /**
   * Output stream holder for standard error.
   */
  private PanelOutputStream posErr = null;
  
  /**
   * The font for the application.
   */
  private Font appFont = null;
  
  /**
   * Flag for how to open the data file.  If true, the data file
   * is opened using via a resource stream.  If false, the file
   * is just opened normally.
   */
  private boolean bOpenDataFromJar = false;
  
  
  /**
   * Default constructor.
   */
  public App()
  {
    super();
  }
  
  
  /**
   * Write a message to standard error.
   * 
   * @param msg the message to write
   */
  private static void writeErr(final String msg)
  {
    System.err.println(msg);
  }
  
  
  /**
   * Create the application's GUI.
   */
  public void createGUI()
  {
    // Initialize the font
    initFont();
    
    // Use the default look and feel
    initLookAndFeel();
    
    // Create and set up the window.
    frame = new JFrame("JSnip - Run Java snippets");
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(this);
    
    // Check how to open the file embedded in the jar
    checkInputFileSource();
    
    // Set the size and center it on the screen
    frame.setSize(new java.awt.Dimension(800, 600));
    centerOnScreen();
    
    // Initialize the menu bar
    initMenuBar();
    
    // Initialize the popup menus
    initPopupMenuTree();
    initPopupMenuScript();
    
    // Create and set up the toolbar
    initToolBar();
    
    // Generate the GUI and add it to the frame
    buildUI();
    
    // Set up the output streams
    posOut = new PanelOutputStream(tpOutput, "regular");
    posErr = new PanelOutputStream(tpOutput, "error");
    
    // Display the window.
    // frame.pack();
    frame.setVisible(true);
    
    // Open the last-opened file
    appProps = new Properties();
    getAppProps();
    
    // Request the focus for the tree
    tree.requestFocusInWindow();
  }
  
  
  /**
   * Initialize the font to use.
   */
  private void initFont()
  {
    // Construct the font
    appFont = new Font("sansserif", Font.PLAIN, 11);
  }
  
  
  /**
   * Check how the application is run and save information
   * about the input file.
   */
  private void checkInputFileSource()
  {
    // See if we can find the input file at the root
    final URL url = getClass().getResource("/" + ICON_PROP_FILE_NAME);
    if (url != null)
    {
      // We're running in a jar file
      iconPropFileName = "/" + ICON_PROP_FILE_NAME;
      bOpenDataFromJar = true;
    }
    else
    {
      // We're not running in a jar file
      iconPropFileName = ICON_PROP_FILE_NAME;
      bOpenDataFromJar = false;
    }
  }
  
  
  /**
   * Construct the user interface.
   */
  private void buildUI()
  {
    // Set up the right-side split pane (input, output)
    JSplitPane spRight = Factory.createStrippedSplitPane(
        JSplitPane.VERTICAL_SPLIT,
        initInputPanel(),
        initOutputPanel(),
        0.5f);
    spRight.setDividerSize(3);
    spRight.setDividerLocation(220);
    spRight.setContinuousLayout(true);
    
    // Set up the split pane (tree on the left, other split
    // pane on the right)
    JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            initTreePanel(),
            spRight);
    sp.setDividerSize(3);
    sp.setDividerLocation(200);
    sp.setResizeWeight(0.0);
    sp.setContinuousLayout(true);
    
    // Set operation to do when user presses enter.
    // Default is GO.
    // frame.getRootPane().setDefaultButton(btnGo);
    
    // Add the main panel to the content pane
    frame.getContentPane().add(sp, BorderLayout.CENTER);
  }
  
  
  /**
   * Initialize the tree panel.
   * 
   * @return the panel
   */
  private JComponent initTreePanel()
  {
    // Create the tree panel and set the background color
    JPanel treePanel = new JPanel(new GridLayout(1, 1), true);
    treePanel.setBackground(java.awt.Color.WHITE);
    
    treePanel.setFont(appFont);
    
    // Initialize the tree
    initializeTree();
    
    // Add a listener so we know when the selection changes
    tree.addTreeSelectionListener(new TreeSelectionListener()
    {
      public void valueChanged(final TreeSelectionEvent evt)
      {
        handleTreeSelectionEvent(evt);
      }
    });
    
    // Add the tree to the panel
    JScrollPane scroll = new JScrollPane(tree);
    treePanel.add(scroll);
    
    // Return the tree panel
    return treePanel;
  }
  
  
  /**
   * Handle a tree selection event.
   * 
   * @param evt the selection event
   */
  private void handleTreeSelectionEvent(final TreeSelectionEvent evt)
  {
    // Handle a change to the selection.  When one node is
    // deselected and another selected, the selection is
    // first in the evt argument.
    
    // Get all nodes whose selection status has changed
    TreePath[] paths = evt.getPaths();
    
    // Save the current contents of the input pane
    String scriptValue = taInput.getText();
    
    // This will hold the new value for the scriptBackup
    String updatedScriptBackup = scriptBackup;
    
    // The selected has changed.  Clear taInput.
    taInput.setText("");
    
    // Whether we've updated taInput yet
    boolean setInput = false;
    
    // Iterate through all affected nodes
    for (int i = 0; i < paths.length; i++)
    {
      if (evt.isAddedPath(i))
      {
        // Only one item can be selected
        if (!setInput)
        {
          // Record that we've updated taInput.  Don't do it again
          // in this loop.
          setInput = true;
          
          // Now get the text for the selected item (if any)
          // and show it in taInput
          TreePath path = paths[i];
          if (path.getPathCount() > 0)
          {
            // Get the selected node
            CodeItemNode node =
              (CodeItemNode) path.getLastPathComponent();
            
            // Save the script
            updatedScriptBackup = node.getScript();
            
            // Check if it's null
            if (updatedScriptBackup != null)
            {
              // Update the input field
              taInput.setText(updatedScriptBackup);
              
              // Set the caret to the first character
              taInput.setCaretPosition(0);
            }
          }
          else
          {
            scriptBackup = null;
          }
        }
      }
      else
      {
        // This node has been deselected.  Save any changes to the text.
        TreePath path = paths[i];
        
        // See if the node changed
        if (checkNodeChanged(path, scriptValue))
        {
          // It did, so update the global variable
          fileChanged = true;
        }
      }
    }
    
    // Save the script backup
    scriptBackup = updatedScriptBackup;
    
    // Update the Edit buttons/toolbars
    checkNodeSelection();
  }
  
  
  /**
   * Check whether to enable or disable certain menu
   * items and toolbar buttons, based on the selected
   * node.
   */
  private void checkNodeSelection()
  {
    // Update New/Edit/Delete:
    //   New - on iff a node is selected
    //   Edit - on iff a node is selected and it's not the root
    //   Delete - on iff a node is selected and it's not the root
    
    // Get the selected node
    final TreePath parentPath = tree.getSelectionPath();
    if (parentPath == null)
    {
      // Nothing selected, so disable the buttons and menu items
      itemNewNode.setEnabled(false);
      itemEditNode.setEnabled(false);
      itemDeleteNode.setEnabled(false);
      
      popupNewNode.setEnabled(false);
      popupEditNode.setEnabled(false);
      popupDeleteNode.setEnabled(false);
      
      btnNewNode.setEnabled(false);
      btnEditNode.setEnabled(false);
      btnDelNode.setEnabled(false);
      
      childMenu.setEnabled(false);
      
      // Now return
      return;
    }
    else
    {
      // Users can create a new node
      itemNewNode.setEnabled(true);
      btnNewNode.setEnabled(true);
      popupNewNode.setEnabled(true);
      
      // Get the current node
      CodeItemNode currNode =
        (CodeItemNode) (parentPath.getLastPathComponent());
      
      // Save whether this is the root node
      boolean isRoot = (currNode.getParent() == null);
      
      // Only enable these items if the selected node
      // is not the root
      itemEditNode.setEnabled(!isRoot);
      itemDeleteNode.setEnabled(!isRoot);
      btnEditNode.setEnabled(!isRoot);
      btnDelNode.setEnabled(!isRoot);
      childMenu.setEnabled(!isRoot);
      popupEditNode.setEnabled(!isRoot);
      popupDeleteNode.setEnabled(!isRoot);
    }
  }
  
  
  /**
   * Initialize the Input panel.
   * 
   * @return the Input panel
   */
  private JComponent initInputPanel()
  {
    JPanel results = new JPanel(new BorderLayout());
    results.setMinimumSize(new Dimension(100, 100));
    results.setPreferredSize(new Dimension(500, 300));
    
    taInput = new JTextArea();
    
    taInput.setFont(appFont);
    
    JScrollPane scrollPane = new JScrollPane(taInput);
    results.add(scrollPane);
    
    // Add a mouse listener to the input window
    taInput.addMouseListener(this);
    
    SimpleInternalFrame sif = new SimpleInternalFrame("Input");
    sif.setPreferredSize(new Dimension(300, 500));
    sif.add(results);
    
    return sif;
  }
  
  
  /**
   * Initialize the Output panel.
   * 
   * @return the output panel
   */
  private JComponent initOutputPanel()
  {
    // Create the output panel and set the size
    JPanel results = new JPanel(new BorderLayout());
    results.setMinimumSize(new Dimension(100, 100));
    results.setPreferredSize(new Dimension(500, 300));
    
    // Border for the pane
    final int borderSize = 2;
    
    // Create the text output area
    tpOutput = new JTextPane();
    tpOutput.setOpaque(false);
    tpOutput.setBorder(BorderFactory.createEmptyBorder(borderSize,
        borderSize, borderSize, borderSize));
    
    StyledDocument doc = tpOutput.getStyledDocument();
    addStylesToDocument(doc);
    
    // Set up the scroll bar
    JScrollPane scrollPane = new JScrollPane(tpOutput);
    scrollPane.setHorizontalScrollBarPolicy(
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    results.add(scrollPane);
    
    // Create the frame for the panel
    SimpleInternalFrame sif = new SimpleInternalFrame("Output");
    sif.setPreferredSize(new Dimension(300, 500));
    sif.add(results);
    
    // Return the frame
    return sif;
  }
  
  
  /**
   * Add styles to the document.
   * 
   * @param doc the StyledDocument to add styles to
   */
  protected static void addStylesToDocument(final StyledDocument doc)
  {
    // Initialize some styles
    Style def = StyleContext.getDefaultStyleContext().
                    getStyle(StyleContext.DEFAULT_STYLE);
    
    // Define the regular style
    Style regular = doc.addStyle("regular", def);
    StyleConstants.setFontFamily(def, "SansSerif");
    StyleConstants.setFontSize(def, 12);
    StyleConstants.setLineSpacing(def, 1.5f);
    StyleConstants.setForeground(def, java.awt.Color.BLACK);
    
    // Define the error style (red)
    Style s = doc.addStyle("error", regular);
    StyleConstants.setForeground(s, java.awt.Color.RED);
  }
  
  
  /**
   * Initialize the popup menu for the script window.
   */
  private void initPopupMenuScript()
  {
    // Create the popup menu
    popupMenuScript = new JPopupMenu();
    
    // Add the Cut menu item
    popupCut = new JMenuItem("Cut");
    popupCut.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        // Cut the selected text
        taInput.cut();
      }
    });
    popupMenuScript.add(popupCut);
    
    // Add the Copy menu item
    popupCopy = new JMenuItem("Copy");
    popupCopy.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        // Copy the selected text
        taInput.copy();
      }
    });
    popupMenuScript.add(popupCopy);
    
    // Add the Paste menu item
    popupPaste = new JMenuItem("Paste");
    popupPaste.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        // Paste the selected text
        taInput.paste();
      }
    });
    popupMenuScript.add(popupPaste);
  }
  
  
  /**
   * Initialize the popup menu for the tree.
   */
  private void initPopupMenuTree()
  {
    // Create the popup menu
    popupMenuTree = new JPopupMenu();
    
    // Add the New Node menu item
    SnippetActionHandler actionNewNode = new SnippetActionHandler("New Node", null,
        this, SnippetActionHandler.NODE_NEW);
    popupNewNode = new JMenuItem(actionNewNode);
    popupNewNode.setMnemonic(KeyEvent.VK_N);
    popupMenuTree.add(popupNewNode);
    
    // Add the Edit Node menu item
    SnippetActionHandler actionEditNode = new SnippetActionHandler("Edit Node", null,
        this, SnippetActionHandler.NODE_EDIT);
    popupEditNode = new JMenuItem(actionEditNode);
    popupEditNode.setMnemonic(KeyEvent.VK_E);
    popupMenuTree.add(popupEditNode);
    
    // Add the Delete Node menu item
    SnippetActionHandler actionDeleteNode =
        new SnippetActionHandler("Delete Node", null, this,
                                 SnippetActionHandler.NODE_DELETE);
    popupDeleteNode = new JMenuItem(actionDeleteNode);
    popupDeleteNode.setMnemonic(KeyEvent.VK_D);
    popupMenuTree.add(popupDeleteNode);
    
    // Copy node name to clipboard
    SnippetActionHandler actionCopyToCB = new SnippetActionHandler(
        "Copy Name to Clipboard", null, this,
        SnippetActionHandler.COPY_NAME_TO_CB);
    JMenuItem itemCopyToCB = new JMenuItem(actionCopyToCB);
    popupMenuTree.add(itemCopyToCB);
    
    // Create the child menu
    childMenu = new JMenu("Rename node to...");
    
    // Rename node to name in clipboard
    SnippetActionHandler actionCopyFromCB = new SnippetActionHandler(
        "Name in Clipboard", null, this,
        SnippetActionHandler.COPY_NAME_FROM_CB);
    JMenuItem itemCopyFromCB = new JMenuItem(actionCopyFromCB);
    childMenu.add(itemCopyFromCB);
    
    // Rename node to date
    SnippetActionHandler actionRenameToDate = new SnippetActionHandler(
        "Date", null, this,
        SnippetActionHandler.RENAME_TO_DATE);
    JMenuItem itemRenameToDate = new JMenuItem(actionRenameToDate);
    childMenu.add(itemRenameToDate);
    
    // Rename node to time
    SnippetActionHandler actionRenameToTime = new SnippetActionHandler(
        "Time", null, this,
        SnippetActionHandler.RENAME_TO_TIME);
    JMenuItem itemRenameToTime = new JMenuItem(actionRenameToTime);
    childMenu.add(itemRenameToTime);
    
    // Rename node to date and time
    SnippetActionHandler actionRenameToDateTime = new SnippetActionHandler(
        "Date and Time", null, this,
        SnippetActionHandler.RENAME_TO_DATE_TIME);
    JMenuItem itemRenameToDateTime = new JMenuItem(actionRenameToDateTime);
    childMenu.add(itemRenameToDateTime);
    
    // Add the child menu to the popup menu
    popupMenuTree.add(childMenu);
  }
  
  
  /**
   * Initialize the Menu Bar and associate actions.
   */
  private void initMenuBar()
  {
    // Create the menu bar
    JMenuBar menuBar = new JMenuBar();
    
    menuBar.setFont(appFont);
    
    /*
     * File Menu
     */
    JMenu menuFile = new JMenu("File");
    menuFile.setMnemonic(KeyEvent.VK_F);
    menuBar.add(menuFile);
    
    // Add the File New menu item
    SnippetActionHandler actionFileNew = new SnippetActionHandler("New", null,
        this, SnippetActionHandler.FILE_NEW);
    JMenuItem itemFileNew = new JMenuItem(actionFileNew);
    itemFileNew.setMnemonic(KeyEvent.VK_N);
    menuFile.add(itemFileNew);
    
    // Add the File Open menu item
    SnippetActionHandler actionFileOpen = new SnippetActionHandler("Open", null,
        this, SnippetActionHandler.FILE_OPEN);
    JMenuItem itemFileOpen = new JMenuItem(actionFileOpen);
    itemFileOpen.setMnemonic(KeyEvent.VK_O);
    menuFile.add(itemFileOpen);
    
    // Add the File Save menu item
    SnippetActionHandler actionSave = new SnippetActionHandler("Save", null,
        this, SnippetActionHandler.FILE_SAVE);
    JMenuItem itemSave = new JMenuItem(actionSave);
    itemSave.setMnemonic(KeyEvent.VK_S);
    menuFile.add(itemSave);
    
    // Add the File Save As menu item
    SnippetActionHandler actionSaveAs = new SnippetActionHandler("Save As", null,
        this, SnippetActionHandler.FILE_SAVE_AS);
    JMenuItem itemSaveAs = new JMenuItem(actionSaveAs);
    itemSaveAs.setMnemonic(KeyEvent.VK_A);
    menuFile.add(itemSaveAs);
    
    // Add a separator
    menuFile.addSeparator();
    
    // Add the Execute menu item
    SnippetActionHandler actionExec = new SnippetActionHandler("Execute", null,
        this, SnippetActionHandler.EXEC_SCRIPT);
    itemExec = new JMenuItem(actionExec);
    itemExec.setMnemonic(KeyEvent.VK_X);
    // Set up F5 as an accelerator for this event
    itemExec.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
    menuFile.add(itemExec);
    
    // Add the Stop menu item
    SnippetActionHandler actionStop = new SnippetActionHandler("Stop Script",
        null, this, SnippetActionHandler.STOP_SCRIPT);
    itemStop = new JMenuItem(actionStop);
    itemStop.setMnemonic(KeyEvent.VK_S);
    itemStop.setEnabled(false);
    menuFile.add(itemStop);
    
    // Add a separator
    menuFile.addSeparator();
    
    // Add the Exit menu item
    SnippetActionHandler actionExit = new SnippetActionHandler("Quit", null,
        this, SnippetActionHandler.EXIT_APP);
    JMenuItem itemExit = new JMenuItem(actionExit);
    itemExit.setMnemonic(KeyEvent.VK_Q);
    menuFile.add(itemExit);
    
    /*
     * Tree menu
     */
    JMenu menuTree = new JMenu("Tree");
    menuTree.setMnemonic(KeyEvent.VK_T);
    menuBar.add(menuTree);
    
    // Add the New Node menu item
    SnippetActionHandler actionNewNode = new SnippetActionHandler("New Node", null,
        this, SnippetActionHandler.NODE_NEW);
    itemNewNode = new JMenuItem(actionNewNode);
    itemNewNode.setMnemonic(KeyEvent.VK_N);
    menuTree.add(itemNewNode);
    
    // Add the Edit Node menu item
    SnippetActionHandler actionEditNode = new SnippetActionHandler("Edit Node", null,
        this, SnippetActionHandler.NODE_EDIT);
    itemEditNode = new JMenuItem(actionEditNode);
    itemEditNode.setMnemonic(KeyEvent.VK_E);
    menuTree.add(itemEditNode);
    
    // Add the Delete Node menu item
    SnippetActionHandler actionDeleteNode =
        new SnippetActionHandler("Delete Node", null, this,
                                 SnippetActionHandler.NODE_DELETE);
    itemDeleteNode = new JMenuItem(actionDeleteNode);
    itemDeleteNode.setMnemonic(KeyEvent.VK_D);
    menuTree.add(itemDeleteNode);
    
    /*
     * Help menu item
     */
    JMenu menuHelp = new JMenu("Help");
    menuHelp.setMnemonic(KeyEvent.VK_H);
    menuBar.add(menuHelp);
    
    // Add the About menu item
    SnippetActionHandler actionAbout = new SnippetActionHandler("About", null,
        this, SnippetActionHandler.ABOUT_INFO);
    JMenuItem itemAbout = new JMenuItem(actionAbout);
    itemAbout.setMnemonic(KeyEvent.VK_A);
    menuHelp.add(itemAbout);
    
    // Add the menu bar on the frame
    frame.setJMenuBar(menuBar);
  }
  
  
  /**
   * Execute the script in the input panel and send the output
   * to the output panel.
   */
  public void execScript()
  {
    // Empty the output area
    tpOutput.setText("");
    
    // Get the input text to process
    final String s = taInput.getText();
    
    // See if any text was entered
    if ((s == null) || (s.length() < 1))
    {
      // No text to process
      return;
    }
    
    // Redirect standard output to the output panel
    System.setOut(new PrintStream(posOut, true));
    
    // Redirect standard error to the output panel
    System.setErr(new PrintStream(posErr, true));
    
    // Disable the Play button and menu item, and enable
    // the Stop button and menu item
    updatePlayControls(true);
    
    // Mark that the thread is running
    isWorking = true;
    
    // Construct a BeanShell interpreter
    final Interpreter i = new Interpreter();
    
    // Extend SwingWorker to handle processing the script
    worker = new SwingWorker()
    {
      public Object construct()
      {
        // Evaluate the text entered by the user
        try
        {
          // Pass it to Beanshell and print any output
          Object result = i.eval(s);
          if (result != null)
          {
            // If anything was returned by BeanShell, add
            // it to the output pane
            addStringToTextPane(result.toString() + "\n",
                                "regular");
          }
          
          // Flush the output streams
          System.out.flush();
          System.err.flush();
        }
        catch (EvalError e)
        {
          // Show the error message
          addStringToTextPane("Error: " + e.getMessage() + "\n",
                              "error");
        }
        
        // Have to return something
        return null;
      }
      
      public void finished()
      {
        // Update the script controls
        updatePlayControls(false);
        
        // Return standard output to the original output stream
        System.setOut(standardOut);
        System.setErr(standardErr);
        
        // The thread is no longer running
        isWorking = false;
      }
    };
    
    // Start the thread
    worker.start();
  }
  
  
  /**
   * Interrupt the script.
   */
  public void stopScript()
  {
    // Check if a thread is running
    if ((worker == null) || (!isWorking))
    {
      return;
    }
    
    // Interrupt the thread.
    worker.interrupt();
    
    // Update the script controls
    updatePlayControls(false);
    
    // Return standard output and error to the original output streams
    System.setOut(standardOut);
    System.setErr(standardErr);
    
    // The thread is no longer running
    isWorking = false;
  }
  
  
  /**
   * Add a string to the styled document.
   * 
   * @param msg the string to append
   * @param style the style name
   */
  private void addStringToTextPane(final String msg,
                                   final String style)
  {
    // Force a repaint of the pane
    tpOutput.revalidate();
    tpOutput.repaint();
    
    // Sleep for a bit
    try
    {
      Thread.sleep(25);
    }
    catch (InterruptedException e1)
    {
      e1.printStackTrace();
    }
    
    // Get the styled document for the pane
    StyledDocument doc = tpOutput.getStyledDocument();
    
    // Insert the string
    try
    {
      doc.insertString(doc.getLength(), msg,
                       doc.getStyle(style));
    }
    catch (BadLocationException e)
    {
      // Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  
  /**
   * Update the controls for starting and interrupting
   * a script.
   * 
   * @param playing whether the script is playing
   */
  private void updatePlayControls(final boolean playing)
  {
    // If a script is running, disable the play button and menu item
    btnPlay.setEnabled(!playing);
    itemExec.setEnabled(!playing);
    
    // If a script is running, enable interrupting the script
    btnStop.setEnabled(playing);
    itemStop.setEnabled(playing);
  }
  
  
  /**
   * Initialize the tool bar.
   */
  private void initToolBar()
  {
    // Instantiate the toolbar
    JToolBar toolBar = new JToolBar("JSNIP");
    
    // Add buttons
    initToolBarButtons(toolBar);
    
    // Set options
    toolBar.setFloatable(false);
    toolBar.setRollover(true);
    
    // Add the toolbar to the content pane
    frame.getContentPane().add(toolBar, BorderLayout.NORTH);
  }
  
  
  /**
   * Load the buttons for the toolbar.
   * 
   * @param toolbar the toolbar to add buttons to
   */
  private void initToolBarButtons(final JToolBar toolbar)
  {
    // The properties file reference
    Properties prop = new Properties();
    final boolean result = loadProperties(prop, iconPropFileName, true);
    if (!result)
    {
      writeErr("Unable to load the toolbar buttons");
      return;
    }
    
    // Create the File New button
    JButton btnFileNew = createActionButton(prop, "button.file_new",
        SnippetActionHandler.FILE_NEW, "New", "New tree");
    toolbar.add(btnFileNew);
    
    // Create the File Open button
    JButton btnFileOpen = createActionButton(prop, "button.file_open",
        SnippetActionHandler.FILE_OPEN, "Open", "Open a file");
    toolbar.add(btnFileOpen);
    
    // Create the Save button
    JButton btnFileSave = createActionButton(prop, "button.file_save",
        SnippetActionHandler.FILE_SAVE, "Save", "Save to file");
    toolbar.add(btnFileSave);
    
    // Create the Save As button
    JButton btnFileSaveAs = createActionButton(prop, "button.file_save_as",
        SnippetActionHandler.FILE_SAVE_AS, "Save As", "Save to a new file");
    toolbar.add(btnFileSaveAs);
    
    // Add a separator
    toolbar.addSeparator();
    
    // Create the New Node button
    btnNewNode = createActionButton(prop, "button.node_new",
        SnippetActionHandler.NODE_NEW, "New Node", "Create a child node");
    toolbar.add(btnNewNode);
    
    // Create the Edit Node button
    btnEditNode = createActionButton(prop, "button.node_edit",
        SnippetActionHandler.NODE_EDIT, "Edit Node", "Edit the node title");
    toolbar.add(btnEditNode);
    
    // Create the Delete Node button
    btnDelNode = createActionButton(prop, "button.node_delete",
        SnippetActionHandler.NODE_DELETE, "Delete Node", "Delete the node");
    toolbar.add(btnDelNode);
    
    // Add a separator
    toolbar.addSeparator();
    
    // Create the Play button
    btnPlay = createActionButton(prop, "button.script_play",
        SnippetActionHandler.EXEC_SCRIPT, "Execute", "Execute the script");
    toolbar.add(btnPlay);
    
    // Create the Stop button
    btnStop = createActionButton(prop, "button.script_stop",
        SnippetActionHandler.STOP_SCRIPT, "Stop", "Stop the script");
    btnStop.setEnabled(false);
    toolbar.add(btnStop);
    
    // Add a separator
    toolbar.addSeparator();
    
    // Create the About button
    JButton btnAbout = createActionButton(prop, "button.about",
        SnippetActionHandler.ABOUT_INFO, "About", "About this application");
    toolbar.add(btnAbout);
    
    // Close the properties file
    prop = null;
  }
  
  
  /**
   * Create a button for the toolbar.
   * 
   * @param prop the Properties object
   * @param propValue the property value to read
   * @param buttonType the type of button
   * @param buttonText the button text
   * @param tooltipText the button tooltip text
   * @return the created button
   */
  private JButton createActionButton(final Properties prop,
                                     final String propValue,
                                     final int buttonType,
                                     final String buttonText,
                                     final String tooltipText)
  {
    // Load the icon
    final ImageIcon icon = loadIcon(prop, propValue);
    
    // Create our handler
    SnippetActionHandler handler = new SnippetActionHandler(buttonText,
                                          icon, this, buttonType);
    
    // Create our button
    JButton btn = new JButton(handler);
    
    // Set the tooltip text
    btn.setToolTipText(tooltipText);
    
    // If the icon was loaded successfully, clear the button text
    if (icon != null)
    {
      btn.setText(null);
    }
    
    // Return the button
    return btn;
  }
  
  
  /**
   * Load the icon with the name of propName in the properties
   * object prop.
   * 
   * @param prop the properties object
   * @param propName the property name
   * @return the icon image
   */
  private ImageIcon loadIcon(final Properties prop,
                             final String propName)
  {
    // The icon that gets returned
    ImageIcon icon = null;
    
    // Get the property value (location of the icon)
    String loc = prop.getProperty(propName);
    if (loc == null)
    {
      writeErr("Error: Could not find property " + propName
          + " in the properties file");
      return null;
    }
    
    // Load the resource
    URL url = this.getClass().getResource(loc);
    if (url != null)
    {
      icon = new ImageIcon(url);
    }
    else
    {
      writeErr("Error: Unable to open the file " + loc);
      return null;
    }
    
    return icon;
  }
  
  
  /**
   * Load the properties file.
   * 
   * @param prop the Properties object to load up
   * @param fileName the name of the file to open
   * @param canBeEmbedded whether the file might be embedded in the jar
   * @return the result of the operation
   */
  private boolean loadProperties(final Properties prop,
                                 final String fileName,
                                 final boolean canBeEmbedded)
  {
    // Store whether an error occurred
    boolean result = false;
    
    InputStream propStream = null;
    try
    {
      // Open the input stream.  If the file can be embedded
      // in the jar, and we've already determined that the
      // file is actually embedded in the jar, then open it
      // as a stream.  Otherwise, open it as a regular file.
      if (canBeEmbedded && bOpenDataFromJar)
      {
        // Open the file as a stream
        propStream = getClass().getResourceAsStream(fileName);
      }
      else
      {
        // Open the file as a file
        propStream = new FileInputStream(fileName);
      }
      
      // Check for an error
      if (propStream != null)
      {
        // Load the input stream
        prop.load(propStream);
        
        // It was successful
        result = true;
        
        // Close the stream
        propStream.close();
        propStream = null;
      }
    }
    catch (IOException ioe)
    {
      // The file was not found
      result = false;
    }
    finally
    {
      // Make sure we close the stream
      if (propStream != null)
      {
        try
        {
          propStream.close();
        }
        catch (IOException e)
        {
          System.err.println(e.getMessage());
        }
        
        propStream = null;
      }
    }
    
    // Return the result
    return result;
  }
  
  
  /**
   * Open a file and replace the tree contents.
   */
  public void performFileOpen()
  {
    // Check if the file changed
    if (!checkFileChanged())
    {
      // Cancel the open
      return;
    }
    
    // This will hold the input file pointer
    File inputFile = null;
    
    // Create a file chooser
    JFileChooser chooser = new JFileChooser();
    
    // Add the file filters
    chooser.addChoosableFileFilter(new SnipFilter());
    chooser.addChoosableFileFilter(chooser.getAcceptAllFileFilter());
    
    // Default to the current directory
    chooser.setCurrentDirectory(new File(currentDirectory));
    
    // Let the user open a file and get the return value
    int returnVal = chooser.showOpenDialog(frame);
    
    // Hold the file's directory temporarily
    String tempDirectory = null;
    
    // See if the user hit the "Open" button
    if (returnVal == JFileChooser.APPROVE_OPTION)
    {
       try
      {
         inputFile = chooser.getSelectedFile().getCanonicalFile();
         tempDirectory = chooser.getSelectedFile().getParent();
      }
      catch (IOException ioe)
      {
        JOptionPane.showMessageDialog(frame,
            "Error while opening: " + ioe.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        
        inputFile = null;
      }
    }
    else
    {
      // The user decided not to open the file
      return;
    }
    
    // Check if the user didn't want to open the input file, or
    // an error occurred
    if ((inputFile == null) || (inputFile.isDirectory()))
    {
      return;
    }
    
    // Clear the file-changed state
    fileChanged = false;
    
    // Save the new directory
    currentDirectory = tempDirectory;
    
    // Load the file
    loadFromFile(inputFile, true);
    
    // Set the root
    treeModel.setRoot(rootNode);
    
    // Initialize the tree
    resetTreeRoot(true);
  }
  
  
  /**
   * Save the tree to a file.
   */
  public void performFileSave()
  {
    // See if the name is null
    if (currentFileName == null)
    {
      performFileSaveAs();
    }
    else
    {
      // Check if the script changed
      checkNodeChanged();
      
      // Save the tree
      saveToFile(new File(currentFileName));
    }
  }
  
  
  /**
   * Check if the tree node changed.
   * 
   * @return whether the node changed
   */
  private boolean checkNodeChanged()
  {
    // Get the currently selected path
    final TreePath path = tree.getSelectionPath();
    
    // Return whether its script changed
    return checkNodeChanged(path, taInput.getText());
  }
  
  
  /**
   * Check if the tree node changed.
   * 
   * @param path the selected path
   * @param updatedScript the current script text
   * @return whether the node changed
   */
  private boolean checkNodeChanged(final TreePath path,
                                   final String updatedScript)
  {
    // Check if the path is null
    if (path == null)
    {
      // No node is selected
      return false;
    }
    
    // The return value
    boolean changed = false;
    
    // See if the node has a path
    if (path.getPathCount() > 0)
    {
      // Get the selected node
      final CodeItemNode node =
        (CodeItemNode) path.getLastPathComponent();
      
      // See if its script changed
      if ((scriptBackup == null) && (updatedScript == null))
      {
        // They're both null, so no change
        changed = false;
      }
      else if (scriptBackup == null)
      {
        // The backup is null is the new script is not null
        if (updatedScript.length() > 0)
        {
          changed = true;
        }
        else
        {
          changed = false;
        }
      }
      else if (updatedScript == null)
      {
        // The backup is not null, and the new script is null
        if (scriptBackup.length() > 0)
        {
          changed = true;
        }
        else
        {
          changed = false;
        }
      }
      else
      {
        // Neither are null
        changed = !(scriptBackup.equals(updatedScript));
      }
      
      // We've set the changed state, so handle it
      if (changed)
      {
        // The contents changed
        changed = true;
        
        // Save that the tree has changed
        fileChanged = true;
        
        // Update the node's script
        node.setScript(updatedScript);
        
        // Update the copy of scriptBackup
        scriptBackup = updatedScript;
      }
    }
    
    // Return whether there were any changes
    return changed;
  }
  
  
  /**
   * Check if the tree changed.
   * 
   * @return whether to continue with closing the tree
   */
  private boolean checkFileChanged()
  {
    // Check if the node changed
    boolean nodeChanged = checkNodeChanged();
    
    // See if the file or node changed
    if (fileChanged || nodeChanged)
    {
      // It has.  See if the user wants to save the changes.
      int result = JOptionPane.showConfirmDialog(frame,
                       "Save changes to the tree?",
                       "Tree has changed",
                       JOptionPane.YES_NO_CANCEL_OPTION);
      
      // Check the user's option
      if (result == JOptionPane.YES_OPTION)
      {
        // Save the file
        performFileSave();
      }
      else if (result == JOptionPane.CANCEL_OPTION)
      {
        // Cancel saving the file
        return false;
      }
    }
    
    // Continue closing the tree
    return true;
  }
  
  
  /**
   * Create a file.
   */
  public void performFileNew()
  {
    // Check if the file changed
    if (!checkFileChanged())
    {
      // Cancel the File New
      return;
    }
    
    // Delete the tree
    resetTreeRoot(false);
    
    // Clear the input and output screen
    taInput.setText("");
    tpOutput.setText("");
    scriptBackup = null;
    
    // Clear the file name
    currentFileName = null;
    
    // Clear that the file has changed
    fileChanged = false;
  }
  
  
  /**
   * Reset the tree.
   * 
   * @param showNewData whether the root node has data to show
   */
  private void resetTreeRoot(final boolean showNewData)
  {
    // Check if the root has data we want to show
    if (!showNewData)
    {
      // It does not, so remove all of the children
      rootNode.removeAllChildren();
      rootNode.setUserObject("Home");
      rootNode.setScript("");
    }
    
    // Reload the tree model
    treeModel.reload();
    
    // Select the root node
    tree.setSelectionRow(0);
    
    // Update the buttons and menu items
    checkNodeSelection();
  }
  
  
  /**
   * Initialize the tree.
   */
  private void initializeTree()
  {
    // Create our tree hierarchy
    rootNode = new CodeItemNode("Home");
    
    // Create the tree model
    treeModel = new DefaultTreeModel(rootNode);
    
    // Create the model listener
    treeModel.addTreeModelListener(new SnipTreeModelListener());
    
    // Initialize the tree
    tree = new DragTree(treeModel, this);
    
    // We want to show root handles
    tree.setShowsRootHandles(true);
    
    // Only allow one selection
    DefaultTreeSelectionModel selModel = new DefaultTreeSelectionModel();
    selModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.setSelectionModel(selModel);
    
    // Select the root node
    tree.setSelectionRow(0);
    
    // Add a mouse button and key listener to the tree
    tree.addMouseListener(this);
    tree.addKeyListener(new SnipKeyListener(this));
    
    // Update the buttons and menu items
    checkNodeSelection();
  }
  
  
  /**
   * Save a file as.
   */
  public void performFileSaveAs()
  {
    // Check if the script changed, and if so, save it
    checkNodeChanged();
    
    // This is the output file
    File outputFile = null;
    
    // Create a file chooser
    JFileChooser chooser = new JFileChooser();
    
    // Add the file filters
    chooser.addChoosableFileFilter(new SnipFilter());
    chooser.addChoosableFileFilter(chooser.getAcceptAllFileFilter());
    
    // Default to the current directory
    chooser.setCurrentDirectory(new File(currentDirectory));
    
    // Hold the file's directory temporarily
    String tempDirectory = null;
    
    // Let the user open a file and get the return value
    int returnVal = chooser.showSaveDialog(frame);
    
    // See if the user hit the "Open" button
    if (returnVal == JFileChooser.APPROVE_OPTION)
    {
       try
      {
         outputFile = chooser.getSelectedFile().getCanonicalFile();
         tempDirectory = chooser.getSelectedFile().getParent();
      }
      catch (IOException ioe)
      {
        JOptionPane.showMessageDialog(frame,
            "Error while saving file: " + ioe.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        
        outputFile = null;
      }
    }
    
    // Check if the user didn't want to open the input file, or
    // an error occurred
    if ((outputFile == null) || (outputFile.isDirectory()))
    {
      return;
    }
    
    // Save the new directory
    currentDirectory = tempDirectory;
    
    // Save the file
    saveToFile(outputFile);
    
    // Clear the file-changed state
    fileChanged = false;
  }
  
  
  /**
   * Save the current contents to a file.
   * 
   * @param outFile the output File object
   */
  private void saveToFile(final File outFile)
  {
    // Whether the data was successfully written
    boolean dataWritten = false;
    
    // Write the file
    ObjectOutputStream oos = null;
    try
    {
      // Create the output stream
      oos = new ObjectOutputStream(new FileOutputStream(outFile));
      
      // Write the data
      oos.writeObject(rootNode);
      
      // The tree is now saved
      fileChanged = false;
      
      // Clear the stream
      oos.close();
      oos = null;
      
      // The data was saved
      dataWritten = true;
    }
    catch (FileNotFoundException fnfe)
    {
      writeErr("File not found: " + fnfe.getMessage());
    }
    catch (IOException ioe)
    {
      writeErr("IOException: " + ioe.getMessage());
    }
    finally
    {
      if (oos != null)
      {
        try
        {
          oos.close();
        }
        catch (IOException ioe)
        {
          writeErr("IOException: " + ioe.getMessage());
        }
        
        oos = null;
      }
    }
    
    // Save the output file name
    try
    {
      currentFileName = outFile.getCanonicalPath();
    }
    catch (IOException ioe)
    {
      writeErr("Error getting the filename: " + ioe.getMessage());
    }
    
    // Check if we need to update the properties file with
    // the name of the file that was just processed
    if (dataWritten)
    {
      // Update the properties file
      updateAppProps();
    }
  }
  
  
  /**
   * Save the name of the last opened or saved file.
   * The file is opened the next time the user starts
   * the application.
   */
  public void updateAppProps()
  {
    // Check if it's null
    if (appProps == null)
    {
      return;
    }
    
    // Whether to save the filename
    boolean bSave = true;
    
    // It's not null.  See if we should store the file name.
    final String saveName = (String) appProps.get("save.filename");
    if ((saveName == null) || (saveName.length() < 1))
    {
      // Default to save
      bSave = true;
      
      // Update the properties with the value
      appProps.put("save.filename", "1");
    }
    else if (saveName.equals("0"))
    {
      bSave = false;
    }
    
    // Check if we want to save
    if (!bSave)
    {
      // We don't, so return
      return;
    }
    
    // Save the property
    appProps.setProperty("last.file", currentFileName);
    
    // Write the properties to a file
    FileOutputStream outStream = null;
    try
    {
      // Open the output stream
      outStream = new FileOutputStream(PROP_FILE_NAME);
      
      // Save the properties
      appProps.store(outStream, "Properties file for JSnip");
      
      // Close the stream
      outStream.close();
      outStream = null;
    }
    catch (FileNotFoundException fnfe)
    {
      writeErr("File not found: " + fnfe.getMessage());
    }
    catch (IOException ioe)
    {
      writeErr("IOException: " + ioe.getMessage());
    }
    finally
    {
      if (outStream != null)
      {
        try
        {
          outStream.close();
        }
        catch (IOException ioe)
        {
          writeErr("IOException: " + ioe.getMessage());
        }
        
        outStream = null;
      }
    }
  }
  
  
  /**
   * Check if the user wants to open the last data file.
   */
  private void getAppProps()
  {
    // Load the properties file
    boolean bResult = loadProperties(appProps, PROP_FILE_NAME, false);
    if (!bResult)
    {
      return;
    }
    
    // Get the file name
    String fileName = (String) appProps.getProperty("last.file");
    if ((fileName == null) || (fileName.length() < 1))
    {
      // Invalid file name
      return;
    }
    
    // Load the file (do not save to the properties file)
    File inputFile = new File(fileName);
    if (!loadFromFile(inputFile, false))
    {
      // An error occurred
      return;
    }
    
    // Set the root
    treeModel.setRoot(rootNode);
    
    // Initialize the tree
    resetTreeRoot(true);
  }
  
  
  /**
   * Mark the tree as changed.
   */
  public void treeChanged()
  {
    // Mark the tree contents as changed
    fileChanged = true;
  }
  
  
  /**
   * Read the ontents of a file.
   * 
   * @param inFile the input File object
   * @param saveFileName whether to save the name of the file
   * @return whether the operation was successful
   */
  private boolean loadFromFile(final File inFile,
                               final boolean saveFileName)
  {
    // Whether the data was successfully read
    boolean dataRead = false;
    
    // Verify that inFile points to an existing file
    if ((inFile == null) || (inFile.isDirectory()) || (!inFile.exists()))
    {
      return false;
    }
    
    // Read the file
    ObjectInputStream os = null;
    try
    {
      // Create the output stream
      os = new ObjectInputStream(new FileInputStream(inFile));
      
      // Write the data
      rootNode = (CodeItemNode) os.readObject();
      
      // Clear the stream
      os.close();
      os = null;
      
      // The data was loaded
      dataRead = true;
    }
    catch (ClassNotFoundException cnfe)
    {
      writeErr("Class not found: " + cnfe.getMessage());
    }
    catch (FileNotFoundException fnfe)
    {
      writeErr("File not found: " + fnfe.getMessage());
    }
    catch (IOException ioe)
    {
      writeErr("IOException: " + ioe.getMessage());
    }
    finally
    {
      if (os != null)
      {
        try
        {
          os.close();
        }
        catch (IOException ioe)
        {
          writeErr("IOException: " + ioe.getMessage());
        }
        
        os = null;
      }
    }
    
    // Save the input file name
    try
    {
      currentFileName = inFile.getCanonicalPath();
    }
    catch (IOException ioe)
    {
      writeErr("Error getting the filename: " + ioe.getMessage());
    }
    
    // Check if we need to update the properties file with
    // the name of the file that was just processed
    if ((dataRead) && (saveFileName))
    {
      // Update the properties file
      updateAppProps();
    }
    
    return dataRead;
  }
  
  
  /**
   * Delete a node.
   */
  public void performNodeDelete()
  {
    // Get the selected node
    final TreePath parentPath = tree.getSelectionPath();
    if (parentPath == null)
    {
      // Nothing selected, so return
      return;
    }
    else
    {
      // Get the current node
      CodeItemNode currNode =
        (CodeItemNode) (parentPath.getLastPathComponent());
      
      // See if the node has a parent
      if (currNode.getParent() == null)
      {
        // The user is trying to delete the root node.
        // Don't let it happen.
        JOptionPane.showMessageDialog(frame,
            "You cannot delete the root node",
            "Warning", JOptionPane.ERROR_MESSAGE);
        return;
      }
      
      // Confirm the user wants to delete
      int result = JOptionPane.showConfirmDialog(frame,
          "Are you sure you want to delete the selected node?",
          "Confirm Deletion", JOptionPane.YES_NO_OPTION);
      if (result == JOptionPane.NO_OPTION)
      {
        // The user does not want to delete, so abort
        return;
      }
      
      // Check if this has a parent (if not, it's the root node)
      CodeItemNode parent = (CodeItemNode) currNode.getParent();
      if (parent == null)
      {
        // The user cannot delete the root node
        return;
      }
      
      // Save the index for this node
      int childNodeFromParent = parent.getIndex(currNode);
      if (childNodeFromParent < 0)
      {
        return;
      }
      
      // Save the number of children in the parent
      final int nNumChildren = parent.getChildCount();
      
      // Record the change
      fileChanged = true;
      
      // Remove the node
      treeModel.removeNodeFromParent(currNode);
      
      // Now select the next node
      CodeItemNode selectNode = null;
      if (nNumChildren == 1)
      {
        // This was the last node, so select the parent
        selectNode = parent;
      }
      else if (childNodeFromParent == (nNumChildren - 1))
      {
        selectNode = (CodeItemNode) parent.getChildAt(childNodeFromParent - 1);
      }
      else
      {
        selectNode = (CodeItemNode) parent.getChildAt(childNodeFromParent);
      }
      
      // Now we want to select the selectNode node.
      // First get the nodes leading to that node.
      TreeNode[] selectNodes = selectNode.getPath();
      
      // Now create a TreePath object for that path
      TreePath selectPath = new TreePath(selectNodes);
      
      // Now select the node
      tree.setSelectionPath(selectPath);
    }
  }
  
  
  /**
   * Edit a node.
   */
  public void performNodeEdit()
  {
    // Get the selected node
    final TreePath parentPath = tree.getSelectionPath();
    if (parentPath == null)
    {
      // Nothing selected, so return
      return;
    }
    else
    {
      // Get the current node
      CodeItemNode currNode =
        (CodeItemNode) (parentPath.getLastPathComponent());
      
      // Check if this has a parent (if not, it's the root node)
      if (currNode.getParent() == null)
      {
        // The user cannot delete the root node
        return;
      }
      
      // Record the change
      fileChanged = true;
      
      // Start editing
      tree.startEditingAtPath(parentPath);
    }
  }
  
  
  /**
   * Create a node.
   */
  public void performNodeNew()
  {
    // Get the selected node
    final TreePath parentPath = tree.getSelectionPath();
    if (parentPath != null)
    {
      // Get the current node
      CodeItemNode currNode =
        (CodeItemNode) (parentPath.getLastPathComponent());
      
      // Create a new node
      CodeItemNode newNode =
        new CodeItemNode("New Node");
      newNode.setScript("");
      
      // Add the node to the tree
      treeModel.insertNodeInto(newNode, currNode, currNode.getChildCount());
      
      // Record the change
      fileChanged = true;
      
      // Make the new node visible
      tree.scrollPathToVisible(new TreePath(newNode.getPath()));
    }
  }
  
  
  /**
   * Copy the name of the selected node to the clipboard.
   */
  public void copyNameToCB()
  {
    // Get the selected node
    final TreePath parentPath = tree.getSelectionPath();
    if (parentPath != null)
    {
      // Get the current node
      CodeItemNode currNode =
        (CodeItemNode) (parentPath.getLastPathComponent());
      
      // Get the node name
      String name = (String) currNode.getUserObject();
      if (name != null)
      {
        StringSelection ss = new StringSelection(name);
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
            .setContents(ss, null);
      }
    }
  }
  
  
  /**
   * Rename a node to some value, indicated by the parameter.
   * 
   * @param nodeNameType how to rename the node
   */
  public void renameNodeTo(final int nodeNameType)
  {
    // This will hold the destination name for the node
    String name = null;
    
    // Compute the name based on the parameter
    switch (nodeNameType)
    {
      // Handle copying the name from the clipboard
      case SnippetActionHandler.NODE_NAME_CB:
      {
        // Get the current text from the clipboard
        name = getTextFromClipboard();
        break;
      }
      
      // Handle setting the name to the current date
      case SnippetActionHandler.NODE_NAME_DATE:
      {
        // Format the current date into a string
        Date date = new Date();
        Format formatter = new SimpleDateFormat("MMddyyyy");
        name = formatter.format(date);
        break;
      }
      
      // Handle setting the name to the current time
      case SnippetActionHandler.NODE_NAME_TIME:
      {
        // Format the current time into a string
        Date date = new Date();
        Format formatter = new SimpleDateFormat("HHmmss");
        name = formatter.format(date);
        break;
      }
      
      // Handle setting the name to the current date and time
      case SnippetActionHandler.NODE_NAME_DATE_TIME:
      {
        // Format the current date/time into a string
        Date date = new Date();
        Format formatter = new SimpleDateFormat("MMddyyyyHHmmss");
        name = formatter.format(date);
        break;
      }
      
      default:
        throw new RuntimeException("Illegal renaming argument");
    }
    
    // Verify that we have a new name
    if ((name == null) || (name.length() == 0))
    {
      // The new name is null or empty, so return
      return;
    }
    
    // Get the selected node
    final TreePath parentPath = tree.getSelectionPath();
    if (parentPath != null)
    {
      // Get the current node
      CodeItemNode currNode =
        (CodeItemNode) (parentPath.getLastPathComponent());
      
      // Update the node's name and reload the model
      currNode.setUserObject(name);
      treeModel.reload();
    }
  }
  
  
  /**
   * Return the text currently in the clipboard.
   * 
   * @return the text in the clipboard
   */
  private String getTextFromClipboard()
  {
    // The string that gets returned
    String name = null;
    
    // Copy the text from the clipboard
    try
    {
      Transferable t = java.awt.Toolkit.getDefaultToolkit()
                           .getSystemClipboard().getContents(null);
      
      // See if it has a string
      if ((t != null) && (t.isDataFlavorSupported(DataFlavor.stringFlavor)))
      {
        // Save the string
        name = (String) t.getTransferData(DataFlavor.stringFlavor);
      }
    }
    catch (UnsupportedFlavorException ufe)
    {
      System.err.println("Exception getting name: " + ufe.getMessage());
    }
    catch (IOException ioe)
    {
      System.err.println("Exception getting name: " + ioe.getMessage());
    }
    catch (IllegalStateException ise)
    {
      System.err.println("Exception getting name: " + ise.getMessage());
    }
    
    // Return the string
    return name;
  }
  
  
  /**
   * Show information about the application.
   */
  public void showAboutInfo()
  {
    // Declare our string builder
    StringBuilder sb = new StringBuilder(300);
    
    // Build the string
    sb.append("JSnip: An application to manage Java snippets.\n");
    sb.append("Written by Mike Wallace.  Copyright 2006.\n");
    sb.append("This is released under the MIT license.\n");
    sb.append("Available at http://mfwallace.googlepages.com/\n");
    sb.append("This uses the Beanshell library: http://beanshell.org/\n");
    
    // Show the message
    JOptionPane.showMessageDialog(frame, sb.toString());
  }
  
  
  /**
   * Handle the window activated.
   * 
   * @param e the window event
   */
  public void windowActivated(final WindowEvent e)
  {
    // Nothing to do here
  }
  
  
  /**
   * Handle the window closed.
   * 
   * @param e the window event
   */
  public void windowClosed(final WindowEvent e)
  {
    // Nothing to do here
  }
  
  
  /**
   * Handle the window closing.
   * 
   * @param e the window event
   */
  public void windowClosing(final WindowEvent e)
  {
    exitApp();
  }
  
  
  /**
   * Handle the window deactivated.
   * 
   * @param e the window event
   */
  public void windowDeactivated(final WindowEvent e)
  {
    // Nothing to do here
  }
  
  
  /**
   * Handle the window deiconified.
   * 
   * @param e the window event
   */
  public void windowDeiconified(final WindowEvent e)
  {
    // Nothing to do here
  }
  
  
  /**
   * Handle the window iconified.
   * 
   * @param e the window event
   */
  public void windowIconified(final WindowEvent e)
  {
    // Nothing to do here
  }
  
  
  /**
   * Handle the window open.
   * 
   * @param e the window event
   */
  public void windowOpened(final WindowEvent e)
  {
    // Nothing to do here
  }
  
  
  /**
   * The mouse click event.
   * 
   * @param e the mouse event
   */
  public void mouseClicked(final MouseEvent e)
  {
    // Check which button was pressed
    if (e.getButton() != MouseEvent.BUTTON3)
    {
      // It's not button 3, so return
      return;
    }
    
    // Get the point
    final Point loc = e.getPoint();
    
    // See if the user clicked on the tree
    if (e.getSource() == tree)
    {
      // Get the location.  If not over a tree node, return
      TreePath path = tree.getPathForLocation(loc.x, loc.y);
      if (path != null)
      {
        // Select the node the user right-clicked on
        tree.setSelectionPath(path);
        
        // Show the popup menu
        popupMenuTree.show(tree, loc.x, loc.y);
        
        return;
      }
    }
    else if (e.getSource() == taInput)
    {
      // Update the menu items for the popup menu
      updateScriptPopupMenu();
      
      // The user must have clicked in the input (script) window)
      popupMenuScript.show(taInput, loc.x, loc.y);
    }
  }
  
  
  /**
   * Update the popup menu for the script.
   */
  private void updateScriptPopupMenu()
  {
    // Check for selected text in the window
    String selText = taInput.getSelectedText();
    boolean hasTextSelected = ((selText != null) && (selText.length() > 0));
    
    // Enable cut/copy based on whether text is selected in the window
    popupCut.setEnabled(hasTextSelected);
    popupCopy.setEnabled(hasTextSelected);
    
    // Check for text in the clipboard
    String cbText = getTextFromClipboard();
    boolean hasTextInClipboard = (cbText != null);
    
    // Enable paste based on whether text is in the clipboard
    popupPaste.setEnabled(hasTextInClipboard);
  }
  
  
  /**
   * The mouse entered event.
   * 
   * @param e the mouse event
   */
  public void mouseEntered(final MouseEvent e)
  {
    // Nothing to do here
  }
  
  
  /**
   * The mouse exited event.
   * 
   * @param e the mouse event
   */
  public void mouseExited(final MouseEvent e)
  {
    // Nothing to do here
  }
  
  
  /**
   * The mouse pressed event.
   * 
   * @param e the mouse event
   */
  public void mousePressed(final MouseEvent e)
  {
    // Nothing to do here
  }
  
  
  /**
   * The mouse released event.
   * 
   * @param e the mouse event
   */
  public void mouseReleased(final MouseEvent e)
  {
    // Nothing to do here
  }
  
  
  /**
   * Close the application.
   */
  public void exitApp()
  {
    // Check if the tree changed
    if (!checkFileChanged())
    {
      // Cancel the app exit
      return;
    }
    
    // Close the application
    System.exit(0);
  }
  
  
  /**
   * Center the application on the screen.
   *
   */
  private void centerOnScreen()
  {
    // Get the size of the screen
    java.awt.Dimension screenDim =
      java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    
    // Determine the new location of the window
    int x = (screenDim.width - frame.getSize().width) / 2;
    int y = (screenDim.height - frame.getSize().height) / 2;
    
    // Move the window
    frame.setLocation(x, y);
  }
  
  
  /**
   * Initialize the application's Look And Feel.
   */
  private void initLookAndFeel()
  {
    // Use the default look and feel
    try
    {
      // Font f1 = (Font) javax.swing.UIManager.get("Label.font");
      // System.out.println(f1.toString());
      
      javax.swing.UIManager.setLookAndFeel(
        javax.swing.UIManager.getSystemLookAndFeelClassName());
      
      // Font f2 = (Font) javax.swing.UIManager.get("Label.font");
      // System.out.println(f2.toString());
      
      // TODO Use a class font object
      // Set the font for the application
      javax.swing.UIManager.put("Label.font", 
          new Font("sansserif", Font.PLAIN, 11));
      
      // Font f3 = (Font) javax.swing.UIManager.get("Label.font");
      // System.out.println(f3.toString());
    }
    catch (Exception e)
    {
      writeErr("Exception: " + e.getMessage());
    }
  }
  
  
  /**
   * Create and show the GUI application.
   */
  private static void createAndShowGUI()
  {
    // Run the application
    new App().createGUI();
  }
  
  
  /**
   * Make the application compatible with Apple Macs.
   * 
   * @param appName the name of the application
   */
  public static void makeMacCompatible(final String appName)
  {
    // Set the system properties that a Mac uses
    System.setProperty("apple.awt.brushMetalLook", "true");
    System.setProperty("apple.laf.useScreenMenuBar", "true");
    System.setProperty("apple.awt.showGrowBox", "true");
    System.setProperty("com.apple.mrj.application.apple.menu.about.name", appName);
  }
  
  
  /**
   * Entry point for the application.
   * 
   * @param args arguments to the application
   */
  public static void main(final String[] args)
  {
    // Set up the Mac-related properties
    makeMacCompatible("JSnip");
    
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        createAndShowGUI();
      }
    });
  }
}

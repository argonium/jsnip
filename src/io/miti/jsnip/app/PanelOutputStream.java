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

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 * Class to allow a JPanel to be used for output.
 * 
 * @author Mike Wallace
 * @version 1.0
 */
public final class PanelOutputStream extends OutputStream
{
  /**
   * Output container.
   */
  private JTextPane tpOutput = null;
  
  /**
   * The name of the style to use.
   */
  private String sStyle = null;
  
  
  /**
   * Default constructor.
   */
  @SuppressWarnings("unused")
  private PanelOutputStream()
  {
    super();
  }
  
  
  /**
   * Constructor taking the output container.
   * 
   * @param output the output text pane
   * @param style the name of the output style
   */
  public PanelOutputStream(final JTextPane output,
                           final String style)
  {
    super();
    tpOutput = output;
    sStyle = style;
  }
  
  
  /**
   * Write out the data to the output panel.
   * 
   * @param n the character to write
   * @throws IOException exception on writing
   */
  @Override
  public void write(final int n)
    throws IOException
  {
    // Add the character to the output
    byte[] b = {((byte) n)};
    String s = new String(b);
    writeOutput(s);
  }
  
  
  /**
   * Write out the data to the output panel.
   * 
   * @param b the byte array
   * @param off the offset of the byte array
   * @param len the length of the string to write
   * @throws IOException exception on writing
   */
  @Override
  public void write(final byte[] b,
                    final int off,
                    final int len)
    throws IOException
  {
    String s = new String(b, off, len);
    writeOutput(s);
  }
  
  
  /**
   * Write out the data to the output panel.
   * 
   * @param b the byte array
   * @throws IOException exception on writing
   */
  @Override
  public void write(final byte[] b) throws IOException
  {
    String s = new String(b);
    writeOutput(s);
  }
  
  
  /**
   * Send the string to the output component.
   * 
   * @param str the output string.
   */
  private void writeOutput(final String str)
  {
    // Check the component
    if (tpOutput == null)
    {
      return;
    }
    
    // Create a thread for writing the output to
    // the component
    Runnable updateComponent = new Runnable()
    {
      public void run()
      {
        try
        {
          StyledDocument doc = tpOutput.getStyledDocument();
          doc.insertString(doc.getLength(), str,
                           doc.getStyle(sStyle));
          tpOutput.repaint();
        }
        catch (BadLocationException e)
        {
          // Auto-generated catch block
          e.printStackTrace();
        }
        
        
        /*
        // Check if we're writing to stderr
        if (!toStdOut)
        {
          // Change the font color to red for errors
          tpOutput.setForeground(java.awt.Color.RED);
        }
        
        // Append the string
        tpOutput.append(str);
        tpOutput.repaint();
        
        // Check if we're writing to stderr
        if (!toStdOut)
        {
          // Restore the font color
          tpOutput.setForeground(java.awt.Color.BLACK);
        }
        */
      }
    };
    
    // Invoke the thread later
    SwingUtilities.invokeLater(updateComponent);
  }
}

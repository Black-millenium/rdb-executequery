/*
 * CharLimitedTextField.java
 *
 * Copyright (C) 2002-2013 Takis Diakoumis
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.underworldlabs.swing;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;

/* ----------------------------------------------------------
 * CVS NOTE: Changes to the CVS repository prior to the 
 *           release of version 3.0.0beta1 has meant a 
 *           resetting of CVS revision numbers.
 * ----------------------------------------------------------
 */

/**
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class CharLimitedTextField extends JTextField {

  private int maxLength;
  private CharLimitedDocument charLimitedDocument;

  public CharLimitedTextField(int maxLength) {
    this.maxLength = maxLength;
    if (charLimitedDocument == null) {
      charLimitedDocument = new CharLimitedDocument();
    }
  }

  public int getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(int maxLength) {
    this.maxLength = maxLength;
  }

  protected Document createDefaultModel() {
    if (charLimitedDocument == null) {
      charLimitedDocument = new CharLimitedDocument();
    }
    return charLimitedDocument;
  }

  class CharLimitedDocument extends PlainDocument {

    private Toolkit toolkit;

    public CharLimitedDocument() {
      toolkit = Toolkit.getDefaultToolkit();
    }

    public void insertString(int offs, String str, AttributeSet a)
        throws BadLocationException {
      if (getLength() >= maxLength) {
        toolkit.beep();
        return;
      }
      super.insertString(offs, str, a);
    }
  }

}














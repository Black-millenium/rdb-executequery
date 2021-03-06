/*
 * SmoothGradientComboBoxEditor.java
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

package org.underworldlabs.swing.plaf.smoothgradient;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;


/**
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
class SmoothGradientComboBoxEditor extends BasicComboBoxEditor {

  public SmoothGradientComboBoxEditor() {
    editor = new JTextField("", 9) {
      // workaround for 4530952
      public void setText(String s) {
        if (getText().equals(s)) {
          return;
        }
        super.setText(s);
      }
    };

    editor.setBorder(UIManager.getBorder("ComboBox.editorBorder"));
  }

  /**
   * A subclass of BasicComboBoxEditor that implements UIResource.
   * BasicComboBoxEditor doesn't implement UIResource
   * directly so that applications can safely override the
   * cellRenderer property with BasicListCellRenderer subclasses.
   */
  public static final class UIResource extends SmoothGradientComboBoxEditor
      implements javax.swing.plaf.UIResource {
  }

}















/*
 * GotoCommand.java
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

package org.executequery.actions.searchcommands;

import org.executequery.GUIUtilities;
import org.executequery.gui.editor.QueryEditor;
import org.underworldlabs.swing.actions.BaseCommand;

import javax.swing.*;
import java.awt.event.ActionEvent;

/* ----------------------------------------------------------
 * CVS NOTE: Changes to the CVS repository prior to the 
 *           release of version 3.0.0beta1 has meant a 
 *           resetting of CVS revision numbers.
 * ----------------------------------------------------------
 */

/**
 * <p>Command for Search | Go To
 *
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class GotoCommand implements BaseCommand {

  public void execute(ActionEvent e) {
    JPanel panel = GUIUtilities.getSelectedCentralPane();
    if (panel instanceof QueryEditor) {
      QueryEditor queryEditor = (QueryEditor) panel;
      try {
        queryEditor.goToRow(Integer.parseInt(
            GUIUtilities.displayInputMessage("Go To", "Line Number:")));
      } catch (NumberFormatException numExc) {
      }
    }
    panel = null;
  }

}



















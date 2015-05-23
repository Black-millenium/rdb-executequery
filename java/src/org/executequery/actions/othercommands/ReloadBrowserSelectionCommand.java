/*
 * ReloadBrowserSelectionCommand.java
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

package org.executequery.actions.othercommands;

import org.executequery.GUIUtilities;
import org.executequery.gui.browser.ConnectionsTreePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action to reload the currently selected node within the
 * connections browser panel.
 *
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class ReloadBrowserSelectionCommand extends AbstractBaseCommand {

  public void execute(ActionEvent e) {
    JPanel panel = GUIUtilities.getDockedTabComponent(ConnectionsTreePanel.PROPERTY_KEY);
    if (panel != null) {
      ((ConnectionsTreePanel) panel).reloadSelection();
    }
  }


}














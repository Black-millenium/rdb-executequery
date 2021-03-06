/*
 * PreferencesCommand.java
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

package org.executequery.actions.toolscommands;

import org.executequery.GUIUtilities;
import org.executequery.actions.OpenFrameCommand;
import org.executequery.gui.BaseDialog;
import org.executequery.gui.prefs.PropertiesPanel;
import org.underworldlabs.swing.actions.BaseCommand;

import java.awt.event.ActionEvent;

/**
 * <p>Executes the Tools | Preferences command.
 *
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class PreferencesCommand extends OpenFrameCommand
    implements BaseCommand {

  public void execute(ActionEvent e) {
    try {
      GUIUtilities.showWaitCursor();
      BaseDialog dialog =
          createDialog(PropertiesPanel.TITLE, true);
      PropertiesPanel panel = new PropertiesPanel(dialog);
      dialog.addDisplayComponentWithEmptyBorder(panel);
      dialog.display();
    } finally {
      GUIUtilities.showNormalCursor();
    }

        /*
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ExecuteQueryDialog(PropertiesPanel.TITLE,
                                       new PropertiesPanel(), 
                                       true);
            }
        });
        */
        /*
        if (!isFrameOpen(PropertiesFrame.TITLE)) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    GUIUtilities.addInternalFrame(PropertiesFrame.TITLE,
                                                  PropertiesFrame.FRAME_ICON,
                                                  new PropertiesFrame(),
                                                  true, false, false, false);
                }
            });
        }
         */
  }

}



















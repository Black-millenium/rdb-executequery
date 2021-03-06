/*
 * HelpCommand.java
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

package org.executequery.actions.helpcommands;

import org.executequery.gui.HelpWindow;
import org.underworldlabs.swing.actions.BaseCommand;

import java.awt.event.ActionEvent;

/**
 * Executes the Help command.<br>
 * This will open the system help in a separate window.
 *
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class HelpCommand implements BaseCommand {

  /**
   * Document help target ID
   */
  private String target;

  public HelpCommand() {
  }

  public HelpCommand(String page) {
    target = page;
  }

  public void execute(ActionEvent e) {

    String aCommand = e.getActionCommand();

    if (aCommand != null) {

      if (aCommand.length() > 0) {
        target = aCommand;
      }
      if ("Help Topics".equals(aCommand)) {
        target = null;
      }

    } else {
      target = null;
    }

    new HelpWindow(target);
  }

}





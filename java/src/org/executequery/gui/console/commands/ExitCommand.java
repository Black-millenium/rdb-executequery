/*
 * ExitCommand.java
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

package org.executequery.gui.console.commands;

import org.executequery.GUIUtilities;
import org.executequery.gui.console.Console;
import org.executequery.gui.console.ConsolePanel;
import org.underworldlabs.util.SystemProperties;

import javax.swing.*;

/* ----------------------------------------------------------
 * CVS NOTE: Changes to the CVS repository prior to the 
 *           release of version 3.0.0beta1 has meant a 
 *           resetting of CVS revision numbers.
 * ----------------------------------------------------------
 */

/**
 * This command exits from the console and closes current window.
 */

/**
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class ExitCommand extends Command {

  private static final String COMMAND_NAME = "exit";

  public String getCommandName() {
    return COMMAND_NAME;
  }

  public String getCommandSummary() {
    return SystemProperties.getProperty("console", "console.exit.command.help");
  }

  public boolean handleCommand(Console console, String command) {

    if (command.equals(COMMAND_NAME)) {
      GUIUtilities.closeDockedComponent(ConsolePanel.TITLE, SwingConstants.SOUTH);
      //GUIUtilities.closeInternalFrame(ConsolePanel.TITLE);
      return true;
    }

    return false;
  }

}














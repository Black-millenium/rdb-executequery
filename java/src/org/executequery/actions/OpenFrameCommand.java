/*
 * OpenFrameCommand.java
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

package org.executequery.actions;

import org.executequery.GUIUtilities;
import org.executequery.datasource.ConnectionManager;
import org.executequery.gui.BaseDialog;

/**
 * Base command for those opening a new frame
 *
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public abstract class OpenFrameCommand {

  protected final boolean isConnected() {

    if (!ConnectionManager.hasConnections()) {

      GUIUtilities.displayErrorMessage(
          "Not Connected.\nPlease connect to continue.");

      return false;
    }

    return true;
  }

  protected final boolean isActionableDialogOpen() {

    return GUIUtilities.isActionableDialogOpen();
  }

  protected final boolean isDialogOpen(String title) {

    if (GUIUtilities.isDialogOpen(title)) {

      GUIUtilities.setSelectedDialog(title);

      return true;
    }

    return false;
  }

  /**
   * Creates a dialog component with the specified name
   * and modality.
   *
   * @param the     dialog name
   * @param whether the dialog is to be modal
   */
  protected final BaseDialog createDialog(String name, boolean modal) {
    return new BaseDialog(name, modal);
  }

  /**
   * Creates a dialog component with the specified name
   * and modality.
   *
   * @param the     dialog name
   * @param whether the dialog is to be modal
   * @param wether  the dialog is resizeable
   */
  protected final BaseDialog createDialog(String name, boolean modal, boolean resizeable) {
    return new BaseDialog(name, modal, resizeable);
  }

}










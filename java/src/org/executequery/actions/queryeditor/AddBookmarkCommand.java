/*
 * AddBookmarkCommand.java
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

package org.executequery.actions.queryeditor;

import org.executequery.GUIUtilities;
import org.executequery.gui.BaseDialog;
import org.executequery.gui.editor.AddQueryBookmarkPanel;

import java.awt.event.ActionEvent;

/**
 * <p>The Query Editor's add bookmark command.
 *
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class AddBookmarkCommand extends AbstractQueryEditorCommand {

  public void execute(ActionEvent e) {

    if (isQueryEditorTheCentralPanel()) {

      if (queryEditor().hasText()) {

        BaseDialog dialog =
            new BaseDialog(AddQueryBookmarkPanel.TITLE, true);

        dialog.addDisplayComponent(
            new AddQueryBookmarkPanel(dialog, queryEditor().getEditorText()));
        dialog.display();

      } else {

        GUIUtilities.displayErrorMessage("Query editor has no text");
      }

    }

  }

}










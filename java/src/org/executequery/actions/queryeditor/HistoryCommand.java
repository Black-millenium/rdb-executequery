/*
 * HistoryCommand.java
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
import org.executequery.gui.editor.SQLHistoryDialog;
import org.executequery.repository.RepositoryCache;
import org.executequery.repository.SqlCommandHistoryRepository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 * <p>The Query Editor's history command execution.
 *
 * @author Takis Diakoumis
 * @version $Revision: 161 $
 * @date $Date: 2013-04-25 16:00:50 +0400 (Чт, 25 апр 2013) $
 */
public class HistoryCommand extends AbstractQueryEditorCommand {

  public void execute(ActionEvent e) {

    if (isQueryEditorTheCentralPanel()) {

      SwingUtilities.invokeLater(new Runnable() {
        public void run() {

          Vector<String> history =
              sqlCommandHistoryRepository().getSqlCommandHistory();

          if (history == null || history.isEmpty()) {

            GUIUtilities.displayInformationMessage("No SQL command history available");
            return;
          }

          new SQLHistoryDialog(history, queryEditor());
        }
      });

    }

  }

  private SqlCommandHistoryRepository sqlCommandHistoryRepository() {

    return (SqlCommandHistoryRepository) RepositoryCache.load(
        SqlCommandHistoryRepository.REPOSITORY_ID);
  }

}










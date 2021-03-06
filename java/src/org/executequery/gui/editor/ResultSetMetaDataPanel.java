/*
 * ResultSetMetaDataPanel.java
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

package org.executequery.gui.editor;

import org.executequery.gui.DefaultTable;
import org.executequery.gui.resultset.ResultSetMetaDataTableModel;
import org.executequery.util.UserProperties;

import javax.swing.*;
import java.awt.*;

/**
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class ResultSetMetaDataPanel extends JPanel {

  public static final String TITLE = "Result Set Meta Data";

  private JTable table;

  public ResultSetMetaDataPanel() {

    super(new BorderLayout());

    try {

      init();

    } catch (Exception e) {

      e.printStackTrace();
    }
  }

  private void init() {

    table = new DefaultTable();
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    table.setRowHeight(rowHeight());

    JScrollPane scroller = new JScrollPane(table);
    scroller.setBorder(null);

    add(scroller, BorderLayout.CENTER);
  }

  private int rowHeight() {

    return UserProperties.getInstance().getIntProperty(
        "results.table.column.height");
  }

  public void setMetaData(ResultSetMetaDataTableModel model) {

    table.setModel(model);
  }

}








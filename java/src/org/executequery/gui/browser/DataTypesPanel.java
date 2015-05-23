/*
 * DataTypesPanel.java
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

package org.executequery.gui.browser;

import org.executequery.gui.SortableColumnsTable;
import org.executequery.gui.resultset.ResultSetTableModel;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

/**
 * Displays data types from the current connection.
 *
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class DataTypesPanel extends ConnectionPropertiesPanel {

  private JTable table;

  /**
   * the data type table model from the result set
   */
  private ResultSetTableModel model;

  public DataTypesPanel() {

    super(new GridBagLayout());

    init();
  }

  private void init() {

    model = new ResultSetTableModel();
    table = new SortableColumnsTable(model);

    setTableProperties(table);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    GridBagConstraints gbc = new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
        GridBagConstraints.SOUTHEAST,
        GridBagConstraints.BOTH,
        new Insets(5, 5, 5, 5), 0, 0);

    add(new JScrollPane(table), gbc);
  }

  public void setDataTypes(ResultSet rs) {

    model.createTable(rs);
    model.fireTableStructureChanged();
  }

}










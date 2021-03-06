/*
 * SortableHeaderRenderer.java
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

package org.underworldlabs.swing.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Header renderer for the table sorter model.
 *
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class SortableHeaderRenderer extends DefaultTableHeaderRenderer
    implements TableCellRenderer {

  /**
   * the up arrow icon
   */
  private ArrowIcon upIcon;

  /**
   * the down arrow icon
   */
  private ArrowIcon downIcon;

  /**
   * the table sorter for this header
   */
  private TableSorter sorter;

  public SortableHeaderRenderer(TableSorter sorter) {

    super(DEFAULT_HEIGHT);

    this.sorter = sorter;

    // init the icons
    upIcon = new ArrowIcon(ArrowIcon.UP);
    downIcon = new ArrowIcon(ArrowIcon.DOWN);

    // set the sort icon to the right of the text
    setHorizontalTextPosition(JLabel.LEFT);
  }

  public Component getTableCellRendererComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus,
                                                 int row,
                                                 int column) {

    int modelColumn = table.convertColumnIndexToModel(column);
    int iconType = sorter.getHeaderRendererIcon(modelColumn);
    if (iconType == -1) {
      setIcon(null);
    } else {
      setIcon(iconType == ArrowIcon.UP ? upIcon : downIcon);
    }

    return super.getTableCellRendererComponent(
        table, value, isSelected, hasFocus, row, column);
  }

}






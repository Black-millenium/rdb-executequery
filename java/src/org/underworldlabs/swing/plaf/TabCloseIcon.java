/*
 * TabCloseIcon.java
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

package org.underworldlabs.swing.plaf;

import javax.swing.*;
import java.awt.*;

/* ----------------------------------------------------------
 * CVS NOTE: Changes to the CVS repository prior to the 
 *           release of version 3.0.0beta1 has meant a 
 *           resetting of CVS revision numbers.
 * ----------------------------------------------------------
 */

/**
 * Simple icon drawing the close button
 * for a closeable tab on the CloseTabbedPane.
 *
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class TabCloseIcon implements Icon {

  /**
   * the icon width
   */
  protected static final int ICON_WIDTH = 6;

  /**
   * the icon height
   */
  protected static final int ICON_HEIGHT = 6;

  /**
   * Creates a new instance of TabCloseButtonIcon
   */
  public TabCloseIcon() {
  }

  /**
   * Returns the icon's height.
   *
   * @return the height of the icon
   */
  public int getIconHeight() {
    return ICON_HEIGHT;
  }

  /**
   * Returns the icon's width.
   *
   * @return the width of the icon
   */
  public int getIconWidth() {
    return ICON_WIDTH;
  }

  /**
   * Draw the icon at the specified location.
   *
   * @param the component
   * @param the graphics context
   * @param x   coordinate
   * @param y   coordinate
   */
  public void paintIcon(Component c, Graphics g, int x, int y) {
    g.setColor(UIManager.getColor("controlShadow").darker().darker());
    g.drawLine(x, y, x + ICON_WIDTH - 1, y + ICON_HEIGHT - 1);
    g.drawLine(x + ICON_WIDTH - 1, y, x, y + ICON_HEIGHT - 1);
  }

}














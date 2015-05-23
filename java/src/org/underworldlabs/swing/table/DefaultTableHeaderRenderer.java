/*
 * DefaultTableHeaderRenderer.java
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

import org.underworldlabs.Constants;
import org.underworldlabs.swing.plaf.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Simple header renderer with tool tip text and configurable height.
 *
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class DefaultTableHeaderRenderer extends DefaultTableCellRenderer {

  /**
   * the default height - 22
   */
  protected static final int DEFAULT_HEIGHT = 24;
  /**
   * the height of the header
   */
  private int height;
  /**
   * the light gradient colour 1
   */
  private Color colour1;
  /**
   * the dark gradient colour 2
   */
  private Color colour2;
  /**
   * whether to fill a gradient background
   */
  private boolean fillGradient;

  /**
   * Creates a new instance of DefaultTableHeaderRenderer
   */
  public DefaultTableHeaderRenderer() {
    this(DEFAULT_HEIGHT);
  }

  /**
   * Creates a new instance of DefaultTableHeaderRenderer
   */
  public DefaultTableHeaderRenderer(int height) {

    this.height = height;

    setHorizontalAlignment(JLabel.CENTER);

    fillGradient = (UIUtils.isDefaultLookAndFeel() || UIUtils.usingOcean() || UIUtils.isNativeMacLookAndFeel());
    if (fillGradient) {
      colour1 = UIManager.getColor("Label.background");
      colour2 = UIUtils.getDarker(colour1, 0.85);
    }

  }

  public Component getTableCellRendererComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus,
                                                 int row,
                                                 int column) {
    String label = null;
    if (value != null) {
      label = value.toString();
    } else {
      label = Constants.EMPTY;
    }

    setText(label);
    setToolTipText(label);

    if (!UIUtils.isNativeMacLookAndFeel()) {

      setBorder(UIManager.getBorder("TableHeader.cellBorder"));

    } else {

      Color color = UIManager.getColor("controlShadow");
      if (column == 0) {

        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, color));

      } else {

        setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, color));
      }

    }


    setFont(UIManager.getFont("TableHeader.font"));
    setForeground(UIManager.getColor("TableHeader.foreground"));
    setBackground(UIManager.getColor("TableHeader.background"));

    return this;
  }

  public void paintComponent(Graphics g) {
    if (fillGradient) {
      int width = getWidth();
      int height = getHeight();

      Graphics2D g2 = (Graphics2D) g;
      Paint originalPaint = g2.getPaint();
      GradientPaint fade = new GradientPaint(0, height, colour2, 0, (int) (height * 0.5), colour1);

      g2.setPaint(fade);
      g2.fillRect(0, 0, width, height);

      g2.setPaint(originalPaint);
    }
    super.paintComponent(g);
  }

  public boolean isOpaque() {
    return !fillGradient;
  }

  public Dimension getPreferredSize() {
    return new Dimension(getWidth(), height);
  }

  public int getHeight() {
    return height;
  }

}



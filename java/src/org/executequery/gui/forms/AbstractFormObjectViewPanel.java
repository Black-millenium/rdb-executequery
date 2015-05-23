/*
 * AbstractFormObjectViewPanel.java
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

package org.executequery.gui.forms;

import org.underworldlabs.swing.GradientLabel;
import org.underworldlabs.swing.plaf.UIUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.print.Printable;

/**
 * @author Takis Diakoumis
 * @version $Revision: 161 $
 * @date $Date: 2013-04-25 16:00:50 +0400 (Чт, 25 апр 2013) $
 */
public abstract class AbstractFormObjectViewPanel extends JPanel
    implements FormObjectView {
  protected static Border emptyBorder;
  private static GridBagConstraints panelConstraints;
  static {
    emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    panelConstraints = new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
        GridBagConstraints.SOUTHEAST,
        GridBagConstraints.BOTH,
        new Insets(5, 5, 5, 5), 0, 0);
  }
  protected GradientLabel gradientLabel;
  private boolean reload;

  public AbstractFormObjectViewPanel() {

    super(new BorderLayout());

    gradientLabel = new GradientLabel();
    if (!UIUtils.isNativeMacLookAndFeel()) {
      gradientLabel.setForeground(new ColorUIResource(0x333333));
    }
    add(gradientLabel, BorderLayout.NORTH);
  }

  /**
   * Returns the standard layout constraints for the panel.
   */
  public static GridBagConstraints getPanelConstraints() {
    return panelConstraints;
  }

  protected void setContentPanel(JComponent panel) {

    add(panel, BorderLayout.CENTER);
  }

  public void setHeader(String text, ImageIcon icon) {
    gradientLabel.setText(text);
  }

  public void setHeaderText(String text) {
    gradientLabel.setText(text);
  }

  public void setHeaderIcon(ImageIcon icon) {
  }

  /**
   * Performs some cleanup and releases resources before being closed.
   */
  public abstract void cleanup();

  /**
   * Flags to refresh the data and clears the cache - if any.
   */
  public void refresh() {
    setReload(true);
  }

  /**
   * Returns the print object - if any.
   */
  public abstract Printable getPrintable();

  /**
   * Returns the name of this panel.
   */
  public abstract String getLayoutName();

  public boolean isReload() {
    return reload;
  }

  public void setReload(boolean reload) {
    this.reload = reload;
  }

}






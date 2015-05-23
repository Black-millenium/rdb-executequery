/*
 * SystemOutputPanel.java
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

package org.executequery.gui;

import org.apache.log4j.Appender;
import org.apache.log4j.PatternLayout;
import org.executequery.GUIUtilities;
import org.executequery.components.BasicPopupMenuListener;
import org.executequery.components.TextAreaLogAppender;
import org.executequery.log.Log;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class SystemOutputPanel extends AbstractDockedTabPanel implements ReadOnlyTextPane {

  /**
   * This panel's title
   */
  public static final String TITLE = "Output Console";
  public static final String MENU_ITEM_KEY = "viewConsole";
  public static final String PROPERTY_KEY = "system.display.console";
  /**
   * the output text area
   */
  private JTextArea textArea;

  public SystemOutputPanel() {

    super(new BorderLayout());

    try {

      init();

    } catch (Exception e) {

      e.printStackTrace();
    }

  }

  private void init() throws Exception {

    textArea = new JTextArea();
    textArea.setFont(new Font("dialog", 0, 11));
    textArea.setEditable(false);

    SystemOutputPanelPopUpMenu systemOutputPanelPopUpMenu = new SystemOutputPanelPopUpMenu(this);
    textArea.addMouseListener(new BasicPopupMenuListener(systemOutputPanelPopUpMenu));

    Appender appender = new TextAreaLogAppender(textArea);
    appender.setLayout(new PatternLayout(Log.PATTERN));
    Log.addAppender(appender);

    JScrollPane scroller = new JScrollPane(textArea);
    scroller.setBorder(BorderFactory.
        createMatteBorder(1, 0, 0, 0, GUIUtilities.getDefaultBorderColour()));

    add(scroller, BorderLayout.CENTER);
  }

  // ----------------------------------------
  // DockedTabView Implementation
  // ----------------------------------------

  public Icon getIcon() {

    return GUIUtilities.loadIcon("SystemOutput.png");
  }

  public String toString() {

    return "Output Console";
  }

  /**
   * Returns the display title for this view.
   *
   * @return the title displayed for this view
   */
  public String getTitle() {

    return TITLE;
  }

  /**
   * Returns the name defining the property name for this docked tab view.
   *
   * @return the key
   */
  public String getPropertyKey() {

    return PROPERTY_KEY;
  }

  /**
   * Returns the name defining the menu cache property
   * for this docked tab view.
   *
   * @return the preferences key
   */
  public String getMenuItemKey() {

    return MENU_ITEM_KEY;
  }

  public void clear() {

    textArea.setText("");
  }

  public void copy() {

    textArea.copy();
  }

  public void selectAll() {

    textArea.selectAll();

  }

  public String getText() {

    return textArea.getText();
  }

  public JTextComponent getTextComponent() {

    return textArea;
  }

}



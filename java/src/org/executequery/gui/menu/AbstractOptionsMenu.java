/*
 * AbstractOptionsMenu.java
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

package org.executequery.gui.menu;

import org.executequery.Constants;
import org.executequery.event.ApplicationEvent;
import org.executequery.event.UserPreferenceEvent;
import org.underworldlabs.swing.menu.MainMenu;
import org.underworldlabs.util.SystemProperties;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public abstract class AbstractOptionsMenu extends MainMenu {

  private Map<String, JMenuItem> menuItems;

  public AbstractOptionsMenu() {

    menuItems = new HashMap<String, JMenuItem>();
  }

  public final Component add(Component c) {

    initialiseComponent(c);

    return super.add(c);
  }

  public final JMenuItem add(JMenuItem menuItem) {

    initialiseComponent(menuItem);

    return super.add(menuItem);
  }

  private void initialiseComponent(Component component) {

    if (component instanceof JCheckBoxMenuItem) {

      JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) component;

      addActionForMenuItem(menuItem);

      setMenuItemValue((JCheckBoxMenuItem) menuItem);

      menuItems.put(menuItem.getActionCommand(), menuItem);
    }
  }

  public boolean canHandleEvent(ApplicationEvent event) {

    return (event instanceof UserPreferenceEvent);
  }

  public void preferencesChanged(UserPreferenceEvent event) {

    if (listeningForEvent(event)) {

      for (JMenuItem menuItem : menuItems.values()) {

        if (menuItem instanceof JCheckBoxMenuItem) {

          setMenuItemValue((JCheckBoxMenuItem) menuItem);

        }

      }

    }

  }

  protected final boolean booleanValueForKey(String key) {

    return SystemProperties.getBooleanProperty(Constants.USER_PROPERTIES_KEY, key);
  }

  protected final JMenuItem getMenuItem(String key) {

    return menuItems.get(key);
  }

  protected final boolean hasMenuItem(String key) {

    return menuItems.containsKey(key);
  }

  protected abstract void setMenuItemValue(JCheckBoxMenuItem menuItem);

  protected abstract void addActionForMenuItem(JCheckBoxMenuItem menuItem);

  protected abstract boolean listeningForEvent(UserPreferenceEvent event);

}










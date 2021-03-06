/*
 * SimpleCloseTabbedPane.java
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

package org.underworldlabs.swing;

import org.underworldlabs.swing.plaf.TabRollOverListener;
import org.underworldlabs.swing.plaf.TabRolloverEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class SimpleCloseTabbedPane extends AbstractTabPopupMenuContainer {

  private List<TabRollOverListener> rollListeners;

  public SimpleCloseTabbedPane() {
    this(TOP, SCROLL_TAB_LAYOUT);
  }

  public SimpleCloseTabbedPane(int tabPlacement) {
    this(tabPlacement, SCROLL_TAB_LAYOUT);
  }

  public SimpleCloseTabbedPane(int tabPlacement, int tabLayoutPolicy) {
    super(tabPlacement, tabLayoutPolicy);
  }

  public void fireTabRollOver(TabRolloverEvent e) {
    if (rollListeners == null || rollListeners.isEmpty()) {
      return;
    }

    for (int i = 0, k = rollListeners.size(); i < k; i++) {
      rollListeners.get(i).tabRollOver(e);
    }
  }

  public void fireTabRollOverFinished(TabRolloverEvent e) {
    if (rollListeners == null || rollListeners.isEmpty()) {
      return;
    }

    for (int i = 0, k = rollListeners.size(); i < k; i++) {
      rollListeners.get(i).tabRollOverFinished(e);
    }
  }

  public void fireTabRollOverCancelled(TabRolloverEvent e) {
    if (rollListeners == null || rollListeners.isEmpty()) {
      return;
    }

    for (int i = 0, k = rollListeners.size(); i < k; i++) {
      rollListeners.get(i).tabRollOverCancelled(e);
    }
  }

  public void addTabRollOverListener(TabRollOverListener listener) {
    if (rollListeners == null) {
      rollListeners = new ArrayList<TabRollOverListener>();
    }
    rollListeners.add(listener);

  }

  public void removeTabRollOverListener(TabRollOverListener listener) {
    if (rollListeners == null) {
      return;
    }
    rollListeners.remove(listener);
  }

}






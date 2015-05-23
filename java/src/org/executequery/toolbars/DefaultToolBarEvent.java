/*
 * DefaultToolBarEvent.java
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

package org.executequery.toolbars;

import org.executequery.event.AbstractApplicationEvent;

public class DefaultToolBarEvent extends AbstractApplicationEvent
    implements ToolBarEvent {

  private String key;

  public DefaultToolBarEvent(Object source, String method, String key) {

    super(source, method);

    this.key = key;
  }

  public String getKey() {

    return key;
  }

}










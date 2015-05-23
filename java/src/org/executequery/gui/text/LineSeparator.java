/*
 * LineSeparator.java
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

package org.executequery.gui.text;

public enum LineSeparator {

  DOS(0, "Unix (\\n)", "\n"),
  WINDOWS(1, "Dos/Windows (\\r\\n)", "\r\n"),
  MAC_OS(2, "MacOS (\\r)", "\r");

  public int index;
  public String label;
  public String value;

  private LineSeparator(int index, String label, String value) {
    this.index = index;
    this.label = label;
    this.value = value;
  }

  public static LineSeparator valueForIndex(int index) {

    switch (index) {

      case 0:
        return DOS;

      case 1:
        return WINDOWS;

      case 2:
        return MAC_OS;

    }

    throw new IllegalArgumentException("Invalid index for line separator " + index);
  }

}





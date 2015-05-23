/*
 * MoveListItemStrategy.java
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

import java.util.List;

/**
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class MoveListItemStrategy<T> {

  private static final int MOVE_DOWN = 1;
  private static final int MOVE_UP = -1;
  private List<T> list;

  public void setList(List<T> list) {

    this.list = list;
  }

  public int moveDown(int index) {

    if (canMoveDown(index)) {

      return moveIndex(MOVE_DOWN, index);
    }

    return index;
  }

  private boolean canMoveDown(int index) {

    return (index != -1 && index < list.size() - 1);
  }

  public int moveUp(int index) {

    if (canMoveUp(index)) {

      return moveIndex(MOVE_UP, index);
    }

    return index;
  }

  private boolean canMoveUp(int index) {

    return (index > 0);
  }

  private int moveIndex(int increment, int index) {

    T element = list.get(index);

    list.remove(index);

    int newIndex = index + increment;
    list.add(newIndex, element);

    return newIndex;
  }

}










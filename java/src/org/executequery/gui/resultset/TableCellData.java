/*
 * TableCellData.java
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

package org.executequery.gui.resultset;

import org.apache.commons.lang.StringUtils;
import org.executequery.log.Log;

import java.io.*;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 * @deprecated
 */
public final class TableCellData {

  /**
   * default buffer read size
   */
  private static final int DEFAULT_BUFFER_SIZE = 2048;
  private byte[] lobValue;
  private String valueAsString;
  private Object value;
  private int dataType;

  public int getDataType() {
    return dataType;
  }

  public void setDataType(int type) {
    this.dataType = type;
  }

  public byte[] getLobValue() {

    if (lobValue == null) {

      if (isBlob()) {

        readBlob();
      }

    }

    return lobValue;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public boolean isValueNull() {
    return (value == null);
  }

  public Object getDisplayValue() {

    if (getValue() != null) {

      if (isClob()) {

        return readClob();
      }

      if (isBlob()) {

        return readBlob();
      }

    }

    return value;
  }

  public String toString() {

    if (getValue() != null) {

      return getValue().toString();
    }

    return null;
  }

  public boolean isClob() {

    return (dataType == Types.CLOB
        || dataType == Types.LONGVARCHAR);
  }

  public boolean isBlob() {

    return (dataType == Types.BLOB
        || dataType == Types.BINARY
        || dataType == Types.VARBINARY
        || dataType == Types.LONGVARBINARY);
  }

  private Object readBlob() {

    if (lobValue != null) {

      return lobValue;
    }

    Blob blob = (Blob) value;

    InputStream binaryStream = null;

    try {

      lobValue = blob.getBytes(1, (int) blob.length());

    } catch (SQLException e) {

      if (Log.isDebugEnabled()) {

        Log.debug("Error reading BLOB data", e);
      }

      return e.getMessage();

    } finally {

      try {

        if (binaryStream != null) {

          binaryStream.close();
        }

      } catch (IOException e) {
      }

    }

    return lobValue;
  }

  private String readClob() {

    if (StringUtils.isNotBlank(valueAsString) || value == null) {

      return valueAsString;
    }

    Clob clob = (Clob) value;

    Writer writer = new StringWriter();
    Reader reader;
    try {

      reader = clob.getCharacterStream();

    } catch (SQLException e) {

      if (Log.isDebugEnabled()) {

        Log.debug("Error reading CLOB data", e);
      }

      return e.getMessage();
    }

    char[] buffer = new char[DEFAULT_BUFFER_SIZE];

    try {

      while (true) {

        int amountRead;
        amountRead = reader.read(buffer);

        if (amountRead == -1) {

          break;
        }

        writer.write(buffer);
      }

      writer.flush();

    } catch (IOException e) {

      if (Log.isDebugEnabled()) {

        Log.debug("Error reading CLOB data", e);
      }

      return e.getMessage();
    }

    valueAsString = writer.toString();
    return valueAsString;
  }

  public void setNull() {
    value = null;
  }

}





/*
 * Copyright (C) 2014 Red Soft Corporation.
 *
 * This file is part of Red nCore.
 *
 * Red nCore is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * Red nCore is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Red nCore; see the file COPYING. If not, write to the
 * Red Soft Corporation, 117105, Russia, Moscow, Nagornyy proyezd, 5.
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library. Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module. An independent module is a module which is not derived from
 * or based on this library. If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so. If you do not wish to do so, delete this
 * exception statement from your version.
 */

package biz.redsoft.udf;

import biz.redsoft.util.storage.FileData;
import biz.redsoft.util.storage.FileReader;
import biz.redsoft.util.storage.block.BlockFileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.firebirdsql.jdbc.FBBlob;
import org.firebirdsql.jdbc.FirebirdBlob;
import org.joda.time.LocalDate;

/**
 * DECLARE EXTERNAL FUNCTION read_server_blob varchar(255), bigint returns blob subtype 1 language java
 * external name 'biz.redsoft.ServerBlobReader.readBlob'
 * @author roman.kisluhin
 * @version 1.0
 *          Date: 24.11.14
 *          Time: 18:35
 */
public class ServerBlobReader {
  private static final LocalDate BASE_DATE = new LocalDate(2000, 1, 1);
  private static final String DATE_FORMAT = "yyyy-MM-dd"; // ISO date format
  public static final int SIZE_BITS = 32;  // 2Gb

  public static FirebirdBlob readBlob(String path, long index) throws IOException, SQLException {
    final FileReader rdr = getFileReader(getReaderPath(path, index), getFileReaderId(index));
    final FileData data = rdr.readFile(getFileReaderOffset(index));
    try {
      final InputStream is = data.getInputStream();
      if (is == null)
        return null;
      final Connection conn = getConnection();
      final FBBlob blob = (FBBlob) conn.createBlob();
      blob.setTemporary(true);
      blob.copyStream(is);
      return blob;
    } finally {
      data.close();
    }
  }

  private static String getReaderPath(final String path, final long index) {
    final long days = index >> (SIZE_BITS + 16);
    return path + "/" + BASE_DATE.plusDays((int) days).toString(DATE_FORMAT);
  }

  private static FileReader getFileReader(final String path, final int number) throws FileNotFoundException {
    final int gen = (number >> 8) & 0xff;
    final int index = number & 0xff;
    final String fullPath = path + "/" + gen;
    return new BlockFileReader(new File(fullPath, index + ".blck"));
  }

  private static Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:default:connection:");
  }

  private static int getFileReaderId(long index) {
    return (int) (index >>  SIZE_BITS) & 0xffff;
  }

  private static long getFileReaderOffset(long index) {
    return index & ((1l << SIZE_BITS) - 1);
  }
}

/*
 * DatabaseConnection.java
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

package org.executequery.databasemediators;

import java.io.Serializable;
import java.util.Properties;

/**
 * <p>This class maintains the necessary information for each
 * saved database connection.<br>
 * Each saved connection appears by name within the
 * saved connections drop-down box displayed on respective
 * windows.
 *
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public interface DatabaseConnection extends Serializable {

  boolean isPasswordStored();

  void setPasswordStored(boolean storePwd);

  Properties getJdbcProperties();

  void setJdbcProperties(Properties jdbcProperties);

  boolean hasAdvancedProperties();

  DatabaseDriver getJDBCDriver();

  void setJDBCDriver(DatabaseDriver driver);

  boolean hasURL();

  int getPortInt();

  String getDriverName();

  void setDriverName(String dName);

  String getPort();

  void setPort(String port);

  boolean hasPort();

  String getURL();

  void setURL(String url);

  String getDatabaseType();

  void setDatabaseType(String databaseType);

  String getPassword();

  void setPassword(String password);

  String getUnencryptedPassword();

  void setEncryptedPassword(String password);

  String getSourceName();

  void setSourceName(String sourceName);

  boolean hasHost();

  boolean hasSourceName();

  String getHost();

  void setHost(String host);

  String getUserName();

  void setUserName(String userName);

  String getName();

  void setName(String name);

  boolean isPasswordEncrypted();

  void setPasswordEncrypted(boolean passwordEncrypted);

  boolean isConnected();

  void setConnected(boolean connected);

  int getTransactionIsolation();

  void setTransactionIsolation(int transactionIsolation);

  boolean isAutoCommit();

  void setAutoCommit(boolean autoCommit);

  long getDriverId();

  void setDriverId(long driverId);

  String getId();

  void setId(String id);

  DatabaseConnection copy();

}










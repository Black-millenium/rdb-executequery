/*
 * ConnectionPool.java
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

package org.executequery.datasource;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author Takis Diakoumis
 * @version $Revision: 161 $
 * @date $Date: 2013-04-25 16:00:50 +0400 (Чт, 25 апр 2013) $
 */
interface ConnectionPool {

  int MAX_POOL_SIZE = 50;
  int MIN_POOL_SIZE = 1;
  int INITIAL_POOL_SIZE = 1;

  int getMaximumConnections();

  void setMaximumConnections(int maximumConnections);

  int getMinimumConnections();

  void setMinimumConnections(int minimumConnections);

  DataSource getDataSource();

  int getMaximumUseCount();

  void setMaximumUseCount(int maximumUseCount);

  boolean isTransactionSupported();

  void setTransactionIsolationLevel(int isolationLevel);

  Connection getConnection();

  void close();

  void close(Connection connection);

  int getSize();

  int getPoolActiveSize();

  int getInitialConnections();

  void setInitialConnections(int initialConnections);

}





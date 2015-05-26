/*
 * ConnectionPoolImpl.java
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

import biz.redsoft.ncore.db.jdbc.ConnectionProps;
import biz.redsoft.ncore.db.jdbc.IsolationLevel;
import biz.redsoft.ncore.db.jdbc.JdbcConnectionFactory;
import biz.redsoft.security.dss.XmlSign;
import biz.redsoft.security.gdsauth.AuthCryptoPluginImpl;
import biz.redsoft.util.Base64;
import org.executequery.GUIUtilities;
import org.executequery.databasemediators.DatabaseConnection;
import org.executequery.log.Log;
import org.firebirdsql.gds.impl.wire.auth.AuthCryptoPlugin;
import org.underworldlabs.jdbc.DataSourceException;

import javax.sql.DataSource;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * @author Takis Diakoumis
 * @version $Revision: 1187 $
 * @date $Date: 2013-02-09 00:34:35 +1100 (Sat, 09 Feb 2013) $
 */
public class ConnectionPoolImpl extends AbstractConnectionPool implements PooledConnectionListener {
  // пул подключений
  public static JdbcConnectionFactory factory;

  static {
    AuthCryptoPlugin.register(new AuthCryptoPluginImpl());
  }

  private final List<PooledConnection> openConnections = new Vector<PooledConnection>();
  private final List<PooledConnection> activeConnections = new Vector<PooledConnection>();
  private final DatabaseConnection databaseConnection;
  private int maximumConnections = MAX_POOL_SIZE;
  private int minimumConnections = MIN_POOL_SIZE;
  private int initialConnections = INITIAL_POOL_SIZE;
  private int defaultTxIsolation = -1;
  private boolean isMfAuth = Boolean.parseBoolean(System.getProperty("ncore.db.mf.auth"));
  private boolean isCert = Boolean.parseBoolean(System.getProperty("ncore.db.mf.cert"));
  private boolean isLogin = Boolean.parseBoolean(System.getProperty("ncore.db.mf.uselogin"));

  private DataSource dataSource;

  public ConnectionPoolImpl(DatabaseConnection databaseConnection) {
    this.databaseConnection = databaseConnection;

    if (isMfAuth) {
      ConnectionProps props = new ConnectionProps(this.databaseConnection.getURL(),
          isLogin ? this.databaseConnection.getUserName() : null,
          isLogin ? this.databaseConnection.getUnencryptedPassword() : null,
          null,
          isMfAuth,
          "Test process",
          null,
          isCert ? getCertbase64() : null);

      try {
        initMfConnectionFactory(props);
      } catch (SQLException e) {
        GUIUtilities.displayErrorMessage("Error connecting" + "\n" + e.toString());
      }
    }
    if (Log.isDebugEnabled()) Log.debug("Creating new pool for connection " + databaseConnection.getName());
  }

  public static void initMfConnectionFactory(ConnectionProps props) throws SQLException {
    factory = JdbcConnectionFactory.getConnectionFactory(props);
  }

  public DatabaseConnection getDatabaseConnection() {
    return databaseConnection;
  }

  public void connectionClosed(PooledConnection pooledConnection) {
    if (Log.isDebugEnabled()) {
      Log.debug("Removing connection " + pooledConnection.getId()
          + " from active connections list");
    }

    activeConnections.remove(pooledConnection);
    reduceCapacity(minimumConnections);
  }

  public void close(Connection connection) {
    if (connection != null) {
      activeConnections.remove(connection);
      PooledConnection pooledConnection = (PooledConnection) connection;
      pooledConnection.destroy();
      openConnections.remove(pooledConnection);
    }

    ensureCapacity(minimumConnections);
  }

  public synchronized void close() {
    if (Log.isDebugEnabled()) Log.debug("Closing connection pool for connection " + databaseConnection.getName());
    for (Connection connection : openConnections) {
      PooledConnection pooledConnection = (PooledConnection) connection;
      pooledConnection.destroy();
    }

    activeConnections.clear();
    openConnections.clear();
  }

  public synchronized Connection getConnection() {
    if (openConnections.size() < minimumConnections) ensureCapacity(minimumConnections);

    PooledConnection connection = getNextOpenAvailable();
    if (connection != null) {
      try {
        if (connection.isClosed()) {
          close(connection);
          return getConnection();
        }
      } catch (SQLException ignored) {
      }

      connection.setInUse(true);
      activeConnections.add(connection);
    } else if (openConnections.size() < maximumConnections) {
      createConnection();
      return getConnection();
    } else {
      throw new DataSourceException("Maximum open connection count exceeded");
    }

    if (Log.isDebugEnabled()) Log.debug("Retrieving connection " + connection.getId());
    return connection;
  }

  private void ensureCapacity(int capacity) {
    if (Log.isDebugEnabled()) Log.debug("Ensuring pool capacity " + capacity);

    while (openConnections.size() < capacity) createConnection();
  }

  private void reduceCapacity(int capacity) {
    if (Log.isDebugEnabled()) Log.debug("Reducing pool capacity " + capacity);
    while (openConnections.size() > capacity) {
      PooledConnection connection = getNextOpenAvailable();
      if (connection != null) close(connection);
      else break;
    }
  }

  private Connection getConn() throws SQLException {
    return factory.newConnection(IsolationLevel.READ_COMMITTED);
  }

  private PooledConnection createConnection() {
    PooledConnection connection = null;

    try {
      if (dataSource == null) dataSource = new SimpleDataSource(databaseConnection);

      Connection realConnection;
      if (isMfAuth) realConnection = getConn();
      else realConnection = dataSource.getConnection();

      if (realConnection == null)
        throw new DataSourceException(
            "A connection to the database could not be " +
                "established.\nPlease ensure that the details " +
                "are correct and the supplied host is available.");

      if (defaultTxIsolation == -1) defaultTxIsolation = realConnection.getTransactionIsolation();

      int transactionIsolation = databaseConnection.getTransactionIsolation();
      if (transactionIsolation != -1)
        realConnection.setTransactionIsolation(databaseConnection.getTransactionIsolation());

      connection = new PooledConnection(realConnection);
      connection.addPooledConnectionListener(this);

      openConnections.add(connection);
      if (Log.isDebugEnabled()) Log.debug("Added new connection to the pool - " + connection.getId());
    } catch (SQLException e) {
      rethrowAsDataSourceException(e);
    }

    return connection;
  }

  private PooledConnection getNextOpenAvailable() {
    for (PooledConnection pooledConnection : openConnections)
      if (pooledConnection.isAvailable()) return pooledConnection;

    return null;
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public int getMaximumConnections() {
    return maximumConnections;
  }

  public void setMaximumConnections(int maximumConnections) {
    if (maximumConnections < 1) throw new IllegalArgumentException("Maximum connection count must be at least 1");
    this.maximumConnections = maximumConnections;
  }

  public int getMaximumUseCount() {
    return 0;
  }

  public void setMaximumUseCount(int maximumUseCount) {

  }

  public int getMinimumConnections() {
    return minimumConnections;
  }

  public void setMinimumConnections(int minimumConnections) {
    if (minimumConnections < 1) throw new IllegalArgumentException("Minimum connection count must be at least 1");

    this.minimumConnections = minimumConnections;
    ensureCapacity(minimumConnections);
  }

  public int getPoolActiveSize() {
    return activeConnections.size();
  }

  public int getSize() {
    return openConnections.size();
  }

  public boolean isTransactionSupported() {
    return false;
  }

  public int getInitialConnections() {
    return initialConnections;
  }

  public void setInitialConnections(int initialConnections) {
    if (initialConnections < 1) throw new IllegalArgumentException("Initial connection count must be at least 1");
    this.initialConnections = initialConnections;
  }

  public void setTransactionIsolationLevel(int isolationLevel) {
    if (!isTransactionSupported()) return;
    if (isolationLevel == -1) isolationLevel = defaultTxIsolation;

    try {
      for (Connection connection : openConnections)
        if (!connection.isClosed()) connection.setTransactionIsolation(databaseConnection.getTransactionIsolation());
    } catch (SQLException e) {
      throw new DataSourceException(e);
    }
  }

  private String getCertbase64() {
    final Certificate certificate = XmlSign.getCertificate(System.getProperty("ncore.db.mf.cert_alias"));
    try {
      return certificate == null ? null : Base64.encodeBytes(certificate.getEncoded());
    } catch (CertificateEncodingException e) {
      GUIUtilities.displayErrorMessage("Error loading certificate" + "\n" + e.toString());
    }
    return null;
  }
}





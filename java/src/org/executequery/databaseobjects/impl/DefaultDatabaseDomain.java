package org.executequery.databaseobjects.impl;

import org.executequery.databaseobjects.DatabaseColumn;
import org.executequery.databaseobjects.DatabaseDomain;
import org.executequery.databaseobjects.DatabaseHost;
import org.executequery.databaseobjects.DatabaseObject;
import org.underworldlabs.jdbc.DataSourceException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 12.06.2015.
 */
public class DefaultDatabaseDomain extends DefaultDatabaseObject
    implements DatabaseDomain {

  private List<DatabaseColumn> columns;

  public DefaultDatabaseDomain(DatabaseHost host) {
    super(host, "DOMAIN");
  }

  public DefaultDatabaseDomain(DatabaseHost host, String metaDataKey) {
    super(host, metaDataKey);
    setName(metaDataKey);
  }

  public DefaultDatabaseDomain(DatabaseObject object) {
    this(object.getHost());
    setName(object.getName());
    setRemarks(object.getRemarks());
  }

  @Override
  public List<DatabaseColumn> getColumns() throws DataSourceException {
    if (!isMarkedForReload() && columns != null) {
      return columns;
    }

    if (columns != null) {
      columns.clear();
      columns = null;
    }

    columns = new ArrayList<DatabaseColumn>(0);

    DatabaseHost host = getHost();
    if (host != null) {
      ResultSet rs = null;
      try {
        Statement statement = host.getConnection().createStatement();
        rs = statement.executeQuery(
            "SELECT RF.RDB$FIELD_NAME,\n" +
                "    RT.RDB$TYPE_NAME,\n" +
                "    RF.RDB$VALIDATION_SOURCE,\n" +
                "    RF.RDB$DESCRIPTION\n" +
                "FROM RDB$FIELDS RF JOIN RDB$TYPES RT ON\n" +
                "    RF.RDB$FIELD_TYPE = RT.RDB$TYPE\n" +
                "WHERE RF.RDB$SYSTEM_FLAG = 0 AND\n" +
                "    RT.RDB$FIELD_NAME='RDB$FIELD_TYPE' AND\n" +
                "    RF.RDB$FIELD_NAME='" + getName() + "';");

        while (rs.next()) {
          DefaultDatabaseColumn defaultDatabaseColumn = new DefaultDatabaseColumn();
          defaultDatabaseColumn.setName(rs.getString(1));
          defaultDatabaseColumn.setTypeName(rs.getString(2));
          columns.add(defaultDatabaseColumn);
        }

        return columns;
      } catch (SQLException e) {
        e.printStackTrace();
        return new ArrayList<DatabaseColumn>(0);
      } finally {
        releaseResources(rs);
      }
    }
    return new ArrayList<DatabaseColumn>(0);
  }

  @Override
  public boolean hasSQLDefinition() {
    return true;
  }


}

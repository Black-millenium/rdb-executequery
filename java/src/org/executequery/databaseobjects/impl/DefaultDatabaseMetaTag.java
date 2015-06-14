/*
 * DefaultDatabaseMetaTag.java
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

package org.executequery.databaseobjects.impl;

import org.apache.commons.lang.StringUtils;
import org.executequery.databaseobjects.*;
import org.underworldlabs.jdbc.DataSourceException;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Default meta tag object implementation.
 *
 * @author Takis Diakoumis
 * @version $Revision: 161 $
 */
public class DefaultDatabaseMetaTag extends AbstractNamedObject
    implements DatabaseMetaTag {

  /**
   * the catalog object for this meta tag
   */
  private DatabaseCatalog catalog;

  /**
   * the schema object for this meta tag
   */
  private DatabaseSchema schema;

  /**
   * the host object for this meta tag
   */
  private DatabaseHost host;

  /**
   * the meta data key name of this object
   */
  private String metaDataKey;

  /**
   * the child objects of this meta type
   */
  private List<NamedObject> children;

  /**
   * Creates a new instance of DefaultDatabaseMetaTag
   */
  public DefaultDatabaseMetaTag(DatabaseHost host,
                                DatabaseCatalog catalog,
                                DatabaseSchema schema,
                                String metaDataKey) {
    this.host = host;
    setCatalog(catalog);
    setSchema(schema);
    this.metaDataKey = metaDataKey;
  }

  /**
   * Returns the db object with the specified name or null if
   * it does not exist.
   *
   * @param name the name of the object
   * @return the NamedObject or null if not found
   */
  public NamedObject getNamedObject(String name) throws DataSourceException {

    List<NamedObject> objects = getObjects();
    if (objects != null) {

      name = name.toUpperCase();

      for (NamedObject object : objects) {

        if (name.equals(object.getName().toUpperCase())) {

          return object;
        }

      }

    }

    return null;
  }

  /**
   * Retrieves child objects classified as this tag type.
   * These may be database tables, functions, procedures, sequences, views, etc.
   *
   * @return this meta tag's child database objects.
   */
  public List<NamedObject> getObjects() throws DataSourceException {

    if (!isMarkedForReload() && children != null) {
      return children;
    }

    int type = getSubType();

    switch (type) {
      case DOMAIN:
        children = getDomains();
        if (children != null) {
          List<NamedObject> _children = new ArrayList<NamedObject>(children.size());
          for (NamedObject i : children) {
            _children.add(new DefaultDatabaseDomain((DatabaseObject) i));
          }
          children = _children;
        }
        break;

      case FUNCTION:
        try {
          if (StringUtils.equalsIgnoreCase(getName(), procedureTerm())) {
            children = getFunctions();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        break;
      case PROCEDURE:
        try {
          if (StringUtils.equalsIgnoreCase(getName(), procedureTerm())) {
            children = getProcedures();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        break;

      case TABLE:
        children = getHost().getTables(null, null, getMetaDataKey());
        if (children != null) {
          List<NamedObject> _children = new ArrayList<NamedObject>(children.size());
          for (NamedObject i : children) {
            _children.add(new DefaultDatabaseTable((DatabaseObject) i));
          }
          children = _children;
        }
        break;

      case VIEW:
        children = getHost().getTables(null, null, getMetaDataKey());
        if (children != null) {
          List<NamedObject> _children = new ArrayList<NamedObject>(children.size());
          for (NamedObject i : children) {
            _children.add(new DefaultDatabaseView((DatabaseObject) i));
          }
          children = _children;
        }
        break;

      case SYSTEM_FUNCTION:
        children = getSystemFunctionTypes();
        break;

      case SYSTEM_TABLE:
        children = getHost().getTables(null, null, getMetaDataKey());
        break;
    }

    addAsParentToObjects(children);
    return children;
  }

  private void addAsParentToObjects(List<NamedObject> children) {

    if (children != null) {

      for (NamedObject i : children) {

        ((DatabaseObject) i).setParent(this);
      }

    }

  }

  public boolean hasChildObjects() throws DataSourceException {
    if (!isMarkedForReload() && children != null) return !children.isEmpty();

    try {
      int type = getSubType();

      switch (type) {
        case DOMAIN:
          return hasDomains();

        case FUNCTION:
          if (isFunctionOrProcedure()) {
            return StringUtils.equalsIgnoreCase(getName(), procedureTerm()) && hasFunctions();
          }
          break;

        case PROCEDURE:
          if (isFunctionOrProcedure()) {
            return StringUtils.equalsIgnoreCase(getName(), procedureTerm()) && hasProcedures();
          }
          break;

        case SYSTEM_FUNCTION:
          break;

        default:
          return getHost().hasTablesForType(null, null, getMetaDataKey());
      }
    } catch (SQLException e) {
      logThrowable(e);
      return false;
    }
    return true;
  }

  private boolean hasDomains() {
    ResultSet rs = null;
    try {
      rs = getDomainsResultSet();
      return rs != null && rs.next();
    } catch (SQLException e) {
      logThrowable(e);
      return false;
    } finally {
      releaseResources(rs);
    }
  }

  private boolean isFunctionOrProcedure() {

    int type = getSubType();
    return type == FUNCTION || type == PROCEDURE;
  }

  private String procedureTerm() throws SQLException {

    return getHost().getDatabaseMetaData().getProcedureTerm();
  }

  private boolean hasFunctions() {

    ResultSet rs = null;
    try {

      rs = getFunctionsResultSet();
      return rs != null && rs.next();

    } catch (SQLException e) {

      logThrowable(e);
      return false;

    } finally {

      releaseResources(rs);
    }
  }

  private boolean hasProcedures() {

    ResultSet rs = null;
    try {

      rs = getProceduresResultSet();
      return rs != null && rs.next();

    } catch (SQLException e) {

      logThrowable(e);
      return false;

    } finally {

      releaseResources(rs);
    }
  }

  /**
   * Loads the database functions.
   */
  private List<NamedObject> getFunctions() throws DataSourceException {

    ResultSet rs = null;
    try {

      rs = getFunctionsResultSet();
      List<NamedObject> list = new ArrayList<NamedObject>();
      if (rs != null) { // informix returns null rs

        while (rs.next()) {

          DefaultDatabaseFunction function = new DefaultDatabaseFunction(this, rs.getString(3));
          function.setRemarks(rs.getString(7));
          list.add(function);
        }

      }
      return list;

    } catch (SQLException e) {

      logThrowable(e);
      return new ArrayList<NamedObject>(0);

    } finally {

      releaseResources(rs);
    }
  }

  /**
   * Loads the database procedures.
   */
  private List<NamedObject> getProcedures() throws DataSourceException {

    ResultSet rs = null;
    try {

      rs = getProceduresResultSet();
      List<NamedObject> list = new ArrayList<NamedObject>();
      while (rs.next()) {

        DefaultDatabaseProcedure procedure = new DefaultDatabaseProcedure(this, rs.getString(3));
        procedure.setRemarks(rs.getString(7));
        list.add(procedure);
      }

      return list;

    } catch (SQLException e) {

      logThrowable(e);
      return new ArrayList<NamedObject>(0);

    } finally {

      releaseResources(rs);
    }
  }

  private ResultSet getProceduresResultSet() throws SQLException {

    String catalogName = getHost().getCatalogNameForQueries(getCatalogName());
    String schemaName = getHost().getSchemaNameForQueries(getSchemaName());

    DatabaseMetaData dmd = getHost().getDatabaseMetaData();
    return dmd.getProcedures(catalogName, schemaName, null);
  }

  private ResultSet getFunctionsResultSet() throws SQLException {

    String catalogName = getHost().getCatalogNameForQueries(getCatalogName());
    String schemaName = getHost().getSchemaNameForQueries(getSchemaName());

    DatabaseMetaData dmd = getHost().getDatabaseMetaData();

    // TODO: 1.6 getFunctions
    return dmd.getProcedures(catalogName, schemaName, null);
  }

  private List<NamedObject> getDomains() throws DataSourceException {
    ResultSet rs = null;
    try {
      rs = getDomainsResultSet();
      List<NamedObject> list = new ArrayList<NamedObject>();
      while (rs.next()) {
        DefaultDatabaseDomain domain = new DefaultDatabaseDomain(getHost(), rs.getString(1));
        domain.setRemarks(rs.getString(4));
        list.add(domain);
      }
      return list;
    } catch (SQLException e) {
      logThrowable(e);
      return new ArrayList<NamedObject>(0);
    } finally {
      releaseResources(rs);
    }
  }

  private ResultSet getDomainsResultSet() throws SQLException {
    Statement stmt = getHost().getConnection().createStatement();

    return stmt.executeQuery(
        "SELECT RF.RDB$FIELD_NAME,\n" +
            "    RT.RDB$TYPE_NAME,\n" +
            "    RF.RDB$VALIDATION_SOURCE,\n" +
            "    RF.RDB$DESCRIPTION\n" +
            "FROM RDB$FIELDS RF JOIN RDB$TYPES RT ON\n" +
            "    RF.RDB$FIELD_TYPE = RT.RDB$TYPE\n" +
            "WHERE RF.RDB$SYSTEM_FLAG = 0 AND\n" +
            "    RT.RDB$FIELD_NAME='RDB$FIELD_TYPE';");
  }

  /**
   * Loads the system function types.
   */
  private List<NamedObject> getSystemFunctionTypes() {

    List<NamedObject> objects = new ArrayList<NamedObject>(3);

    objects.add(new DefaultSystemFunctionMetaTag(
        this, SYSTEM_STRING_FUNCTIONS, "String Functions"));

    objects.add(new DefaultSystemFunctionMetaTag(
        this, SYSTEM_NUMERIC_FUNCTIONS, "Numeric Functions"));

    objects.add(new DefaultSystemFunctionMetaTag(
        this, SYSTEM_DATE_TIME_FUNCTIONS, "Date/Time Functions"));

    return objects;
  }

  /**
   * Returns the sub-type indicator of this meta tag - the type this
   * meta tag ultimately represents.
   *
   * @return the sub-type, or -1 if not found/available
   */
  public int getSubType() {

    String key = getMetaDataKey();
    for (int i = 0; i < META_TYPES.length; i++) {

      if (META_TYPES[i].equals(key)) {

        return i;
      }

    }

    return -1;
  }

  /**
   * Returns the parent host object.
   *
   * @return the parent object
   */
  public DatabaseHost getHost() {
    return host;
  }

  /**
   * Returns the name of this object.
   *
   * @return the object name
   */
  public String getName() {
    return getMetaDataKey();
  }

  /**
   * Override to do nothing - name is the meta data key value.
   */
  public void setName(String name) {
  }

  /**
   * Returns the catalog name or null if there is
   * no catalog attached.
   */
  private String getCatalogName() {

    DatabaseCatalog _catalog = getCatalog();
    if (_catalog != null) {

      return _catalog.getName();
    }

    return null;
  }

  /**
   * Returns the parent catalog object.
   *
   * @return the parent catalog object
   */
  public DatabaseCatalog getCatalog() {
    return catalog;
  }

  public void setCatalog(DatabaseCatalog catalog) {
    this.catalog = catalog;
  }

  /**
   * Returns the schema name or null if there is
   * no schema attached.
   */
  private String getSchemaName() {

    DatabaseSchema _schema = getSchema();
    if (_schema != null) {

      return _schema.getName();
    }

    return null;
  }

  /**
   * Returns the parent schema object.
   *
   * @return the parent schema object
   */
  public DatabaseSchema getSchema() {
    return schema;
  }

  public void setSchema(DatabaseSchema schema) {
    this.schema = schema;
  }

  /**
   * Returns the parent named object of this object.
   *
   * @return the parent object - catalog or schema
   */
  public NamedObject getParent() {
    return getSchema() == null ? getCatalog() : getSchema();
  }

  /**
   * Returns the database object type.
   *
   * @return the object type
   */
  public int getType() {
    return META_TAG;
  }

  /**
   * Returns the meta data key name of this object.
   *
   * @return the meta data key name.
   */
  public String getMetaDataKey() {
    return metaDataKey;
  }

  /**
   * Does nothing.
   */
  public int drop() throws DataSourceException {
    return 0;
  }

}

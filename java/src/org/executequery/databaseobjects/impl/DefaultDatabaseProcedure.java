package org.executequery.databaseobjects.impl;

import org.executequery.databaseobjects.DatabaseMetaTag;
import org.executequery.databaseobjects.DatabaseProcedure;

public class DefaultDatabaseProcedure extends DefaultDatabaseExecutable
    implements DatabaseProcedure {

  public DefaultDatabaseProcedure() {
  }

  public DefaultDatabaseProcedure(DatabaseMetaTag metaTagParent, String name) {
    super(metaTagParent, name);
  }

  public DefaultDatabaseProcedure(String schema, String name) {
    setName(name);
    setSchemaName(schema);
  }

  public int getType() {
    return PROCEDURE;
  }

  public String getMetaDataKey() {
    return META_TYPES[PROCEDURE];
  }

}








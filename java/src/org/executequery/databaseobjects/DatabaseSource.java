/*
 * DatabaseSource.java
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

package org.executequery.databaseobjects;

import org.underworldlabs.jdbc.DataSourceException;

import java.util.List;

/**
 * Defines a database 'source' object - typically a catalog or schema.
 *
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public interface DatabaseSource extends NamedObject {

  /**
   * Returns the meta object with the specified name
   *
   * @param name the meta tag name
   * @return the meta tag object
   */
  DatabaseMetaTag getDatabaseMetaTag(String name) throws DataSourceException;

  /**
   * Returns the meta type objects from this schema
   *
   * @return the meta type objects
   */
  List<DatabaseMetaTag> getMetaObjects() throws DataSourceException;

  /**
   * Returns the parent host object.
   *
   * @return the parent object
   */
  DatabaseHost getHost();

  /**
   * Returns the tables belonging to this source.
   *
   * @return the hosted tables
   */
  List<NamedObject> getTables() throws DataSourceException;

  /**
   * Returns the procedure with the specified name.
   *
   * @param name
   * @return the named procedure
   */
  DatabaseProcedure getProcedure(String name);

  /**
   * Returns whether this is the default source connection.
   *
   * @return true | false
   */
  boolean isDefault();

  /**
   * Returns the parent catalog object.
   *
   * @return the parent catalog object
   */
  DatabaseCatalog getCatalog();

  /**
   * Returns the parent schema object.
   *
   * @return the parent schema object
   */
  DatabaseSchema getSchema();

}











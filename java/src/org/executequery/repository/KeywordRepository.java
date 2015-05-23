/*
 * KeywordRepository.java
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

package org.executequery.repository;

import java.util.List;

/**
 * @author Takis Diakoumis
 * @version $Revision:1105 $
 * @date $Date:2008-02-08 15:05:55 +0000 (Fri, 08 Feb 2008) $
 */
public interface KeywordRepository extends Repository {

  String REPOSITORY_ID = "keywords";

  List<String> getSQL92();

  List<String> getUserDefinedSQL();

  List<String> getSQLKeywords();

  void setUserDefinedKeywords(List<String> keywords);

  boolean contains(String word);

  void addUserDefinedKeyword(String word);

}










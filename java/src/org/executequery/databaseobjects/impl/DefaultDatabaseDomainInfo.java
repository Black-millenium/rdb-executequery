package org.executequery.databaseobjects.impl;


/**
 * Created by Roman on 13.06.2015.
 */
public class DefaultDatabaseDomainInfo {
  private String name;
  private String value;

  public DefaultDatabaseDomainInfo(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return getName();
  }
}

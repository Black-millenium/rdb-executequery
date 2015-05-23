package org.executequery.databaseobjects;

import org.executequery.gui.resultset.RecordDataItem;

import java.util.List;

/**
 * @author Takis Diakoumis
 * @version $Revision: 1185 $
 * @date $Date: 2013-02-08 22:16:55 +1100 (Fri, 08 Feb 2013) $
 */
public class TableDataChange {

  private final List<RecordDataItem> rowDataForRow;

  public TableDataChange(List<RecordDataItem> rowDataForRow) {
    this.rowDataForRow = rowDataForRow;
  }

  public List<RecordDataItem> getRowDataForRow() {
    return rowDataForRow;
  }
}

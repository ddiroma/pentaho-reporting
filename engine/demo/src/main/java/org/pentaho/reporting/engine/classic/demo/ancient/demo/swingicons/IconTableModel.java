/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.reporting.engine.classic.demo.ancient.demo.swingicons;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * A simple <code>TableModel</code> implementation used for demonstration purposes.
 *
 * @author David Gilbert
 */
public class IconTableModel extends AbstractTableModel
{

  /**
   * The table data.
   */
  private final List data;

  /**
   * Constructs a new IconTableModel, initially empty.
   */
  public IconTableModel()
  {
    this.data = new ArrayList();
  }

  /**
   * Adds a new entry to the table model.
   *
   * @param name     The icon name.
   * @param category The category name.
   * @param icon     The icon.
   * @param size     The size of the icon image in bytes.
   */
  protected void addIconEntry(final String name, final String category,
                              final Image icon, final Long size)
  {
    final Object[] item = new Object[4];
    item[0] = name;
    item[1] = category;
    item[2] = icon;
    item[3] = size;
    data.add(0, item);
    fireTableDataChanged();
  }

  public void clear()
  {
    data.clear();
    fireTableDataChanged();
  }

  /**
   * Returns the number of rows in the table model.
   *
   * @return The row count.
   */
  public int getRowCount()
  {
    return data.size();
  }

  /**
   * Returns the number of columns in the table model.
   *
   * @return The column count.
   */
  public int getColumnCount()
  {
    return 4;
  }

  /**
   * Returns the data item at the specified row and column.
   *
   * @param row    The row index.
   * @param column The column index.
   * @return The data item.
   */
  public Object getValueAt(final int row, final int column)
  {
    final Object[] item = (Object[]) data.get(row);
    return item[column];
  }

  /**
   * Returns the class of the specified column.
   *
   * @param column The column index.
   * @return The column class.
   */
  public Class getColumnClass(final int column)
  {
    if (column == 2)
    {
      return java.awt.Image.class;
    }
    else
    {
      return Object.class;
    }
  }

  /**
   * Returns the name of the specified column.
   *
   * @param column The column.
   * @return The column name.
   */
  public String getColumnName(final int column)
  {
    switch (column)
    {
      case 0:
        return "Name";
      case 1:
        return "Category";
      case 2:
        return "Icon";
      case 3:
        return "Size";
      default:
        return "Error";
    }
  }

}

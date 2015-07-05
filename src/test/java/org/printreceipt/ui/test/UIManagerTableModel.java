/*
 * Copyright 2015 Michele Lazzeri
 * 
 * This file is part of Print Receipt!
 * 
 *  Print Receipt! is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  Print Receipt! is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with Print Receipt!.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.printreceipt.ui.test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.TableModel;

public class UIManagerTableModel implements TableModel {

	String[] colName = { "Key", "Value", "Color" };
	Object[][] rowData;

	public UIManagerTableModel() {
		UIDefaults defaults = UIManager.getDefaults();
		rowData = new Object[defaults.size()][3];
		int i = 0;

		for (Enumeration<?> e = defaults.keys(); e.hasMoreElements(); i++) {
			Object key = e.nextElement();
			rowData[i][0] = key.toString();
			rowData[i][1] = "" + defaults.get(key);
			if (defaults.get(key) instanceof ColorUIResource) {
				rowData[i][2] = defaults.get(key);
			} else {
				rowData[i][2] = null;
			}
			System.out.println(rowData[i][0] + " \t\t\t " + rowData[i][1]);
		}

		Arrays.sort(rowData, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				return o1[0].toString().compareTo(o2[0].toString());
			}
		});
	}

	@Override
	public int getRowCount() {
		return rowData.length;
	}

	@Override
	public int getColumnCount() {
		return colName.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return colName[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnIndex == 2 ? ColorUIResource.class : String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return rowData[rowIndex][columnIndex];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

}

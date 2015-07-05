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


package org.printreceipt.utils;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.printreceipt.AppException;
import org.printreceipt.Messages;

public class ConfigureTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6421008879462297504L;

	public static final int MOVE_UP = 1;
	public static final int MOVE_DOWN = -1;

	private List<String> columnNames;
	private List<Object> types;

	private ArrayList<ConfigureTableRow> data = new ArrayList<ConfigureTableRow>();

	public ConfigureTableModel(List<String> columnNames, List<Object> types)
			throws AppException {
		super();
		this.columnNames = columnNames;
		this.types = types;
		refresh();
	}

	public void refresh() throws AppException {
		fireTableDataChanged();
	}

	public ArrayList<ConfigureTableRow> getData() {
		return data;
	}

	public void addTableRow(ConfigureTableRow row) throws AppException {
		this.data.add(row);
		refresh();
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int indexCol) {
		return Messages.getString((String) columnNames.get(indexCol));
	}

	public void removeAt(int indexRow) throws AppException {
		data.remove(indexRow);
		refresh();

	}

	public Object getValueAt(int indexRow, int indexCol) {
		return data.get(indexRow).getValue(indexCol);
	}

	public void setValueAt(Object value, int indexRow, int indexCol) {
		if (indexCol < getColumnCount() - 3) {
			data.get(indexRow).setValue(indexCol, value);
		}
	}

	public Class<?> getColumnClass(int indexCol) {
		if (this.types.get(indexCol) instanceof Class<?>) {
			return (Class<?>) this.types.get(indexCol);
		} else {
			return this.types.get(indexCol).getClass();
		}
	}

	public boolean isCellEditable(int indexRow, int indexCol) {
		return true;
	}

	public void move(int indexRow, int direction) throws AppException {
		ConfigureTableRow tmp;
		if (direction == MOVE_UP && indexRow > 0) {
			tmp = data.get(indexRow - 1);
			data.set(indexRow - 1, data.get(indexRow));
			data.set(indexRow, tmp);
		}
		if (direction == MOVE_DOWN && indexRow < getRowCount() - 1) {
			tmp = data.get(indexRow + 1);
			data.set(indexRow + 1, data.get(indexRow));
			data.set(indexRow, tmp);
		}
		refresh();
	}
}
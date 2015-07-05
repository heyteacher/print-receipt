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


package org.printreceipt.controller;

import java.text.NumberFormat;

import javax.swing.table.AbstractTableModel;

import org.printreceipt.AppException;
import org.printreceipt.Application;
import org.printreceipt.Messages;
import org.printreceipt.datamodel.EventItemStatistics;
import org.printreceipt.model.ModelManager;

public class SoldsTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8376172690172002909L;

	private static NumberFormat numberformat = NumberFormat.getIntegerInstance();
	static {
		numberformat.setMaximumFractionDigits(0);
	}

	private static final int NAME_COL = 0;
	private static final int VALUE_COL = 1;

	private static String[] columnNames = { 
		Messages.getString("Item"),
		Messages.getString("Quantity"), };

	private EventItemStatistics[] data;
	private Application application;

	public SoldsTableModel(Application application) throws AppException {
		super();
		this.application = application;
		refresh();
	}

	public void refresh() throws AppException {
		data = ModelManager
				.getECBManager()
				.getEventStatistics(application.getEvent(),
						application.getUser()).getItems_statistics();
		fireTableDataChanged();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		switch (col) {
		case NAME_COL:
			return data[row].getName();
		case VALUE_COL:
			return numberformat.format(data[row].getQuantity());
		default:
			return "";
		}
	}

	public Class<?> getColumnClass(int c) {
		return Number.class;
	}
}
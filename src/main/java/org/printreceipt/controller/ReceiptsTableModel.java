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
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.printreceipt.AppException;
import org.printreceipt.Application;
import org.printreceipt.Messages;
import org.printreceipt.datamodel.Receipt;
import org.printreceipt.model.ModelManager;
import org.printreceipt.utils.Config;

public class ReceiptsTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8376172690172002909L;

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");
	private static NumberFormat numberformat = NumberFormat.getIntegerInstance();
	static {
		numberformat.setMinimumFractionDigits(2);
		numberformat.setMaximumFractionDigits(2);
	}

	private static final int DATE_COL = 0;
	private static final int NOTE_COL = 1;
	private static final int AMOUNT_COL = 2;
	private static final int DEL_COL = 3;

	private static String[] columnNames = { 
		Messages.getString("Date"),
		Messages.getString("Note"), 
		Messages.getString("Amount"),
		Messages.getString("Delete") };

	private List<Receipt> data;
	private Application application;

	public ReceiptsTableModel(Application application) throws AppException {
		super();
		this.application = application;
		reload();
	}

	private void reload() throws AppException {
		data = ModelManager.getReceiptManager().loadReceipts(
				application.getEvent(), application.getUser());
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case DATE_COL:
			return sdf.format(data.get(row).getRegDate());
		case NOTE_COL:
			return data.get(row).getNote();
		case AMOUNT_COL:
			return Config.currencyFormat(data.get(row).getAmount())
					//+ " €"
					;
		case DEL_COL:
			return data.get(row);
		default:
			return "";
		}
	}

	@Override
	public Class<?> getColumnClass(int col) {
		if (col == DEL_COL) {
			return Receipt.class;
		}
		return Number.class;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == DEL_COL) {
			return true;
		}
		return false;
	}

	public Receipt getReceiptAt(int index) {
		if (index >= data.size() || index < 0) {
			return null;
		}
		return data.get(index);
	}

	public void removeAt(int index) throws AppException {
		ModelManager.getReceiptManager().delete(data.get(index).getId(),
				application.getUser());
		reload();
		fireTableDataChanged();
	}

	public void removeAll() throws AppException {
		ModelManager.getReceiptManager().empty(application.getEvent(),
				application.getUser());
		data.clear();
		fireTableDataChanged();
	}
}
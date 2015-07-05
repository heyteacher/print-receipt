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
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.printreceipt.AppException;
import org.printreceipt.Application;
import org.printreceipt.Messages;
import org.printreceipt.datamodel.Receipt;
import org.printreceipt.datamodel.ReceiptItem;
import org.printreceipt.model.ModelManager;
import org.printreceipt.utils.Config;

public class ReceiptItemsTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8376172690172002909L;

	private static NumberFormat numberformat = NumberFormat.getIntegerInstance();
	private static NumberFormat currencyformat = NumberFormat
			.getIntegerInstance();
	static {
		numberformat.setMaximumFractionDigits(2);
		currencyformat.setMinimumFractionDigits(2);
		currencyformat.setMaximumFractionDigits(2);
	}

	private static final int NAME_COL = 0;
	private static final int QUANTITY_COL = 1;
	private static final int PRICE_COL = 2;
	private static final int AMOUNT_COL = 3;

	private static String[] columnNames = { 
		Messages.getString("Item"),
		Messages.getString("Quantity"), 
		Messages.getString("Price"),
		Messages.getString("Amount") };
	private List<ReceiptItem> data;
	private Application application;

	public ReceiptItemsTableModel(Application application) throws AppException {
		super();
		this.application = application;
	}

	public void loadItems(Receipt receipt) throws AppException {
		if (receipt != null) {
			data = ModelManager.getReceiptManager().loadReceiptItems(receipt,
					application.getUser());
		} else {
			data = null;
		}

		fireTableDataChanged();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data != null ? data.size() : 0;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		if (data == null) {
			return "";
		}
		switch (col) {
		case NAME_COL:
			return data.get(row).getItem().getName();
		case QUANTITY_COL:
			return numberformat.format(data.get(row).getQuantity());
		case PRICE_COL:
			return  Config.currencyFormat(data.get(row).getItem().getPrice());
		case AMOUNT_COL:
			return Config.currencyFormat(
					data.get(row).getItem().getPrice() * 
					data.get(row).getQuantity());
		default:
			return "";
		}
	}

	public Class<?> getColumnClass(int c) {
		return Number.class;
	}

	public void clean() {
		data = null;
		fireTableDataChanged();
	}
}
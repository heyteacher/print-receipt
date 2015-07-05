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

import java.awt.Color;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class DateCellTableEditor extends AbstractCellEditor implements
		TableCellEditor {

	JTextField textField = new JTextField();
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1769081038437322896L;

	@Override
	public Object getCellEditorValue() {
		try {
			textField.setForeground(Color.BLACK);
			return sdf.parse(textField.getText());
		} catch (ParseException e) {
			textField.setForeground(Color.RED);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		ConfigureTableModel tableModel = (ConfigureTableModel) table.getModel();
		Date date = (Date) tableModel.getValueAt(row, column);
		if (date != null) {
			textField.setText(sdf.format(date));
		}
		else {
		    textField.setText("dd/MM/yyyy");
		}
		return textField;
	}
}
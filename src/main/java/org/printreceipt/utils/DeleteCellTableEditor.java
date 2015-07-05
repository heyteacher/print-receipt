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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.printreceipt.AppException;
import org.printreceipt.Application;
import org.printreceipt.Messages;

public class DeleteCellTableEditor extends AbstractCellEditor implements
		ActionListener, TableCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6614535295576038511L;

	JButton button;
	ConfigureTableModel tableModel;
	int currentRow;
	Application application;

	public DeleteCellTableEditor(Application application) {
		this.application = application;
		button = new JButton(Messages.getString("Delete"));
		button.addActionListener(this);
		button.setBorder(BorderFactory.createEmptyBorder());
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		try {
			int dialogResponse = JOptionPane.showConfirmDialog(application,
					Messages.getString("Do you want to delete selected row?"),
					Messages.getString("Cancel"), JOptionPane.YES_NO_OPTION);
			if (dialogResponse == JOptionPane.YES_OPTION) {
				tableModel.removeAt(currentRow);
			}
		} catch (AppException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		tableModel = (ConfigureTableModel) table.getModel();
		currentRow = row;
		return button;
	}
}
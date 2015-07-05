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
import javax.swing.table.TableModel;

import org.printreceipt.AppException;
import org.printreceipt.Messages;
import org.printreceipt.controller.CashOpTableModel;
import org.printreceipt.controller.ReceiptsTableModel;
import org.printreceipt.view.SummaryView;

public class SummaryViewDeleteCellTableEditor extends AbstractCellEditor
		implements TableCellEditor, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6614535295576038511L;

	JButton button;
	SummaryView view;
	JTable table;
	TableModel tableModel;
	int currentRow;

	public SummaryViewDeleteCellTableEditor(SummaryView view, JTable table) {
		this.view = view;
		this.table = table;
		button = new JButton(Messages.getString("Delete"));
		button.addActionListener(this);
		button.setBorder(BorderFactory.createEmptyBorder());
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		try {
			if (tableModel instanceof CashOpTableModel) {
				int dialogResponse = JOptionPane.showConfirmDialog(view
						.getApplication(), Messages
						.getString("Do you want delete selected cash operation?"),
						Messages.getString("Cancel"),
						JOptionPane.YES_NO_OPTION);
				if (dialogResponse == JOptionPane.YES_OPTION) {
					CashOpTableModel cashOpTableModel = (CashOpTableModel) tableModel;
					cashOpTableModel.removeAt(currentRow);
				}
			} else if (tableModel instanceof ReceiptsTableModel) {
				int dialogResponse = JOptionPane.showConfirmDialog(view
						.getApplication(), Messages
						.getString("Do you want to delete selected receipt?"),
						Messages.getString("Cancel"),
						JOptionPane.YES_NO_OPTION);
				if (dialogResponse == JOptionPane.YES_OPTION) {
					ReceiptsTableModel receiptsTableModel = (ReceiptsTableModel) tableModel;
					receiptsTableModel.removeAt(currentRow);
					view.getReceiptItemsTableModel().clean();
					view.getSoldsTableModel().refresh();
				}
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
		tableModel = table.getModel();
		currentRow = row;
		return button;
	}
}
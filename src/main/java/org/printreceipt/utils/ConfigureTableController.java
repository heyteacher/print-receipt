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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class ConfigureTableController implements ActionListener {

	public static final String ADD_ACTION_COMMAND = "add";
	// public static final String DELETE_ACTION_COMMAND = "delete";
	// public static final String MOVE_UP_ACTION_COMMAND = "moveup";
	// public static final String MOVE_DOWN_ACTION_COMMAND = "movedown";

	// public static final int DELETE_ROW = 1;
	// public static final int NEW_ROW = 2;
	// public static final int MODIFY_ROW = 3;
	// public static final int MOVE_ROW = 4;

	private JPanel view;

	private ArrayList<Integer> keys = new ArrayList<Integer>();

	private Map<Integer, ConfigureTableRow> tableRows = new HashMap<Integer, ConfigureTableRow>();

	public ConfigureTableController(JPanel view) {
		this.view = view;
	}

	public ArrayList<Integer> getKeys() {
		return keys;
	}

	public Integer addRow(ConfigureTableRow tableRow) {
		return this.addRow(null, tableRow);
	}

	public Integer addRow(Integer index, ConfigureTableRow tableRow) {
		int key = tableRow.hashCode();
		tableRows.put(key, tableRow);
		if (index != null) {
			keys.add(index, key);
		} else {
			keys.add(key);
		}
		return key;
	}

	public int indexOf(Integer key) {
		return keys.indexOf(key);
	}

	public void deleteRow(Integer key) {
		int pos = indexOf(key);
		keys.remove(pos);
		tableRows.remove(key);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ConfigureTableView configureTableView = (ConfigureTableView) view;
		String actionCommand = e.getActionCommand();
		try {
			// if (actionCommand.startsWith(DELETE_ACTION_COMMAND)) {
			// eventDayPanel.deleteRow(Integer.valueOf(actionCommand
			// .substring(DELETE_ACTION_COMMAND.length() + 1)));
			// eventDayPanel.firePropertyChange("updateTable", 0, DELETE_ROW);
			// } else if (actionCommand.startsWith(MOVE_DOWN_ACTION_COMMAND)) {
			// eventDayPanel.moveDownRow(Integer.valueOf(actionCommand
			// .substring(MOVE_DOWN_ACTION_COMMAND.length() + 1)));
			// eventDayPanel.firePropertyChange("updateTable", 0, MOVE_ROW);
			// } else if (actionCommand.startsWith(MOVE_UP_ACTION_COMMAND)) {
			// eventDayPanel.moveUpRow(Integer.valueOf(actionCommand
			// .substring(MOVE_UP_ACTION_COMMAND.length() + 1)));
			// eventDayPanel.firePropertyChange("updateTable", 0, MOVE_ROW);
			if (actionCommand.startsWith(ADD_ACTION_COMMAND)) {
				configureTableView.addRow();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// eventDayPanel.debug();
		view.validate();

	}

	// @Override
	// public void caretUpdate(CaretEvent e) {
	// view.firePropertyChange("updateTable", 0, MODIFY_ROW);
	// }

	public Integer getRowCount() {
		return tableRows.size();
	}

	public ConfigureTableRow getTableRow(Integer key) {
		return tableRows.get(key);
	}
}

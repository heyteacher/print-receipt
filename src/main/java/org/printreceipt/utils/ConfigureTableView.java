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

import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import org.printreceipt.AppException;
import org.printreceipt.Application;
import org.printreceipt.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigureTableView extends RowsPanel {

	private static final long serialVersionUID = 1876012733154401936L;

	private static Logger log = LoggerFactory
			.getLogger(ConfigureTableView.class);

	private ConfigureTableController tableController;
	private List<Object> types;
	private List<String> names;
	JTable configureTable = null;
	ConfigureTableModel configureTableModel = null;

	public ConfigureTableView(String title, List<String> names,
			List<Object> types, Application application) throws AppException {
		this.types = types;
		this.types.add(ConfigureTableRow.class);
		this.types.add(ConfigureTableRow.class);
		this.types.add(ConfigureTableRow.class);
		this.names = names;
		this.names.add(" ");
		this.names.add(" ");
		this.names.add(" ");
		this.tableController = new ConfigureTableController(this);
		JButton addButton = new JButton(Messages.getString("Add"));
		addButton.setActionCommand(ConfigureTableController.ADD_ACTION_COMMAND);
		addButton.addActionListener(tableController);

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
		titlePanel.add(Box.createHorizontalGlue());
		titlePanel.add(Config.createLabelHeader(title, false));
		titlePanel.add(Box.createHorizontalGlue());
		titlePanel.add(addButton);
		titlePanel.setBackground(Config.HEADER2_COLOR);

		super.initialize();

		configureTableModel = new ConfigureTableModel(names, types);
		configureTable = new JTable(configureTableModel);
		configureTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		configureTable.setRowHeight(20);

		for (int i = 0; i < types.size(); i++) {
			TableColumn comboColumn = configureTable.getColumnModel()
					.getColumn(i);
			if (types.get(i) instanceof ConfigureTableView) {
				comboColumn.setCellEditor(new ConfigureTableCellTableEditor(
						(ConfigureTableView) types.get(i)));
			}
			else if (Date.class.equals(types.get(i)) ) {
				comboColumn.setCellEditor(new DateCellTableEditor());				
			}
		}
		TableColumn actColumnumn;

		actColumnumn = configureTable.getColumnModel().getColumn(
				types.size() - 3);
		actColumnumn.setCellEditor(new MoveUpDownCellTableEditor(
				ConfigureTableModel.MOVE_UP));
		actColumnumn.setCellRenderer(new ButtonTableRenderer("↑"));
		actColumnumn.setMaxWidth(150);

		actColumnumn = configureTable.getColumnModel().getColumn(
				types.size() - 2);
		actColumnumn.setCellEditor(new MoveUpDownCellTableEditor(
				ConfigureTableModel.MOVE_DOWN));
		actColumnumn.setCellRenderer(new ButtonTableRenderer("↓"));
		actColumnumn.setMaxWidth(150);

		actColumnumn = configureTable.getColumnModel().getColumn(
				types.size() - 1);
		actColumnumn.setCellEditor(new DeleteCellTableEditor(application));
		actColumnumn.setCellRenderer(new ButtonTableRenderer(Messages.getString("Delete")));
		actColumnumn.setMaxWidth(120);

		this.getContentPane().add(titlePanel);
		this.getContentPane().add(new JScrollPane(configureTable));
	}

	private int getColumsCount() {
		return this.names.size();
	}

	public void addRow(ConfigureTableRow tableRow) throws AppException {
		configureTableModel.addTableRow(tableRow);
	}

	public void addRow() throws AppException {
		Object[] values = new Object[getColumsCount()];
		for (int columnIndex = 0; columnIndex < getColumsCount(); columnIndex++) {
			if (this.types.get(columnIndex).equals(Date.class)) {
				values[columnIndex] = new Date();
			} else if (this.types.get(columnIndex).equals(Float.class)) {
				values[columnIndex] = new Float(0);
			} else if (this.types.get(columnIndex).equals(Double.class)) {
				values[columnIndex] = new Double(0);
			} else if (this.types.get(columnIndex).equals(Long.class)) {
				values[columnIndex] = new Long(0);
			} else if (this.types.get(columnIndex).equals(String.class)) {
				values[columnIndex] = "";
			} else if (this.types.get(columnIndex).equals(Boolean.class)) {
				values[columnIndex] = new Boolean(false);
			} else if (this.types.get(columnIndex) instanceof List<?>) {
				values[columnIndex] = null;
			}
		}
		ConfigureTableRow tableRow = new ConfigureTableRow(getColumsCount());
		tableRow.setValues(values);

		addRow(tableRow);
	}

	public void refresh() {
		configureTableModel.fireTableDataChanged();

	}

	public void stopEditing() {
		if (configureTable.getCellEditor() != null) {
			configureTable.getCellEditor().stopCellEditing();
		}
	}

	public List<ConfigureTableRow> getTableData() {
		return configureTableModel.getData();
	}

	public void updateCombo(List<ValueBean> values) {
//		log.debug("update combo {}", values);
	}

	public ConfigureTableModel getConfigureTableModel() {
		return configureTableModel;
	}
}
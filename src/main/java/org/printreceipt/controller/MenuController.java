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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.printreceipt.AppException;
import org.printreceipt.Application;
import org.printreceipt.Messages;
import org.printreceipt.datamodel.EventDay;
import org.printreceipt.datamodel.Item;
import org.printreceipt.datamodel.ItemGroup;
import org.printreceipt.model.ModelManager;
import org.printreceipt.utils.ConfigureTableRow;
import org.printreceipt.utils.ConfigureTableView;
import org.printreceipt.utils.FileUtils;
import org.printreceipt.utils.ValueBean;
import org.printreceipt.view.MenuView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuController implements ActionListener, PropertyChangeListener {

	private MenuView itemView;
	private Application application;
	public static final String ACT_SAVE = "save";
	public static final String ACT_IMPORT = "import";
	public static final String ACT_EXPORT = "export";
	public static final String ACT_NEW = "new";
	public static final String ACT_CLEAN = "clean";
	public static final String IS_EVENT = "is_event";

	private static Logger log = LoggerFactory.getLogger(MenuController.class);

	public MenuController(Application application, MenuView itemView) {
		this.itemView = itemView;
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		try {
			this.itemView.getItemGroupPanel().stopEditing();
			this.itemView.getItemPanel().stopEditing();
			this.itemView.getEventDayPanel().stopEditing();

			String act = ae.getActionCommand();
			if (ACT_SAVE.equals(act)) {
				save();
				application.showInfo(Messages.getString("Items Saved"));
				application.gotoReceit();
			} else if (ACT_IMPORT.equals(act)) {
				importEvent();
			} else if (ACT_EXPORT.equals(act)) {
				exportEvent();
			} else if (ACT_NEW.equals(act)) {
				newEvent();
			} else if (ACT_CLEAN.equals(act)) {
				cleanEvent();
//			} else if (IS_EVENT.equals(act)) {
//				itemView.getEventDayPanel().setVisible(
//						itemView.getIsEventCheckbox().isSelected());
			}
		} catch (AppException e) {
			log.error("Error on actionPerformed", e);
			application.showError(e.getMessageKey());
		}
	}

	private void newEvent() throws AppException {

		int dialogResponse = JOptionPane
				.showConfirmDialog(
						this.application,
						Messages.getString("Do you want export current data? If no, current data will be lost."),
						Messages.getString("Cancel"),
						JOptionPane.YES_NO_CANCEL_OPTION);

		if (dialogResponse == JOptionPane.YES_OPTION) {
			this.exportEvent();
		}

		if (dialogResponse != JOptionPane.CANCEL_OPTION) {
			ModelManager.getECBManager().deleteEvent();
			application.showWarning("Application will be relaunched");
			Application.reboot();
		}
	}

	private void cleanEvent() throws AppException {

		int dialogResponse = JOptionPane
				.showConfirmDialog(
						this.application,
						Messages.getString("Do You want export current event?. If no, current data will be lost"),
						Messages.getString("Cancel"),
						JOptionPane.YES_NO_CANCEL_OPTION);

		if (dialogResponse == JOptionPane.YES_OPTION) {
			this.exportEvent();
		}

		if (dialogResponse != JOptionPane.CANCEL_OPTION) {
			ModelManager.getECBManager().cleanEvent();
		}
	}

	private void exportEvent() throws AppException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int ret = fileChooser.showSaveDialog(itemView);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File dialogDirectory = fileChooser.getSelectedFile();
			DateFormat sdfDayMR = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

			String eventName = application.getEvent().getName();
			eventName = eventName.replace(' ', '_');

			File dialogFile = new File(dialogDirectory, eventName + "."
					+ sdfDayMR.format(new Date()) + ".ecb");
			File backupFile = ModelManager.getECBManager().exportEvent();
			FileUtils.copyfile(backupFile, dialogFile);
		}
	}

	private void importEvent() throws AppException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "*.ecb";
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".ecb");
			}
		});
		int ret = fileChooser.showOpenDialog(itemView);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File dialogFile = fileChooser.getSelectedFile();
			ModelManager.getECBManager().importEvent(dialogFile);
			application.showWarning(Messages
					.getString("Application will be relaunched"));
			Application.reboot();
		}

	}

	private void save() throws AppException {
		String eventName = itemView.getReceiptHeaderComponent().getText();
		application.getEvent().setName(eventName);
		
		EventDay[] eventDays = getEventDays(itemView.getEventDayPanel().getTableData());
		ItemGroup[] itemGroups = getItemGroups(itemView.getItemGroupPanel().getTableData());
		Item[] items = getItems(itemView.getItemPanel().getTableData());

		ModelManager.getECBManager().save(
				application.getEvent(), 
				eventDays,
				itemGroups, 
				items, 
				application.getUser());
	}

	private EventDay[] getEventDays(List<ConfigureTableRow> tableRows) {
		EventDay[] ret = new EventDay[tableRows.size()];
		for (int i = 0; i < tableRows.size(); i++) {
			EventDay object = new EventDay();
			object.setId(tableRows.get(i).getKey());
			
			Date startDate = (Date) tableRows.get(i).getValue(0);
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startDate);
			object.setStart_date(startCalendar);
			
			Date endDate = (Date) tableRows.get(i).getValue(1);
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(endDate);			
			object.setEnd_date(endCalendar);
			
			ret[i] = object;
		}
		return ret;
	}

	private ItemGroup getItemGroup(ConfigureTableRow tableRow, int position) {
		ItemGroup object = new ItemGroup();
		object.setId(tableRow.getKey());
		object.setName((String) tableRow.getValue(0));
		object.setOrd(new BigInteger(position + ""));
		object.setEvent(application.getEvent());
		return object;
	}

	private ItemGroup[] getItemGroups(List<ConfigureTableRow> tableRows) {
		ItemGroup[] ret = new ItemGroup[tableRows.size()];
		for (int i = 0; i < tableRows.size(); i++) {
			ret[i] = getItemGroup(tableRows.get(i), i);
		}
		return ret;
	}

	private Item[] getItems(List<ConfigureTableRow> tableRows) {
		Item[] ret = new Item[tableRows.size()];
		for (int i = 0; i < tableRows.size(); i++) {
			Item object = new Item();
			object.setId(tableRows.get(i).getKey());
			object.setName((String) tableRows.get(i).getValue(0));
			try {
				object.setPrice((Double) tableRows.get(i).getValue(1));
			} catch (Exception e) {
				object.setPrice(((Long) tableRows.get(i).getValue(1))
						.doubleValue());
			}
			ValueBean valueBean = (ValueBean) tableRows.get(i).getValue(2);
			if (valueBean != null && valueBean.getId() != null) {
				ItemGroup itemGroup = new ItemGroup();
				itemGroup.setId(new BigInteger("" + valueBean.getId()));
				object.setItem_group(itemGroup);
			}
			object.setHide((Boolean) tableRows.get(i).getValue(3) == true);
			object.setOrd(new BigInteger("" + i));
			ret[i] = object;
		}
		return ret;
	}

	public void loadEventDays(ConfigureTableView tablePanel)
			throws AppException {
		EventDay[] eventDays = ModelManager.getECBManager()
				.getEvent(application.getUser()).getDays();

//		boolean isEvent = eventDays != null && eventDays.length > 0;
//		tablePanel.setVisible(isEvent);
//		itemView.getIsEventCheckbox().setSelected(isEvent);

		for (int i = 0; i < eventDays.length; i++) {
			Object[] values = new Object[2];
			values[0] = eventDays[i].getStart_date().getTime();
			values[1] = eventDays[i].getEnd_date().getTime();
//			values[0] = Config.formatDay(eventDays[i].getStart_date());
//			values[1] = Config.formatDay(eventDays[i].getEnd_date());
			tablePanel.addRow(new ConfigureTableRow(eventDays[i].getId(),
					values));
		}
	}

	public List<ValueBean> loadItemGroups(ConfigureTableView tablePanel)
			throws AppException {
		ItemGroup[] itemGroups = ModelManager.getECBManager().getItemGroups(
				application.getEvent(), application.getUser());
		for (int i = 0; i < itemGroups.length; i++) {
			Object[] values = new Object[2];
			values[0] = itemGroups[i].getName();
			values[1] = false;
			tablePanel.addRow(new ConfigureTableRow(itemGroups[i].getId(),
					values));
		}
		return getItemGroupsValueBeans(itemGroups);
	}

	private List<ValueBean> getItemGroupsValueBeans(ItemGroup[] itemGroups) {
		List<ValueBean> valueBeans = new ArrayList<ValueBean>();
		for (int i = 0; i < itemGroups.length; i++) {
			valueBeans.add(new ValueBean(itemGroups[i].getId(), itemGroups[i]
					.getName()));
		}
		return valueBeans;
	}

	public void loadItems(ConfigureTableView tablePanel) throws AppException {
		Item[] items = ModelManager.getECBManager().getItems(
				application.getEvent(), application.getUser(), true);
		for (int i = 0; i < items.length; i++) {
			Object[] values = new Object[4];
			values[0] = items[i].getName();
			values[1] = items[i].getPrice();
			values[2] = new ValueBean(items[i].getItem_group().getId(),
					items[i].getItem_group().getName());
			values[3] = items[i].getHide();
			tablePanel.addRow(new ConfigureTableRow(items[i].getId(), values));
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		try {
			ConfigureTableView source = (ConfigureTableView) evt.getSource();
			if (itemView.getItemGroupPanel() == source) {
				List<ValueBean> values = new ArrayList<ValueBean>();
				for (ConfigureTableRow tableRow : source.getTableData()) {
					if (tableRow.getKey() == null) {
						BigInteger itemGroupId = ModelManager.getECBManager()
								.createItemGroup(
										getItemGroup(tableRow,
												tableRow.getValues().length));
						tableRow.setKey(itemGroupId);
					}
					values.add(new ValueBean(tableRow.getKey(),
							(String) tableRow.getValue(0)));
				}
				itemView.getItemPanel().updateCombo(values);
			}
		} catch (AppException e) {
			application.showError(e);
		}
	}
}
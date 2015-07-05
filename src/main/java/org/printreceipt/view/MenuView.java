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


package org.printreceipt.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import org.printreceipt.AppException;
import org.printreceipt.Application;
import org.printreceipt.Messages;
import org.printreceipt.controller.MenuController;
import org.printreceipt.utils.ComponentFactory;
import org.printreceipt.utils.Config;
import org.printreceipt.utils.ConfigureTableView;
import org.printreceipt.utils.LocalStorage;
import org.printreceipt.utils.RowsPanel;
import org.printreceipt.utils.ValueBean;
import org.printreceipt.utils.WrapPrintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuView extends RowsPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7022266197692125774L;

	private static Logger log = LoggerFactory.getLogger(MenuView.class);

	private Application application;
	private MenuController controller;

	private ConfigureTableView eventDayConfigureTableView = null;
	private ConfigureTableView itemGroupConfigureTableView = null;
	private ConfigureTableView itemConfigureTableView = null;

	public ConfigureTableView getEventDayPanel() {

		return eventDayConfigureTableView;
	}

	public ConfigureTableView getItemGroupPanel() {
		return itemGroupConfigureTableView;
	}

	public ConfigureTableView getItemPanel() {
		return itemConfigureTableView;
	}

	private JButton newButton = new JButton(Messages.getString("New Event"));
	private JButton exportButton = new JButton(Messages.getString("Export Event"));
	private JButton importButton = new JButton(Messages.getString("Import Event"));
	private JButton saveButton = new JButton(Messages.getString("Save"));

	private JComboBox<WrapPrintService> printersCombobox;
	private JComboBox<ValueBean> paperSizeCombobox;

	// private JScrollPane scrollPane;

	private JTextComponent receiptHeaderComponent;

	//private JCheckBox isEventCheckbox;

	public JTextComponent getReceiptHeaderComponent() {
		return receiptHeaderComponent;
	}

//	public JCheckBox getIsEventCheckbox() {
//		return isEventCheckbox;
//	}

	public void refresh() {
		// scrollPane.validate();
	}

	public MenuView(Application application) throws AppException {
		super();
		this.application = application;
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(
				null, null);
		WrapPrintService[] wrapPrintServices = WrapPrintService
				.getWrapPrintServices(printServices);

		printersCombobox = new JComboBox(wrapPrintServices);
		String printServiceName = LocalStorage.loadPrintServiceName();

		if (printServiceName != null) {
			int i = 0;
			for (; i < wrapPrintServices.length; i++) {
				WrapPrintService wrapPrintService = wrapPrintServices[i];
				if (printServiceName.equals(wrapPrintService.getName())) {
					printersCombobox.setSelectedIndex(i);
					break;
				}
			}
		} else {
			PrintService defaultPrintService = PrintServiceLookup
					.lookupDefaultPrintService();
			if (defaultPrintService != null) {
				printersCombobox.setSelectedItem(new WrapPrintService(
						defaultPrintService));
			} else {
				printersCombobox.setSelectedIndex(printersCombobox
						.getItemCount() - 1);
			}
		}
		
		printersCombobox.setMaximumSize(new Dimension(150, 20));
		printersCombobox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					WrapPrintService wrapPrintService = (WrapPrintService) printersCombobox
							.getSelectedItem();
//					log.debug("printersCombobox change: {}", wrapPrintService
//							.getPrintService().getName());
					LocalStorage.storePrintServiceName(wrapPrintService
							.getPrintService().getName());
				} catch (AppException e1) {
					log.error("printersCombobox.actionPerformed", e1);
				}
			}
		});
		
		ValueBean[] paperSizes = new ValueBean[2];
		paperSizes[0] = new ValueBean(new Integer(80), "80 mm.");
		paperSizes[1] = new ValueBean(new Integer(57), "57 mm.");
		paperSizeCombobox = new JComboBox<ValueBean>(paperSizes);
		Integer paperSize = (Integer) LocalStorage.loadPaperSize();
		for (int i = 0; i < paperSizes.length; i++) {
			if (paperSizes[i].getId().equals(paperSize)) {
				paperSizeCombobox.setSelectedIndex(i);
				break;
			}
		}
		
		paperSizeCombobox.setMaximumSize(new Dimension(50, 20));
		paperSizeCombobox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ValueBean paperSize = (ValueBean) paperSizeCombobox.getSelectedItem();
					LocalStorage.storePaperSize((Integer) paperSize.getId());
				} catch (AppException e1) {
					log.error("paperSizeCombobox.actionPerformed", e1);
				}
			}
		});
		
	}

	public void initialize() throws AppException {

		if (controller != null) {
			newButton.removeActionListener(controller);
			saveButton.removeActionListener(controller);
			exportButton.removeActionListener(controller);
			importButton.removeActionListener(controller);
			this.removePropertyChangeListener(controller);
		}
		controller = new MenuController(this.application, this);

		newButton.addActionListener(controller);
		newButton.setActionCommand(MenuController.ACT_NEW);

		saveButton.addActionListener(controller);
		saveButton.setActionCommand(MenuController.ACT_SAVE);

		exportButton.addActionListener(controller);
		exportButton.setActionCommand(MenuController.ACT_EXPORT);

		importButton.addActionListener(controller);
		importButton.setActionCommand(MenuController.ACT_IMPORT);

		super.initialize();
		this.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		// scrollPane = new JScrollPane(mainPanel);
		// this.add(scrollPane, BorderLayout.CENTER);
		this.add(mainPanel, BorderLayout.CENTER);

		// JPanel mainHeaderTitlePanel = new JPanel();
		// mainHeaderTitlePanel.setLayout(new BoxLayout(mainHeaderTitlePanel,
		// BoxLayout.X_AXIS));
		// mainHeaderTitlePanel.add(ComponentFactory.createSpace(40));
		// mainHeaderTitlePanel.add(Box.createHorizontalGlue());
		// mainHeaderTitlePanel.add(Config.createLabelHeader("_main", false));
		// mainHeaderTitlePanel.add(Box.createHorizontalGlue());
		// mainHeaderTitlePanel.setBackground(Config.HEADER2_COLOR);
		// //Config.setBorder(mainHeaderTitlePanel);
		// mainPanel.add(mainHeaderTitlePanel);

		JPanel mainHeaderPanel = new JPanel();
		mainHeaderPanel.setLayout(new BoxLayout(mainHeaderPanel,
				BoxLayout.X_AXIS));
		//mainHeaderPanel.add(ComponentFactory.createSpace(40));
		// Config.setBorder(mainHeaderPanel);
		mainHeaderPanel.add(Box.createHorizontalGlue());
		receiptHeaderComponent = ComponentFactory.createTextField("event", 20);
		receiptHeaderComponent.setText(application.getEvent().getName());
		//receiptHeaderComponent.addCaretListener(controller);
		JLabel receiptHeaderLabel = new JLabel(
				Messages.getString("Event Name"));
		receiptHeaderLabel.setLabelFor(receiptHeaderComponent);

		// isEventCheckbox = ComponentFactory
		// .createCheckBox(MenuController.IS_EVENT);
		// isEventCheckbox.addActionListener(controller);
		// JLabel isEventCheckboxLabel = new JLabel(
		// Messages.getString("_is_event"));
		// isEventCheckboxLabel.setLabelFor(isEventCheckbox);
		//
		// isEventCheckbox.setVisible(false);
		// isEventCheckboxLabel.setVisible(false);

		mainHeaderPanel.add(receiptHeaderLabel);
		receiptHeaderLabel.setBorder(Config.createEmptyBorder());
		mainHeaderPanel.add(receiptHeaderComponent);
		receiptHeaderComponent.setBorder(Config.createEmptyBorder());
		// mainHeaderPanel.add(isEventCheckboxLabel);
		// isEventCheckboxLabel.setBorder(Config.createEmptyBorder());
//		mainHeaderPanel.add(isEventCheckbox);
//		isEventCheckbox.setBorder(Config.createEmptyBorder());

		mainHeaderPanel.add(Box.createHorizontalGlue());
		mainPanel.add(mainHeaderPanel);

		List<String> names = new ArrayList<String>();
		List<Object> types = new ArrayList<Object>();
		names.add("Start Date");
		names.add("End Date");
		types.add(Date.class);
		types.add(Date.class);

		this.eventDayConfigureTableView = new ConfigureTableView("Event Days",
				names, types, this.getApplication());
		mainPanel.add(eventDayConfigureTableView);
		controller.loadEventDays(eventDayConfigureTableView);

		names = new ArrayList<String>();
		types = new ArrayList<Object>();
		names.add("Group");
		types.add(String.class);

		this.itemGroupConfigureTableView = new ConfigureTableView(
				"Item Groups", names, types, this.getApplication());
		List<ValueBean> itemGroups = controller
				.loadItemGroups(itemGroupConfigureTableView);
		mainPanel.add(itemGroupConfigureTableView);

		names = new ArrayList<String>();
		types = new ArrayList<Object>();
		names.add("Item");
		names.add("Price");
		names.add("Group");
		names.add("Hide");
		types.add(String.class);
		types.add(Double.class);
		types.add(this.itemGroupConfigureTableView);
		types.add(Boolean.class);

		this.itemConfigureTableView = new ConfigureTableView("Items", names,
				types, this.getApplication());
		mainPanel.add(itemConfigureTableView);
		controller.loadItems(itemConfigureTableView);

		this.itemGroupConfigureTableView.addPropertyChangeListener(
				"updateTable", controller);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setBorder(Config.createEmptyBorder());
		buttonPanel.add(newButton);
		buttonPanel.add(Config.createRigidArea());
		buttonPanel.add(importButton);
		buttonPanel.add(Config.createRigidArea());
		buttonPanel.add(exportButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(getPrintersCombobox());
		//buttonPanel.add(Config.createRigidArea());
		buttonPanel.add(paperSizeCombobox);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(saveButton);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	public JComboBox<WrapPrintService> getPrintersCombobox() {
		return printersCombobox;
	}

	public Application getApplication() {
		return this.application;
	}

	public Integer getPaperSize() {
		return (Integer)((ValueBean) paperSizeCombobox.getSelectedItem()).getId();
	}
}
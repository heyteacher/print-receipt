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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import org.printreceipt.AppException;
import org.printreceipt.Application;
import org.printreceipt.Messages;
import org.printreceipt.controller.CashOpTableModel;
import org.printreceipt.controller.ReceiptItemsTableModel;
import org.printreceipt.controller.ReceiptsTableModel;
import org.printreceipt.controller.SoldsTableModel;
import org.printreceipt.controller.SummaryController;
import org.printreceipt.datamodel.CashOperation;
import org.printreceipt.datamodel.Receipt;
import org.printreceipt.utils.ButtonTableRenderer;
import org.printreceipt.utils.Config;
import org.printreceipt.utils.RowsPanel;
import org.printreceipt.utils.SummaryViewDeleteCellTableEditor;
import org.printreceipt.utils.WrapEventDay;

public class SummaryView extends RowsPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7022266197692125774L;

	private Application application;
	private SummaryController controller;
	private JTable receiptTable, receiptItemsTable, cashOpTable, soldsTable;
	private JScrollPane scrollPane;

	private JButton loadButton = new JButton(Messages.getString("Cash Load"));
	private JButton downloadButton = new JButton(
			Messages.getString("Cash Download"));

	private JFormattedTextField cashOperationFormattedTextField = new JFormattedTextField(
			Config.getFloatFormat());

	private ReceiptsTableModel receiptTableModel;
	private ReceiptItemsTableModel receiptItemsTableModel;
	private CashOpTableModel cashOpTableModel;
	private SoldsTableModel soldsTableModel;

	private JComboBox eventStatistictsDaysComboBox;

	// private JButton cleanButton = new
	// JButton(ECBMessages.getString("_empty_receipts"));

	public ReceiptsTableModel getReceiptTableModel() {
		return receiptTableModel;
	}

	public ReceiptItemsTableModel getReceiptItemsTableModel() {
		return receiptItemsTableModel;
	}

	public JTable getReceiptTable() {
		return receiptTable;
	}

	public void refresh() {
		scrollPane.validate();
	}

	public SummaryView(Application application) throws AppException {
		super();
		this.application = application;
	}

	public void initialize() throws AppException {

		if (controller != null) {
			// cleanButton.removeActionListener(controller);
			if (receiptTable != null) {
				receiptTable.getSelectionModel().removeListSelectionListener(
						controller);
			}
			loadButton.removeActionListener(controller);
			downloadButton.removeActionListener(controller);
		}

		controller = new SummaryController(this.application, this);

		super.initialize();
		JPanel firstRowPanelHeader = new JPanel(new GridLayout(1, 2,
				Config.SPACE, Config.SPACE));
		JPanel firstRowPanel = new JPanel(new GridLayout(1, 2, Config.SPACE,
				Config.SPACE));
		JPanel secondRowPanelHeader = new JPanel(new GridLayout(1, 2,
				Config.SPACE, Config.SPACE));
		JPanel secondRowPanel = new JPanel(new GridLayout(1, 2, Config.SPACE,
				Config.SPACE));

		JPanel firstSecondHeader = Config.createLabelHeader("Receipts", false);
		JPanel secondSecondHeader = Config.createLabelHeader("Receipt details",
				false);
		JPanel firstFirstCellHeader = Config.createLabelHeader("Solds", true);
		JPanel secondFirstHeader = Config.createLabelHeader("Cash Operation", true);

		getContentPane().add(firstRowPanelHeader);
		getContentPane().add(firstRowPanel);
		getContentPane().add(secondRowPanelHeader);
		getContentPane().add(secondRowPanel);

		firstRowPanelHeader.add(firstFirstCellHeader);
		firstRowPanelHeader.add(firstSecondHeader);
		secondRowPanelHeader.add(secondFirstHeader);
		secondRowPanelHeader.add(secondSecondHeader);

		soldsTableModel = new SoldsTableModel(application);
		soldsTable = new JTable(soldsTableModel);
		soldsTable.setRowHeight(20);
		soldsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		soldsTable.getSelectionModel().addListSelectionListener(controller);
		firstRowPanel.add(new JScrollPane(soldsTable));

		cashOpTableModel = new CashOpTableModel(application);
		cashOpTable = new JTable(cashOpTableModel);
		cashOpTable.setRowHeight(20);

		cashOpTable.setDefaultEditor(CashOperation.class,
				new SummaryViewDeleteCellTableEditor(this, cashOpTable));
		cashOpTable.setDefaultRenderer(CashOperation.class,
				new ButtonTableRenderer(Messages.getString("Delete")));

		TableColumn actColumnumn = cashOpTable.getColumnModel().getColumn(
				cashOpTable.getColumnCount() - 1);
		actColumnumn.setMaxWidth(150);

		actColumnumn = cashOpTable.getColumnModel().getColumn(0);
		actColumnumn.setMinWidth(150);

		cashOpTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cashOpTable.getSelectionModel().addListSelectionListener(controller);
		secondRowPanel.add(new JScrollPane(cashOpTable));

		receiptTableModel = new ReceiptsTableModel(application);
		receiptTable = new JTable(receiptTableModel);
		receiptTable.setRowHeight(20);

		actColumnumn = receiptTable.getColumnModel().getColumn(
				receiptTable.getColumnCount() - 1);
		actColumnumn.setMaxWidth(150);
		
		actColumnumn = receiptTable.getColumnModel().getColumn(0);
		actColumnumn.setMinWidth(150);

		receiptTable.setDefaultEditor(Receipt.class,
				new SummaryViewDeleteCellTableEditor(this, receiptTable));
		receiptTable.setDefaultRenderer(Receipt.class, 
				new ButtonTableRenderer(Messages.getString("Delete")));

		receiptTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		receiptTable.getSelectionModel().addListSelectionListener(controller);
		

		firstRowPanel.add(new JScrollPane(receiptTable));

		receiptItemsTableModel = new ReceiptItemsTableModel(application);
		receiptItemsTable = new JTable(receiptItemsTableModel);
		receiptItemsTable.setRowHeight(20);
		receiptItemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		secondRowPanel.add(new JScrollPane(receiptItemsTable));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setBorder(Config.createEmptyBorder());

		eventStatistictsDaysComboBox = new JComboBox();
		eventStatistictsDaysComboBox
				.setActionCommand(SummaryController.EVENT_DAY_STATISTICS);
		eventStatistictsDaysComboBox.addActionListener(controller);
		eventStatistictsDaysComboBox.setMaximumSize(new Dimension(150, 20));
		initializeEventStatisticsDays();

		resetCashOperatorComponents();

		buttonPanel.add(eventStatistictsDaysComboBox);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(createCachOperationPanel());
		this.add(buttonPanel, BorderLayout.SOUTH);

	}

	public void resetCashOperatorComponents() {
		cashOperationFormattedTextField.setText("");
		getLoadButton().setEnabled(false);
		getDownloadButton().setEnabled(false);
	}

	/**
	 * @param application
	 * @throws AppException
	 */
	public void initializeEventStatisticsDays() throws AppException {

		Vector wrapEventDays = WrapEventDay.getWrapEventDays(application
				.getEvent().getDays());
		wrapEventDays.add(SummaryController.PRINT_REPORT_NONE,
				Messages.getString("Print Totals"));
		wrapEventDays.add(SummaryController.PRINT_REPORT_COMPLETE,
				Messages.getString("Complete"));
		wrapEventDays.add(SummaryController.PRINT_REPORT_COMPLETE_XLS,
				Messages.getString("Complete XLS"));
		wrapEventDays.add(SummaryController.PRINT_REPORT_PRICE_LIST,
				Messages.getString("Price List"));

		eventStatistictsDaysComboBox.removeAllItems();
		for (Iterator iterator = wrapEventDays.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			eventStatistictsDaysComboBox.addItem(object);
		}
	}

	public JTable getCashOperationTable() {
		return cashOpTable;
	}

	public CashOpTableModel getCashOperationTableModel() {
		return cashOpTableModel;
	}

	public Application getApplication() {
		return application;
	}

	public SoldsTableModel getSoldsTableModel() {
		// TODO Auto-generated method stub
		return soldsTableModel;
	}

	public JButton getLoadButton() {
		return loadButton;
	}

	public JButton getDownloadButton() {
		return downloadButton;
	}

	public JFormattedTextField getCashOperationFormattedTextField() {
		return cashOperationFormattedTextField;
	}

	/**
	 * @return
	 */
	private JPanel createCachOperationPanel() {
		JPanel cashOperationPanel = new JPanel();
		cashOperationPanel.setLayout(new FlowLayout());
		cashOperationPanel.setBorder(Config.createEmptyBorder());

		cashOperationPanel.setMinimumSize(new Dimension(200, 50));
		cashOperationPanel.setPreferredSize(new Dimension(1000, 50));
		cashOperationPanel.setMaximumSize(new Dimension(1000, 50));
		cashOperationPanel.add(new JLabel(Messages.getString("Cash Operation")));
		cashOperationFormattedTextField.setMinimumSize(new Dimension(50, 25));
		cashOperationFormattedTextField.setPreferredSize(new Dimension(50, 25));
		cashOperationFormattedTextField.setMaximumSize(new Dimension(50, 25));
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(cashOperationFormattedTextField);
		cashOperationPanel.add(panel);

		cashOperationFormattedTextField.addKeyListener(controller);

		downloadButton.setActionCommand("DOWNLOAD");
		downloadButton.addActionListener(controller);
		downloadButton.setMinimumSize(new Dimension(80, 25));
		downloadButton.setPreferredSize(new Dimension(90, 25));
		downloadButton.setMaximumSize(new Dimension(200, 25));
		cashOperationPanel.add(downloadButton);

		loadButton.setActionCommand("LOAD");
		loadButton.addActionListener(controller);
		loadButton.setMinimumSize(new Dimension(80, 25));
		loadButton.setPreferredSize(new Dimension(90, 25));
		loadButton.setMaximumSize(new Dimension(200, 25));
		cashOperationPanel.add(loadButton);

		return cashOperationPanel;
	}

}
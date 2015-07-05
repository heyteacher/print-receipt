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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.printreceipt.AppException;
import org.printreceipt.Application;
import org.printreceipt.Messages;
import org.printreceipt.datamodel.CashOperation;
import org.printreceipt.datamodel.EventDay;
import org.printreceipt.datamodel.EventItemStatistics;
import org.printreceipt.datamodel.EventStatistics;
import org.printreceipt.datamodel.Item;
import org.printreceipt.datamodel.Receipt;
import org.printreceipt.model.ModelManager;
import org.printreceipt.utils.Config;
import org.printreceipt.utils.WrapEventDay;
import org.printreceipt.view.SummaryView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SummaryController implements ActionListener,
		ListSelectionListener, KeyListener {

	private SummaryView summaryView;
	private Application application;

	public static final String ACT_DELETE = "delete";
	public static final String ACT_CLEAN = "clean";
	public static final String EVENT_STATISTICS = "EVENT_STATISTICS";
	public static final String EVENT_DAY_STATISTICS = "EVENT_DAY_STATISTICS";
	public static final int PRINT_REPORT_NONE = 0;
	public static final int PRINT_REPORT_COMPLETE = 1;
	public static final int PRINT_REPORT_COMPLETE_XLS = 2;
	public static final int PRINT_REPORT_PRICE_LIST = 3;

	private static Logger log = LoggerFactory
			.getLogger(SummaryController.class);

	public SummaryController(Application application, SummaryView historyView) {
		this.summaryView = historyView;
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		try {
			String act = ae.getActionCommand();
			if (ACT_DELETE.equals(act)) {
				int dialogResponse = JOptionPane
						.showConfirmDialog(
								this.application,
								Messages.getString("Do you want delete selected receipt?"),
								Messages.getString("Cancel"),
								JOptionPane.YES_NO_OPTION);
				if (dialogResponse == JOptionPane.YES_OPTION) {
					summaryView.getReceiptTableModel().removeAt(
							summaryView.getReceiptTable().getSelectedRow());
//					log.debug("selected row {} removed", summaryView
//							.getReceiptTable().getSelectedRow());
				}
				return;
			}
			if (ACT_CLEAN.equals(act)) {
				int dialogResponse = JOptionPane.showConfirmDialog(
						this.application,
						Messages.getString("Do you want delete allreceipts"),
						Messages.getString("Cancel"),
						JOptionPane.YES_NO_OPTION);
				if (dialogResponse == JOptionPane.YES_OPTION) {
					summaryView.getReceiptTableModel().removeAll();
				}
				return;
			}
			if ("LOAD".equals(ae.getActionCommand())) {
				addCachOperation(1);
				summaryView.resetCashOperatorComponents();
				return;
			}
			if ("DOWNLOAD".equals(ae.getActionCommand())) {
				addCachOperation(-1);
				summaryView.resetCashOperatorComponents();
				return;
			}

			EventDay eventDay = null;
			JComboBox combo = (JComboBox) ae.getSource();
			if (combo.getItemCount() == 0) {
				return;
			}

			EventStatistics eventStatistics = null;
			switch (combo.getSelectedIndex()) {

			case PRINT_REPORT_NONE:
				break;

			case PRINT_REPORT_COMPLETE:
				eventStatistics = ModelManager.getECBManager()
						.getEventStatistics(application.getEvent(),
								application.getUser());
				showEventStatistics(eventDay, eventStatistics);
				combo.setSelectedIndex(0);
				break;

			case PRINT_REPORT_COMPLETE_XLS:
				writeCompleteXLS();
				combo.setSelectedIndex(0);
				break;

			case PRINT_REPORT_PRICE_LIST:
				printPriceList();
				combo.setSelectedIndex(0);
				break;

			default:
				WrapEventDay wrapEventDay = (WrapEventDay) combo
						.getSelectedItem();
				if (wrapEventDay != null) {
					eventDay = wrapEventDay.getEventDay();
					eventStatistics = ModelManager.getECBManager()
							.getEventDayStatistics(eventDay,
									application.getUser());
				}
				showEventStatistics(eventDay, eventStatistics);
				combo.setSelectedIndex(0);
			}
		} catch (Exception e) {
			application.showError(e);
			e.printStackTrace();
		}

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		try {

			boolean summaryRowSelected = summaryView.getReceiptTable()
					.getSelectedRow() >= 0;
			if (summaryRowSelected) {
				Receipt receipt = summaryView.getReceiptTableModel()
						.getReceiptAt(
								summaryView.getReceiptTable().getSelectedRow());
				summaryView.getReceiptItemsTableModel().loadItems(receipt);
			}
		} catch (AppException ecbe) {
			log.error("Error on actionPerformed", ecbe);
			application.showError(ecbe.getMessageKey());
		}
	}

	private void showEventStatistics(EventDay eventDay,
			EventStatistics eventStatistics) throws AppException,
			PrinterException {
		if (eventStatistics == null) {
			return;
		}

		StringBuffer body = new StringBuffer("");

		if (eventDay != null) {
			body.append(Messages.getString("Day"));
			body.append(" ");
			body.append(Config.formatDay(eventDay.getStart_date()));
			body.append("\n\n");
		}
		body.append(Messages.getString("Total Income"));
		body.append(": ");
		body.append(Config.currencyFormat(eventStatistics.getAmount()));
		body.append("\n");

		if (eventStatistics.getItems_statistics().length > 0) {
			body.append("\n");
			body.append(Messages.getString("Items Report"));
			body.append("\n");
		}
		for (EventItemStatistics eventItemStatisctics : eventStatistics
				.getItems_statistics()) {
			body.append(" - ");
			body.append(eventItemStatisctics.getName());
			body.append(": ");
			body.append(Config.formatInt(eventItemStatisctics.getQuantity()));
			body.append("\n");
		}
		if (eventStatistics.getCash_operations().length > 0) {
			body.append("\n");
			body.append(Messages.getString("Cash Operation"));
			body.append("\n");
		}
		for (CashOperation cashOperation : eventStatistics.getCash_operations()) {
			body.append(" - <");
			body.append(cashOperation.getUser().getUsername());
			body.append("> ");
			body.append(cashOperation.getNote());
			body.append(": ");
			body.append(Config.currencyFormat(cashOperation.getAmount()));
			body.append("\n");
		}
		body.append("\n");

		if (JOptionPane.showOptionDialog(
				application,
				body,
				Messages.getString("Totals"),
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,
				new String[] { Messages.getString("Print"),
						Messages.getString("Close") }, null) == JOptionPane.OK_OPTION) {
			printEventStatistics(eventStatistics, eventDay);
		}
	}

	private void printPriceList() throws AppException, PrinterException {
		Item[] items = ModelManager.getECBManager().getItems(
				application.getEvent(), application.getUser(), true);

		StringBuilder sb = new StringBuilder();
		sb.append("<table>");

		// sb.append(" <tr>");
		// sb.append("  <th colspan='3' width='200px'>");
		// sb.append("<img height='50' src=\"file://");
		// sb.append(ConfigController.getLocalIconRepositoryPath());
		// sb.append("\"/>");
		// sb.append("  </th>");
		// sb.append(" </tr>");

		sb.append(" <tr>");
		sb.append("  <th colspan='3' width='");
		sb.append(application.getMenuPanel().getPaperSize());
		sb.append("mm'>");
		
		sb.append(application.getEvent().getName());
		sb.append("  </th>");
		sb.append(" </tr>");

		if (items.length > 0) {

			sb.append(" <tr>");
			sb.append("  <th colspan='3'>");
			sb.append(Messages.getString("Price List"));
			sb.append("  </th>");
			sb.append("  <th>");

			sb.append(" <tr>");
			sb.append("  <th>");
			sb.append(Messages.getString("Item"));
			sb.append("  </th>");
			sb.append("  <th>");
			sb.append(Messages.getString("Price"));
			sb.append("  </th>");
			sb.append("  <th>");
			sb.append(Messages.getString("Quantity"));
			sb.append("  </th>");
			sb.append(" </tr>");
		}

		for (Item item : items) {

			sb.append("<tr>");

			sb.append(" <td>");
			sb.append(item.getName());
			sb.append(" </td>");

			sb.append(" <td>");
			sb.append(item.getPrice());
			sb.append("&nbsp;");
			sb.append(Messages.getString("Currency"));
			sb.append(" </td>");

			sb.append(" <td style='text-align: right;'>");
			sb.append("_____");
			sb.append(" </td>");

			sb.append("</tr>");
		}
		sb.append("</table>");

		Config.print(sb.toString(), Messages.getString("Price List") + "_"
				+ application.getEvent().getName(), application);
		application.showInfo(Messages.getString("Price List") + "\""
				+ application.getEvent().getName() + "\" "
				+ Messages.getString("Printed"));
	}

	private EventStatistics writeCompleteXLS() throws AppException, IOException {
		EventStatistics eventStatistics = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int ret = fileChooser.showSaveDialog(summaryView);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File dialogDirectory = fileChooser.getSelectedFile();
			DateFormat sdfDayMR = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

			String eventName = application.getEvent().getName();
			eventName = eventName.replace(' ', '_');
			eventStatistics = ModelManager.getECBManager().getEventStatistics(
					application.getEvent(), application.getUser());
			StringBuffer body = new StringBuffer("");
			body.append(application.getEvent().getName());
			body.append("\n");
			body.append("\n");

			body.append(Messages.getString("Item"));
			body.append(";");
			body.append(Messages.getString("Quantity"));
			body.append(";");
			body.append(Messages.getString("Price"));
			body.append(";");
			body.append(Messages.getString("Total"));
			body.append("\n");

			Item[] items = ModelManager.getECBManager().getItems(
					application.getEvent(), application.getUser(), true);

			Map<BigInteger, Item> itemMap = new HashMap<BigInteger, Item>();

			for (int i = 0; i < items.length; i++) {
				itemMap.put(items[i].getId(), items[i]);
			}
			for (EventItemStatistics eventItemStatisctics : eventStatistics
					.getItems_statistics()) {
				Item item = itemMap.get(eventItemStatisctics.getId());
				body.append(eventItemStatisctics.getName());
				body.append(";");
				body.append(Config.formatInt(eventItemStatisctics.getQuantity()));
				body.append(";");
				body.append(item.getPrice());
				body.append(";");
				body.append(item.getPrice()
						* eventItemStatisctics.getQuantity());
				body.append("\n");
			}
			body.append(Messages.getString("Total Income"));
			body.append(";");
			body.append(";");
			body.append(";");
			body.append(eventStatistics.getAmount());
			body.append("\n");

			if (eventStatistics.getCash_operations().length > 0) {
				body.append("\n");
				body.append(Messages.getString("Cash Operation"));
				body.append("\n");
			}
			for (CashOperation cashOperation : eventStatistics
					.getCash_operations()) {
				body.append(cashOperation.getNote());
				body.append(";");
				body.append(cashOperation.getAmount());
				body.append("\n");
			}

			//log.debug("body:\n{}", body);

			File dialogFile = new File(dialogDirectory, "report_" + eventName
					+ "." + sdfDayMR.format(new Date()) + ".csv");
			FileWriter fw = new FileWriter(dialogFile);
			fw.write(body.toString());
			fw.close();
		}
		return eventStatistics;
	}

	private void addCachOperation(int sign) {
		try {
			JFormattedTextField textField = summaryView
					.getCashOperationFormattedTextField();
			
			Double amount;
			if (textField.getValue() instanceof Long) {
				amount = ((Long)textField.getValue()).doubleValue();
				amount = amount * sign;
			}
			else {
				amount = ((Double)textField.getValue())  * sign; 
			}

			String note = (String) JOptionPane.showInputDialog(application,
					Messages.getString("Insert Note"),
					Messages.getString("Cash Operation"), JOptionPane.PLAIN_MESSAGE);

			CashOperation cashOperation = new CashOperation();
			cashOperation.setAmount(amount.doubleValue());
			cashOperation.setUser(application.getUser());
			cashOperation.setEvent(application.getEvent());
			cashOperation.setNote(note);

			ModelManager.getCashManager().addCashAction(cashOperation,
					application.getUser());

			application.showInfo(Messages.getString("Cash Operation"), Config.currencyFormat(textField.getValue()));
			textField.setValue(null);
			summaryView.getCashOperationTableModel().update();

		} catch (Exception e) {
			application.showError(e);
		}
	}

	private void printEventStatistics(EventStatistics eventStatistics,
			EventDay eventDay) throws AppException, PrinterException {
		String day = "";
		StringBuilder sb = new StringBuilder();
		sb.append("<table>");

		// sb.append(" <tr>");
		// sb.append("  <th colspan='3' width='200px'>");
		// sb.append("<img height='50' src=\"file://");
		// sb.append(ConfigController.getLocalIconRepositoryPath());
		// sb.append("\"/>");
		// sb.append("  </th>");
		// sb.append(" </tr>");

		sb.append(" <tr>");
		sb.append("  <th colspan='3' width='");
		sb.append(application.getMenuPanel().getPaperSize());
		sb.append("mm'>");
		sb.append(application.getEvent().getName());

		if (eventDay != null) {
			day = Messages.getString("Day") + " "
					+ Config.formatDay(eventDay.getStart_date());
			sb.append(" <tr>");
			sb.append("  <th colspan='3'>");
			sb.append(day);
			sb.append("  </th>");
			sb.append(" </tr>");
		}

		sb.append("  </th>");
		sb.append(" </tr>");

		sb.append(" <tr>");
		sb.append("  <td>");
		sb.append(Messages.getString("Total Income"));
		sb.append("  </td>");
		sb.append("  <td style='text-align: right;'>");
		sb.append(Config.currencyFormat(eventStatistics.getAmount()));
		sb.append("  <td>");
		sb.append(Messages.getString("Currency"));
		sb.append("  </td>");
		sb.append("</td>");
		sb.append(" </tr>");

		if (eventStatistics.getItems_statistics().length > 0) {
			sb.append(" <tr>");
			sb.append("  <th colspan='3'>");
			sb.append(Messages.getString("Items Report"));
			sb.append("  </th>");
			sb.append("  <th>");

			sb.append(" <tr>");
			sb.append("  <th  colspan='2'>");
			sb.append(Messages.getString("Item"));
			sb.append("  </th>");
			sb.append("  <th>");
			sb.append(Messages.getString("Quantity"));
			sb.append("  </th>");
			sb.append(" </tr>");
		}

		for (EventItemStatistics eventItemStatisctics : eventStatistics
				.getItems_statistics()) {

			sb.append("<tr>");
			sb.append(" <td  colspan='2' >");
			sb.append(eventItemStatisctics.getName());
			sb.append(" </td>");
			sb.append(" <td style='text-align: right;'>");
			sb.append(Config.formatInt(eventItemStatisctics.getQuantity()));
			sb.append(" </td>");
			sb.append("</tr>");
		}

		if (eventStatistics.getCash_operations().length > 0) {
			sb.append(" <tr>");
			sb.append("  <th colspan='3'>");
			sb.append(Messages.getString("Cash Operation"));
			sb.append("  </th>");
			sb.append("  <th>");

			sb.append(" <tr>");
			sb.append("  <th>");
			sb.append(Messages.getString("User"));
			sb.append("  </th>");
			sb.append("  <th>");
			sb.append(Messages.getString("Date"));
			sb.append("  </th>");
			sb.append("  <th>");
			sb.append(Messages.getString("Amount"));
			sb.append("  </th>");
			sb.append(" </tr>");
		}

		for (CashOperation cashOperation : eventStatistics.getCash_operations()) {

			sb.append("<tr>");
			sb.append(" <td>");
			sb.append(cashOperation.getUser().getUsername());
			sb.append(" </td>");
			sb.append(" <td>");
			sb.append(Config.format(cashOperation.getReg_date()));
			sb.append(" </td>");
			sb.append(" <td style='text-align: right;'>");
			sb.append(Config.formatInt(cashOperation.getAmount()));
			sb.append("&nbsp;");
			sb.append(Messages.getString("Currency"));
			sb.append(" </td>");
			sb.append("</tr>");
		}
		sb.append("</table>");

		day = "";
		if (eventDay != null) {
			day = " "
					+ Config.formatDayMachineReadable(eventDay.getStart_date());
		}
		Config.print(sb.toString(), Messages.getString("Totals") + "_"
				+ application.getEvent().getName() + day, application);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		JFormattedTextField comp = summaryView
				.getCashOperationFormattedTextField();
		NumberFormat format = NumberFormat.getIntegerInstance();
		format.setMaximumFractionDigits(2);
		try {
			if (comp.getText() != null && !comp.getText().trim().equals("")) {
				comp.commitEdit();
				comp.setForeground(Config.FG_COLOR);
			} else {
				comp.setValue(null);
			}
		} catch (ParseException e) {
			comp.setForeground(Config.FG_ALARM_COLOR);
		}
		boolean enableCashOp = summaryView.getCashOperationFormattedTextField()
				.getValue() != null;
		summaryView.getLoadButton().setEnabled(enableCashOp);
		summaryView.getDownloadButton().setEnabled(enableCashOp);
	}
}
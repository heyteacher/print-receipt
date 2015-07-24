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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterException;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.printreceipt.AppException;
import org.printreceipt.Application;
import org.printreceipt.Messages;
import org.printreceipt.datamodel.Event;
import org.printreceipt.datamodel.Item;
import org.printreceipt.datamodel.Receipt;
import org.printreceipt.datamodel.ReceiptItem;
import org.printreceipt.model.ModelManager;
import org.printreceipt.utils.Config;
import org.printreceipt.utils.ReceiptStatisticWrapper;
import org.printreceipt.view.ReceiptView;

public class ReceiptController implements ActionListener, KeyListener {

	// private static Logger log =
	// LoggerFactory.getLogger(ReceiptController.class);

	private Application application;
	private JPanel view;

	private Event event;
	private List<Item> items;
	JFormattedTextField[] qtys;
	private ReceiptStatisticWrapper receiptStatisticWrapper;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private SimpleDateFormat sdfWeekDay = new SimpleDateFormat("E");

	public ReceiptStatisticWrapper getReceiptStatReceiptWrapper() {
		if (receiptStatisticWrapper == null) {
			receiptStatisticWrapper = new ReceiptStatisticWrapper();
		}
		return receiptStatisticWrapper;
	}

	public JFormattedTextField[] getQtys() {
		return qtys;
	}

	public void setQtys(JFormattedTextField[] qtys) {
		this.qtys = qtys;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public ReceiptController(Application application, JPanel view) {
		this.application = application;
		this.view = view;
	}

	public Application getApplication() {
		return application;
	}

	public JPanel getView() {
		return view;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if ("PRINT".equals(ae.getActionCommand())) {
			printReceipt();
		} else {
			int idx = Integer.parseInt(ae.getActionCommand());
			int pos = 0;
			int incr = 0;
			if (idx < 0) {
				pos = (idx * -1) - 1;
				incr = -1;
			} else {
				pos = idx - 1;
				incr = 1;
			}
			Long value = (Long) qtys[pos].getValue();
			value = value == null ? incr : value + incr;
			qtys[pos].setValue(value);
		}
		((ReceiptView) getView()).recalculate();
	}

	private void printReceipt() {

		JTextField noteTextField = ((ReceiptView) getView()).getNoteTextField();
		try {

			Receipt receipt = new Receipt();
			receipt.setEvent(getEvent());

			Set<ReceiptItem> repeiptItems = new HashSet<ReceiptItem>();

			TreeMap<BigInteger, List<Item>> itemsOrderedByGroupOrd = new TreeMap<BigInteger, List<Item>>();

			// creo una albero ordinato di liste per gruppo
			for (Item item : getItems()) {
				BigInteger groupOrd = BigInteger.valueOf(item.getItem_group().getId());
				if (!itemsOrderedByGroupOrd.containsKey(groupOrd)) {
					itemsOrderedByGroupOrd.put(groupOrd, new ArrayList<Item>());
				}
				itemsOrderedByGroupOrd.get(groupOrd).add(item);
			}

			// ciclo per aggiungere gli item al receipt
			for (BigInteger groupOrd : itemsOrderedByGroupOrd.keySet()) {
				for (int i = 0; i < itemsOrderedByGroupOrd.get(groupOrd).size(); i++) {
					Item item = itemsOrderedByGroupOrd.get(groupOrd).get(i);
					int pos = getItems().indexOf(item);
					Long qty = (Long) getQtys()[pos].getValue();
					repeiptItems.add(new ReceiptItem(item, qty.doubleValue()));
				}
			}

			// inserisco nel db
			receipt.setReceipt_items((ReceiptItem[]) repeiptItems
					.toArray(new ReceiptItem[] {}));
			receipt.setUser(getApplication().getUser());
			if (noteTextField.getText() != null
					&& noteTextField.getText().trim().length() > 0) {
				receipt.setNote(noteTextField.getText());
			}
			ReceiptStatisticWrapper localReceiptStatisticWrapper = new ReceiptStatisticWrapper(
					ModelManager.getReceiptManager().insertReceipt(receipt,
							getApplication().getUser()));
			// if (ConfigController.isOnline()) {
			// receiptStatisticWrapper = localReceiptStatisticWrapper;
			// } else {
			getReceiptStatReceiptWrapper().setReceiptId(
					localReceiptStatisticWrapper.getReceiptId());
			// }

			// riciclo nuovamente per stampare i receipt per gruppo
			for (BigInteger groupOrd : itemsOrderedByGroupOrd.keySet()) {
				float total = 0;
				StringBuilder sb = new StringBuilder();
				String groupName = "";
				BigInteger itemGroupId = null;
				for (int i = 0; i < itemsOrderedByGroupOrd.get(groupOrd).size(); i++) {
					Item item = itemsOrderedByGroupOrd.get(groupOrd).get(i);
					int pos = getItems().indexOf(item);
					groupName = item.getItem_group().getName();
					itemGroupId = BigInteger.valueOf(item.getItem_group().getId());
					Long qty = (Long) getQtys()[pos].getValue();
					if (qty != null && qty > 0) {
						sb.append("<tr>");
						float totalItem = item.getPrice().floatValue() * qty;
						total += totalItem;
						sb.append("<td>");
						sb.append("<b>");
						sb.append(qty);
						sb.append("</b>&nbsp;&nbsp;");
						sb.append(item.getName());
						sb.append("</td>");
						sb.append("<td style='text-align: right;'>");
						sb.append(Config.currencyFormat(totalItem));
//						sb.append("&nbsp;€</td>");
						sb.append("</tr>");
					}
				}
				print(itemGroupId, groupName, sb.toString(), total);
			}

			// Stampo le note
			if (noteTextField.getText() != null
					&& noteTextField.getText().trim().length() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("<tr>");
				sb.append(" <td colspan='2'>");
				sb.append(noteTextField.getText());
				sb.append(" </td>");
				sb.append("</tr>");
				print(BigInteger.ZERO, "Varie", sb.toString(), 0);
			}

			getApplication().showInfo(Messages.getString("Print ticket position"),
					  Config.currencyFormat(getReceiptTotal(receipt)) 
					  //+ " €"
					  );

		} catch (AppException e) {
			getApplication().showError(e);
		} catch (PrinterException pe) {
			getApplication().showError(pe);
		} finally {

			for (int i = 0; i < getItems().size(); i++) {
				getQtys()[i].setValue(null);
			}
			noteTextField.setText("");
		}
	}

	private float getReceiptTotal(Receipt receipt) {
		Float total = 0f;
		for (ReceiptItem receiptItem : (ReceiptItem[]) receipt
				.getReceipt_items()) {
			total += receiptItem.getQuantity().floatValue()
					* receiptItem.getItem().getPrice().floatValue();
		}
		return total;
	}

	private void print(BigInteger itemGroupId, String itemGroupName,
			String textToPrint, float total) throws PrinterException,
			AppException {

		int priority = 1;

		if (textToPrint == null || textToPrint.trim().equalsIgnoreCase(""))
			return;
		StringBuffer sb = new StringBuffer();

		// log.debug("icon {}", ConfigController.getLocalIconRepositoryPath());
		int count = 1;
		// if (ConfigController.isOnline()) {
		// count =
		// getReceiptStatReceiptWrapper().getItemGroupCount(itemGroupId).intValue();
		// } else {
		count = getReceiptStatReceiptWrapper().incrementItemGroupCount(
				itemGroupId).intValue();
		// }
		priority = count % Config.RECEIPT_COUNT_ROTATION;

		sb.append("<table>");

		sb.append(" <tr>");
//		sb.append("  <th colspan='2' width='");
//		sb.append(paperSize);
//		sb.append("mm'>");
//		sb.append("<img height='50' width='100' src=\"file://");
//		sb.append(ConfigController.getLocalIconRepositoryPath());
//		sb.append("\"/>");
//		sb.append("  </th>");
		sb.append(" </tr>");

		sb.append(" <tr>");
		sb.append("  <th colspan='2' width='");
		sb.append(application.getMenuPanel().getPaperSize());
		sb.append("mm'>");
		sb.append(getEvent().getName());
		sb.append("  </th>");
		sb.append(" </tr>");
		sb.append(" <tr>");
		sb.append("  <td style='text-align: right;'>");
		sb.append(Messages.getString("Queue Number") + ": ");
		sb.append("  </td>");
		sb.append("  <th>");
		sb.append(priority);
		sb.append("  </th>");
		sb.append(" </tr>");
		sb.append(" <tr>");
		sb.append("  <td style='text-align: right;'>");
		sb.append(Messages.getString("Group") + ": ");
		sb.append("  </td>");
		sb.append("  <th>");
		sb.append(itemGroupName);
		sb.append("  </th>");
		sb.append(" </tr>");
		sb.append(textToPrint);

		if (total > 0) {
			sb.append("<tr>");
			sb.append(" <td style='text-align: right;'>");
			sb.append(Messages.getString("Total"));
			sb.append(" </td>");
			sb.append(" <td style='text-align: right;'>");
			sb.append(Config.currencyFormat(total));
			//sb.append("&nbsp;€");
			sb.append(" </td>");
			sb.append("</tr>");
		}
		sb.append("</table>");

		sb.append("<font size=\"+2\">");
		sb.append(sdfWeekDay.format(
				getReceiptStatReceiptWrapper().getTimestamp()).toUpperCase());
		sb.append("<font/>");
		sb.append("&nbsp;");
		sb.append("<font size=\"-3\">");
//		sb.append(getApplication().getUser().getUsername());
//		sb.append(" - ");
		sb.append(sdf.format(getReceiptStatReceiptWrapper().getTimestamp()));
		sb.append("<font/>");

		String body = sb.toString();
		String jobName = priority + "_" + itemGroupName;

		Config.print(body, jobName, getApplication());

		getApplication().showInfo(Messages.getString("Print ticket position off-line"), priority);
	}

	public void manageButtons() {
		boolean disablePrint = true;
		ReceiptView receiptView = (ReceiptView) getView();
		for (int i = 0; i < getItems().size(); i++) {
			Font font = getQtys()[i].getFont();
			if (getQtys()[i].getValue() != null
					&& !getQtys()[i].getText().equals("")
					&& (Long) getQtys()[i].getValue() != 0L) {
				disablePrint = false;
				getQtys()[i].setBackground(Config.FG_ALARM_COLOR);
				getQtys()[i].setForeground(Color.WHITE);

				font = font.deriveFont(Config.HEADER_FONT_SIZE);
				getQtys()[i].setFont(font);
			} else {
				font = font.deriveFont(Config.TEXT_FONT_SIZE);
				getQtys()[i].setFont(font);
				getQtys()[i].setBackground(Config.BG_COLOR);
				getQtys()[i].setForeground(Config.FG_COLOR);
			}
		}
		if (receiptView.getNoteTextField().getText() != null
				&& receiptView.getNoteTextField().getText().trim().length() > 0) {
			disablePrint = false;
		}
		receiptView.getPrintButton().setEnabled(!disablePrint);

		// receiptView.getDeleteLastReceiptButton().setEnabled(getReceiptStatReceiptWrapper().getReceiptId()
		// != null);
	}

	public void loadEvent() throws AppException {
		setEvent(ModelManager.getECBManager().getEvent(
				getApplication().getUser()));
		getApplication().setEvent(getEvent());

	}

	private void setEvent(Event event) {
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		JTextField source = (JTextField) keyEvent.getSource();
		JFormattedTextField comp = null;
		try {
			if (source instanceof JFormattedTextField) {
				comp = (JFormattedTextField) source;
				NumberFormat format = NumberFormat.getIntegerInstance();
				format.setMaximumFractionDigits(2);
				if (comp.getText() != null && !comp.getText().trim().equals("")) {
					comp.commitEdit();
					comp.setForeground(Config.FG_COLOR);
				} else {
					comp.setValue(null);
				}
			}
		} catch (ParseException e) {
			comp.setForeground(Config.FG_ALARM_COLOR);
		} finally {
			((ReceiptView) getView()).recalculate();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}

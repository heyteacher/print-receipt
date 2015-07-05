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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.text.ParsePosition;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.printreceipt.AppException;
import org.printreceipt.Application;
import org.printreceipt.controller.ReceiptController;
import org.printreceipt.datamodel.Event;
import org.printreceipt.datamodel.Item;
import org.printreceipt.datamodel.User;
import org.printreceipt.model.ModelManager;
import org.printreceipt.utils.Config;
import org.printreceipt.utils.RowsPanel;

public class ReceiptView extends RowsPanel {

	private static final long serialVersionUID = -3336507869903479046L;

	private Application application;
	private ReceiptController controller;

	private JTextField noteTextField = new JTextField();
	private JLabel[] totals;

	private JButton printButton = new JButton();

	public ReceiptView(Application application) throws AppException {
		super();
		this.application = application;
	}

	public void initialize() throws AppException {

		if (controller != null) {
			printButton.removeActionListener(controller);
			// deleteLastReceiptButton.removeActionListener(controller);
		}
		controller = new ReceiptController(this.application, this);

		super.initialize();

		loadEvent();
		User user = application.getUser();
		Event event = application.getEvent();
		Item[] items = ModelManager.getECBManager()
				.getItems(event, user, false);

		controller.setItems(Arrays.asList(items));

		getContentPane().setLayout(
				new GridLayout(controller.getItems().size() + 2, 4));
		controller
				.setQtys(new JFormattedTextField[controller.getItems().size()]);
		totals = new JLabel[controller.getItems().size() + 2];

		// Font font = printButton.getFont();
		// printButton.setFont(font.deriveFont(25f));
		printButton.setActionCommand("PRINT");
		printButton.setMnemonic(KeyEvent.VK_S);
		setPrintButtonLabel(0f);
		printButton.addActionListener(controller);

		// head
		getContentPane().add(Config.createLabelHeader("Item", true));
		getContentPane().add(Config.createLabelHeader("Quantity", true));
		getContentPane().add(Config.createLabelHeader("Unit Price", true));
		getContentPane().add(Config.createLabelHeader("Total", true));

		// body
		for (int i = 0; i < controller.getItems().size(); i++) {
			Item item = controller.getItems().get(i);
			getContentPane().add(
					createLabel(String.format("%s %s [%s]", getKeyLabel(i),
							item.getName(), item.getItem_group().getName()),
							Config.TEXT_FONT_SIZE, JLabel.LEFT, null, null));
			getContentPane().add(createFloatField(Config.TEXT_FONT_SIZE, i));
			getContentPane().add(
					createLabel(Config.currencyFormat(item.getPrice().doubleValue()),
							Config.TEXT_FONT_SIZE, JLabel.CENTER, null, null));
			getContentPane().add(
					createLabel(Config.currencyFormat(item.getPrice().doubleValue()),
							Config.TEXT_FONT_SIZE, JLabel.RIGHT, null, i));
		}

		JPanel notePanel = new JPanel();
		notePanel.setLayout(new BoxLayout(notePanel, BoxLayout.X_AXIS));
		notePanel.add(noteTextField);
		noteTextField.addKeyListener(getController());
		Config.setBorder(notePanel);

		getContentPane().add(Config.createLabelHeader("Note", true));
		getContentPane().add(notePanel);
		getContentPane().add(Config.createLabelHeader("Total", true));

		JPanel printButtonContainer = new JPanel(new BorderLayout());
		printButtonContainer.setBorder(new EmptyBorder(0, Config.SPACE, 0,
				Config.SPACE));
		printButtonContainer.add(printButton);
		getContentPane().add(printButtonContainer);

		// add(createBottomPanel(), BorderLayout.SOUTH);
		recalculate();
	}

	private JComponent createLabel(String text, float size, int align,
			Color color, Integer pos) {

		JLabel label = new JLabel(text, align);

		JPanel panel = Config.createLabel(label, size, align, color, null, true);

		if (pos != null) {
			totals[pos] = label;
		}

		return panel;
	}

	public JComponent createFloatField(float size, Integer pos) {
		return createFloatField(size, pos, true);
	}

	public JComponent createFloatField(float size, Integer pos,
			boolean plusMinusButton) {
		JFormattedTextField formattedTextField = new JFormattedTextField(
				Config.getIntegerFormat());
		controller.getQtys()[pos] = formattedTextField;
		return createFloatField(size, formattedTextField, plusMinusButton, pos);
	}

	public JComponent createFloatField(float size,
			JFormattedTextField formattedTextField) {
		return createFloatField(size, formattedTextField, false, -1);
	}

	private JComponent createFloatField(float size,
			JFormattedTextField formattedTextField, boolean plusMinusButton,
			int pos) {

		formattedTextField.setHorizontalAlignment(JFormattedTextField.CENTER);
		Font font = formattedTextField.getFont();
		formattedTextField.setFont(font.deriveFont(size));

		formattedTextField.addKeyListener(getController());

		JPanel panel = new JPanel();
		formattedTextField.setMinimumSize(new Dimension(35, 20));
		formattedTextField.setPreferredSize(new Dimension(80, 40));
		formattedTextField.setMaximumSize(new Dimension(80, 40));

		BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);

		panel.setLayout(layout);
		Config.setBorder(panel);

		panel.add(Box.createGlue());

		if (plusMinusButton) {
			JButton plusButton = new JButton("+");
			plusButton.setMinimumSize(new Dimension(35, 20));
			plusButton.setPreferredSize(new Dimension(200, 40));
			plusButton.setMaximumSize(new Dimension(200, 40));
			plusButton.setActionCommand("" + (pos + 1));
			plusButton.addActionListener(controller);
			Integer keyCode = getKeyCode(pos);
			if (keyCode != null) {
				plusButton.setMnemonic(getKeyCode(pos));
			}
			panel.add(plusButton);
			panel.add(Box.createRigidArea(new Dimension(1, 1)));

			JButton minusButton = new JButton("-");
			minusButton.setMinimumSize(new Dimension(35, 20));
			minusButton.setPreferredSize(new Dimension(200, 40));
			minusButton.setMaximumSize(new Dimension(200, 40));
			minusButton.setActionCommand("" + ((pos + 1) * -1));
			minusButton.addActionListener(controller);
			panel.add(minusButton);
			panel.add(Box.createRigidArea(new Dimension(1, 1)));
		}

		panel.add(formattedTextField);
		return panel;
	}

	private static Integer getKeyCode(int pos) {
		switch (pos) {

		case 0:
			return KeyEvent.VK_1;
		case 1:
			return KeyEvent.VK_2;
		case 2:
			return KeyEvent.VK_3;
		case 3:
			return KeyEvent.VK_4;
		case 4:
			return KeyEvent.VK_5;
		case 5:
			return KeyEvent.VK_6;
		case 6:
			return KeyEvent.VK_7;
		case 7:
			return KeyEvent.VK_8;
		case 8:
			return KeyEvent.VK_9;
		case 9:
			return KeyEvent.VK_0;
		case 10:
			return KeyEvent.VK_Q;
		case 11:
			return KeyEvent.VK_W;
		case 12:
			return KeyEvent.VK_E;
		case 13:
			return KeyEvent.VK_R;
		case 14:
			return KeyEvent.VK_T;
		case 15:
			return KeyEvent.VK_Y;
		case 16:
			return KeyEvent.VK_U;
		case 17:
			return KeyEvent.VK_I;
		case 18:
			return KeyEvent.VK_O;
		case 19:
			return KeyEvent.VK_P;
		default:
			return null;
		}
	}

	private String getKeyLabel(int pos) {
		switch (pos) {

		case 0:
			return "1.";
		case 1:
			return "2.";
		case 2:
			return "3.";
		case 3:
			return "4.";
		case 4:
			return "5.";
		case 5:
			return "6.";
		case 6:
			return "7.";
		case 7:
			return "8.";
		case 8:
			return "9.";
		case 9:
			return "0.";
		case 10:
			return "Q.";
		case 11:
			return "W.";
		case 12:
			return "E.";
		case 13:
			return "R.";
		case 14:
			return "T.";
		case 15:
			return "Y.";
		case 16:
			return "U.";
		case 17:
			return "I.";
		case 18:
			return "O.";
		case 19:
			return "P.";
		default:
			return "";
		}
	}

	public void recalculate() {
		Float tots = 0f;

		for (int i = 0; i < controller.getItems().size(); i++) {
			ParsePosition pp = new ParsePosition(0);
			Number qta = (Number) Config.getIntegerFormat().parse(
					controller.getQtys()[i].getText(), pp);
			qta = qta == null || qta.intValue() < 0 ? 0 : qta;
			Float tot = qta.intValue()
					* controller.getItems().get(i).getPrice().floatValue();
			totals[i].setText(new String(Config.currencyFormat(tot)));
			if (qta == null || qta.intValue() <= 0) {
				controller.getQtys()[i].setText("");
				controller.getQtys()[i].setValue(0l);
			}
			if (tot > 0) {
				totals[i].setForeground(Config.HEADER_COLOR);
			} else {
				totals[i].setForeground(Config.FG_COLOR);
			}
			tots += tot;
		}
		setPrintButtonLabel(tots);
		controller.manageButtons();
	}

	public void save() throws AppException {
	}

	public ReceiptController getController() {
		return controller;
	}

	public void loadEvent() throws AppException {
		controller.loadEvent();
	}

	public JTextField getNoteTextField() {
		return noteTextField;
	}

	public JButton getPrintButton() {
		return printButton;
	}

	private void setPrintButtonLabel(Float tots) {
		printButton.setText(tots == 0 ? "" : Config.currencyFormat(tots));
		printButton.setIcon(new ImageIcon(getClass().getResource("/printer.png")));
	}

}
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

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentFactory {

	private static Logger log = LoggerFactory.getLogger(ComponentFactory.class);

	public static JTextComponent createTextField(String field, int columns) {
		JTextField textField = new JTextField(columns);
		textField
				.setMaximumSize(new Dimension(textField.getColumns() * 20, 20));
		return textField;
	}

	public static JFormattedTextField createFloatField(String field, int columns) {

		NumberFormat numberFormat = NumberFormat
				.getNumberInstance();
		numberFormat.setMaximumFractionDigits(2);
		JFormattedTextField formattedTextField = new JFormattedTextField(
				numberFormat);
		formattedTextField.setColumns(columns);
		formattedTextField.setMaximumSize(new Dimension(formattedTextField
				.getColumns() * 20, 20));

		formattedTextField.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent focusEvent) {
			}

			public void focusLost(FocusEvent focusEvent) {
				JFormattedTextField comp = (JFormattedTextField) focusEvent
						.getComponent();
				NumberFormat format = NumberFormat
						.getIntegerInstance();
				format.setMaximumFractionDigits(2);

				try {
					if (comp.getText() == null
							|| comp.getText().trim().equals("")) {
						comp.setValue(null);
						return;
					}
					format.parse(comp.getText());
					comp.setForeground(Config.FG_COLOR);
				} catch (ParseException e) {
//					log.debug(e.getMessage());
					// comp.requestFocus();
					comp.setText((String) comp.getValue());
					comp.setForeground(Config.FG_ALARM_COLOR);
				}
			}
		});
		return formattedTextField;
	}

	public static JFormattedTextField createIntegerField(String field,
			int columns, String postfix) {

		NumberFormat numberFormat = NumberFormat
				.getIntegerInstance();
		numberFormat.setParseIntegerOnly(true);
		JFormattedTextField formattedTextField = new JFormattedTextField(
				numberFormat);
		formattedTextField.setColumns(columns);
		formattedTextField.setMaximumSize(new Dimension(formattedTextField
				.getColumns() * 20, 20));

		formattedTextField.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent focusEvent) {
			}

			public void focusLost(FocusEvent focusEvent) {
				JFormattedTextField comp = (JFormattedTextField) focusEvent
						.getComponent();
				NumberFormat format = NumberFormat
						.getIntegerInstance();
				format.setParseIntegerOnly(true);
				try {
					if (comp.getText() == null
							|| comp.getText().trim().equals("")) {
						comp.setValue(null);
						return;
					}
					format.parse(comp.getText());
					comp.setForeground(Config.FG_COLOR);
				} catch (ParseException e) {
//					log.debug(e.getMessage());
					// comp.requestFocus();
					comp.setText((String) comp.getValue());
					comp.setForeground(Config.FG_ALARM_COLOR);
				}
			}
		});
		return formattedTextField;
	}

	public static JFormattedTextField createDateTextField(String actionCommand) {
		MaskFormatter maskFormatter = null;
		try {
			maskFormatter = new MaskFormatter("##/##/####");
		} catch (ParseException e) {
		}
		JFormattedTextField formattedTextField = new JFormattedTextField(
				maskFormatter);
		formattedTextField.setColumns(10);
		formattedTextField.setActionCommand(actionCommand);
		formattedTextField.setMaximumSize(new Dimension(10 * 20, 20));
		formattedTextField.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent focusEvent) {
			}

			public void focusLost(FocusEvent focusEvent) {
				JFormattedTextField comp = (JFormattedTextField) focusEvent
						.getComponent();
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				format.setLenient(false);
				try {
					if (comp.getText() == null
							|| comp.getText().equals("  /  /    ")) {
						comp.setValue(null);
						return;
					}
					format.parse(comp.getText());
					comp.setForeground(Config.FG_COLOR);
				} catch (ParseException e) {
//					log.debug(e.getMessage());
					comp.setForeground(Config.FG_ALARM_COLOR);
				}
			}
		});

		return formattedTextField;
	}

//	public static Component createMicroSpace() {
//		return Box.createRigidArea(new Dimension(5, 5));
//	}

//	public static Component createSpace(int dim) {
//		return Box.createRigidArea(new Dimension(dim, dim));
//	}

//	public static Component createMiniSpace() {
//		return Box.createRigidArea(new Dimension(10, 10));
//	}

//	public static Border createMicroBorder() {
//		return BorderFactory.createEmptyBorder(5, 5, 5, 5);
//	}

	public static Border createMiniBorder() {
		return BorderFactory.createEmptyBorder(10, 10, 10, 10);
	}

	public static Border createTopBigBorder() {
		return BorderFactory.createEmptyBorder(10, 10, 0, 10);
	}
}
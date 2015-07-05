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


package org.printreceipt.ui.test;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple demonstration for an Applet <-> Servlet communication.
 */
public class EchoApplet extends Applet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextField inputField = new TextField();
	private TextField outputField = new TextField();
	private TextArea exceptionArea = new TextArea();

	/**
	 * Setup the GUI.
	 */
	public void init() {
		// set new layout
		setLayout(new GridBagLayout());

		// add title
		Label title = new Label("Echo Applet", Label.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 14));
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		add(title, c);

		// add input label, field and send button
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		add(new Label("Input:", Label.RIGHT), c);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		add(inputField, c);
		Button sendButton = new Button("Send");
		c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(sendButton, c);
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSendData();
			}
		});

		// add output label and non-editable field
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		add(new Label("Output:", Label.RIGHT), c);
		c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		add(outputField, c);
		outputField.setEditable(false);

		// add exception label and non-editable textarea
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		add(new Label("Exception:", Label.RIGHT), c);
		c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(exceptionArea, c);
		exceptionArea.setEditable(false);
	}

	/**
	 * Send the inputField data to the servlet and show the result in the
	 * outputField.
	 */
	private void onSendData() {
		try {

			System.out.println("on send data");

		} catch (Exception ex) {
			ex.printStackTrace();
			exceptionArea.setText(ex.toString());
		}
	}
}
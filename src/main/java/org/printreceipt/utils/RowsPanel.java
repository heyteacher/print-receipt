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

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.printreceipt.AppException;

public class RowsPanel extends JPanel {

	private static final long serialVersionUID = 731576314945344860L;

	private JPanel contentPane;

	public RowsPanel() {
	}

	public void initialize() throws AppException {
		initialize(BorderLayout.CENTER);
	}

	public void initialize(String index) throws AppException {
		removeAll();
		contentPane = new JPanel();
		setLayout(new BorderLayout());

		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		add(contentPane, index);
	}

	// public void addComponentRows(Component[] components, boolean center) {
	// contentPane.add(ComponentFactory.createComponentsRows(components,
	// center));
	// }
	//
	// public void addComponentRows(Component[] components) {
	// contentPane.add(ComponentFactory
	// .createComponentsRows(components, false));
	// }

	// public Component addComponentRow(Component component) {
	// return addComponentRow(component, false);
	// }

	// public Component addComponentRow(Component component, boolean center) {
	// Component panel = ComponentFactory.createComponentsRows(
	// new Component[] { component }, center);
	// contentPane.add(panel);
	// return panel;
	// }

	public JPanel getContentPane() {
		return contentPane;
	}

	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}
}
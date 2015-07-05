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

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.TableCellRenderer;

public class ColorUIResourceRenderer extends JLabel implements
		TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1737580860104345562L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		ColorUIResource colorUIResource = (ColorUIResource) value;
		this.setBackground(colorUIResource);
		this.setForeground(colorUIResource);
		if (colorUIResource != null) {
			this.setText(String.format("[%s,%s,%s]", colorUIResource.getRed(),
					colorUIResource.getGreen(), colorUIResource.getBlue()));
		}
		return this;
	}

}

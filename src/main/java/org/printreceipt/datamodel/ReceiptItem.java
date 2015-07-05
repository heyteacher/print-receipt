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


/**
 * ReceiptItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.printreceipt.datamodel;

public class ReceiptItem implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8099568474753589291L;

	private org.printreceipt.datamodel.Item item;

	public void setQuantity(java.lang.Double quantity) {
		this.quantity = quantity;
	}

	private java.lang.Double quantity;

	public ReceiptItem() {
	}

	public ReceiptItem(org.printreceipt.datamodel.Item item,
			java.lang.Double quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	/**
	 * Gets the item value for this ReceiptItem.
	 * 
	 * @return item
	 */
	public org.printreceipt.datamodel.Item getItem() {
		return item;
	}

	/**
	 * Sets the item value for this ReceiptItem.
	 * 
	 * @param item
	 */
	public void setItem(org.printreceipt.datamodel.Item item) {
		this.item = item;
	}

	/**
	 * Gets the quantity value for this ReceiptItem.
	 * 
	 * @return quantity
	 */
	public java.lang.Double getQuantity() {
		return quantity;
	}
}

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
 * ReceiptStatistic.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.printreceipt.datamodel;

public class ReceiptStatistic implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2582765437127773399L;

	private java.math.BigInteger receipt_id;

	private org.printreceipt.datamodel.ItemGroupStatistic[] item_group_statistics;

	private java.util.Calendar timestamp;

	public ReceiptStatistic() {
	}

	public ReceiptStatistic(
			java.math.BigInteger receipt_id,
			org.printreceipt.datamodel.ItemGroupStatistic[] item_group_statistics,
			java.util.Calendar timestamp) {
		this.receipt_id = receipt_id;
		this.item_group_statistics = item_group_statistics;
		this.timestamp = timestamp;
	}

	/**
	 * Gets the receipt_id value for this ReceiptStatistic.
	 * 
	 * @return receipt_id
	 */
	public java.math.BigInteger getReceipt_id() {
		return receipt_id;
	}

	/**
	 * Sets the receipt_id value for this ReceiptStatistic.
	 * 
	 * @param receipt_id
	 */
	public void setReceipt_id(java.math.BigInteger receipt_id) {
		this.receipt_id = receipt_id;
	}

	/**
	 * Gets the item_group_statistics value for this ReceiptStatistic.
	 * 
	 * @return item_group_statistics
	 */
	public org.printreceipt.datamodel.ItemGroupStatistic[] getItem_group_statistics() {
		return item_group_statistics;
	}

	/**
	 * Sets the item_group_statistics value for this ReceiptStatistic.
	 * 
	 * @param item_group_statistics
	 */
	public void setItem_group_statistics(
			org.printreceipt.datamodel.ItemGroupStatistic[] item_group_statistics) {
		this.item_group_statistics = item_group_statistics;
	}

	/**
	 * Gets the timestamp value for this ReceiptStatistic.
	 * 
	 * @return timestamp
	 */
	public java.util.Calendar getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp value for this ReceiptStatistic.
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(java.util.Calendar timestamp) {
		this.timestamp = timestamp;
	}
}
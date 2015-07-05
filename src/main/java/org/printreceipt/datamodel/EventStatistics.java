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
 * EventStatistics.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.printreceipt.datamodel;

public class EventStatistics implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5038628386717655069L;

	private org.printreceipt.datamodel.CashOperation[] cash_operations;

	private org.printreceipt.datamodel.EventItemStatistics[] items_statistics;

	private java.lang.Double amount;

	public EventStatistics() {
	}

	public EventStatistics(
			org.printreceipt.datamodel.CashOperation[] cash_operations,
			org.printreceipt.datamodel.EventItemStatistics[] items_statistics,
			java.lang.Double amount) {
		this.cash_operations = cash_operations;
		this.items_statistics = items_statistics;
		this.amount = amount;
	}

	/**
	 * Gets the cash_operations value for this EventStatistics.
	 * 
	 * @return cash_operations
	 */
	public org.printreceipt.datamodel.CashOperation[] getCash_operations() {
		return cash_operations;
	}

	/**
	 * Sets the cash_operations value for this EventStatistics.
	 * 
	 * @param cash_operations
	 */
	public void setCash_operations(
			org.printreceipt.datamodel.CashOperation[] cash_operations) {
		this.cash_operations = cash_operations;
	}

	/**
	 * Gets the items_statistics value for this EventStatistics.
	 * 
	 * @return items_statistics
	 */
	public org.printreceipt.datamodel.EventItemStatistics[] getItems_statistics() {
		return items_statistics;
	}

	/**
	 * Sets the items_statistics value for this EventStatistics.
	 * 
	 * @param items_statistics
	 */
	public void setItems_statistics(
			org.printreceipt.datamodel.EventItemStatistics[] items_statistics) {
		this.items_statistics = items_statistics;
	}

	/**
	 * Gets the amount value for this EventStatistics.
	 * 
	 * @return amount
	 */
	public java.lang.Double getAmount() {
		return amount;
	}

	/**
	 * Sets the amount value for this EventStatistics.
	 * 
	 * @param amount
	 */
	public void setAmount(java.lang.Double amount) {
		this.amount = amount;
	}
}

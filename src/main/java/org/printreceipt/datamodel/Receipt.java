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
 * Receipt.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.printreceipt.datamodel;

public class Receipt implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2287340621639861223L;

	private java.lang.String note;

	private org.printreceipt.datamodel.ReceiptItem[] receipt_items;

	private java.math.BigInteger id;

	private java.math.BigInteger priority;

	private java.util.Date regDate;

	private Float amount;

	private org.printreceipt.datamodel.User user;

	private org.printreceipt.datamodel.Event event;

	public Receipt() {
	}

	public Receipt(java.lang.String note,
			org.printreceipt.datamodel.ReceiptItem[] receipt_items,
			java.math.BigInteger id, java.math.BigInteger priority,
			org.printreceipt.datamodel.User user,
			org.printreceipt.datamodel.Event event) {
		this.note = note;
		this.receipt_items = receipt_items;
		this.id = id;
		this.priority = priority;
		this.user = user;
		this.event = event;
	}

	/**
	 * Gets the note value for this Receipt.
	 * 
	 * @return note
	 */
	public java.lang.String getNote() {
		return note;
	}

	/**
	 * Sets the note value for this Receipt.
	 * 
	 * @param note
	 */
	public void setNote(java.lang.String note) {
		this.note = note;
	}

	/**
	 * Gets the receipt_items value for this Receipt.
	 * 
	 * @return receipt_items
	 */
	public org.printreceipt.datamodel.ReceiptItem[] getReceipt_items() {
		return receipt_items;
	}

	/**
	 * Sets the receipt_items value for this Receipt.
	 * 
	 * @param receipt_items
	 */
	public void setReceipt_items(
			org.printreceipt.datamodel.ReceiptItem[] receipt_items) {
		this.receipt_items = receipt_items;
	}

	/**
	 * Gets the id value for this Receipt.
	 * 
	 * @return id
	 */
	public java.math.BigInteger getId() {
		return id;
	}

	/**
	 * Sets the id value for this Receipt.
	 * 
	 * @param id
	 */
	public void setId(java.math.BigInteger id) {
		this.id = id;
	}

	/**
	 * Gets the priority value for this Receipt.
	 * 
	 * @return priority
	 */
	public java.math.BigInteger getPriority() {
		return priority;
	}

	/**
	 * Sets the priority value for this Receipt.
	 * 
	 * @param priority
	 */
	public void setPriority(java.math.BigInteger priority) {
		this.priority = priority;
	}

	/**
	 * Gets the user value for this Receipt.
	 * 
	 * @return user
	 */
	public org.printreceipt.datamodel.User getUser() {
		return user;
	}

	/**
	 * Sets the user value for this Receipt.
	 * 
	 * @param user
	 */
	public void setUser(org.printreceipt.datamodel.User user) {
		this.user = user;
	}

	/**
	 * Gets the event value for this Receipt.
	 * 
	 * @return event
	 */
	public org.printreceipt.datamodel.Event getEvent() {
		return event;
	}

	/**
	 * Sets the event value for this Receipt.
	 * 
	 * @param event
	 */
	public void setEvent(org.printreceipt.datamodel.Event event) {
		this.event = event;
	}

	public java.util.Date getRegDate() {
		return regDate;
	}

	public void setRegDate(java.util.Date regDate) {
		this.regDate = regDate;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Float getAmount() {
		return this.amount;
	}

}

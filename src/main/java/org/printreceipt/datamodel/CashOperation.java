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
 * CashOperation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.printreceipt.datamodel;

public class CashOperation implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8002910651560359390L;

	private java.math.BigInteger id;

	private java.lang.String note;

	private java.util.Calendar reg_date;

	private java.lang.Double amount;

	private org.printreceipt.datamodel.User user;

	private org.printreceipt.datamodel.Event event;

	public CashOperation() {
	}

	public CashOperation(java.math.BigInteger id, java.lang.String note,
			java.util.Calendar reg_date, java.lang.Double amount,
			org.printreceipt.datamodel.User user,
			org.printreceipt.datamodel.Event event) {
		this.id = id;
		this.note = note;
		this.reg_date = reg_date;
		this.amount = amount;
		this.user = user;
		this.event = event;
	}

	/**
	 * Gets the id value for this CashOperation.
	 * 
	 * @return id
	 */
	public java.math.BigInteger getId() {
		return id;
	}

	/**
	 * Sets the id value for this CashOperation.
	 * 
	 * @param id
	 */
	public void setId(java.math.BigInteger id) {
		this.id = id;
	}

	/**
	 * Gets the note value for this CashOperation.
	 * 
	 * @return note
	 */
	public java.lang.String getNote() {
		return note;
	}

	/**
	 * Sets the note value for this CashOperation.
	 * 
	 * @param note
	 */
	public void setNote(java.lang.String note) {
		this.note = note;
	}

	/**
	 * Gets the reg_date value for this CashOperation.
	 * 
	 * @return reg_date
	 */
	public java.util.Calendar getReg_date() {
		return reg_date;
	}

	/**
	 * Sets the reg_date value for this CashOperation.
	 * 
	 * @param reg_date
	 */
	public void setReg_date(java.util.Calendar reg_date) {
		this.reg_date = reg_date;
	}

	/**
	 * Gets the amount value for this CashOperation.
	 * 
	 * @return amount
	 */
	public java.lang.Double getAmount() {
		return amount;
	}

	/**
	 * Sets the amount value for this CashOperation.
	 * 
	 * @param amount
	 */
	public void setAmount(java.lang.Double amount) {
		this.amount = amount;
	}

	/**
	 * Gets the user value for this CashOperation.
	 * 
	 * @return user
	 */
	public org.printreceipt.datamodel.User getUser() {
		return user;
	}

	/**
	 * Sets the user value for this CashOperation.
	 * 
	 * @param user
	 */
	public void setUser(org.printreceipt.datamodel.User user) {
		this.user = user;
	}

	/**
	 * Gets the event value for this CashOperation.
	 * 
	 * @return event
	 */
	public org.printreceipt.datamodel.Event getEvent() {
		return event;
	}

	/**
	 * Sets the event value for this CashOperation.
	 * 
	 * @param event
	 */
	public void setEvent(org.printreceipt.datamodel.Event event) {
		this.event = event;
	}
}

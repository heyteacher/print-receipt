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
 * EventDay.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.printreceipt.datamodel;

public class EventDay implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9063632933208760948L;

	private java.util.Calendar end_date;

	private java.util.Calendar start_date;

	private java.math.BigInteger id;

	public EventDay() {
	}

	public EventDay(java.util.Calendar end_date, java.util.Calendar start_date,
			java.math.BigInteger id) {
		this.end_date = end_date;
		this.start_date = start_date;
		this.id = id;
	}

	/**
	 * Gets the end_date value for this EventDay.
	 * 
	 * @return end_date
	 */
	public java.util.Calendar getEnd_date() {
		return end_date;
	}

	/**
	 * Sets the end_date value for this EventDay.
	 * 
	 * @param end_date
	 */
	public void setEnd_date(java.util.Calendar end_date) {
		this.end_date = end_date;
	}

	/**
	 * Gets the start_date value for this EventDay.
	 * 
	 * @return start_date
	 */
	public java.util.Calendar getStart_date() {
		return start_date;
	}

	/**
	 * Sets the start_date value for this EventDay.
	 * 
	 * @param start_date
	 */
	public void setStart_date(java.util.Calendar start_date) {
		this.start_date = start_date;
	}

	/**
	 * Gets the id value for this EventDay.
	 * 
	 * @return id
	 */
	public java.math.BigInteger getId() {
		return id;
	}

	/**
	 * Sets the id value for this EventDay.
	 * 
	 * @param id
	 */
	public void setId(java.math.BigInteger id) {
		this.id = id;
	}
}

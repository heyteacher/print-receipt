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
 * Event.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.printreceipt.datamodel;

public class Event implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6246837046183405505L;

	private java.lang.String name;

	private org.printreceipt.datamodel.EventDay[] days;

	private java.math.BigInteger id;

	public Event() {
	}

	public Event(java.lang.String name,
			org.printreceipt.datamodel.EventDay[] days, java.math.BigInteger id) {
		this.name = name;
		this.days = days;
		this.id = id;
	}

	/**
	 * Gets the name value for this Event.
	 * 
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this Event.
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the days value for this Event.
	 * 
	 * @return days
	 */
	public org.printreceipt.datamodel.EventDay[] getDays() {
		return days;
	}

	/**
	 * Sets the days value for this Event.
	 * 
	 * @param days
	 */
	public void setDays(org.printreceipt.datamodel.EventDay[] days) {
		this.days = days;
	}

	/**
	 * Gets the id value for this Event.
	 * 
	 * @return id
	 */
	public java.math.BigInteger getId() {
		return id;
	}

	/**
	 * Sets the id value for this Event.
	 * 
	 * @param id
	 */
	public void setId(java.math.BigInteger id) {
		this.id = id;
	}
}

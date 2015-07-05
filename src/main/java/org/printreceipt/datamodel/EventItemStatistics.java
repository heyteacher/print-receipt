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
 * EventItemStatistics.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.printreceipt.datamodel;

public class EventItemStatistics implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private java.lang.String name;

	private java.math.BigInteger id;

	private java.lang.Double quantity;

	public EventItemStatistics() {
	}

	public EventItemStatistics(java.lang.String name, java.math.BigInteger id,
			java.lang.Double quantity) {
		this.name = name;
		this.id = id;
		this.quantity = quantity;
	}

	/**
	 * Gets the name value for this EventItemStatistics.
	 * 
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this EventItemStatistics.
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the id value for this EventItemStatistics.
	 * 
	 * @return id
	 */
	public java.math.BigInteger getId() {
		return id;
	}

	/**
	 * Sets the id value for this EventItemStatistics.
	 * 
	 * @param id
	 */
	public void setId(java.math.BigInteger id) {
		this.id = id;
	}

	/**
	 * Gets the quantity value for this EventItemStatistics.
	 * 
	 * @return quantity
	 */
	public java.lang.Double getQuantity() {
		return quantity;
	}

	/**
	 * Sets the quantity value for this EventItemStatistics.
	 * 
	 * @param quantity
	 */
	public void setQuantity(java.lang.Double quantity) {
		this.quantity = quantity;
	}

}

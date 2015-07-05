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
 * ItemGroupStatistic.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.printreceipt.datamodel;

public class ItemGroupStatistic implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4203903005996344466L;

	private java.math.BigInteger count;

	private java.math.BigInteger id;

	public ItemGroupStatistic() {
	}

	public ItemGroupStatistic(java.math.BigInteger count,
			java.math.BigInteger id) {
		this.count = count;
		this.id = id;
	}

	/**
	 * Gets the count value for this ItemGroupStatistic.
	 * 
	 * @return count
	 */
	public java.math.BigInteger getCount() {
		return count;
	}

	/**
	 * Sets the count value for this ItemGroupStatistic.
	 * 
	 * @param count
	 */
	public void setCount(java.math.BigInteger count) {
		this.count = count;
	}

	/**
	 * Gets the id value for this ItemGroupStatistic.
	 * 
	 * @return id
	 */
	public java.math.BigInteger getId() {
		return id;
	}

	/**
	 * Sets the id value for this ItemGroupStatistic.
	 * 
	 * @param id
	 */
	public void setId(java.math.BigInteger id) {
		this.id = id;
	}
}
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
 * ItemGroup.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.printreceipt.datamodel;

public class ItemGroup implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1786805857369311725L;

	private java.lang.String name;

	private org.printreceipt.datamodel.Event event;

	private java.math.BigInteger ord;

	private long id;

	public ItemGroup() {
	}

	public ItemGroup(java.lang.String name,
			org.printreceipt.datamodel.Event event, java.math.BigInteger ord,
			long id) {
		this.name = name;
		this.event = event;
		this.ord = ord;
		this.id = id;
	}

	/**
	 * Gets the name value for this ItemGroup.
	 * 
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this ItemGroup.
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the event value for this ItemGroup.
	 * 
	 * @return event
	 */
	public org.printreceipt.datamodel.Event getEvent() {
		return event;
	}

	/**
	 * Sets the event value for this ItemGroup.
	 * 
	 * @param event
	 */
	public void setEvent(org.printreceipt.datamodel.Event event) {
		this.event = event;
	}

	/**
	 * Gets the ord value for this ItemGroup.
	 * 
	 * @return ord
	 */
	public java.math.BigInteger getOrd() {
		return ord;
	}

	/**
	 * Sets the ord value for this ItemGroup.
	 * 
	 * @param ord
	 */
	public void setOrd(java.math.BigInteger ord) {
		this.ord = ord;
	}

	/**
	 * Gets the id value for this ItemGroup.
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id value for this ItemGroup.
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
}

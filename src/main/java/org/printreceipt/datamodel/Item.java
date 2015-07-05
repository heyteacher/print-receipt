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
 * Item.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.printreceipt.datamodel;

public class Item implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6591295394844696344L;

	private java.lang.Boolean hide;

	private java.lang.String name;

	private java.lang.Double price;

	private org.printreceipt.datamodel.ItemGroup item_group;

	private java.math.BigInteger ord;

	private java.math.BigInteger id;

	public Item() {
	}

	public Item(java.lang.Boolean hide, java.lang.String name,
			java.lang.Double price,
			org.printreceipt.datamodel.ItemGroup item_group,
			java.math.BigInteger ord, java.math.BigInteger id) {
		this.hide = hide;
		this.name = name;
		this.price = price;
		this.item_group = item_group;
		this.ord = ord;
		this.id = id;
	}

	/**
	 * Gets the hide value for this Item.
	 * 
	 * @return hide
	 */
	public java.lang.Boolean getHide() {
		return hide;
	}

	/**
	 * Sets the hide value for this Item.
	 * 
	 * @param hide
	 */
	public void setHide(java.lang.Boolean hide) {
		this.hide = hide;
	}

	/**
	 * Gets the name value for this Item.
	 * 
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this Item.
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the price value for this Item.
	 * 
	 * @return price
	 */
	public java.lang.Double getPrice() {
		return price;
	}

	/**
	 * Sets the price value for this Item.
	 * 
	 * @param price
	 */
	public void setPrice(java.lang.Double price) {
		this.price = price;
	}

	/**
	 * Gets the item_group value for this Item.
	 * 
	 * @return item_group
	 */
	public org.printreceipt.datamodel.ItemGroup getItem_group() {
		return item_group;
	}

	/**
	 * Sets the item_group value for this Item.
	 * 
	 * @param item_group
	 */
	public void setItem_group(org.printreceipt.datamodel.ItemGroup item_group) {
		this.item_group = item_group;
	}

	/**
	 * Gets the ord value for this Item.
	 * 
	 * @return ord
	 */
	public java.math.BigInteger getOrd() {
		return ord;
	}

	/**
	 * Sets the ord value for this Item.
	 * 
	 * @param ord
	 */
	public void setOrd(java.math.BigInteger ord) {
		this.ord = ord;
	}

	/**
	 * Gets the id value for this Item.
	 * 
	 * @return id
	 */
	public java.math.BigInteger getId() {
		return id;
	}

	/**
	 * Sets the id value for this Item.
	 * 
	 * @param id
	 */
	public void setId(java.math.BigInteger id) {
		this.id = id;
	}
}

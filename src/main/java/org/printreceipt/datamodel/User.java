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
 * User.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.printreceipt.datamodel;

public class User implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5090105736272729233L;

	private java.lang.String username;

	private java.lang.String last_name;

	private java.lang.String first_name;

	private java.lang.String password;

	private java.math.BigInteger id;

	public User() {
	}

	public User(java.lang.String username, java.lang.String last_name,
			java.lang.String first_name, java.lang.String password,
			java.math.BigInteger id) {
		this.username = username;
		this.last_name = last_name;
		this.first_name = first_name;
		this.password = password;
		this.id = id;
	}

	/**
	 * Gets the username value for this User.
	 * 
	 * @return username
	 */
	public java.lang.String getUsername() {
		return username;
	}

	/**
	 * Sets the username value for this User.
	 * 
	 * @param username
	 */
	public void setUsername(java.lang.String username) {
		this.username = username;
	}

	/**
	 * Gets the last_name value for this User.
	 * 
	 * @return last_name
	 */
	public java.lang.String getLast_name() {
		return last_name;
	}

	/**
	 * Sets the last_name value for this User.
	 * 
	 * @param last_name
	 */
	public void setLast_name(java.lang.String last_name) {
		this.last_name = last_name;
	}

	/**
	 * Gets the first_name value for this User.
	 * 
	 * @return first_name
	 */
	public java.lang.String getFirst_name() {
		return first_name;
	}

	/**
	 * Sets the first_name value for this User.
	 * 
	 * @param first_name
	 */
	public void setFirst_name(java.lang.String first_name) {
		this.first_name = first_name;
	}

	/**
	 * Gets the password value for this User.
	 * 
	 * @return password
	 */
	public java.lang.String getPassword() {
		return password;
	}

	/**
	 * Sets the password value for this User.
	 * 
	 * @param password
	 */
	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	/**
	 * Gets the id value for this User.
	 * 
	 * @return id
	 */
	public java.math.BigInteger getId() {
		return id;
	}

	/**
	 * Sets the id value for this User.
	 * 
	 * @param id
	 */
	public void setId(java.math.BigInteger id) {
		this.id = id;
	}

}

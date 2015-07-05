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

package org.printreceipt;

public class AppException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7630184611892746325L;

	public AppException() {
		super();
	}

	public AppException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AppException(String arg0) {
		super(arg0);
	}

	public AppException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * return the real message
	 * 
	 * @return the real message
	 */
	public String getMessageKey() {
		if (this.getCause() != null) {
			if (this.getCause() instanceof AppException) {
				return ((AppException) this.getCause()).getMessageKey();
			} else {
				return this.getCause().getMessage();
			}
		} else {
			return this.getMessage();
		}
	}
}
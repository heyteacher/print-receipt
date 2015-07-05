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


package org.printreceipt.utils;

import java.util.StringTokenizer;

public class StringUtils {

	/**
	 * Lower case the capital letter (es. StreetView -> streetView)
	 * 
	 * @param s
	 *            the string to operate
	 * @return the string modified
	 */
	static String capitalLowerCase(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}

	/**
	 * Upper case the capital letter (es. streetView -> StreetView)
	 * 
	 * @param s
	 *            the string to operate
	 * @return the string modified
	 */
	static String capitalUpperCase(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * camel case of the string (es. street_view -> streetView)
	 * 
	 * @param s
	 *            the string to operate
	 * @return the string modified
	 */
	static String camelCase(String guiName) {
		StringTokenizer stk = new StringTokenizer(guiName, "_");
		StringBuffer camelCase = new StringBuffer();
		for (; stk.hasMoreTokens();) {
			camelCase.append(capitalUpperCase(stk.nextToken()));
		}
		return capitalLowerCase(camelCase.toString());
	}

}

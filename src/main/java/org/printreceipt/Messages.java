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

import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Messages {

	private static Logger log = LoggerFactory.getLogger(Messages.class);

	private static final String BUNDLE_NAME = "org.printreceipt.messages"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE;

	private static Set<String> keys = new HashSet<String>();

	static {
		Locale locale = Locale.getDefault();
		//Locale locale = Locale.ITALY;
		log.debug("locale: {}", locale.getLanguage());
		initResourceBundle(locale);
	}

	private static void initResourceBundle(Locale locale) {
		try {
			RESOURCE_BUNDLE = ResourceBundle .getBundle(BUNDLE_NAME, locale);
		} catch (Exception e) {
		}		
	}
	
	public static String getString(String key) {
		try {
			if (RESOURCE_BUNDLE == null) {
				return key;
			}
			return RESOURCE_BUNDLE.getString(key);
			
		} catch (MissingResourceException e) {
			if (!keys.contains(key)) {
				keys.add(key);
				log.debug("{}=", key);
			}
			return key;
		}
	}
}
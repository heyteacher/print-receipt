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


package org.printreceipt.model;

import org.printreceipt.model.impl.hsql.HsqlModelFactory;
import org.printreceipt.model.interfaces.CashManager;
import org.printreceipt.model.interfaces.ECBManager;
import org.printreceipt.model.interfaces.ReceiptManager;
import org.printreceipt.model.interfaces.UserManager;

public class ModelManager {

	private static ModelFactory getModelFactory() {
		return new HsqlModelFactory();
	}

	public static CashManager getCashManager() {
		return getModelFactory().getCashManager();
	}

	public static ECBManager getECBManager() {
		return getModelFactory().getECBManager();

	}

	public static ReceiptManager getReceiptManager() {
		return getModelFactory().getReceiptManager();
	}

	public static UserManager getUserManager() {
		return getModelFactory().getUserManager();
	}
}

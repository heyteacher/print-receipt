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


package org.printreceipt.model.interfaces;

import java.math.BigInteger;
import java.util.List;

import org.printreceipt.AppException;
import org.printreceipt.datamodel.Event;
import org.printreceipt.datamodel.Receipt;
import org.printreceipt.datamodel.ReceiptItem;
import org.printreceipt.datamodel.ReceiptStatistic;
import org.printreceipt.datamodel.User;

public interface ReceiptManager {

	public abstract void delete(BigInteger receipt_id, User user)
			throws AppException;

	public abstract ReceiptStatistic insertReceipt(Receipt receipt, User user)
			throws AppException;

	public abstract List<Receipt> loadReceipts(Event event, User user)
			throws AppException;

	public abstract List<ReceiptItem> loadReceiptItems(Receipt receipt,
			User user) throws AppException;

	public abstract void empty(Event event, User user) throws AppException;
}
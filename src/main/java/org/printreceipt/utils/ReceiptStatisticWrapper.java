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

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.printreceipt.controller.ReceiptController;
import org.printreceipt.datamodel.ItemGroupStatistic;
import org.printreceipt.datamodel.ReceiptStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiptStatisticWrapper {
	private static Logger log = LoggerFactory
			.getLogger(ReceiptController.class);

	private BigInteger receiptId;

	public BigInteger getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(BigInteger receiptId) {
		this.receiptId = receiptId;
	}

	private Date timestamp;

	public Date getTimestamp() {
		if (timestamp == null)
			return new Date();
		return timestamp;
	}

	private Map<BigInteger, BigInteger> itemGroupCounts = new HashMap<BigInteger, BigInteger>();

	public Map<BigInteger, BigInteger> getItemGroupCounts() {
		return itemGroupCounts;
	}

	public ReceiptStatisticWrapper() {
		try {
			Map<BigInteger, BigInteger> itemGroupCounts = LocalStorage
					.loadItemGroupCounts();
			if (itemGroupCounts != null) {
				this.itemGroupCounts = itemGroupCounts;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public ReceiptStatisticWrapper(ReceiptStatistic receiptStatistic) {
		super();
		receiptId = receiptStatistic.getReceipt_id();
		if (receiptStatistic.getTimestamp() != null) {
			timestamp = receiptStatistic.getTimestamp().getTime();
		} else {
			timestamp = new Date();
		}
		if (receiptStatistic.getItem_group_statistics() != null) {
			for (ItemGroupStatistic itemGroupStatistic : receiptStatistic
					.getItem_group_statistics()) {
				itemGroupCounts.put(itemGroupStatistic.getId(),
						itemGroupStatistic.getCount());
			}
		}
		try {
			LocalStorage.storeItemGroupCounts(this.itemGroupCounts);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public BigInteger getItemGroupCount(BigInteger itemGroupId) {
		BigInteger count = itemGroupCounts.get(itemGroupId);
		if (count == null) {
			count = BigInteger.valueOf(1);
		}
		return count;
	}

	public BigInteger incrementItemGroupCount(BigInteger itemGroupId) {
		BigInteger count = getItemGroupCount(itemGroupId);
		count = count.add(BigInteger.ONE);
		itemGroupCounts.put(itemGroupId, count);
		try {
			LocalStorage.storeItemGroupCounts(this.itemGroupCounts);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return count;
	}
}
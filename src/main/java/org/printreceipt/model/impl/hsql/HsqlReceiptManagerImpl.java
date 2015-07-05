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


package org.printreceipt.model.impl.hsql;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.printreceipt.AppException;
import org.printreceipt.datamodel.Event;
import org.printreceipt.datamodel.Item;
import org.printreceipt.datamodel.ItemGroupStatistic;
import org.printreceipt.datamodel.Receipt;
import org.printreceipt.datamodel.ReceiptItem;
import org.printreceipt.datamodel.ReceiptStatistic;
import org.printreceipt.datamodel.User;
import org.printreceipt.model.interfaces.ReceiptManager;

public class HsqlReceiptManagerImpl implements ReceiptManager {

	@Override
	public void delete(BigInteger receiptId, User user) throws AppException {
		try {
			PreparedStatement pstm = HsqlModelFactory.getConnection()
					.prepareStatement("DELETE FROM ecb_receipt WHERE id = ? ");
			pstm.setInt(1, receiptId.intValue());
			pstm.executeUpdate();

		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	@Override
	public ReceiptStatistic insertReceipt(Receipt receipt, User user)
			throws AppException {

		try {
			Calendar receiptDate = Calendar.getInstance();

			HsqlModelFactory.setAutoCommit(false);
			PreparedStatement pstm = HsqlModelFactory
					.getConnection()
					.prepareStatement(
							"INSERT INTO ecb_receipt (event_id, reg_date, note, user_id) VALUES (?,?,?,?) ");
			pstm.setInt(1, receipt.getEvent().getId().intValue());
			pstm.setDate(2, new java.sql.Date(receiptDate.getTime().getTime()));
			pstm.setString(3, receipt.getNote());
			pstm.setInt(4, receipt.getUser().getId().intValue());
			pstm.execute();

			BigInteger receiptId = HsqlModelFactory.lastIdentity();
			receipt.setId(receiptId);

			for (ReceiptItem receiptItem : receipt.getReceipt_items()) {
				if (receiptItem.getQuantity().doubleValue() > 0) {
					pstm = HsqlModelFactory
							.getConnection()
							.prepareStatement(
									"INSERT INTO ecb_receiptitem (item_id, receipt_id, quantity) VALUES (?,?,?) ");
					pstm.setInt(1, receiptItem.getItem().getId().intValue());
					pstm.setInt(2, receiptId.intValue());
					pstm.setDouble(3, receiptItem.getQuantity());
					pstm.executeUpdate();
				}
			}
			pstm.close();

			ReceiptStatistic receiptStatistic = getReceiptStatistics(receipt,
					receiptDate);
			HsqlModelFactory.setAutoCommit(true);

			return receiptStatistic;

		} catch (Exception e) {
			HsqlModelFactory.rollback();
			throw new AppException(e);
		} finally {
			HsqlModelFactory.setAutoCommit(true);
		}
	}

	private ReceiptStatistic getReceiptStatistics(Receipt receipt,
			Calendar receiptDate) throws AppException, SQLException,
			ClassNotFoundException {
		String query = "SELECT ecb_item.item_group_id AS item_group_id, "
				+ " COUNT(DISTINCT ecb_receipt.id) AS item_group_count "
				+ " FROM ecb_receipt "
				+ " INNER JOIN ecb_receiptitem ON (ecb_receipt.id = ecb_receiptitem.receipt_id) "
				+ " INNER JOIN ecb_item ON (ecb_item.id = ecb_receiptitem.item_id) "
				+ " INNER JOIN ecb_itemgroup ON (ecb_itemgroup.id = ecb_item.item_group_id) "
				+ " WHERE  ecb_receipt.event_id = ? "
				+ " GROUP BY ecb_item.item_group_id";

		PreparedStatement pstmt = HsqlModelFactory.getConnection()
				.prepareStatement(query);
		pstmt.setInt(1, receipt.getEvent().getId().intValue());
		ResultSet resultSet = pstmt.executeQuery();

		ReceiptStatistic receiptStatistic = new ReceiptStatistic();
		receiptStatistic.setReceipt_id(receipt.getId());
		receiptStatistic.setTimestamp(receiptDate);
		ArrayList<ItemGroupStatistic> itemGroupStatistics = new ArrayList<ItemGroupStatistic>();
		while (resultSet.next()) {
			BigInteger itemGroupId = BigInteger.valueOf(resultSet
					.getInt("item_group_id"));
			BigInteger itemGroupCount = BigInteger.valueOf(resultSet
					.getInt("item_group_count"));
			itemGroupStatistics.add(new ItemGroupStatistic(itemGroupCount,
					itemGroupId));
		}
		resultSet.close();
		pstmt.close();

		receiptStatistic.setItem_group_statistics(itemGroupStatistics
				.toArray(new ItemGroupStatistic[] {}));
		return receiptStatistic;
	}

	@Override
	public List<Receipt> loadReceipts(Event event, User user)
			throws AppException {
		List<Receipt> receiptsList = new ArrayList<Receipt>();
		try {
			PreparedStatement pstm = HsqlModelFactory
					.getConnection()
					.prepareStatement(
							" SElECT ecb_receipt.id, ecb_receipt.reg_date, ecb_receipt.note, "
									+ "        SUM(ecb_receiptitem.quantity * ecb_item.price) as amount"
									+ " FROM ecb_receipt "
									+ " INNER JOIN ecb_receiptitem ON (ecb_receipt.id = ecb_receiptitem.receipt_id) "
									+ " INNER JOIN ecb_item ON (ecb_item.id = ecb_receiptitem.item_id) "
									+ " WHERE event_id = ? "
									+ " GROUP BY ecb_receipt.id, ecb_receipt.reg_date, ecb_receipt.note "
									+ " ORDER BY ecb_receipt.reg_date DESC "
									+ " LIMIT 0, 100");
			pstm.setInt(1, event.getId().intValue());
			ResultSet resultSet = pstm.executeQuery();
			while (resultSet.next()) {
				Receipt receipt = new Receipt();

				BigInteger id = BigInteger.valueOf(resultSet.getLong("id"));
				Date regDate = resultSet.getTimestamp("reg_date");
				String note = resultSet.getString("note");
				Float amount = resultSet.getFloat("amount");

				receipt.setRegDate(regDate);
				receipt.setId(id);
				receipt.setNote(note);
				receipt.setAmount(amount);

				receiptsList.add(receipt);
			}
			resultSet.close();
			pstm.close();

			return receiptsList;
		} catch (SQLException sqle) {
			throw new AppException(sqle);
		} catch (ClassNotFoundException cnfe) {
			throw new AppException(cnfe);
		}
	}

	@Override
	public List<ReceiptItem> loadReceiptItems(Receipt receipt, User user)
			throws AppException {
		List<ReceiptItem> receiptItemsList = new ArrayList<ReceiptItem>();
		try {
			PreparedStatement pstm = HsqlModelFactory
					.getConnection()
					.prepareStatement(
							" SElECT "
									+ "   ecb_item.id as ecb_item_id, "
									+ "   ecb_item.name, "
									+ "   ecb_item.price, "
									+ "   ecb_receiptitem.quantity "
									+ " FROM ecb_receiptitem "
									+ " INNER JOIN ecb_item ON (ecb_item.id = ecb_receiptitem.item_id) "
									+ " WHERE ecb_receiptitem.receipt_id = ? ");
			pstm.setInt(1, receipt.getId().intValue());
			ResultSet resultSet = pstm.executeQuery();
			while (resultSet.next()) {
				ReceiptItem receiptItem = new ReceiptItem();
				Item item = new Item();
				item.setName(resultSet.getString("name"));
				item.setPrice(resultSet.getDouble("price"));
				receiptItem.setItem(item);
				receiptItem.setQuantity(resultSet.getDouble("quantity"));

				receiptItemsList.add(receiptItem);
			}
			resultSet.close();
			pstm.close();

			return receiptItemsList;
		} catch (SQLException sqle) {
			throw new AppException(sqle);
		} catch (ClassNotFoundException cnfe) {
			throw new AppException(cnfe);
		}
	}

	@Override
	public void empty(Event event, User user) throws AppException {
		try {
			PreparedStatement pstm = HsqlModelFactory.getConnection()
					.prepareStatement(
							"DELETE FROM ecb_receipt WHERE event_id = ? ");
			pstm.setInt(1, event.getId().intValue());
			pstm.executeUpdate();

		} catch (Exception e) {
			throw new AppException(e);
		}
	}
}
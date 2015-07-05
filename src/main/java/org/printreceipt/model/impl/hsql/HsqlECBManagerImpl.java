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

import java.io.File;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.printreceipt.AppException;
import org.printreceipt.datamodel.CashOperation;
import org.printreceipt.datamodel.Event;
import org.printreceipt.datamodel.EventDay;
import org.printreceipt.datamodel.EventItemStatistics;
import org.printreceipt.datamodel.EventStatistics;
import org.printreceipt.datamodel.Item;
import org.printreceipt.datamodel.ItemGroup;
import org.printreceipt.datamodel.User;
import org.printreceipt.model.interfaces.ECBManager;
import org.printreceipt.utils.FileUtils;

public class HsqlECBManagerImpl implements ECBManager {

	@Override
	public Event getEvent(User user) throws AppException {
		try {
			Statement statement = HsqlModelFactory.getConnection()
					.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT * FROM ecb_event");
			resultSet.next();

			int event_id = resultSet.getInt("id");
			Event event = new Event();
			event.setId(BigInteger.valueOf(event_id));
			event.setName(resultSet.getString("name"));

			resultSet.close();
			statement.close();

			statement = HsqlModelFactory.getConnection().createStatement();
			resultSet = statement
					.executeQuery("SELECT * FROM ecb_eventday WHERE ecb_eventday.event_id = '"
							+ event_id + "' ORDER BY start_date");
			ArrayList<EventDay> eventDays = new ArrayList<EventDay>();
			while (resultSet.next()) {
				BigInteger id = BigInteger.valueOf(resultSet.getInt("id"));
				Calendar start_date = Calendar.getInstance();
				start_date.setTime(resultSet.getDate("start_date"));
				Calendar end_date = Calendar.getInstance();
				end_date.setTime(resultSet.getDate("end_date"));
				EventDay eventDay = new EventDay(end_date, start_date, id);
				eventDays.add(eventDay);
			}
			statement.close();
			resultSet.close();

			event.setDays(eventDays.toArray(new EventDay[] {}));
			return event;

		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	@Override
	public EventStatistics getEventDayStatistics(EventDay eventDay, User user)
			throws AppException {
		try {
			EventStatistics eventStatistics = new EventStatistics();

			String query = "SELECT "
					+ "    ed.id, sum( ri.quantity * i.price ) AS amount "
					+ " FROM ecb_event e "
					+ " LEFT JOIN ecb_eventday ed ON ed.event_id = e.id "
					+ " LEFT JOIN ecb_receipt r ON r.event_id = e.id "
					+ " LEFT JOIN ecb_receiptitem ri ON r.id = ri.receipt_id "
					+ " LEFT JOIN ecb_item i ON ri.item_id = i.id "
					+ " WHERE ed.id = ? "
					+ "     AND r.reg_date BETWEEN ed.start_date AND ed.end_date "
					+ " GROUP BY ed.id ";

			PreparedStatement pstmt = HsqlModelFactory.getConnection()
					.prepareStatement(query);
			pstmt.setInt(1, eventDay.getId().intValue());
			ResultSet resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				eventStatistics.setAmount(resultSet.getDouble("amount"));
			} else {
				eventStatistics.setAmount(0d);
			}
			resultSet.close();
			pstmt.close();

			query = "SELECT " + " i.id, i.name, sum( ri.quantity) AS quantity "
					+ " FROM ecb_item i "
					+ " LEFT JOIN ecb_itemgroup ig ON i.item_group_id = ig.id "
					+ " LEFT JOIN ecb_receiptitem ri ON ri.item_id = i.id  "
					+ " LEFT JOIN ecb_receipt r ON ri.receipt_id = r.id "
					+ " LEFT JOIN ecb_event e ON r.event_id = e.id "
					+ " LEFT JOIN ecb_eventday ed ON ed.event_id = e.id "
					+ " WHERE ed.id = ? "
					+ "  AND r.reg_date BETWEEN ed.start_date AND ed.end_date "
					+ " GROUP BY i.id, i.name, ig.ord, i.ord "
					+ " ORDER BY ig.ord, i.ord";
			pstmt = HsqlModelFactory.getConnection().prepareStatement(query);
			pstmt.setInt(1, eventDay.getId().intValue());
			resultSet = pstmt.executeQuery();

			ArrayList<EventItemStatistics> eventItemStatistics = new ArrayList<EventItemStatistics>();
			while (resultSet.next()) {
				BigInteger id = BigInteger.valueOf(resultSet.getInt("id"));
				String name = resultSet.getString("name");
				Double quantity = resultSet.getDouble("quantity");
				eventItemStatistics.add(new EventItemStatistics(name, id,
						quantity));
			}
			resultSet.close();
			pstmt.close();
			eventStatistics.setItems_statistics((eventItemStatistics
					.toArray(new EventItemStatistics[] {})));

			query = "SELECT id, note, reg_date, amount, event_id, e.name AS event_name"
					+ " FROM ecb_cashoperation "
					+ " LEFT JOIN ecb_event e ON e.id = ecb_cashoperation.event_id "
					+ " LEFT JOIN ecb_eventday ed ON ed.event_id = ecb_cashoperation.event_id "
					+ " WHERE ecb_cashoperation.event_id = ? "
					+ " AND ecb_cashoperation.reg_date BETWEEN ed.start_date AND ed.end_date";
			pstmt = HsqlModelFactory.getConnection().prepareStatement(query);
			pstmt.setInt(1, eventDay.getId().intValue());
			resultSet = pstmt.executeQuery();

			ArrayList<CashOperation> cashOperations = new ArrayList<CashOperation>();
			while (resultSet.next()) {
				BigInteger id = BigInteger.valueOf(resultSet.getInt("id"));
				String note = resultSet.getString("note");
				Double amount = resultSet.getDouble("amount");

				Calendar reg_date = Calendar.getInstance();
				reg_date.setTime(resultSet.getDate("reg_date"));

				BigInteger event_id = BigInteger.valueOf(resultSet
						.getInt("event_id"));
				String event_name = resultSet.getString("event_name");
				Event event = new Event(event_name, null, event_id);

				cashOperations.add(new CashOperation(id, note, reg_date,
						amount, user, event));
			}
			resultSet.close();
			pstmt.close();
			eventStatistics.setCash_operations(cashOperations
					.toArray(new CashOperation[] {}));
			return eventStatistics;
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	@Override
	public EventStatistics getEventStatistics(Event event, User user)
			throws AppException {
		try {
			EventStatistics eventStatistics = new EventStatistics();

			String query = "SELECT e.id, sum( ri.quantity * i.price ) AS amount "
					+ " FROM ecb_event e "
					+ " LEFT JOIN ecb_receipt r ON e.id = r.event_id "
					+ " LEFT JOIN ecb_receiptitem ri ON r.id = ri.receipt_id "
					+ " LEFT JOIN ecb_item i ON ri.item_id = i.id "
					+ " WHERE e.id = ? " + " GROUP BY e.id ";

			PreparedStatement pstmt = HsqlModelFactory.getConnection()
					.prepareStatement(query);
			pstmt.setInt(1, event.getId().intValue());
			ResultSet resultSet = pstmt.executeQuery();
			resultSet.next();
			eventStatistics.setAmount(resultSet.getDouble("amount"));
			resultSet.close();
			pstmt.close();

			query = "SELECT i.id, i.name, sum( ri.quantity) AS quantity "
					+ " FROM ecb_item i "
					+ " LEFT JOIN ecb_itemgroup ig ON i.item_group_id = ig.id "
					+ " LEFT JOIN ecb_receiptitem ri ON ri.item_id = i.id  "
					+ " LEFT JOIN ecb_receipt r ON ri.receipt_id = r.id "
					+ " LEFT JOIN ecb_event e ON r.event_id = e.id "
					+ " WHERE e.id = ? "
					+ " GROUP BY i.id, i.name, ig.ord, i.ord "
					+ " ORDER BY ig.ord, i.ord";
			pstmt = HsqlModelFactory.getConnection().prepareStatement(query);
			pstmt.setInt(1, event.getId().intValue());
			resultSet = pstmt.executeQuery();

			ArrayList<EventItemStatistics> eventItemStatistics = new ArrayList<EventItemStatistics>();
			while (resultSet.next()) {
				BigInteger id = BigInteger.valueOf(resultSet.getInt("id"));
				String name = resultSet.getString("name");
				Double quantity = resultSet.getDouble("quantity");
				eventItemStatistics.add(new EventItemStatistics(name, id,
						quantity));
			}
			resultSet.close();
			pstmt.close();
			eventStatistics.setItems_statistics((eventItemStatistics
					.toArray(new EventItemStatistics[] {})));

			query = "SELECT id, note, reg_date, amount FROM ecb_cashoperation "
					+ " WHERE ecb_cashoperation.event_id = ? ";
			pstmt = HsqlModelFactory.getConnection().prepareStatement(query);
			pstmt.setInt(1, event.getId().intValue());
			resultSet = pstmt.executeQuery();

			ArrayList<CashOperation> cashOperations = new ArrayList<CashOperation>();
			while (resultSet.next()) {
				BigInteger id = BigInteger.valueOf(resultSet.getInt("id"));
				String note = resultSet.getString("note");
				Calendar reg_date = Calendar.getInstance();
				reg_date.setTime(resultSet.getDate("reg_date"));
				Double amount = resultSet.getDouble("amount");
				cashOperations.add(new CashOperation(id, note, reg_date,
						amount, user, event));
			}
			resultSet.close();
			pstmt.close();
			eventStatistics.setCash_operations(cashOperations
					.toArray(new CashOperation[] {}));
			return eventStatistics;
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	@Override
	public Item[] getItems(Event event, User user, boolean showHidden)
			throws AppException {

		try {
			String query = "SELECT id, name, price, hide, item_group_id, ord, "
					+ "		ecb_itemgroup.ord AS item_group_ord, "
					+ "		ecb_itemgroup.name AS item_group_name, "
					+ "		ecb_itemgroup.event_id AS item_group_event_id "
					+ "		FROM ecb_item"
					+ "		INNER JOIN ecb_itemgroup ON ecb_itemgroup.id = ecb_item.item_group_id "
					+ " 		WHERE ecb_itemgroup.event_id = ? "
					+ (showHidden ? "       " : "       AND hide <> 1 ")
					+ "		ORDER BY ecb_itemgroup.ord, ecb_item.ord";
			PreparedStatement pstmt = HsqlModelFactory.getConnection()
					.prepareStatement(query);
			pstmt.setInt(1, event.getId().intValue());
			ResultSet resultSet = pstmt.executeQuery();

			ArrayList<Item> items = new ArrayList<Item>();
			while (resultSet.next()) {
				BigInteger id = BigInteger.valueOf(resultSet.getInt("id"));
				String name = resultSet.getString("name");
				Double price = resultSet.getDouble("price");
				Boolean hide = resultSet.getBoolean("hide");
				BigInteger ord = BigInteger.valueOf(resultSet.getInt("ord"));

				BigInteger ig_id = BigInteger.valueOf(resultSet
						.getInt("item_group_id"));
				String ig_name = resultSet.getString("item_group_name");
				BigInteger ig_ord = BigInteger.valueOf(resultSet
						.getInt("item_group_ord"));
				ItemGroup itemGroup = new ItemGroup(ig_name, event, ig_ord,
						ig_id);

				items.add(new Item(hide, name, price, itemGroup, ord, id));
			}
			resultSet.close();
			pstmt.close();

			return items.toArray(new Item[] {});

		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	@Override
	public ItemGroup[] getItemGroups(Event event, User user)
			throws AppException {

		try {
			String query = "SELECT id, name, ord, event_id "
					+ "		FROM ecb_itemgroup " + " 		WHERE event_id = ? "
					+ "		ORDER BY ord";

			PreparedStatement pstmt = HsqlModelFactory.getConnection()
					.prepareStatement(query);
			pstmt.setInt(1, event.getId().intValue());
			ResultSet resultSet = pstmt.executeQuery();

			ArrayList<ItemGroup> itemGroups = new ArrayList<ItemGroup>();
			while (resultSet.next()) {
				BigInteger id = BigInteger.valueOf(resultSet.getInt("id"));
				String name = resultSet.getString("name");
				BigInteger ord = BigInteger.valueOf(resultSet.getInt("ord"));

				ItemGroup itemGroup = new ItemGroup(name, event, ord, id);
				itemGroups.add(itemGroup);
			}
			resultSet.close();
			pstmt.close();

			return itemGroups.toArray(new ItemGroup[] {});

		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	@Override
	public void save(Event event, EventDay[] eventDays, ItemGroup[] itemGroups,
			Item[] items, User user) throws AppException {
		try {
			HsqlModelFactory.setAutoCommit(false);
			saveEventDays(event, eventDays);
			Map<String, BigInteger> itemGroupSaved = saveItemGroups(event, itemGroups, user);
			saveItems(event, items, itemGroupSaved, user);
			HsqlModelFactory.commit();
		} catch (Exception e) {
			HsqlModelFactory.rollback();
			throw new AppException(e);
		} finally {
			HsqlModelFactory.setAutoCommit(true);
		}
	}

	private void saveEventDays(Event event, EventDay[] eventDays)
			throws AppException {

		BigInteger eventId = event.getId();

		try {
			//HsqlModelFactory.setAutoCommit(false);

			PreparedStatement preparedStatement = HsqlModelFactory
					.getConnection().prepareStatement(
							"UPDATE ecb_event SET name = ? WHERE id = ?");
			preparedStatement.setString(1, event.getName());
			preparedStatement.setInt(2, event.getId().intValue());
			preparedStatement.executeUpdate();
			preparedStatement.close();

			preparedStatement = HsqlModelFactory
					.getConnection()
					.prepareStatement(
							"DELETE FROM ecb_eventday WHERE ecb_eventday.event_id = ?");
			preparedStatement.setInt(1, eventId.intValue());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			for (int i = 0; i < eventDays.length; i++) {

				EventDay eventDay = eventDays[i];

				if (eventDay.getStart_date() == null
						|| eventDay.getEnd_date() == null) {
					throw new AppException("Event day cannot by empty");
				}
				if (!eventDay.getStart_date().before(eventDay.getEnd_date())) {
					throw new AppException(
							"Event day start date cannot be before end date");
				}
				eventDay.getStart_date().add(Calendar.HOUR, 12);
				eventDay.getEnd_date().add(Calendar.HOUR, 12);

				preparedStatement = HsqlModelFactory
						.getConnection()
						.prepareStatement(
								"INSERT INTO ecb_eventday (event_id, start_date, end_date) VALUES (?,?,?)");
				preparedStatement.setInt(1, event.getId().intValue());
				preparedStatement.setDate(2, new java.sql.Date(eventDays[i]
						.getStart_date().getTime().getTime()));
				preparedStatement.setDate(3, new java.sql.Date(eventDays[i]
						.getEnd_date().getTime().getTime()));
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}
			//HsqlModelFactory.commit();
		} catch (Exception e) {
			HsqlModelFactory.rollback();
			throw new AppException(e);
		} finally {
			//HsqlModelFactory.setAutoCommit(true);
		}
	}

	public Map<String, BigInteger> saveItemGroups(Event event, ItemGroup[] itemGroups, User user)
			throws SQLException, ClassNotFoundException, AppException {

		try {
			//HsqlModelFactory.setAutoCommit(false);
			PreparedStatement preparedStatement = null;

			Map<String, BigInteger> itemsGroupSaved = new HashMap<String, BigInteger>();

			for (int i = 0; i < itemGroups.length; i++) {
				if (itemGroups[i].getName() == null
						|| "".equals(itemGroups[i].getName().trim())) {
					throw new AppException("Item group name cannot be empty");
				}
				if (itemGroups[i].getId() == null) {
					preparedStatement = HsqlModelFactory
							.getConnection()
							.prepareStatement(
									"INSERT INTO ecb_itemgroup (name, ord, event_id) VALUES (?,?,?)");
					preparedStatement.setInt(3, event.getId().intValue());
				} else {
					preparedStatement = HsqlModelFactory
							.getConnection()
							.prepareStatement(
									"UPDATE ecb_itemgroup SET name = ?, ord = ? WHERE id = ?");
					preparedStatement.setInt(3, itemGroups[i].getId()
							.intValue());
					itemsGroupSaved.put(itemGroups[i].getName(), itemGroups[i].getId());
				}
				preparedStatement.setString(1, itemGroups[i].getName());
				preparedStatement.setInt(2, i);
				preparedStatement.executeUpdate();
				if (itemGroups[i].getId() == null) {
					itemGroups[i].setId(HsqlModelFactory.lastIdentity());
					itemsGroupSaved.put(itemGroups[i].getName(), HsqlModelFactory.lastIdentity());
				}
				preparedStatement.close();
			}

			ItemGroup[] oldItemGroups = getItemGroups(event, user);
			for (int i = 0; i < oldItemGroups.length; i++) {
				if (!itemsGroupSaved.containsValue(oldItemGroups[i].getId())) {
					preparedStatement = HsqlModelFactory.getConnection()
							.prepareStatement(
									"DELETE FROM ecb_itemgroup WHERE id = ?");
					preparedStatement.setInt(1, oldItemGroups[i].getId().intValue());
					preparedStatement.executeUpdate();
					preparedStatement.close();
				}
			}
			//HsqlModelFactory.commit();
			return itemsGroupSaved;
		} catch (Exception e) {
			HsqlModelFactory.rollback();
			throw new AppException(e);
		} finally {
			//HsqlModelFactory.setAutoCommit(true);
		}
	}

	public void saveItems(
			Event event, 
			Item[] items, 
			Map<String, BigInteger> itemGroupSaved,
			User user)
			throws SQLException, ClassNotFoundException, AppException {

		try {
			//HsqlModelFactory.setAutoCommit(false);
			PreparedStatement preparedStatement = null;

			Set<BigInteger> itemsSaved = new HashSet<BigInteger>();

			for (int i = 0; i < items.length; i++) {
				if (items[i].getName() == null
						|| "".equals(items[i].getName().trim())) {
					throw new AppException("Item name cannot by empty");
				}
				if (items[i].getItem_group() != null) {
						if (items[i].getItem_group().getId() == null) {
							items[i].getItem_group().setId(
									itemGroupSaved.get(items[i].getItem_group().getName()));
						}
				}
				if (items[i].getItem_group() == null || items[i].getItem_group().getId() == null) {
					throw new AppException("Items group cannot by empty");
				}

				if (items[i].getId() == null) {
					preparedStatement = HsqlModelFactory
							.getConnection()
							.prepareStatement(
									"INSERT INTO ecb_item (name, ord, item_group_id, price, hide) VALUES (?,?,?,?,?)");
				} else {
					preparedStatement = HsqlModelFactory
							.getConnection()
							.prepareStatement(
									"UPDATE ecb_item SET name = ?, ord = ?, item_group_id = ?, price = ?, hide = ? WHERE id = ?");
				}
				preparedStatement.setString(1, items[i].getName());
				preparedStatement.setInt(2, i);
				preparedStatement.setInt(3, items[i].getItem_group().getId().intValue());
				preparedStatement.setDouble(4, items[i].getPrice());
				preparedStatement.setInt(5, items[i].getHide() ? 1 : 0);
				if (items[i].getId() != null) {
					preparedStatement.setInt(6, items[i].getId().intValue());
					itemsSaved.add(items[i].getId());
				}
				preparedStatement.executeUpdate();
				if (items[i].getId() == null) {
					items[i].setId(HsqlModelFactory.lastIdentity());
					itemsSaved.add(HsqlModelFactory.lastIdentity());
				}
				preparedStatement.close();
			}

			Item[] oldItems = getItems(event, user, true);
			for (int i = 0; i < oldItems.length; i++) {
				if (!itemsSaved.contains(oldItems[i].getId())) {
					preparedStatement = HsqlModelFactory.getConnection()
							.prepareStatement(
									"DELETE FROM ecb_item WHERE id = ?");
					preparedStatement.setInt(1, oldItems[i].getId().intValue());
					preparedStatement.executeUpdate();
					preparedStatement.close();
				}
			}
			//HsqlModelFactory.commit();
		} catch (SQLIntegrityConstraintViolationException sqlicve) {
			HsqlModelFactory.rollback();
			throw new AppException(
					"Items cannot be deleted if already used in receipts");
		} catch (Exception e) {
			HsqlModelFactory.rollback();
			throw new AppException(e);
		} finally {
			//HsqlModelFactory.setAutoCommit(true);
		}
	}

	@Override
	public BigInteger createItemGroup(ItemGroup itemGroup) throws AppException {
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = HsqlModelFactory
					.getConnection()
					.prepareStatement(
							"INSERT INTO ecb_itemgroup (name, ord, event_id) VALUES (?,?,?)");
			preparedStatement.setString(1, itemGroup.getName());
			preparedStatement.setInt(2, itemGroup.getOrd().intValue());
			preparedStatement
					.setInt(3, itemGroup.getEvent().getId().intValue());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			return HsqlModelFactory.lastIdentity();
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	@Override
	public File exportEvent() throws AppException {
		shutdown();
		return HsqlModelFactory.getDBNameFile();
	}

	@Override
	public void deleteEvent() throws AppException {
		shutdown();
		HsqlModelFactory.getDBNameFile().delete();
		deleteDatabaseFiles();
	}

	@Override
	public void importEvent(File file) throws AppException {

		shutdown();
		FileUtils.copyfile(file, HsqlModelFactory.getDBNameFile());
		deleteDatabaseFiles();
	}

	private void deleteDatabaseFiles() throws AppException {
		new File(HsqlModelFactory.getDBNameFile().getParent(), "ecb_hsql.log")
				.delete();
		new File(HsqlModelFactory.getDBNameFile().getParent(),
				"ecb_hsql.properties").delete();
		HsqlModelFactory.init();
	}

	private void shutdown() throws AppException {
		try {
			HsqlModelFactory.shutdown();
		} catch (Exception e) {
			throw new AppException("cannot close db", e);
		}
	}

	@Override
	public void cleanEvent() throws AppException {
		try {
			String sql = "DELETE FROM ECB_RECEIPTITEM " + "WHERE id IN ( "
					+ "	SELECT ri.id " + "	FROM ECB_RECEIPTITEM ri "
					+ "		LEFt JOIN  ECB_RECEIPT r ON (ri.receipt_id = r.id) "
					+ ") ";
			PreparedStatement preparedStatement = null;
			preparedStatement = HsqlModelFactory.getConnection()
					.prepareStatement(sql);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

}

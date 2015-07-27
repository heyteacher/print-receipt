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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.printreceipt.AppException;
import org.printreceipt.Messages;
import org.printreceipt.datamodel.User;
import org.printreceipt.model.ModelFactory;
import org.printreceipt.model.interfaces.CashManager;
import org.printreceipt.model.interfaces.ECBManager;
import org.printreceipt.model.interfaces.ReceiptManager;
import org.printreceipt.model.interfaces.UserManager;
import org.printreceipt.utils.Config;

public class HsqlModelFactory implements ModelFactory {

	private static Connection connection;
	private static String dbname = Config.getLocalObjRepositoryPath()
			+ "/ecb_hsql";

	public static User USER = new User(Messages.getString("User"), "", "",
			"", new BigInteger("1"));

	public static File getDBNameFile() {
		return new File(dbname + ".script");
	}

	public static Connection getConnection() throws SQLException,
			ClassNotFoundException {
		if (connection == null || connection.isClosed()) {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:file:"
					+ dbname + ";ifexists=true");
			Statement statement = connection.createStatement();
			statement.execute("SET FILES WRITE DELAY FALSE");
			statement.close();
		}
		return connection;
	}

	public static void shutdown() throws SQLException, ClassNotFoundException {

		Statement statement = getConnection().createStatement();
		statement.execute("SHUTDOWN SCRIPT");
		statement.close();
		connection.close();
	}

	public static void rollback() throws AppException {
		try {
			HsqlModelFactory.getConnection().rollback();
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	public static void commit() throws AppException {
		try {
			HsqlModelFactory.getConnection().commit();
			HsqlModelFactory.getConnection().setAutoCommit(true);
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	public static void setAutoCommit(Boolean autoCommit) throws AppException {
		try {
			HsqlModelFactory.getConnection().setAutoCommit(autoCommit);
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	public static BigInteger lastIdentity() throws SQLException,
			ClassNotFoundException {
		PreparedStatement pstm = HsqlModelFactory.getConnection()
				.prepareStatement("CALL IDENTITY()");
		ResultSet resultset = pstm.executeQuery();
		resultset.next();
		int identity = resultset.getInt(1);
		resultset.close();
		pstm.close();
		return BigInteger.valueOf(identity);
	}

	static {
		init();
	}

	@Override
	public CashManager getCashManager() {
		return new HsqlCashManagerImpl();
	}

	@Override
	public ECBManager getECBManager() {
		return new HsqlECBManagerImpl();
	}

	@Override
	public ReceiptManager getReceiptManager() {
		return new HsqlReceiptManagerImpl();
	}

	@Override
	public UserManager getUserManager() {
		return new HsqlUserManagerImpl();
	}

	public static void init() {
		try {
			File new_db = new File(dbname + ".script.new");
			if (new_db.exists()) {
				System.out.println("imported file? "
						+ new_db.renameTo(new File(dbname + ".script")));
			}
			boolean db_exists = new File(dbname + ".script").exists();
			Connection connection = DriverManager
					.getConnection("jdbc:hsqldb:file:" + dbname);
			Statement statement = connection.createStatement();

			if (!db_exists) {

				statement.executeUpdate("CREATE TABLE ecb_event ("
						+ "    			  id IDENTITY,"
						+ "    			  name varchar(50) NOT NULL,"
						+ "    			  description varchar(50) NOT NULL" + ")");

				statement.executeUpdate("CREATE TABLE ecb_eventday ("
						+ "    			  id IDENTITY,"
						+ "    			  start_date datetime NOT NULL,"
						+ "    			  end_date datetime NOT NULL,"
						+ "    			  event_id int NOT NULL," + ")");

				statement.executeUpdate("CREATE TABLE ecb_itemgroup ("
						+ "    			  id IDENTITY,"
						+ "    			  ord int NOT NULL,"
						+ "    			  name varchar(50) NOT NULL,"
						+ "    			  event_id int NOT NULL," + ")");

				statement.executeUpdate("CREATE TABLE ecb_item ("
						+ "    			  id IDENTITY,"
						+ "    			  name varchar(50) NOT NULL,"
						+ "    			  price double NOT NULL,"
						+ "    			  item_group_id int NOT NULL,"
						+ "    			  ord int NOT NULL,"
						+ "    			  hide boolean DEFAULT TRUE NOT NULL," + ")");

				statement.executeUpdate("CREATE TABLE ecb_cashoperation ("
						+ "    			  id IDENTITY,"
						+ "    			  note varchar(500) NOT NULL,"
						+ "    			  reg_date datetime NOT NULL,"
						+ "    			  amount double NOT NULL,"
						+ "    			  user_id int NOT NULL,"
						+ "    			  event_id int NOT NULL," + ")");

				statement.executeUpdate("CREATE TABLE ecb_receipt ("
						+ "    			  id IDENTITY,"
						+ "    			  event_id int NOT NULL,"
						+ "    			  reg_date datetime NOT NULL,"
						+ "    			  note varchar(500) DEFAULT NULL,"
						+ "    			  user_id int NOT NULL," + ")");

				statement.executeUpdate("CREATE TABLE ecb_receiptitem ("
						+ "    			  id IDENTITY,"
						+ "    			  item_id int NOT NULL,"
						+ "    			  receipt_id int NOT NULL,"
						+ "    			  quantity int NOT NULL," + ")");

				statement.executeUpdate("ALTER TABLE ecb_receiptitem "
						+ "					ADD FOREIGN KEY (item_id) "
						+ "					REFERENCES  ecb_item (id) "
						+ "					ON DELETE RESTRICT ON UPDATE RESTRICT");

				statement.executeUpdate("ALTER TABLE ecb_receiptitem "
						+ "					ADD FOREIGN KEY ( receipt_id ) "
						+ "					REFERENCES  ecb_receipt (id) "
						+ "					ON DELETE CASCADE ON UPDATE RESTRICT ");

				statement.executeUpdate("INSERT INTO ecb_event "
						+ "(id, name, description) VALUES " + "('1', 'Print Receipt', 'Print Receipt')");

				// statement
				// .executeUpdate("INSERT INTO ecb_eventday "
				// + "(id, start_date, end_date, event_id) VALUES "
				// + "(1, '2012-01-01 00:00:00', '2013-01-01 00:00:00', 1)");

				statement.executeUpdate("INSERT INTO ecb_itemgroup "
						+ "(id, ord, name, event_id) VALUES "
						+ "	(1, 1, '" + Messages.getString("Stand") + " 1', 1)");

				statement
						.executeUpdate("INSERT INTO ecb_item "
								+ "	(id, name, price, item_group_id, ord, hide) VALUES "
								+ "	(1, '" + Messages.getString("Product") + " 1', 12.2, 1, 1, 0)");

				statement
						.executeUpdate("INSERT INTO ecb_item "
								+ "	(id, name, price, item_group_id, ord, hide) VALUES "
								+ "	(2, '" + Messages.getString("Product") + " 2', 13.5, 1, 2, 0)");

				statement
						.executeUpdate("INSERT INTO ecb_item "
								+ "	(id, name, price, item_group_id, ord, hide) VALUES "
								+ "	(3, '" + Messages.getString("Product") + " 3', 22.1, 1, 3, 0)");
			}
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
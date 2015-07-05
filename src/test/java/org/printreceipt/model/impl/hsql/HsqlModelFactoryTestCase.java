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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.junit.Before;

public abstract class HsqlModelFactoryTestCase extends TestCase {

	@Before
	public void setUp() throws Exception {
	}

	public void notestDBCreation() throws SQLException, ClassNotFoundException {
		try {
			Connection connection = HsqlModelFactory.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("INSERT INTO ecb_itemgroup (ord, name, event_id) VALUES (1,'ciao', 1)");
			statement.execute();
			statement.close();

			PreparedStatement preparedStatement = connection
					.prepareStatement("select * from ecb_itemgroup");
			ResultSet resultset = preparedStatement.executeQuery();
			int count = 0;
			while (resultset.next()) {
				count++;
				int ord = resultset.getInt("ord");
				int id = resultset.getInt("id");
				String name = resultset.getString("name");
				int event_id = resultset.getInt("event_id");
				System.out.printf("id %d, name %s, ord %d, event %d\n",
						new Object[] { id, name, ord, event_id });
			}
			System.out.println("count " + count);

			statement = connection
					.prepareStatement("DELETE FROM ecb_itemgroup");
			statement.execute();
			statement.close();

			// connection.prepareStatement("SHUTDOWN").execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
}

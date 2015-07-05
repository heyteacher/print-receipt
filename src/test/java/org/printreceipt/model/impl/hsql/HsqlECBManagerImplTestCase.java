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

import junit.framework.TestCase;

import org.printreceipt.AppException;
import org.printreceipt.datamodel.Event;
import org.printreceipt.datamodel.EventStatistics;
import org.printreceipt.datamodel.User;

public class HsqlECBManagerImplTestCase extends TestCase {

	private HsqlECBManagerImpl hsqlECBManagerImpl;

	protected void setUp() throws Exception {
		super.setUp();
		hsqlECBManagerImpl = new HsqlECBManagerImpl();
		System.out.println("START -------------" + this.getClass().getName()
				+ "-------------");
	}

	public void testEventStatistics() {
		try {
			User user = new HsqlUserManagerImpl().autenticate("", "");
			Event event = new HsqlECBManagerImpl().getEvent(user);

			EventStatistics eventStatistics = hsqlECBManagerImpl
					.getEventStatistics(event, user);
			System.out.println("getEventStatistics(event, user) id created: "
					+ eventStatistics.getAmount());
		} catch (AppException e) {
			e.printStackTrace();
			fail(this.getClass().getName());
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		System.out.println("END  -------------" + this.getClass().getName()
				+ "-------------");
	}

}

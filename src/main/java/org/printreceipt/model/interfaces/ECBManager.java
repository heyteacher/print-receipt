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

import java.io.File;
import java.math.BigInteger;

import org.printreceipt.AppException;
import org.printreceipt.datamodel.Event;
import org.printreceipt.datamodel.EventDay;
import org.printreceipt.datamodel.EventStatistics;
import org.printreceipt.datamodel.Item;
import org.printreceipt.datamodel.ItemGroup;
import org.printreceipt.datamodel.User;

public interface ECBManager {

	public abstract Event getEvent(User user) throws AppException;

	public abstract Item[] getItems(Event event, User user, boolean showHidden)
			throws AppException;

	public abstract ItemGroup[] getItemGroups(Event event, User user)
			throws AppException;

	public EventStatistics getEventStatistics(Event event, User user)
			throws AppException;

	public EventStatistics getEventDayStatistics(EventDay eventDay, User user)
			throws AppException;

	public void save(Event event, EventDay[] eventDays, ItemGroup[] itemGroups,
			Item[] items, User user) throws AppException;

	public void deleteEvent() throws AppException;

	public File exportEvent() throws AppException;

	public void importEvent(File file) throws AppException;

	public BigInteger createItemGroup(ItemGroup itemGroup) throws AppException;

	public void cleanEvent() throws AppException;

}
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

import java.util.Vector;

import org.printreceipt.datamodel.EventDay;

public class WrapEventDay {

	EventDay eventDay;

	public EventDay getEventDay() {
		return eventDay;
	}

	public WrapEventDay(EventDay eventDay) {
		this.eventDay = eventDay;
	}

	@Override
	public String toString() {
		if (eventDay != null) {
			return Config.formatDay(eventDay.getStart_date());
		}
		return "";
	}

	public static Vector<WrapEventDay> getWrapEventDays(EventDay[] eventDays) {
		Vector<WrapEventDay> wrapEventDays = new Vector<WrapEventDay>();
		for (int i = 0; i < eventDays.length; i++) {
			wrapEventDays.add(new WrapEventDay(eventDays[i]));
		}
		return wrapEventDays;
	}
}

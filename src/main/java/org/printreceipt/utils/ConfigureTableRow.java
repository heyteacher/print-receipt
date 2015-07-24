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


public class ConfigureTableRow {

	private long key;
	private Object[] values;

	public ConfigureTableRow(int columnsCount) {
		this.key = Math.round(Math.random() * -1000000);
		this.values = new Object[columnsCount];
	}

	public ConfigureTableRow(long key, Object[] values) {
		this.key = key;
		this.values = values;
	}

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public Object[] getValues() {
		return values;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}

	public void setValue(int pos, Object value) {
		this.values[pos] = value;
	}

	public Object getValue(int pos) {
		if (this.values == null || pos >= values.length) {
			return null;
		}
		return this.values[pos];
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("key=" + getKey());
		for (int i = 0; i < getValues().length; i++) {
			sb.append(", [" + i + "]=" + getValue(i));
		}
		return sb.toString();

	}
}
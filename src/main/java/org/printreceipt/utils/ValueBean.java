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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Constructor;

import org.printreceipt.AppException;

public class ValueBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8952189867627352685L;

	private Object id;
	private String descr;

	public ValueBean(Object id, String descr) {
		this.setId(id);
		this.setDescr(descr);
	}

	public ValueBean(Object value) throws AppException {
		try {
			if (value != null) {

				if (value instanceof Integer) {
					this.id = value;
					return;
				}

				BeanInfo info = Introspector.getBeanInfo(value.getClass());
				for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
					if ("id".equals(pd.getName())) {
						this.setId(pd.getReadMethod().invoke(value));
					}
					if ("descr".equals(pd.getName())) {
						this.setDescr((String) pd.getReadMethod().invoke(value));
					}
				}
			}
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	public Object getObject(Class<?> cls) throws AppException {
		try {
			if (Integer.class.equals(cls)) {
				return getId();
			}
			Constructor<?> constructor = cls.getConstructor(new Class[] {});
			Object value = constructor.newInstance();
			BeanInfo info = Introspector.getBeanInfo(cls);
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				if ("id".equals(pd.getName())) {
					pd.getWriteMethod().invoke(value, getId());
				}
				if ("descr".equals(pd.getName())) {
					pd.getWriteMethod().invoke(value, getDescr());
				}
			}
			return value;

		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj instanceof ValueBean) {
			Object aId = ((ValueBean) obj).getId();
			if (id == null && aId == null)
				return true;
			if (id != null && aId == null)
				return false;
			if (id == null && aId != null)
				return false;

			boolean ret = obj != null && id.equals(aId);
			return ret;
		} else {
			return false;
		}
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getId() {
		return id;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getDescr() {
		return descr;
	}

	public String toString() {
		return getDescr();
	}
}
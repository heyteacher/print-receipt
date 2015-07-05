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

import java.io.Serializable;

import javax.print.PrintService;

import org.printreceipt.Messages;

public class WrapPrintService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1022796302689194396L;

	PrintService printService;

	public PrintService getPrintService() {
		return printService;
	}

	public WrapPrintService(PrintService printService) {
		this.printService = printService;
	}

	@Override
	public String toString() {
		if (printService != null && printService.getName() != null) {
			if (printService.getName().length() > 25) {
				return printService.getName().substring(0, 22) + "...";
			} else {
				return printService.getName();
			}
		}
		return Messages.getString("Select Printer");
	}

	public static WrapPrintService[] getWrapPrintServices(
			PrintService[] printServices) {
		WrapPrintService[] wrapPrintServices = new WrapPrintService[printServices.length + 1];
		for (int i = 0; i < printServices.length; i++) {
			wrapPrintServices[i] = new WrapPrintService(printServices[i]);
		}
		wrapPrintServices[printServices.length] = new WrapPrintService(null);
		return wrapPrintServices;
	}

	public Object getName() {
		if (printService != null) {
			return printService.getName();
		}
		return null;
	}
}

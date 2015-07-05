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


package org.printreceipt.print.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;

public class ReceiptPrintTest {

	public static void main(String[] args) throws FileNotFoundException,
			PrintException {
		FileInputStream textStream;
		textStream = new FileInputStream("/home/syd/Desktop/prova.txt");

		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		Doc mydoc = new SimpleDoc(textStream, flavor, null);
		HashPrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		PrintService[] services = PrintServiceLookup.lookupPrintServices(
				flavor, aset);
		PrintService defaultService = PrintServiceLookup
				.lookupDefaultPrintService();

		if (services.length == 0) {
			if (defaultService == null) {
				// no printer found

			} else {
				// print using default
				DocPrintJob job = defaultService.createPrintJob();
				job.print(mydoc, aset);

			}

		} else {

			// built in UI for printing you may not use this
			PrintService service = ServiceUI.printDialog(null, 200, 200,
					services, defaultService, flavor, aset);

			if (service != null) {
				DocPrintJob job = service.createPrintJob();
				job.print(mydoc, aset);
			}

		}
	}

}
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

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobSheets;
import javax.print.attribute.standard.MediaName;

public class TicketPrintPage implements Printable {

	private String[] ticket;

	public TicketPrintPage(String[] f) {
		ticket = f;
	}

	public int print(Graphics g, PageFormat pf, int pageIndex)
			throws PrinterException {
		int interline = 12;
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Courier", Font.PLAIN, 12));
		int x = (int) pf.getImageableX();
		int y = (int) pf.getImageableY();
		System.out.println(x + " - " + y);
		String s;
		for (int i = 0; i < ticket.length; i++) {
			s = ticket[i];
			y += interline;
			g2.drawString(s, x, y);
		}

		return Printable.PAGE_EXISTS;
	}

	public static void printTicketFile(String[] ticket) throws PrinterException {
		PrinterJob pjob = PrinterJob.getPrinterJob();
		// get printer using and looking at the name
		pjob.setPrintService(PrintServiceLookup.lookupPrintServices(null, null)[0]);
		// job title
		pjob.setJobName("Ticket");

		// page fomat
		PageFormat pf = pjob.defaultPage();
		// landscape or portrait
		pf.setOrientation(PageFormat.PORTRAIT);
		// Paper properties
		Paper a4Paper = new Paper();
		double paperWidth = 3.13;
		double paperHeight = 11.69;
		double margin = 0;
		a4Paper.setSize(paperWidth * 72.0, paperHeight * 72.0);
		a4Paper.setImageableArea(margin,
		// 0,
				margin,
				// 0,
				a4Paper.getWidth() - 2 * margin,
				// a4Paper.getWidth(),
				a4Paper.getHeight() - 2 * margin
		// a4Paper.getHeight()
		); // no margin = no scaling
		pf.setPaper(a4Paper);
		// Custom class that defines how to layout file text
		TicketPrintPage pages = new TicketPrintPage(ticket);
		// adding the page to a book
		Book book = new Book();
		book.append(pages, pf);
		// Adding the book to a printjob
		pjob.setPageable(book);
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

		// No jobsheet (banner page, the page with user name, job name, date and
		// whatnot)
		pras.add(JobSheets.STANDARD);
		pras.add(MediaName.ISO_A4_TRANSPARENT);
		// Printing
		pjob.print(pras);
	}

	public static void main(String[] args) throws PrinterException {

		String[] tickets = { "prova prova ", "prova prova", "aaaaaaaaaaaaaa",
				"ddddddddddddd" };
		TicketPrintPage.printTicketFile(tickets);
	}
}

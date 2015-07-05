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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.printreceipt.Application;
import org.printreceipt.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

	private static Logger log = LoggerFactory.getLogger(Config.class);

	public static int RECEIPT_COUNT_ROTATION = 500;

	/**
	 * Color
	 */
	public static final Color FG_COLOR = new Color(106, 106, 90);
	public static final Color BG_COLOR = new Color(204, 204, 153);

	public static final Color FG_ALARM_COLOR = new Color(204, 51, 51);

	public static final Color FG_TITLE_COLOR = new Color(0, 0, 102);

	public static final Color HEADER_COLOR = new Color(0, 0, 102);
	public static final Color BG_2_COLOR = new Color(255, 255, 153);
	public static final Color HEADER2_COLOR = new Color(151, 142, 67);

	/**
	 * Font
	 */
	public static final float HEADER_FONT_SIZE = 20;
	public static final float TEXT_FONT_SIZE = 15;
	public static final int SPACE = 5;

	private static String localObjRepositoryPath;
	private static String localIconRepositoryPath;

	private static NumberFormat integerFormat = NumberFormat
			.getNumberInstance();
	private static NumberFormat currencyFormat = NumberFormat
			.getCurrencyInstance();
	private static NumberFormat floatFormat = NumberFormat
			.getNumberInstance();

	private static DateFormat sdfDay = new SimpleDateFormat("dd/MM/yyyy");
	private static DateFormat sdfDayMR = new SimpleDateFormat("dd_MM_yyyy");
	private static DateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");

	static {
		integerFormat.setMaximumFractionDigits(0);
		currencyFormat.setMinimumFractionDigits(2);
		//currencyFormat.setCurrency(Currency.);
		floatFormat.setMinimumFractionDigits(2);
	}

	public static NumberFormat getIntegerFormat() {
		return integerFormat;
	}

	private static NumberFormat getCurrencyFormat() {
		return currencyFormat;
	}

	public static NumberFormat getFloatFormat() {
		return floatFormat;
	}

	public static String currencyFormat(Object value) {
		if (value == null)
			value = 0f;
		return getCurrencyFormat().format(value);
	}
	
	public static String currencyFormat(Float value) {
		if (value == null)
			value = 0f;
		return getCurrencyFormat().format(value);
	}

	public static String currencyFormat(Double value) {
		if (value == null)
			value = 0d;
		return getCurrencyFormat().format(value);
	}

	public static String format(Integer value) {
		if (value == null)
			value = 0;
		return getIntegerFormat().format(value);
	}

	public static String formatInt(Double value) {
		if (value == null)
			value = 0d;
		return getIntegerFormat().format(value);
	}

	public static String format(Calendar date) {
		if (date == null)
			return "";
		return sdf.format(date.getTime());
	}

	public static String formatDay(Calendar date) {
		if (date == null)
			return "";
		return sdfDay.format(date.getTime());
	}

	public static Calendar parsetDay(String text) {
		try {
			if (text == null) {
				return null;
			}
			Date date = sdfDay.parse(text);
			if (date != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				return calendar;
			}
		} catch (ParseException e) {
			log.error("parseDay(" + text + ")", e);
		}
		return null;
	}

	public static String formatDayMachineReadable(Calendar date) {
		if (date == null) {
			return "";
		}
		return sdfDayMR.format(date.getTime());
	}

	/**
	 * start up operation
	 */
	static {
		try {
			integerFormat.setMaximumFractionDigits(0);
			currencyFormat.setMinimumFractionDigits(2);
			currencyFormat.setCurrency(Currency.getInstance(Locale.getDefault()));

			String fs = System.getProperty("file.separator");
			String persistPath = System.getProperty("user.home") + fs + ".ecb"
					+ fs;
			localObjRepositoryPath = persistPath + fs + "obj";
			String localIconRepositoryDir = persistPath + fs + "icons";

			File persistentObjPathFile = new File(localObjRepositoryPath);
			if (!persistentObjPathFile.exists()) {
				persistentObjPathFile.mkdirs();
			}
			File persistentIconPathFile = new File(localIconRepositoryDir);
			if (!persistentIconPathFile.exists()) {
				persistentIconPathFile.mkdirs();
			}
			InputStream is = Config.class.getResourceAsStream("/icon.png");
			byte imageByteArray[] = new byte[is.available()];
			is.read(imageByteArray);
			localIconRepositoryPath = localIconRepositoryDir.concat(fs).concat(
					"icon.png");
			FileOutputStream fos = new FileOutputStream(localIconRepositoryPath);
			fos.write(imageByteArray);
			fos.flush();
			fos.close();
			is.close();

//			log.debug("persistPath: obj {} icons {} created",
//					persistentObjPathFile.getAbsoluteFile(),
//					persistentIconPathFile.getAbsoluteFile());
		} catch (Exception e) {
			log.error("error ConfigController static", e);
		}
	}

	public static void setBorder(JComponent panel) {
		Border emptyBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
		Border lineBorder = BorderFactory
				.createLineBorder(Config.BG_2_COLOR, 1);
		Border border1 = BorderFactory.createCompoundBorder(emptyBorder,
				lineBorder);
		Border border = BorderFactory
				.createCompoundBorder(border1, emptyBorder);
		panel.setBorder(border);
	}

	public static void checkForGarbageCollection() {

		long heapMaxSize = Runtime.getRuntime().maxMemory();
		long heapFreeSize = Runtime.getRuntime().freeMemory();
		long heapTotalSize = Runtime.getRuntime().totalMemory();

		long percentageUsed = (heapFreeSize * 100) / heapMaxSize;

		try {
			if (percentageUsed < 20) {
				log.info(
						"Free heap BEFORE gc: {}% (free {}, total {}, max {})",
						new Object[] { percentageUsed, heapFreeSize,
								heapTotalSize, heapMaxSize });
				System.gc();
				log.info(
						"Free AFTER BEFORE gc: {}% (free {}, total {}, max {})",
						new Object[] { percentageUsed, heapFreeSize,
								heapTotalSize, heapMaxSize });
			}
		} catch (Exception e) {
			log.error("checkForGarbageCollection:", e);
		}

	}

	/**
	 * @param body
	 * @param jobName
	 * @throws PrinterException
	 */
	public static void print(String body, String jobName,
			Application application) throws PrinterException {

		WrapPrintService wrapPrintService = (WrapPrintService) application
				.getMenuPanel().getPrintersCombobox().getSelectedItem();
		
		JTextPane textPane = new JTextPane();
		textPane.setContentType("text/html");
		textPane.setText(body);

		HashPrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
		attributes.add(new MediaPrintableArea(0, 0, 600, 800,
				MediaPrintableArea.MM));
		attributes.add(MediaName.ISO_A4_TRANSPARENT);
		attributes.add(new JobName(jobName, Locale.getDefault()));
		MessageFormat header = new MessageFormat("");
		MessageFormat footer = new MessageFormat("");
		textPane.print(header, footer, false,
				wrapPrintService.getPrintService(), attributes, false);
	}

	public static String getLocalObjRepositoryPath() {
		return localObjRepositoryPath;
	}

	public static String getLocalIconRepositoryPath() {
		return localIconRepositoryPath;
	}

	public static Border createEmptyBorder() {
		return new EmptyBorder(Config.SPACE, Config.SPACE, Config.SPACE,
				Config.SPACE);
	}

	public static Border createLineBorder() {
		return new LineBorder(FG_COLOR);
	}

	public static Component createRigidArea() {
		return Box.createRigidArea(new Dimension(Config.SPACE, Config.SPACE));
	}

	public static JPanel createLabelHeader(String title, boolean addBorder) {
		return createLabel(new JLabel(Messages.getString(title)),
				Config.HEADER_FONT_SIZE, JLabel.CENTER, Config.BG_2_COLOR,
				Config.HEADER2_COLOR, addBorder);
	}

	public static JPanel createLabelSubHeader(String title) {
		return createLabel(new JLabel(Messages.getString(title)),
				Config.HEADER_FONT_SIZE, JLabel.CENTER, Config.HEADER_COLOR,
				Config.BG_COLOR, true);
	}

	/**
	 * 
	 * @param label
	 * @return
	 */
	public static JPanel createLabelBody(String label) {
		return createLabel(new JLabel(label), Config.TEXT_FONT_SIZE,
				JLabel.CENTER, null, null, true);
	}

	/**
	 * @param label
	 *            the label
	 * @param size
	 *            the font size
	 * @param align
	 *            the horizontal alignment
	 * @param foregroundColor
	 *            the foreground color
	 * @param backgroundColor
	 *            the foreground color
	 * @return
	 */
	public static JPanel createLabel(JLabel label, float size, int align,
			Color foregroundColor, Color backgroundColor, boolean addBorder) {

		if (foregroundColor != null) {
			label.setForeground(foregroundColor);
		}

		Font font = label.getFont();
		label.setFont(font.deriveFont(size));
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
		panel.setLayout(layout);
		if (backgroundColor != null) {
			panel.setBackground(backgroundColor);
		}
		if (addBorder)
			Config.setBorder(panel);

		if (align == JLabel.CENTER || align == JLabel.RIGHT) {
			panel.add(Box.createHorizontalGlue());
		}

		panel.add(label);

		if (align == JLabel.CENTER) {
			panel.add(Box.createHorizontalGlue());
		}
		panel.setMaximumSize(new Dimension(300, 50));
		return panel;
	}

}

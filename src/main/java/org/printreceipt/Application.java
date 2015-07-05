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


package org.printreceipt;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.BorderUIResource.LineBorderUIResource;

import org.printreceipt.datamodel.Event;
import org.printreceipt.datamodel.User;
import org.printreceipt.utils.Config;
import org.printreceipt.view.MenuView;
import org.printreceipt.view.ReceiptView;
import org.printreceipt.view.SummaryView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application extends JFrame {

	private static final long serialVersionUID = -1136176013469311135L;

	private static Logger log = LoggerFactory.getLogger(Application.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	private User user;

	private Event event;

	public static final int APPLICATION_HEIGHT = 600;
	public static final int APPLICATION_WIDTH = 800;

	private static final String TABBED_PANEL = "tabbed";

	JPanel cardPanel;

	JTabbedPane tabbedPane;
	private ReceiptView receiptPanel;
	private MenuView itemsPanel;
	private SummaryView summaryPanel;

	private JPanel messagePanel;
	private JLabel messageLabel = new JLabel();

	static {

		UIManager.put("Button.background", Config.BG_2_COLOR);
		UIManager.put("Button.foreground", Config.HEADER_COLOR);

		UIManager.put("CheckBox.background", Config.BG_COLOR);
		UIManager.put("CheckBox.disabledText", Config.FG_COLOR);
		UIManager.put("CheckBoxMenuItem.disabledForeground", Config.FG_COLOR);

		UIManager.put("ComboBox.backgroud", Config.BG_COLOR);
		UIManager.put("ComboBox.foreground", Config.FG_COLOR);
		UIManager.put("ComboBox.selected", Config.BG_2_COLOR);
		UIManager.put("ComboBox.disabledBackground", Color.WHITE);
		UIManager.put("ComboBox.disabledForeground", Config.FG_COLOR);

		UIManager.put("FormattedTextField.inactiveForeground", Config.FG_COLOR);
		UIManager.put("FormattedTextField.inactiveBackground", Color.WHITE);

		UIManager.put("Label.disabledShadow", Config.FG_COLOR);
		UIManager.put("Label.disabledForeground", Config.FG_COLOR);

		UIManager.put("OptionPane.background", Config.BG_COLOR);

		UIManager.put("Panel.background", Config.BG_COLOR);

		UIManager.put("ProgressBar.background", Config.BG_COLOR);
		UIManager.put("ProgressBar.foreground", Config.BG_2_COLOR);

		UIManager.put("RadioButton.disabledText", Config.FG_COLOR);

		UIManager.put("ScrollBar.shadow", Config.BG_COLOR);
		UIManager.put("ScrollBar.thumb", Config.BG_COLOR);
		UIManager.put("ScrollBar.highlight", Config.BG_COLOR);
		UIManager.put("ScrollBar.track", Config.BG_COLOR);
		UIManager.put("ScrollBar.trackHighlight", Config.BG_COLOR);

		UIManager.put("ScrollBar.background", Config.BG_COLOR);
		UIManager.put("Slider.background", Config.BG_COLOR);

		UIManager.put("TableHeader.background", Config.BG_COLOR);
		UIManager.put("TableHeader.foreground", Config.HEADER_COLOR);
		Font font = UIManager.getFont("TableHeader.font");
		UIManager.put("TableHeader.font",
				font.deriveFont(Font.BOLD, font.getSize()));

		// UIManager.put("Table.background", Config.BG_COLOR);
		UIManager.put("Table.selectionBackground", Config.BG_2_COLOR);

		LineBorderUIResource lineBorder = new LineBorderUIResource(
				Config.HEADER2_COLOR);

		UIManager.put("TabbedPane.background", Config.HEADER2_COLOR);
		UIManager.put("TabbedPane.foreground", Config.HEADER_COLOR);
		UIManager.put("TabbedPane.selected", Config.BG_2_COLOR);
		UIManager.put("TabbedPane.focus", Config.BG_2_COLOR);
		UIManager.put("TabbedPane.borderHightlightColor", Config.BG_COLOR);
		UIManager.put("TabbedPane.contentAreaColor", Config.BG_COLOR);
		UIManager.put("TabbedPane.selectHighlight", Config.BG_COLOR);

		UIManager.put("TabbedPane.shadow", Config.FG_COLOR);

		font = UIManager.getFont("TabbedPane.font");
		UIManager.put("TabbedPane.font",
				font.deriveFont(Config.HEADER_FONT_SIZE));

		UIManager.put("TitledBorder.titleColor", Config.HEADER_COLOR);
		UIManager.put("TitledBorder.border", lineBorder);

		UIManager.put("TextArea.inactiveForeground", Config.FG_COLOR);
		UIManager.put("TextPane.inactiveForeground", Config.FG_COLOR);
		UIManager.put("TextField.inactiveForeground", Config.FG_COLOR);
		UIManager.put("TextField.inactiveBackground", Color.WHITE);

		UIManager.put("Viewport.background", Config.BG_COLOR);
	}

	public Application() throws AppException {

		super(Messages.getString("Print Receipt"));

		ImageIcon icon = new ImageIcon(getClass().getResource("/icon.png"));
		setIconImage(icon.getImage());

		tabbedPane = new JTabbedPane();

		//statusPanel = new StatusView(this);

		receiptPanel = new ReceiptView(this);
		itemsPanel = new MenuView(this);
		summaryPanel = new SummaryView(this);

		tabbedPane.addTab(Messages.getString("Receipts"), receiptPanel);
		tabbedPane.addTab(Messages.getString("Summary"), summaryPanel);
		tabbedPane.addTab(Messages.getString("Price List"), itemsPanel);

		// statusPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		cardPanel = new JPanel(new CardLayout());
		messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		Config.setBorder(messagePanel);
		messagePanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		messagePanel.add(messageLabel, BorderLayout.CENTER);
		cardPanel.add(tabbedPane, TABBED_PANEL);

		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					switch (tabbedPane.getSelectedIndex()) {
					case 0:
						gotoReceit();
						break;
					case 1:
						gotoSummary();
						break;
					case 2:
						gotoItems();
						break;
					default:
						throw new AppException("invalid index "
								+ tabbedPane.getSelectedIndex());
					}
				} catch (AppException e1) {
					showError(e1);
					log.error("stateChanged", e);
				}
			}
		});

		// if (ConfigController.isStandalone()) {
		User user = new User();
		user.setId(new BigInteger("1"));
		user.setUsername("mono");
		this.setUser(user);
		gotoReceit();
		goTo(TABBED_PANEL);

		// }
		// else if
		// (ConfigController.getServerUrl().getHost().equals("localhost") &&
		// ConfigController.getServerUrl().getPort() == 8000 ) {
		// User user = new User();
		// user.setId(new BigInteger("3"));
		// user.setUsername("cassacena");
		// user.setPassword("cassacena");
		// this.setUser(user);
		// gotoReceit();
		// goTo(TABBED_PANEL);
		// }

		// cardPanel.setBorder(ComponentFactory.createMicroBorder());

		getContentPane().setLayout(new BorderLayout());
		// getContentPane().add(statusPanel, BorderLayout.NORTH);
		getContentPane().add(cardPanel, BorderLayout.CENTER);
		getContentPane().add(messagePanel, BorderLayout.SOUTH);

		if (this.getEvent() != null
				&& (this.getEvent().getName() == null || "".equals(this
						.getEvent().getName().trim()))) {
			this.gotoItems();
		}

		// Display the window.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT));
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		// this.setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH);
	}

	public void showError(String message) {
		showError(message, "");
	}

	public void showError(String message, Object value) {
		String val = value != "" ? ": " + value : "";
		messageLabel.setForeground(Config.FG_ALARM_COLOR);
		JOptionPane.showMessageDialog(this, Messages.getString(message) + val,
				Messages.getString("Error"), JOptionPane.ERROR_MESSAGE);
		messageLabel.setText(sdf.format(new Date()) + " - "
				+ Messages.getString(message) + val);
	}

	public void showError(Throwable t) {
		showError(t.getMessage());
		t.printStackTrace();
	}

	public void showWarning(String message) {
		JOptionPane.showMessageDialog(this, Messages.getString("Warning")
				+ ": " + Messages.getString(message),
				Messages.getString("Warning"), JOptionPane.WARNING_MESSAGE);
	}

	public void showInfo(String message) {
		showInfo(message, null);
	}

	public void showInfo(String message, Object value) {
		if (message != null) {
			String val = value != null ? ": " + value : "";
			messageLabel.setForeground(Config.FG_COLOR);
			messageLabel.setText(sdf.format(new Date()) + " - "
					+ message + val);
		} else {
			messageLabel.setText("");
		}
	}

	private void goTo(String panel) {
		CardLayout cl = (CardLayout) (cardPanel.getLayout());
		cl.show(cardPanel, panel);
	}

	public void gotoReceit() throws AppException {
		receiptPanel.initialize();
		tabbedPane.setSelectedComponent(receiptPanel);
	}

	public void gotoItems() throws AppException {
		itemsPanel.initialize();
		tabbedPane.setSelectedComponent(itemsPanel);
	}

	public void gotoSummary() throws AppException {
		summaryPanel.initialize();
		tabbedPane.setSelectedComponent(summaryPanel);
	}

	public void setUser(User user) throws AppException {
		try {
			if (user == null) {
				throw new Exception("user must be not null");
			}

			this.user = user;
			// statusPanel.setUser(user);
			receiptPanel.initialize();

			// if (ConfigController.isOnline()) {
			// LocalController.sendOfflineReceipts(user);
			// }

		} catch (Exception e) {
			showError(e.getMessage());
			this.user = null;
			// statusPanel.setUser(null);
		}
	}

	public User getUser() {
		return user;
	}

	public void setEvent(Event event) throws AppException {
		this.event = event;
		// getStatusPanel().setEvent(event);
	}

	public Event getEvent() {
		return event;
	}

	public static void reboot() throws AppException {
		app.dispose();
		System.gc();
		app = new Application();
	}

	private static Application app = null;

	public static void main(String[] args) throws AppException {
		app = new Application();
	}

	public boolean isAuthenticated() {
		return getUser() != null;
	}

	public MenuView getMenuPanel() {
		return itemsPanel;
	}
}
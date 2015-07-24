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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.printreceipt.AppException;
import org.printreceipt.datamodel.CashOperation;
import org.printreceipt.datamodel.Event;
import org.printreceipt.datamodel.Item;
import org.printreceipt.datamodel.Receipt;
import org.printreceipt.datamodel.User;
import org.printreceipt.model.ModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The controller of local repository, where are store the object in off-line
 * execution. Offers store, remove listing, and operations for specified object
 * into local repository.
 * 
 * @author syd
 * 
 */
public class LocalStorage {

	// private static final String DIRTIES_PATH = "Dirty";
	private static Logger log = LoggerFactory.getLogger(LocalStorage.class);

	/**
	 * load a event from local repository
	 * 
	 * @return the event stored
	 * @throws AppException
	 */
	public static Event loadEvent() throws AppException {
		return (Event) loadSingle(Event.class.getSimpleName(), 0L);
	}

	/**
	 * store the event
	 * 
	 * @param the
	 *            event to store
	 * @throws AppException
	 *             if an error occurs
	 */
	public static void storeEvent(Event event) throws AppException {
		remove(Event.class.getSimpleName());
		store(Event.class.getSimpleName(), new Long(0), event);
	}

	/**
	 * Load the receipts from local repository
	 * 
	 * @param max_loads
	 *            the maximum count of receipt to load
	 * @return the recepits
	 * @throws AppException
	 */
	public static List<Object> loadReceipts(int max_loads) throws AppException {
		return loadList(Receipt.class.getSimpleName(), max_loads);
	}

	public static BigInteger storeReceipt(Receipt receipt) throws AppException {
		if (receipt.getId() == null) {

			BigInteger id = BigInteger.valueOf(new Date().getTime() * -1);
			receipt.setId(id);
			String note = receipt.getNote() == null ? "" : receipt.getNote();
			receipt.setNote(note + ": offline " + new Date());
		}
		store(Receipt.class.getSimpleName(), receipt.getId().longValue(),
				receipt);
		return receipt.getId();
	}

	public static void deleteReceipt(BigInteger receiptId) throws AppException {
		remove(Receipt.class.getSimpleName(), receiptId.longValue());
	}

	/**
	 * load the items from local repository
	 * 
	 * @return the items from local repository
	 * @throws AppException
	 *             if an error occurs
	 */
	public static Item[] loadItems() throws AppException {

		List<Object> objects = loadList(Item.class.getSimpleName(), -1);
		TreeMap<BigInteger, Item> items = new TreeMap<BigInteger, Item>();

		for (Object object : objects) {
			Item item = (Item) object;
			BigInteger itemGroupOrd = item.getItem_group().getOrd()
					.multiply(BigInteger.valueOf(100l));
			BigInteger itemOrd = item.getOrd();

			items.put(itemGroupOrd.add(itemOrd), item);
		}

		Collection<Item> itemsCollection = items.values();
		List<Item> list = new ArrayList<Item>();
		for (Item item : itemsCollection) {
			list.add(item);
		}
		return (Item[]) list.toArray(new Item[] {});
	}

	/**
	 * store items in local repository
	 * 
	 * @param the
	 *            items
	 * @throws AppException
	 *             if an error occurs
	 */
	public static void storeItems(Item[] items) throws AppException {
		remove(Item.class.getSimpleName());
		for (Item item : items) {
			store(Item.class.getSimpleName(), item.getId(), item);
		}
	}

	/**
	 * load the item group counts from local store
	 * 
	 * @return the event stored
	 * @throws AppException
	 */
	public static Map<BigInteger, BigInteger> loadItemGroupCounts()
			throws AppException {
		return (Map<BigInteger, BigInteger>) loadSingle("itemGroupCounts", 0L);
	}

	/**
	 * store the item group counts into local store
	 * 
	 * @param the
	 *            receipt statistic to store
	 * @throws AppException
	 *             if an error occurs
	 */
	public static void storeItemGroupCounts(
			Map<BigInteger, BigInteger> itemGroupCounts) throws AppException {
		remove("itemGroupCounts");
		store("itemGroupCounts", new Long(0), itemGroupCounts);
	}

	/**
	 * Load the cash operation
	 * 
	 * @param max_loads
	 *            the maximum count of receipt to load
	 * @return the recepits
	 * @throws AppException
	 */
	public static List<Object> loadCashOperations() throws AppException {
		return loadList(CashOperation.class.getSimpleName(), -1);
	}

	/**
	 * store a cash operation
	 * 
	 * @param the
	 *            event to store
	 * @throws AppException
	 *             if an error occurs
	 */
	public static void storeCashOperation(CashOperation cashOperation)
			throws AppException {
		long id = new Date().getTime() * -1;
		cashOperation.setId(BigInteger.valueOf(id));
		store(CashOperation.class.getSimpleName(), id, cashOperation);
	}

	/**
	 * delete a cash operation
	 * 
	 * @param the
	 *            event to store
	 * @throws AppException
	 *             if an error occurs
	 */
	public static void deleteCashOperation(Long cashOperationId)
			throws AppException {
		remove(CashOperation.class.getSimpleName(), cashOperationId);
	}

	public static void storePrintServiceName(String wrapPrintServiceName)
			throws AppException {
		store(WrapPrintService.class.getSimpleName(), new Long(0),
				wrapPrintServiceName);
	}

	public static String loadPrintServiceName() throws AppException {
		return (String) loadSingle(WrapPrintService.class.getSimpleName(),
				new Long(0));
	}

	public static void storePaperSize(Integer paperSize) throws AppException{
		store("PaperSize", 0l, paperSize);
		
	}
	
	public static Integer loadPaperSize() throws AppException {
		return (Integer) loadSingle("PaperSize", 0l);
	}
	
	/**
	 * local authentication
	 * 
	 * @param login
	 *            user login
	 * @param pwd
	 *            the password
	 * @return the user object if login and password match a local user
	 * @throws TsbException
	 */
	public static User autenticate(String login, String pwd)
			throws AppException {

		List<Object> objects = loadList(User.class.getSimpleName(), -1);

		for (Object object : objects) {
			if (object instanceof User) {
				User usr = (User) object;
				if (usr.getUsername().equals(login)) {
					return usr;
				}
			}
		}
		log.info("user {} NOT autenticate", login);
		return null;
	}

	/**
	 * store the user object in local repository
	 * 
	 * @param usr
	 *            the usr object
	 * @throws TsbException
	 *             if an error occurs
	 */
	public static void storeUsr(User usr) throws AppException {
		if (usr == null)
			return;
		store(User.class.getSimpleName(), usr.getId().longValue(), usr);
	}

	/**
	 * the path in the local repository of object specified by id
	 * 
	 * @param relativePath
	 *            the relative path
	 * @param id
	 *            the identifier
	 * @return the path in the local repository of object specified by id
	 * @throws AppException
	 */
	private static String getLocalRepositoryObjPath(String relativePath, Long id)
			throws AppException {
		return getLocalRepositoryPath(relativePath, id) + ".obj";
	}

	/**
	 * the path in the local repository of object specified by id
	 * 
	 * @param relativePath
	 *            the relative path
	 * @param id
	 *            the identifier
	 * @return the path in the local repository of object specified by id
	 * @throws AppException
	 */
	private static String getLocalRepositoryPath(String relativePath, Long id)
			throws AppException {
		String persistPath = Config.getLocalObjRepositoryPath() + "/"
				+ relativePath;
		File persistPathFile = new File(persistPath);
		if (!persistPathFile.exists()) {
			if (!persistPathFile.mkdirs()) {
				throw new AppException("cannot create " + persistPathFile);
			}
		}
		if (id != null) {
			return persistPath + "/" + id;
		}
		return persistPath;
	}

	private static String getLocalRepositoryPath(String relativePath)
			throws AppException {
		return getLocalRepositoryPath(relativePath, null);
	}

	/**
	 * store a object in local repository
	 * 
	 * @param relativePath
	 *            the relative path where store the object
	 * @param id
	 *            the object identifier
	 * @param obj
	 *            the object
	 * @throws AppException
	 *             if an error occurs
	 */
	private static void store(String relativePath, Long id, Object obj)
			throws AppException {
		String path = getLocalRepositoryObjPath(relativePath, id);

		File file = new File(path);
		store(obj, file);
//		log.debug("relativePath: '{}' id '{}'", relativePath, id);

	}

	/**
	 * load the object store in local repository
	 * 
	 * @param relativePath
	 *            relative path where search the object
	 * @parm id the object identifier
	 * @return the object store in local repository
	 * @throws AppException
	 */
	private static Object loadSingle(String relativePath, Long id)
			throws AppException {
		String path = getLocalRepositoryObjPath(relativePath, id);
		File objectPath = new File(path);
		Object object = load(objectPath);
		return object;
	}

	/**
	 * load the objects store in local repository
	 * 
	 * @param relativePath
	 *            relative path where search the object
	 * @param max_loads
	 *            the max number of objects to load, if -1 load all objects
	 * @return the list of object store in relative path
	 * @throws AppException
	 */
	private static List<Object> loadList(String relativePath, int max_loads)
			throws AppException {
		File directoryPath = new File(getLocalRepositoryPath(relativePath));
		File[] files = directoryPath.listFiles();
		ArrayList<Object> objects = new ArrayList<Object>();

		max_loads = max_loads == -1 ? files.length : max_loads;

		// log.debug("directory path: '{}'", directoryPath);
		for (int i = 0; i < Math.min(files.length, max_loads); i++) {

			// skip directory and not obj files
			if (files[i].isDirectory() && !files[i].getName().endsWith(".obj")) {
				continue;
			}
			File file = files[i];
			Object obj = load(file);
			if (obj != null) {
				objects.add(obj);
//				log.debug("pos '{}' file '{}'", i, files[i]);
			} else {
//				log.debug("file '{}' skipped", files[i]);
			}
		}
		return objects;
	}

	/**
	 * load the objects store in local repository
	 * 
	 * @param relativePath
	 *            relative path where search the object
	 * @return the list of object store in relative path
	 * @throws AppException
	 */
	private static void remove(String relativePath) throws AppException {
		File directoryPath = new File(getLocalRepositoryPath(relativePath));
		File[] files = directoryPath.listFiles();

		for (int i = 0; i < files.length; i++) {

			// skip directory and not obj files
			if (files[i].isDirectory() && !files[i].getName().endsWith(".obj")) {
				continue;
			}
			File file = files[i];
			file.delete();
		}
	}

	/**
	 * remove object by path specified
	 * 
	 * @param path
	 */
	private static void removeByPath(String path) {
		File file = new File(path);
		file.delete();
	}

	private static Object load(File objectPath) throws AppException {
		Object object = null;

		try {
			FileInputStream fin = new FileInputStream(objectPath);
			ObjectInputStream ois = new ObjectInputStream(fin);
			object = ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			return null;
		} catch (InvalidClassException e) {
			log.error("oggetto locale imcompatibile", e);
			removeByPath(objectPath.getAbsolutePath());
		} catch (IOException e) {
			throw new AppException("io error in  file " + objectPath, e);
		} catch (ClassNotFoundException e) {
			log.error("oggetto locale non trovato", e);
			// throw new ECBException("io error in  file " + objectPath, e);
		}
		return object;
	}

	private static void store(Object obj, File file) throws AppException {
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(obj);
		} catch (FileNotFoundException e) {
			throw new AppException("file " + file + " not found!", e);
		} catch (IOException e) {
			throw new AppException("io error in  file " + file, e);
		} finally {
			try {
				fout.close();
			} catch (IOException e) {
			}
		}
	}

	private static boolean sendOfflineReceiptsRunning = false;

	/**
	 * send to the server and remove from local repository the receipts printed
	 * when the cash was offline
	 * 
	 * @param user
	 *            the user authenticated
	 * @return the
	 * @throws AppException
	 */
	public static int sendOfflineCachOperations(User user) throws AppException {
		try {
			if (user == null)
				return 0;
			if (sendOfflineReceiptsRunning) {
				log.info("sendOfflineReceipts already running");
			}
			int cashOperations = 0;
			for (Object cashOperationObject : loadCashOperations()) {
				CashOperation cashOperation = (CashOperation) cashOperationObject;
				long cashOperationId = cashOperation.getId().longValue();
				cashOperation.setId(null);
				ModelManager.getCashManager()
						.addCashAction(cashOperation, user);
				deleteCashOperation(cashOperationId);
				cashOperations++;
			}
			return cashOperations;
		} catch (Exception e) {
			log.error("sendOfflineCachOperations", e);
			throw new AppException(e);
		} finally {
			sendOfflineReceiptsRunning = false;
		}
	}

	// public static int sendOfflineReceipts(User user) throws ECBException {
	//
	// if (user == null) return 0;
	// if (sendOfflineReceiptsRunning) {
	// log.info("sendOfflineReceipts already running");
	// }
	// sendOfflineReceiptsRunning = true;
	// int MAX_LOADS = 20;
	// int receiptCount = 0;
	// int totalReceiptCount = 0;
	// int tries = 0;
	// try {
	// do {
	// tries++;
	// if (!ConfigController.isOnline()) {
	// throw new
	// ECBException("cannot send offline receipts. ECB not connected");
	// }
	// List<Object> receipts = loadList(Receipt.class.getSimpleName(),
	// MAX_LOADS);
	// receiptCount = receipts.size();
	// totalReceiptCount = totalReceiptCount + receiptCount;
	// //log.debug("load {} offline receipts", receiptCount);
	// for (Object object : receipts) {
	// Receipt receipt = (Receipt) object;
	// Long id = receipt.getId().longValue();
	// receipt.setId(null);
	// if (ConfigController.isOnline()) {
	// ModelManager.getReceiptManager().insertReceipt(receipt, user);
	// log.debug("receipt offline sent to the center: {}", id);
	// remove(Receipt.class.getSimpleName(), id);
	// }
	// }
	// } while (receiptCount > 0 && tries < 2 * MAX_LOADS);
	//
	// } catch (Exception e) {
	// log.error("", e);
	// throw new ECBException(e);
	// } finally {
	// sendOfflineReceiptsRunning = false;
	// }
	// return totalReceiptCount;
	// }

	private static void remove(String relativePath, Long id)
			throws AppException {
		String path = getLocalRepositoryObjPath(relativePath, id);
//		log.debug("{} con id {} rimosso", relativePath, id);
		removeByPath(path);

	}
}
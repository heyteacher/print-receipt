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


package org.printreceipt.model.impl.hsql;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.printreceipt.AppException;
import org.printreceipt.datamodel.CashOperation;
import org.printreceipt.datamodel.Event;
import org.printreceipt.datamodel.User;
import org.printreceipt.model.interfaces.CashManager;

public class HsqlCashManagerImpl implements CashManager {

	@Override
	public Serializable addCashAction(CashOperation cashOperation, User user)
			throws AppException {
		try {
			PreparedStatement pstm = HsqlModelFactory
					.getConnection()
					.prepareStatement(
							"INSERT INTO ecb_cashoperation "
									+ "(reg_date, amount, note, user_id, event_id) "
									+ "VALUES (?,?,?,?,?)");

			pstm.setDate(1, new java.sql.Date(new Date().getTime()));
			pstm.setDouble(2, cashOperation.getAmount());
			pstm.setString(3, cashOperation.getNote());
			pstm.setInt(4, cashOperation.getEvent().getId().intValue());
			pstm.setInt(5, cashOperation.getUser().getId().intValue());

			pstm.execute();
			pstm.close();

			cashOperation.setId(HsqlModelFactory.lastIdentity());

		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
		return cashOperation;
	}

	@Override
	public List<CashOperation> loadCashOperations(Event event, User user)
			throws AppException {
		List<CashOperation> cashOperationsList = new ArrayList<CashOperation>();
		try {
			PreparedStatement pstm = HsqlModelFactory.getConnection()
					.prepareStatement(
							" SElECT * FROM ecb_cashoperation "
									+ " WHERE event_id = ? "
									+ " ORDER BY reg_date DESC");
			pstm.setInt(1, event.getId().intValue());
			ResultSet resultSet = pstm.executeQuery();
			while (resultSet.next()) {
				CashOperation cashOperation = new CashOperation();

				BigInteger id = BigInteger.valueOf(resultSet.getLong("id"));
				Date regDate = resultSet.getTimestamp("reg_date");
				String note = resultSet.getString("note");
				Double amount = resultSet.getDouble(("amount"));

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(regDate);
				cashOperation.setReg_date(calendar);
				cashOperation.setId(id);
				cashOperation.setNote(note);
				cashOperation.setAmount(amount);

				cashOperationsList.add(cashOperation);
			}
			resultSet.close();
			pstm.close();

			return cashOperationsList;
		} catch (SQLException sqle) {
			throw new AppException(sqle);
		} catch (ClassNotFoundException cnfe) {
			throw new AppException(cnfe);
		}
	}

	@Override
	public void deleteCashOperation(BigInteger id, User user)
			throws AppException {
		try {
			PreparedStatement pstm = HsqlModelFactory.getConnection()
					.prepareStatement(
							"DELETE FROM ecb_cashoperation WHERE id = ?");
			pstm.setInt(1, id.intValue());
			pstm.execute();
			pstm.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
	}
}
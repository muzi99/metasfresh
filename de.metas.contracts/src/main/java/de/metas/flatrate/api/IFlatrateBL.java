package de.metas.flatrate.api;

/*
 * #%L
 * de.metas.contracts
 * %%
 * Copyright (C) 2015 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.ISingletonService;
import org.compiere.model.I_AD_User;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_C_DocType;
import org.compiere.model.I_C_UOM;
import org.compiere.model.I_M_Product;
import org.compiere.process.SvrProcess;

import de.metas.adempiere.model.I_C_Order;
import de.metas.contracts.subscription.model.I_C_OrderLine;
import de.metas.flatrate.model.I_C_Flatrate_Conditions;
import de.metas.flatrate.model.I_C_Flatrate_DataEntry;
import de.metas.flatrate.model.I_C_Flatrate_Term;
import de.metas.flatrate.model.I_C_Flatrate_Transition;
import de.metas.inout.model.I_M_InOutLine;

public interface IFlatrateBL extends ISingletonService
{
	String beforeCompleteDataEntry(I_C_Flatrate_DataEntry dataEntry);

	/**
	 * Validates the pricing for the given term, e.g. if the flatrate product associated with the term has a price during the term's runtime.
	 *
	 * @param fc
	 * @throws AdempiereException if the pricing is not OK
	 */
	void validatePricing(I_C_Flatrate_Term fc);

	/**
	 * Retrieves data entries with the given term and uom and whose periodes are between the given periodFrom and periodTo (inclusive!). Checks if they have been completed and fully invoiced
	 *
	 * @param flatrateTerm
	 * @param periodFrom
	 * @param periodTo
	 * @param uom
	 * @param errors the method adds a user friendly error message to this list for every data entry that is not yet ready.
	 * @return the retrieved data entries or <code>null</code> if they are not yet completed or fully invoiced.
	 */
	List<I_C_Flatrate_DataEntry> retrieveAndCheckInvoicingEntries(
			I_C_Flatrate_Term flatrateTerm,
			Timestamp startDate,
			Timestamp endDate,
			I_C_UOM uom,
			List<String> errors);

	/**
	 *
	 * @param ctx
	 * @param flatrateTerm the term under which the entries are created
	 * @param logReceiver may be <code>null</code>. If not null, then the receivers's addLog() method will be used to log important messages
	 * @param trxName
	 */
	void createDataEntriesForTerm(I_C_Flatrate_Term flatrateTerm, SvrProcess logReceiver);

	/**
	 * Updates various fields of the given entry, all based of the entry's current Qty_Reported and ActualQty values
	 *
	 * @param dataEntry
	 */
	void updateEntry(I_C_Flatrate_DataEntry dataEntry);

	/**
	 * Create a new flatrate term using the given term as template. The new term's C_Year will be the year after the given term's C_Year.
	 *
	 * <b>IMPORTANT:</b> method might set the given term's C_FlatrateTerm_Next_ID, but won't save it!
	 *
	 * @param term
	 * @param forceExtend will create a new term, even if the given <code>term</code> has <code>IsAutoRenew='N'</code>
	 * @param forceComplete will complete a new term (if one has been created), even if it has <code>IsAutoComplete='N'</code>
	 * @param ol if a new term is created, this order line (if !=null) will be referenced from the new term.
	 */
	void extendContract(I_C_Flatrate_Term term, boolean forceExtend, boolean forceComplete, I_C_OrderLine ol);

	/**
	 * Updates the <code>NoticeDate</code> and <code>EndDate</code> dates of the given term, using the term's values such as <code>StartDate</code>, as well as the {@link I_C_Flatrate_Transition}
	 * associated with the term.
	 *
	 * It is assume that the term is not completed.
	 *
	 * @param term
	 */
	void updateNoticeDateAndEndDate(I_C_Flatrate_Term term);

	I_C_DocType getDocTypeFor(I_C_Flatrate_Term term);

	/**
	 * Creates a new C_Order for the given term. This method is supposed to be used if a term is extended and if we need a dedicated C_Order for the new term, e.g. in order to print if and send if to
	 * the customer.
	 *
	 * @param term
	 * @return
	 */
	I_C_Order createOrderForTerm(I_C_Flatrate_Term term);

	int getWarehouse(I_C_Flatrate_Term term);

	/**
	 * Copy relevant columns from {@link I_C_Flatrate_Conditions} to given <code>term</code>.
	 *
	 * @param term
	 */
	void updateFromConditions(I_C_Flatrate_Term term);

	/**
	 * Updates the corresponding {@link I_C_Flatrate_DataEntry#COLUMNNAME_ActualQty} when a M_InOutLine is added/changed/deleted.
	 *
	 * @param product used (together with <code>inOutLine</code>) to look up the data entry record to update.
	 * @param qty the qty to add to or remove from the data entry's <code>ActualQty</code> value
	 * @param inOutLine this inout line's header (M_InOut) is used to get the C_BPartner_ID and MovementDate, which in turn are used to get the {@link I_C_Flatrate_DataEntry} which shall be updates.
	 *            If there is no such entry, the method does nothing.
	 * @param substract if <code>true</code> then the given <code>qty</code> is added, otherwise it is subtracted.
	 */
	void updateFlatrateDataEntryQty(I_M_Product product, BigDecimal qty, I_M_InOutLine inOutLine, boolean substract);

	/**
	 * Creates a new flatrate term for the given partner. If necessary, it also creates a <code>C_Flata_Data</code> record for the term. Assumes that the given <code>flatrateConditions</code> have the
	 * type <code>Refundable</code>.
	 * <p>
	 * Note: obtain a {@link org.adempiere.util.ILoggable} and log to it:
	 * <ul>
	 * <li>the bpartner's value</li>
	 * <li>whether a term was created</li>
	 * <li>if no term was created, it logs the reason</li>
	 * </ul>
	 * Note that as of now, the log messages are non-localized EN strings.
	 *
	 * @param partner the partner to be used as bill partner. Also this partner's sales rep and billto location are used.
	 * @param flatrateConditions
	 * @param startDate the start date for the new term
	 * @param userInCharge may be <code>null</code>. If set, then this value is used for <code>C_FLatrate_Term.AD_User_InCharge_ID</code>. Otherwise, the method tries
	 *            <code>C_BPartner.SalesRep_ID</code>
	 * @param product may be <code>null</code>. If set, then this value is used for <code>C_Flatrate_Term.M_Product_ID</code>
	 * @return the newly created and completed term or <code>null</code>.
	 */
	I_C_Flatrate_Term createTerm(
			I_C_BPartner bPartner,
			I_C_Flatrate_Conditions flatrateConditions,
			Timestamp startDate,
			I_AD_User userInCharge,
			I_M_Product product);
}

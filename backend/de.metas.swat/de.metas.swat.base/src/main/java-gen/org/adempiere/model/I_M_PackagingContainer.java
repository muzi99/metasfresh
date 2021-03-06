/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.adempiere.model;

/*
 * #%L
 * de.metas.swat.base
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

import org.compiere.model.I_C_UOM;
import org.compiere.model.I_M_Product;
import org.compiere.model.MTable;
import org.compiere.util.KeyNamePair;

/** Generated Interface for M_PackagingContainer
 *  @author Adempiere (generated) 
 *  @version Release 3.5.4a#$
{
env.BUILD_NUMBER}

 */
public interface I_M_PackagingContainer 
{

    /** TableName=M_PackagingContainer */
    public static final String Table_Name = "M_PackagingContainer";

    /** AD_Table_ID=540020 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Mandant.
	  * Mandant fuer diese Installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organisation.
	  * Organisatorische Einheit des Mandanten
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organisation.
	  * Organisatorische Einheit des Mandanten
	  */
	public int getAD_Org_ID();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Erstellt.
	  * Datum, an dem dieser Eintrag erstellt wurde
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Erstellt durch.
	  * Nutzer, der diesen Eintrag erstellt hat
	  */
	public int getCreatedBy();

    /** Column name C_UOM_Length_ID */
    public static final String COLUMNNAME_C_UOM_Length_ID = "C_UOM_Length_ID";

	/** Set Masseinheit fuer Laenge.
	  * Standardmasseinheit fuer Laenge
	  */
	public void setC_UOM_Length_ID (int C_UOM_Length_ID);

	/** Get Masseinheit fuer Laenge.
	  * Standardmasseinheit fuer Laenge
	  */
	public int getC_UOM_Length_ID();

	public I_C_UOM getC_UOM_Length() throws RuntimeException;

    /** Column name C_UOM_Weight_ID */
    public static final String COLUMNNAME_C_UOM_Weight_ID = "C_UOM_Weight_ID";

	/** Set Masseinheit fuer Gewicht.
	  * Standardmasseinheit fuer Gewicht
	  */
	public void setC_UOM_Weight_ID (int C_UOM_Weight_ID);

	/** Get Masseinheit fuer Gewicht.
	  * Standardmasseinheit fuer Gewicht
	  */
	public int getC_UOM_Weight_ID();

	public I_C_UOM getC_UOM_Weight() throws RuntimeException;

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name Height */
    public static final String COLUMNNAME_Height = "Height";

	/** Set Hoehe	  */
	public void setHeight (BigDecimal Height);

	/** Get Hoehe	  */
	public BigDecimal getHeight();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Aktiv.
	  * Der Eintrag ist im System aktiv
	  */
	public void setIsActive (boolean IsActive);

	/** Get Aktiv.
	  * Der Eintrag ist im System aktiv
	  */
	public boolean isActive();

    /** Column name Length */
    public static final String COLUMNNAME_Length = "Length";

	/** Set Laenge	  */
	public void setLength (BigDecimal Length);

	/** Get Laenge	  */
	public BigDecimal getLength();

    /** Column name MaxVolume */
    public static final String COLUMNNAME_MaxVolume = "MaxVolume";

	/** Set Max. Volumen	  */
	public void setMaxVolume (BigDecimal MaxVolume);

	/** Get Max. Volumen	  */
	public BigDecimal getMaxVolume();

    /** Column name MaxWeight */
    public static final String COLUMNNAME_MaxWeight = "MaxWeight";

	/** Set Max. Gewicht	  */
	public void setMaxWeight (BigDecimal MaxWeight);

	/** Get Max. Gewicht	  */
	public BigDecimal getMaxWeight();

    /** Column name M_PackagingContainer_ID */
    public static final String COLUMNNAME_M_PackagingContainer_ID = "M_PackagingContainer_ID";

	/** Set Verpackung	  */
	public void setM_PackagingContainer_ID (int M_PackagingContainer_ID);

	/** Get Verpackung	  */
	public int getM_PackagingContainer_ID();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Produkt.
	  * Produkt, Leistung, Artikel
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Produkt.
	  * Produkt, Leistung, Artikel
	  */
	public int getM_Product_ID();

	public I_M_Product getM_Product() throws RuntimeException;

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Aktualisiert.
	  * Datum, an dem dieser Eintrag aktualisiert wurde
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Aktualisiert durch.
	  * Nutzer, der diesen Eintrag aktualisiert hat
	  */
	public int getUpdatedBy();

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Suchschluessel.
	  * Suchschluessel fuer den Eintrag im erforderlichen Format - muss eindeutig sein
	  */
	public void setValue (String Value);

	/** Get Suchschluessel.
	  * Suchschluessel fuer den Eintrag im erforderlichen Format - muss eindeutig sein
	  */
	public String getValue();

    /** Column name Width */
    public static final String COLUMNNAME_Width = "Width";

	/** Set Breite	  */
	public void setWidth (BigDecimal Width);

	/** Get Breite	  */
	public BigDecimal getWidth();
}

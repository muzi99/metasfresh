package de.metas.procurement.webui.model;

import javax.persistence.Transient;

/*
 * #%L
 * metasfresh-procurement-webui
 * %%
 * Copyright (C) 2016 metas GmbH
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
/**
 * This subclass adds a <b>transient</b> (not to be stored in DB) property <code>syncConfirmId</code>, so that if a {@link SyncConfirm} record was created, its ID can be forwarded to the component
 * responsible to push the sync data to the remote endpoint.<br>
 * That component shall set the <code>syncConfirmId</code> to the {@link de.metas.procurement.sync.protocol.AbstractSyncModel} which is is about to send.<br>
 * This way, the remote endpoint is able to create and send back a specific {@link de.metas.procurement.sync.protocol.SyncConfirmation}.
 *
 * @author metas-dev <dev@metasfresh.com>
 * @task https://metasfresh.atlassian.net/browse/FRESH-206
 *
 */
@SuppressWarnings("serial")
public class AbstractSyncConfirmAwareEntity extends AbstractEntity
{
	@Transient
	private long syncConfirmId;

	public long getSyncConfirmId()
	{
		return syncConfirmId;
	}

	public void setSyncConfirmId(long syncConfirmId)
	{
		this.syncConfirmId = syncConfirmId;

	}
}

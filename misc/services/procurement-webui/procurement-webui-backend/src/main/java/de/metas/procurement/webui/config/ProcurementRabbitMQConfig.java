/*
 * #%L
 * procurement-webui-backend
 * %%
 * Copyright (C) 2021 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

package de.metas.procurement.webui.config;

import de.metas.common.procurement.sync.Constants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcurementRabbitMQConfig
{
	@Bean(name = Constants.QUEUE_NAME_MF_TO_PW)
	public Queue metasfreshToProcurementWebQueue()
	{
		return new Queue(Constants.QUEUE_NAME_MF_TO_PW);
	}

	@Bean(name = Constants.QUEUE_NAME_PW_TO_MF)
	public Queue procurementWebTometasfreshQueue()
	{
		return new Queue(Constants.QUEUE_NAME_PW_TO_MF);
	}
}

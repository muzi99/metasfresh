package de.metas.notification;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.adempiere.service.ClientId;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.metas.organization.OrgId;
import de.metas.user.UserId;
import de.metas.util.Check;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

/*
 * #%L
 * de.metas.adempiere.adempiere.base
 * %%
 * Copyright (C) 2018 metas GmbH
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

@Value
public class UserNotificationsConfig
{
	private UserId userId;
	private String userADLanguage; // might be null
	private ClientId clientId;
	private OrgId orgId;

	@Getter(AccessLevel.NONE)
	private final ImmutableList<UserNotificationsGroup> userNotificationGroups; // needed for toBuilder()
	private Map<NotificationGroupName, UserNotificationsGroup> userNotificationGroupsByInternalName;
	private final UserNotificationsGroup defaults;

	private String email;
	private UserId userInChargeId;

	@Builder(toBuilder = true)
	private UserNotificationsConfig(
			@NonNull final UserId userId,
			final String userADLanguage,
			final ClientId clientId,
			final OrgId orgId,
			@NonNull @Singular final Collection<UserNotificationsGroup> userNotificationGroups,
			@NonNull final UserNotificationsGroup defaults,
			final String email,
			final UserId userInChargeId)
	{
		this.userId = userId;
		this.userADLanguage = Check.isEmpty(userADLanguage) ? null : userADLanguage;

		this.clientId = clientId != null ? clientId : ClientId.SYSTEM;
		this.orgId = orgId != null ? orgId : OrgId.ANY;

		this.userNotificationGroups = ImmutableList.copyOf(userNotificationGroups);
		this.userNotificationGroupsByInternalName = Maps.uniqueIndex(userNotificationGroups, UserNotificationsGroup::getGroupInternalName);
		this.defaults = defaults;

		this.email = Check.isEmpty(email, true) ? null : email.trim();
		this.userInChargeId = userInChargeId;
	}

	public UserNotificationsGroup getGroupByName(@NonNull final NotificationGroupName groupName)
	{
		return userNotificationGroupsByInternalName.getOrDefault(groupName, defaults);
	}

	public String getUserADLanguageOrGet(@NonNull final Supplier<String> defaultLanguageSupplier)
	{
		final String adLanguage = getUserADLanguage();
		return adLanguage != null ? adLanguage : defaultLanguageSupplier.get();
	}

	public boolean isUserInChargeSet()
	{
		return userInChargeId != null;
	}

	public UserNotificationsConfig deriveWithNotificationTypes(final Set<NotificationType> notificationTypes)
	{
		return toBuilder()
				.clearUserNotificationGroups()
				.defaults(UserNotificationsGroup.prepareDefault().notificationTypes(notificationTypes).build())
				.build();
	}

	public UserNotificationsConfig deriveWithNotificationGroups(final List<UserNotificationsGroup> notificationGroups)
	{
		if (notificationGroups.isEmpty())
		{
			return this;
		}

		final Map<NotificationGroupName, UserNotificationsGroup> newUserNotificationGroupsByInternalName = new HashMap<>(userNotificationGroupsByInternalName);
		notificationGroups.forEach(notificationGroup -> newUserNotificationGroupsByInternalName.put(notificationGroup.getGroupInternalName(), notificationGroup));

		return toBuilder()
				.clearUserNotificationGroups()
				.userNotificationGroups(newUserNotificationGroupsByInternalName.values())
				.build();
	}
}

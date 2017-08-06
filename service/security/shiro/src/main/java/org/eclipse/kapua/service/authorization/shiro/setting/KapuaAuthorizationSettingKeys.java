/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.shiro.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Authorization setting key
 */
public enum KapuaAuthorizationSettingKeys implements SettingKey {
    AUTHORIZATION_KEY("authorization.key"),
    AUTHORIZATION_PUBLISH_INTERNAL_EVENT_ADDRESS("authorization.publishInternalEventAddress"),
    AUTHORIZATION_INTERNAL_EVENT_ADDRESS("authorization.internalEventAddress"),
    ACCOUNT_ACCESS_INFO_UPSTREAM_EVENT_ADDRESS("authorization.account.access_info.upstreamEventAddress"),
    ACCOUNT_ROLE_UPSTREAM_EVENT_ADDRESS("authorization.account.role.upstreamEventAddress"),
    ACCOUNT_DOMAIN_UPSTREAM_EVENT_ADDRESS("authorization.account.domain.upstreamEventAddress"),
    ACCOUNT_GROUP_UPSTREAM_EVENT_ADDRESS("authorization.account.group.upstreamEventAddress"),
    USER_ACCESS_INFO_UPSTREAM_EVENT_ADDRESS("authorization.user.access_info.upstreamEventAddress");

    private String key;

    private KapuaAuthorizationSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}

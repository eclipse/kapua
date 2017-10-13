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
    AUTHORIZATION_INTERNAL_EVENT_ADDRESS("authorization.internalEventAddress"),
    AUTHORIZATION_SERVICES_NAMES("authorization.servicesNames"),
    ACCOUNT_EVENT_ADDRESS("account.eventAddress"),
    USER_EVENT_ADDRESS("user.eventAddress"),
    ACCESS_INFO_SUBSCRIPTION_NAME("accessInfo.subscriptionName"),
    ROLE_SUBSCRIPTION_NAME("role.subscriptionName"),
    DOMAIN_SUBSCRIPTION_NAME("domain.subscriptionName"),
    GROUP_SUBSCRIPTION_NAME("group.subscriptionName");

    private String key;

    private KapuaAuthorizationSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}

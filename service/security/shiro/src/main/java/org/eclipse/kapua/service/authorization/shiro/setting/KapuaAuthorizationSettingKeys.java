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
 *
 * @since 1.0
 *
 */
public enum KapuaAuthorizationSettingKeys implements SettingKey {
    AUTHORIZATION_KEY("authorization.key"),

    AUTO_REGISTRATION_SIMPLE_ROOT_ACCOUNT("auto.registration.simple.rootAccount"),
    AUTO_REGISTRATION_SIMPLE_MAX_NUMBER_OF_CHILD_USERS("auto.registration.simple.maximumNumberOfChildUsers");

    private String key;

    private KapuaAuthorizationSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}

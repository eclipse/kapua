/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.security.registration.simple.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Authorization setting key.
 */
public enum SimpleSettingKeys implements SettingKey {
    SIMPLE_ROOT_ACCOUNT("auto.registration.simple.rootAccount"), //
    SIMPLE_MAX_NUMBER_OF_CHILD_USERS("auto.registration.simple.maximumNumberOfChildUsers"),
    ACCOUNT_EXPIRATION_DATE_DAYS("auto.registration.simple.accountExpirationDate.days");

    private String key;

    private SimpleSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}

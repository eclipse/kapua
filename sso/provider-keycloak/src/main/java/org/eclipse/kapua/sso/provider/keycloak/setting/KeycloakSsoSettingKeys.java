/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others
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
package org.eclipse.kapua.sso.provider.keycloak.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum KeycloakSsoSettingKeys implements SettingKey {

    KEYCLOAK_URI("sso.keycloak.uri"), //
    KEYCLOAK_REALM("sso.keycloak.realm"), //
    ;

    private final String key;

    KeycloakSsoSettingKeys(final String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}

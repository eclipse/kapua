/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.provider.keycloak.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum KeycloakSsoSettingKeys implements SettingKey {

    KEYCLOAK_URI("sso.keycloak.uri"), //

    KEYCLOAK_REALM("sso.keycloak.realm"), //
    KEYCLOAK_CLIENT_ID("sso.keycloak.client.id"), //
    KEYCLOAK_CLIENT_SECRET("sso.keycloak.client.secret");

    private final String key;

    private KeycloakSsoSettingKeys(final String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}

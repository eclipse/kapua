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
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.plugin.sso.openid.provider.keycloak.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

public class KeycloakOpenIDSetting extends AbstractKapuaSetting<KeycloakOpenIDSettingKeys> {

    private static final String KEYCLOAK_OPENID_SETTING_RESOURCE = "openid-keycloak-setting.properties";

    private static final KeycloakOpenIDSetting INSTANCE = new KeycloakOpenIDSetting();

    private KeycloakOpenIDSetting() {
        super(KEYCLOAK_OPENID_SETTING_RESOURCE);
    }

    KeycloakOpenIDSetting(final String resourceName) {
        super(resourceName);
    }

    public static KeycloakOpenIDSetting getInstance() {
        return INSTANCE;
    }
}

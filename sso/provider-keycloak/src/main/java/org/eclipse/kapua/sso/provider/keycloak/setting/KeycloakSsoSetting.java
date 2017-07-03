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
package org.eclipse.kapua.sso.provider.keycloak.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

public class KeycloakSsoSetting extends AbstractKapuaSetting<KeycloakSsoSettingKeys> {

    private static final String KEYCLOAK_SSO_SETTING_RESOURCE = "sso-keycloak-setting.properties";

    private static final KeycloakSsoSetting INSTANCE = new KeycloakSsoSetting();

    private KeycloakSsoSetting() {
        super(KEYCLOAK_SSO_SETTING_RESOURCE);
    }

    KeycloakSsoSetting(final String resourceName) {
        super(resourceName);
    }

    public static KeycloakSsoSetting getInstance() {
        return INSTANCE;
    }
}

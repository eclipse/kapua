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
package org.eclipse.kapua.plugin.sso.openid.provider.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

public class SsoSetting extends AbstractKapuaSetting<SsoSettingKeys> {

    private static final String SSO_SETTING_RESOURCE = "sso-setting.properties";

    private static final SsoSetting INSTANCE = new SsoSetting();

    private SsoSetting() {
        super(SSO_SETTING_RESOURCE);
    }

    SsoSetting(final String resourceName) {
        super(resourceName);
    }

    public static SsoSetting getInstance() {
        return INSTANCE;
    }
}

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
package org.eclipse.kapua.plugin.sso.openid.provider.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * @since 1.0.0
 */
public class OpenIDSetting extends AbstractKapuaSetting<OpenIDSettingKeys> {

    private static final String OPENID_SETTING_RESOURCE = "openid-setting.properties";

    private static final OpenIDSetting INSTANCE = new OpenIDSetting();

    private OpenIDSetting() {
        super(OPENID_SETTING_RESOURCE);
    }

    OpenIDSetting(final String resourceName) {
        super(resourceName);
    }

    public static OpenIDSetting getInstance() {
        return INSTANCE;
    }
}

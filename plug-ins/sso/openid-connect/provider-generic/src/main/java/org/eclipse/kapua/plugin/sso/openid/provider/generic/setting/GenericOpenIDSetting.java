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
package org.eclipse.kapua.plugin.sso.openid.provider.generic.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

public class GenericOpenIDSetting extends AbstractKapuaSetting<GenericOpenIDSettingKeys> {

    private static final String GENERIC_OPENID_SETTING_RESOURCE = "openid-generic-setting.properties";

    private static final GenericOpenIDSetting INSTANCE = new GenericOpenIDSetting();

    private GenericOpenIDSetting() {
        super(GENERIC_OPENID_SETTING_RESOURCE);
    }

    GenericOpenIDSetting(final String resourceName) {
        super(resourceName);
    }

    public static GenericOpenIDSetting getInstance() {
        return INSTANCE;
    }
}

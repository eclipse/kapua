/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.shiro.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

import javax.inject.Inject;

/**
 * Authorization setting implementation.
 */
public class KapuaAuthorizationSetting extends AbstractKapuaSetting<KapuaAuthorizationSettingKeys> {

    private static final String AUTHORIZATION_SETTING_RESOURCE = "kapua-authorization-setting.properties";

    /**
     * Construct a new authorization setting reading settings from {@link KapuaAuthorizationSetting#AUTHORIZATION_SETTING_RESOURCE}
     */
    @Inject
    public KapuaAuthorizationSetting() {
        super(AUTHORIZATION_SETTING_RESOURCE);
    }

}

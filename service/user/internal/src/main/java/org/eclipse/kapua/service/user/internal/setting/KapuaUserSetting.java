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
package org.eclipse.kapua.service.user.internal.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

import javax.inject.Inject;

/**
 * Class that offers access to user settings
 *
 * @since 1.0
 */
//TODO: FIXME: singletons should not be handled manually, we have DI for that
public class KapuaUserSetting extends AbstractKapuaSetting<KapuaUserSettingKeys> {

    /**
     * Resource file from which source properties.
     */
    private static final String USER_SETTING_RESOURCE = "kapua-user-setting.properties";

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link KapuaUserSettingKeys#USER_KEY} value.
     */
    @Inject
    public KapuaUserSetting() {
        super(USER_SETTING_RESOURCE);
    }
}

/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

import javax.inject.Inject;

/**
 * Service authentication implementation.<br>
 * This class handles settings for the {@link ServiceAuthenticationSettingKey}.
 */
public final class ServiceAuthenticationSetting extends AbstractKapuaSetting<ServiceAuthenticationSettingKey> {

    private static final String CONFIG_RESOURCE_NAME = "kapua-service-authentication-setting.properties";

    @Inject
    public ServiceAuthenticationSetting() {
        super(CONFIG_RESOURCE_NAME);
    }

}

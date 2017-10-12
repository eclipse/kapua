/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.certificate.internal.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Authentication setting implementation.
 *
 * @since 1.0
 *
 */
public class KapuaCertificateSetting extends AbstractKapuaSetting<KapuaCertificateSettingKeys> {

    private static final String CERTIFICATE_SETTING_PROPERTIES = "kapua-certificate-setting.properties";

    private static final KapuaCertificateSetting INSTANCE = new KapuaCertificateSetting();

    /**
     * Construct a new authentication setting reading settings from {@link KapuaCertificateSetting#CERTIFICATE_SETTING_PROPERTIES}
     */
    private KapuaCertificateSetting() {
        super(CERTIFICATE_SETTING_PROPERTIES);
    }

    /**
     * Return the authentication setting instance (singleton)
     *
     * @return
     */
    public static KapuaCertificateSetting getInstance() {
        return INSTANCE;
    }
}

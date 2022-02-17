/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.certificate.internal.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum KapuaCertificateSettingKeys implements SettingKey {

    CERTIFICATE_JWT_PRIVATE_KEY("certificate.jwt.private.key"),
    CERTIFICATE_JWT_PRIVATE_KEY_PASSWORD("certificate.jwt.private.key.password"),
    CERTIFICATE_JWT_CERTIFICATE("certificate.jwt.certificate");

    private String key;

    KapuaCertificateSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}

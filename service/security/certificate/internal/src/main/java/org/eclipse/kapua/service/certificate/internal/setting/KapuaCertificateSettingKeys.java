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

import org.eclipse.kapua.commons.setting.SettingKey;

public enum KapuaCertificateSettingKeys implements SettingKey {

    CERTIFICATE_JWT_PRIVATE_KEY("certificate.jwt.private.key"),
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

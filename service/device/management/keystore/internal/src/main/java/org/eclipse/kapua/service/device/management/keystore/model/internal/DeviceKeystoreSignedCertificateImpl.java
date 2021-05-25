/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.keystore.model.internal;

import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreSignedCertificate;

public class DeviceKeystoreSignedCertificateImpl implements DeviceKeystoreSignedCertificate {

    private String certificate;

    public DeviceKeystoreSignedCertificateImpl() {
    }

    @Override
    public String getSignedCertificate() {
        return certificate;
    }

    @Override
    public void setSignedCertificate(String signedCertificate) {
        this.certificate = signedCertificate;
    }
}
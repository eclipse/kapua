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
package org.eclipse.kapua.app.api.core.model.device.management.keystore;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "deviceKeystoreCertificateInfo")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DeviceKeystoreCertificateInfo {

    private String keystoreId;
    private String alias;
    private KapuaId certificateInfoId;

    public DeviceKeystoreCertificateInfo() {
    }

    public String getKeystoreId() {
        return keystoreId;
    }

    public void setKeystoreId(String keystoreId) {
        this.keystoreId = keystoreId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getCertificateInfoId() {
        return certificateInfoId;
    }

    public void setCertificateInfoId(KapuaId certificateInfoId) {
        this.certificateInfoId = certificateInfoId;
    }
}

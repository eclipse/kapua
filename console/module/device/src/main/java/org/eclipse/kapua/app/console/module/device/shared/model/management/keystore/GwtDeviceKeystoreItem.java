/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.shared.model.management.keystore;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GwtDeviceKeystoreItem extends GwtEntityModel {

    @Override
    public <X> X get(String property) {
        if ("notBeforeFormatted".equals(property)) {
            return getNotBefore() != null ? (X) (DateUtils.formatDateTime(getNotBefore())) : null;
        } else if ("notAfterFormatted".equals(property)) {
            return getNotAfter() != null ? (X) (DateUtils.formatDateTime(getNotAfter())) : null;
        } else {
            return super.get(property);
        }
    }

    public String getKeystoreId() {
        return get("keystoreId");
    }

    public void setKeystoreId(String keystoreId) {
        set("keystoreId", keystoreId);
    }

    public String getItemType() {
        return get("itemType");
    }

    public void setItemType(String itemType) {
        set("itemType", itemType);
    }

    public Integer getSize() {
        return get("size");
    }

    public void setSize(Integer size) {
        set("size", size);
    }

    public String getAlgorithm() {
        return get("algorithm");
    }

    public void setAlgorithm(String algorithm) {
        set("algorithm", algorithm);
    }

    public String getAlias() {
        return get("alias");
    }

    public void setAlias(String alias) {
        set("alias", alias);
    }

    public String getSubjectDN() {
        return get("subjectDN");
    }

    public void setSubjectDN(String subjectDN) {
        set("subjectDN", subjectDN);
    }

    public List<String[]> getSubjectAN() {
        if (get("subjectAN") == null) {
            setSubjectAN(new ArrayList<String[]>());
        }

        return get("subjectAN");
    }

    public void setSubjectAN(List<String[]> subjectAN) {
        set("subjectAN", subjectAN);
    }

    public String getIssuer() {
        return get("issuer");
    }

    public void setIssuer(String issuer) {
        set("issuer", issuer);
    }

    public Date getNotBefore() {
        return get("notBefore");
    }

    public String getNotBeforeFormatted() {
        return get("notBeforeFormatted");
    }

    public void setNotBefore(Date notBefore) {
        set("notBefore", notBefore);
    }

    public Date getNotAfter() {
        return get("notAfter");
    }

    public String getNotAfterFormatted() {
        return get("notAfterFormatted");
    }

    public void setNotAfter(Date notAfter) {
        set("notAfter", notAfter);
    }

    public String getCertificate() {
        return get("certificate");
    }

    public void setCertificate(String certificate) {
        set("certificate", certificate);
    }

    public List<String> getCertificateChain() {
        if (get("certificateChain") == null) {
            setCertificateChain(new ArrayList<String>());
        }

        return get("certificateChain");
    }

    public void addCertificateChain(String certificate) {
        getCertificateChain().add(certificate);
    }

    public void setCertificateChain(List<String> certificateChain) {
        set("certificateChain", certificateChain);
    }
}

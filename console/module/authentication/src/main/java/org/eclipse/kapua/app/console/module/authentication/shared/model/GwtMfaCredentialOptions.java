/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.authentication.shared.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

public class GwtMfaCredentialOptions extends GwtUpdatableEntityModel implements Serializable {

    private static final long serialVersionUID = -7186218877584536030L;

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("trustExpirationDateFormatted".equals(property)) {
            if (getTrustExpirationDate() != null) {
                return (X) ((DateUtils.formatDateTime(getTrustExpirationDate())));
            } else {
                return null;
            }
        } else {
            return super.get(property);
        }
    }

    public Date getTrustExpirationDate() {
        return get("trustExpirationDate");
    }

    public String getTrustExpirationDateFormatted() {
        return get("trustExpirationDateFormatted");
    }

    public void setTrustExpirationDate(Date trustExpirationDate) {
        set("trustExpirationDate", trustExpirationDate);
    }

    public String getTrustKey() {
        return get("trustKey");
    }

    public void setTrustKey(String trustKey) {
        set("trustKey", trustKey);
    }

    public String getAuthenticationKey() {
        return get("authenticationKey");
    }

    public void setAuthenticationKey(String authenticationKey) {
        set("authenticationKey", authenticationKey);
    }

    public String getQRCodeImage() {
        return get("qrCodeImage");
    }

    public void setQRCodeImage(String qrCodeImage) {
        set("qrCodeImage", qrCodeImage);
    }

    public List<String> getScratchCodes() {
        return get("scratchCodes");
    }

    public void setScratchCodes(List<String> scratchCodes) {
        set("scratchCodes", scratchCodes);
    }

}

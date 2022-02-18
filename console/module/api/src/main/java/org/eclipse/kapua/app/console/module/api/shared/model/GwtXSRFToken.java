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
package org.eclipse.kapua.app.console.module.api.shared.model;

import java.util.Date;

public class GwtXSRFToken implements java.io.Serializable {

    private static final long serialVersionUID = 6731819179007021824L;
    private static final long TOKEN_VALIDITY_PERIOD_MS = 300000;

    private String token;

    private Date expiresOn;

    public GwtXSRFToken() {
        this.setExpiresOn(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_PERIOD_MS));
    }

    public GwtXSRFToken(String tokenString) {
        this.token = tokenString;
        this.setExpiresOn(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_PERIOD_MS));
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

}

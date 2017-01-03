/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.test.authentication;

import org.eclipse.kapua.service.authentication.AccessTokenCredentials;

public class AccessTokenCredentialsMock implements AccessTokenCredentials {

    private String tokenId;

    public AccessTokenCredentialsMock(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public String getTokenId() {
        return this.tokenId;
    }

    @Override
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

}

/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.shiro;

import org.eclipse.kapua.service.authentication.RefreshTokenCredentials;


public class RefreshTokenCredentialsImpl implements RefreshTokenCredentials {
    
    private String tokenId;
    private String refreshToken;
    
    public RefreshTokenCredentialsImpl(String tokenId, String refreshToken) {
        this.tokenId = tokenId;
        this.refreshToken = refreshToken;
    }
    
    @Override
    public String getTokenId() {
        return tokenId;
    }
    
    @Override
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
    
    @Override
    public String getRefreshToken() {
        return refreshToken;
    }
    
    @Override
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}

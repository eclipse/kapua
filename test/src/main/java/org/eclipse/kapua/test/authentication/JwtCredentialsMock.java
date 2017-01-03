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

import org.eclipse.kapua.service.authentication.JwtCredentials;

public class JwtCredentialsMock implements JwtCredentials {

    private String jwt;

    public JwtCredentialsMock(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public String getJwt() {
        return this.jwt;
    }

    @Override
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

}

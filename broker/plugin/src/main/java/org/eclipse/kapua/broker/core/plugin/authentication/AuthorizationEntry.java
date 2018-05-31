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
package org.eclipse.kapua.broker.core.plugin.authentication;

import org.eclipse.kapua.broker.core.plugin.Acl;

public class AuthorizationEntry {

    private String address;
    private Acl acl;

    public AuthorizationEntry(String address, Acl acl) {
        this.address = address;
        this.acl = acl;
    }

    public String getAddress() {
        return address;
    }

    public Acl getAcl() {
        return acl;
    }

}

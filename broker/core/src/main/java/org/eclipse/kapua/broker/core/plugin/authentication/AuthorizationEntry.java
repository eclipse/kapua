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

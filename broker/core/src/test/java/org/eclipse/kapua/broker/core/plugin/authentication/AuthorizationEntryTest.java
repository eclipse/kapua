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
package org.eclipse.kapua.broker.core.plugin.authentication;

import org.eclipse.kapua.broker.core.plugin.Acl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class AuthorizationEntryTest extends Assert {

    @Test
    public void authorizationEntryTest() {
        String[] addresses = {null, "", "address123", "AdReSs <12>", "Adr_ess adr-ess", "!#2 adress-1", "adress(123) adress_##"};
        Acl[] aclArray = new Acl[]{null, Acl.ALL, Acl.READ, Acl.WRITE, Acl.ADMIN, Acl.WRITE_ADMIN, Acl.READ_ADMIN};

        for (String address : addresses) {
            for (Acl acl : aclArray) {
                AuthorizationEntry authorizationEntry = new AuthorizationEntry(address, acl);
                assertEquals("Expected and actual values should be the same.", address, authorizationEntry.getAddress());
                assertEquals("Expected and actual values should be the same.", acl, authorizationEntry.getAcl());
            }
        }
    }
}
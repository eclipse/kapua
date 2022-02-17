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
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class AclTest extends Assert {

    boolean[] readPermissions;
    boolean[] writePermissions;
    boolean[] adminPermission;

    @Before
    public void initialize() {
        readPermissions = new boolean[]{true, false};
        writePermissions = new boolean[]{true, false};
        adminPermission = new boolean[]{true, false};
    }

    @Test
    public void aclTest() {
        for (boolean read : readPermissions) {
            for (boolean write : writePermissions) {
                for (boolean admin : adminPermission) {
                    Acl acl = new Acl(read, write, admin);
                    assertEquals("Expected and actual values should be the same.", read, acl.isRead());
                    assertEquals("Expected and actual values should be the same.", write, acl.isWrite());
                    assertEquals("Expected and actual values should be the same.", admin, acl.isAdmin());
                }
            }
        }
    }
}
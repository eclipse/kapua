/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class AclTest extends Assert {

    @Test
    public void aclAllTrueTest() {
        Acl acl = new Acl(true, true, true);
        assertTrue(acl.isAdmin());
        assertTrue(acl.isRead());
        assertTrue(acl.isWrite());
    }

    @Test
    public void aclAllFalseTest() {
        Acl acl = new Acl(false, false, false);
        assertFalse(acl.isAdmin());
        assertFalse(acl.isRead());
        assertFalse(acl.isWrite());
    }

    @Test
    public void aclMixedTest() {
        Acl acl = new Acl(true, false, true);
        assertTrue(acl.isAdmin());
        assertTrue(acl.isRead());
        assertFalse(acl.isWrite());
    }
}

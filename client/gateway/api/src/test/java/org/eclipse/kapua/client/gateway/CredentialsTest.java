/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway;

import org.eclipse.kapua.client.gateway.Credentials.UserAndPassword;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class CredentialsTest {

    @Test
    public void testNull1() {
        final UserAndPassword c = Credentials.userAndPassword(null, (char[]) null);
        Assert.assertNull(c.getUsername());
        Assert.assertNull(c.getPassword());
        Assert.assertNull(c.getPasswordAsString());
    }

    @Test
    public void testNull2() {
        final UserAndPassword c = Credentials.userAndPassword(null, (String) null);
        Assert.assertNull(c.getUsername());
        Assert.assertNull(c.getPassword());
        Assert.assertNull(c.getPasswordAsString());
    }

    @Test
    public void testNonNull1() {
        final UserAndPassword c = Credentials.userAndPassword("user", "password".toCharArray());
        Assert.assertNotNull(c.getUsername());
        Assert.assertNotNull(c.getPassword());
        Assert.assertNotNull(c.getPasswordAsString());
    }

    @Test
    public void testNonNull2() {
        final UserAndPassword c = Credentials.userAndPassword("user", "password");
        Assert.assertNotNull(c.getUsername());
        Assert.assertNotNull(c.getPassword());
        Assert.assertNotNull(c.getPasswordAsString());
    }
}

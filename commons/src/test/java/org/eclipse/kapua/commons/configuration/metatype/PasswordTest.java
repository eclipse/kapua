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
package org.eclipse.kapua.commons.configuration.metatype;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class PasswordTest extends Assert {

    @Test
    public void testGetPassword() throws Exception {
        Password password = new Password("pass");
        Password password2 = new Password(null);
        Assert.assertEquals("pass", password.getPassword());
        Assert.assertNull(password2.getPassword());
    }

    @Test
    public void testToString() {
        Password password = new Password("pass");
        Password password2 = new Password(null);
        Assert.assertEquals("pass", password.toString());
        Assert.assertNull(password2.getPassword());
    }
}
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
package org.eclipse.kapua.transport.utils;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ClientIdGeneratorTest extends Assert {

    ClientIdGenerator clientIdGenerator;

    @Before
    public void start() {
        clientIdGenerator = ClientIdGenerator.getInstance();
    }

    @Test
    public void nextWithoutParametersTest() {
        String newId = clientIdGenerator.next();
        assertTrue(newId.startsWith("Id"));
    }

    @Test
    public void nextWithParametersTest() {
        String[] parameters = {"1", "testtest", "1232153", "!!$%&/&(())", "a1"};
        for (String parameter : parameters) {
            String newId = clientIdGenerator.next(parameter);
            assertTrue(newId.startsWith(parameter));
        }
    }

    @Test
    public void nextWithNullParametersTest() {
        String newId = clientIdGenerator.next(null);
        assertTrue(newId.startsWith("null"));
    }
}

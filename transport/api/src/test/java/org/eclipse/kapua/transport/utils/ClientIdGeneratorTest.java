/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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

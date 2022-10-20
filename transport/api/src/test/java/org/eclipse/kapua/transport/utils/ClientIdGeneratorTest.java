/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;


@Category(JUnitTests.class)
public class ClientIdGeneratorTest {

    ClientIdGenerator clientIdGenerator;

    @Before
    public void initialize() {
        clientIdGenerator = ClientIdGenerator.getInstance();
    }

    @Test
    public void clientIdGeneratorTest() throws Exception {
        Constructor<ClientIdGenerator> clientIdGenerator = ClientIdGenerator.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(clientIdGenerator.getModifiers()));
        clientIdGenerator.setAccessible(true);
        clientIdGenerator.newInstance();
    }

    @Test
    public void nextWithoutParameterTest() {
        String newId = clientIdGenerator.next();
        Assert.assertTrue("True expected.", newId.startsWith("Id"));
    }

    @Test
    public void nextWithParameterTest() {
        String[] parameters = {"1", "testtest", "1232153", "!!$%&/&(())", "a1"};
        for (String parameter : parameters) {
            String newId = clientIdGenerator.next(parameter);
            Assert.assertTrue("True expected.", newId.startsWith(parameter));
        }
    }

    @Test
    public void nextWithNullParameterTest() {
        String newId = clientIdGenerator.next(null);
        Assert.assertTrue("True expected.", newId.startsWith("null"));
    }
}
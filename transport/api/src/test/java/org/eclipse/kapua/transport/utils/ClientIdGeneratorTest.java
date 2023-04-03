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
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ClientIdGenerator} tests.
 *
 * @since 1.0.0
 */
@Category(JUnitTests.class)
public class ClientIdGeneratorTest {

    private static final Logger LOG = LoggerFactory.getLogger(ClientIdGeneratorTest.class);

    @Test
    public void getInstanceTest() {
        ClientIdGenerator clientIdGenerator1 = ClientIdGenerator.getInstance();
        Assert.assertNotNull(clientIdGenerator1);

        ClientIdGenerator clientIdGenerator2 = ClientIdGenerator.getInstance();
        Assert.assertNotNull(clientIdGenerator2);

        Assert.assertEquals(clientIdGenerator1, clientIdGenerator2);
    }

    @Test
    public void nextTest() {
        ClientIdGenerator clientIdGenerator = ClientIdGenerator.getInstance();

        String nextId = clientIdGenerator.next();

        Assert.assertNotNull(nextId);
        Assert.assertTrue("Default Id generation should start with 'Id'", nextId.startsWith("Id"));
        Assert.assertTrue("Pattern should be 'Id-[0-9].*-[0-9].*", nextId.matches("Id-[0-9].*-[0-9].*"));
    }

    @Test
    public void nextGenerationTest() {
        ClientIdGenerator clientIdGenerator = ClientIdGenerator.getInstance();

        List<String> generatedIds = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            String nextId = clientIdGenerator.next();
            LOG.trace("Generated Id: {}", nextId);

            Assert.assertNotNull(nextId);
            Assert.assertTrue("Generated ids shouldn't have duplicates", generatedIds.add(nextId));

            generatedIds.add(nextId);
        }
    }

    @Test
    public void nextWithPrefixTest() {
        ClientIdGenerator clientIdGenerator = ClientIdGenerator.getInstance();

        String nextId = clientIdGenerator.next("MyPrefix");

        Assert.assertNotNull(nextId);
        Assert.assertTrue("Default Id generation should start with 'MyPrefix'", nextId.startsWith("MyPrefix"));
        Assert.assertTrue("Pattern should be 'MyPrefix-[0-9].*-[0-9].*", nextId.matches("MyPrefix-[0-9].*-[0-9].*"));
    }

    @Test
    public void nextWithPrefixGenerationTest() {
        ClientIdGenerator clientIdGenerator = ClientIdGenerator.getInstance();

        List<String> generatedIds = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            String nextId = clientIdGenerator.next("MyPrefix");
            LOG.trace("Generated Id: {}", nextId);

            Assert.assertNotNull(nextId);
            Assert.assertTrue("Generated ids shouldn't have duplicates", generatedIds.add(nextId));

            generatedIds.add(nextId);
        }
    }
}
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
package org.eclipse.kapua.extras.esmigrator;

import org.eclipse.kapua.extras.esmigrator.Es7Migration;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class Es7MigrationTests {

    private final Es7Migration es7Migration = new Es7Migration(null, null, null);

    @Test
    public void messageIndexWithoutPrefix() {
        Assert.assertTrue(es7Migration.isOldMessageIndexNaming("1", "1-2020-47", null));
        Assert.assertTrue(es7Migration.isOldMessageIndexNaming("1", "1-2020-47-32", null));
        Assert.assertTrue(es7Migration.isOldMessageIndexNaming("1", "1-2020-47-32-15", null));
        Assert.assertTrue(es7Migration.isOldMessageIndexNaming("7472159827853538751", "7472159827853538751-2020-47", null));
        Assert.assertTrue(es7Migration.isOldMessageIndexNaming("7472159827853538751", "7472159827853538751-2020-47-32", null));
        Assert.assertTrue(es7Migration.isOldMessageIndexNaming("7472159827853538751", "7472159827853538751-2020-47-32-15", null));

        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("7472159827853538751", "1-2020-47", null));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("7472159827853538751", "1-2020-47-32", null));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("7472159827853538751", "1-2020-47-32-15", null));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "7472159827853538751-2020-47", null));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "7472159827853538751-2020-47-32", null));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "7472159827853538751-2020-47-32-15", null));

        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", ".1-channel", null));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", ".1-client", null));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", ".1-metric", null));

        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "1-channel", null));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "1-client", null));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "1-metric", null));
    }

    @Test
    public void messageIndexWithPrefix() {
        Assert.assertTrue(es7Migration.isOldMessageIndexNaming("1", "testPrefix-1-2020-47", "testPrefix"));
        Assert.assertTrue(es7Migration.isOldMessageIndexNaming("1", "testPrefix-1-2020-47-32", "testPrefix"));
        Assert.assertTrue(es7Migration.isOldMessageIndexNaming("1", "testPrefix-1-2020-47-32-15", "testPrefix"));
        Assert.assertTrue(es7Migration.isOldMessageIndexNaming("7472159827853538751", "testPrefix-7472159827853538751-2020-47", "testPrefix"));
        Assert.assertTrue(es7Migration.isOldMessageIndexNaming("7472159827853538751", "testPrefix-7472159827853538751-2020-47-32", "testPrefix"));
        Assert.assertTrue(es7Migration.isOldMessageIndexNaming("7472159827853538751", "testPrefix-7472159827853538751-2020-47-32-15", "testPrefix"));

        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "1-2020-47", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "1-2020-47-32", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "1-2020-47-32-15", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("7472159827853538751", "7472159827853538751-2020-47", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("7472159827853538751", "7472159827853538751-2020-47-32", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("7472159827853538751", "7472159827853538751-2020-47-32-15", "testPrefix"));

        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("7472159827853538751", "testPrefix-1-2020-47", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("7472159827853538751", "testPrefix-1-2020-47-32", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("7472159827853538751", "testPrefix-1-2020-47-32-15", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "testPrefix-7472159827853538751-2020-47", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "testPrefix-7472159827853538751-2020-47-32", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "testPrefix-7472159827853538751-2020-47-32-15", "testPrefix"));

        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "testPrefix-.1-channel", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "testPrefix-.1-client", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "testPrefix-.1-metric", "testPrefix"));

        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "testPrefix-1-channel", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "testPrefix-1-client", "testPrefix"));
        Assert.assertFalse(es7Migration.isOldMessageIndexNaming("1", "testPrefix-1-metric", "testPrefix"));
    }

    @Test
    public void dataMessageIndexNewNames() {
        Assert.assertEquals("1-data-message-2020-47", es7Migration.getNewDataMessageIndexName("1-2020-47", "1"));
        Assert.assertEquals("1-data-message-2020-47-32", es7Migration.getNewDataMessageIndexName("1-2020-47-32", "1"));
        Assert.assertEquals("1-data-message-2020-47-32-15", es7Migration.getNewDataMessageIndexName("1-2020-47-32-15", "1"));

        Assert.assertEquals("7472159827853538751-data-message-2020-47", es7Migration.getNewDataMessageIndexName("7472159827853538751-2020-47", "7472159827853538751"));
        Assert.assertEquals("7472159827853538751-data-message-2020-47-32", es7Migration.getNewDataMessageIndexName("7472159827853538751-2020-47-32", "7472159827853538751"));
        Assert.assertEquals("7472159827853538751-data-message-2020-47-32-15", es7Migration.getNewDataMessageIndexName("7472159827853538751-2020-47-32-15", "7472159827853538751"));

        Assert.assertEquals("testPrefix-1-data-message-2020-47", es7Migration.getNewDataMessageIndexName("testPrefix-1-2020-47", "testPrefix-1"));
        Assert.assertEquals("testPrefix-1-data-message-2020-47-32", es7Migration.getNewDataMessageIndexName("testPrefix-1-2020-47-32", "testPrefix-1"));
        Assert.assertEquals("testPrefix-1-data-message-2020-47-32-15", es7Migration.getNewDataMessageIndexName("testPrefix-1-2020-47-32-15", "testPrefix-1"));

        Assert.assertEquals("testPrefix-7472159827853538751-data-message-2020-47", es7Migration.getNewDataMessageIndexName("testPrefix-7472159827853538751-2020-47", "testPrefix-7472159827853538751"));
        Assert.assertEquals("testPrefix-7472159827853538751-data-message-2020-47-32", es7Migration.getNewDataMessageIndexName("testPrefix-7472159827853538751-2020-47-32", "testPrefix-7472159827853538751"));
        Assert.assertEquals("testPrefix-7472159827853538751-data-message-2020-47-32-15", es7Migration.getNewDataMessageIndexName("testPrefix-7472159827853538751-2020-47-32-15", "testPrefix-7472159827853538751"));
    }

    @Test
    public void dataRegistryIndexNewNames() {
        Assert.assertEquals("1-data-channel", es7Migration.getNewDataRegistryIndexName("1-data", "-channel"));
        Assert.assertEquals("1-data-client", es7Migration.getNewDataRegistryIndexName("1-data", "-client"));
        Assert.assertEquals("1-data-metric", es7Migration.getNewDataRegistryIndexName("1-data", "-metric"));

        Assert.assertEquals("7472159827853538751-data-channel", es7Migration.getNewDataRegistryIndexName("7472159827853538751-data", "-channel"));
        Assert.assertEquals("7472159827853538751-data-client", es7Migration.getNewDataRegistryIndexName("7472159827853538751-data", "-client"));
        Assert.assertEquals("7472159827853538751-data-metric", es7Migration.getNewDataRegistryIndexName("7472159827853538751-data", "-metric"));

        Assert.assertEquals("testPrefix-1-data-channel", es7Migration.getNewDataRegistryIndexName("testPrefix-1-data", "-channel"));
        Assert.assertEquals("testPrefix-1-data-client", es7Migration.getNewDataRegistryIndexName("testPrefix-1-data", "-client"));
        Assert.assertEquals("testPrefix-1-data-metric", es7Migration.getNewDataRegistryIndexName("testPrefix-1-data", "-metric"));

        Assert.assertEquals("testPrefix-7472159827853538751-data-channel", es7Migration.getNewDataRegistryIndexName("testPrefix-7472159827853538751-data", "-channel"));
        Assert.assertEquals("testPrefix-7472159827853538751-data-client", es7Migration.getNewDataRegistryIndexName("testPrefix-7472159827853538751-data", "-client"));
        Assert.assertEquals("testPrefix-7472159827853538751-data-metric", es7Migration.getNewDataRegistryIndexName("testPrefix-7472159827853538751-data", "-metric"));
    }

}

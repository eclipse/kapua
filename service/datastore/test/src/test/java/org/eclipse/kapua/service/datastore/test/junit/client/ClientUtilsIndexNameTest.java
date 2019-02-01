/*******************************************************************************
 * Copyright (c) 2017, 2019 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.test.junit.client;

import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.test.junit.DatastoreJAXBContextProvider;

import org.eclipse.kapua.test.junit.JUnitTests;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ClientUtilsIndexNameTest extends Assert {

    private static final KapuaId ONE = new KapuaEid(BigInteger.ONE);

    @BeforeClass
    public static void setUpBeforeClass() {
        XmlUtil.setContextProvider(new DatastoreJAXBContextProvider());
    }

    @Test
    public void test1() {
        final Instant instant = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
        try {
            // Index by Week
            Assert.assertEquals("1-2017-01", DatastoreUtils.getDataIndexName(ONE, instant.toEpochMilli(), DatastoreUtils.INDEXING_WINDOW_OPTION_WEEK));

            // Index by Day
            Assert.assertEquals("1-2017-01-01", DatastoreUtils.getDataIndexName(ONE, instant.toEpochMilli(), DatastoreUtils.INDEXING_WINDOW_OPTION_DAY));

            // Index by Hour
            Assert.assertEquals("1-2017-01-01-00", DatastoreUtils.getDataIndexName(ONE, instant.toEpochMilli(), DatastoreUtils.INDEXING_WINDOW_OPTION_HOUR));
        } catch (KapuaException kaex) {
            Assert.fail("Error while generating index name");
        }
    }

    @Test
    public void test2() {
        final Instant instant = ZonedDateTime.of(2017, 1, 8, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
        try {
            // Index by Week
            Assert.assertEquals("1-2017-02", DatastoreUtils.getDataIndexName(ONE, instant.toEpochMilli(), DatastoreUtils.INDEXING_WINDOW_OPTION_WEEK));

            // Index by Day
            Assert.assertEquals("1-2017-02-01", DatastoreUtils.getDataIndexName(ONE, instant.toEpochMilli(), DatastoreUtils.INDEXING_WINDOW_OPTION_DAY));

            // Index by Hour
            Assert.assertEquals("1-2017-02-01-00", DatastoreUtils.getDataIndexName(ONE, instant.toEpochMilli(), DatastoreUtils.INDEXING_WINDOW_OPTION_HOUR));
        } catch (KapuaException kaex) {
            Assert.fail("Error while generating index name");
        }
    }
}

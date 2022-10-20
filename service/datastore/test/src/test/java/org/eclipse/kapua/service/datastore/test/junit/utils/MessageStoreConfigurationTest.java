/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.test.junit.utils;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Category(JUnitTests.class)
public class MessageStoreConfigurationTest {

    @Test
    public void test1() {
        Map<String, Object> values = new HashMap<>();

        values.put(MessageStoreConfiguration.CONFIGURATION_EXPIRATION_DATE_KEY, "2017-06-30T16:30:00.999Z");
        values.put(MessageStoreConfiguration.CONFIGURATION_DATA_TTL_KEY, 42);

        MessageStoreConfiguration cfg = new MessageStoreConfiguration(values);

        Assert.assertNotNull(cfg);

        Assert.assertEquals(42, cfg.getDataTimeToLive());
        Assert.assertEquals(Duration.ofDays(42).toMillis(), cfg.getDataTimeToLiveMilliseconds());

        Assert.assertEquals(Date.from(ZonedDateTime.of(2017, 6, 30, 16, 30, 0, 999000000, ZoneOffset.UTC).toInstant()), cfg.getExpirationDate());
    }
}

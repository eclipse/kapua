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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class MessageStoraConfigurationTest {
    
    @Test
    public void test1 () {
        Map<String, Object> values = new HashMap<>();
        
        values.put(MessageStoreConfiguration.CONFIGURATION_EXPIRATION_DATE_KEY, "02/24/2017 4:21 PM");
        values.put(MessageStoreConfiguration.CONFIGURATION_DATA_TTL_KEY, 42);
        
        MessageStoreConfiguration cfg = new MessageStoreConfiguration (values);
        
        Assert.assertNotNull(cfg);
        
        Assert.assertEquals(42, cfg.getDataTimeToLive());
        Assert.assertEquals(Duration.ofDays(42).toMillis(), cfg.getDataTimeToLiveMilliseconds());
        
        Assert.assertEquals(Date.from(ZonedDateTime.of(2017, 2, 24, 16, 21, 0, 0, ZoneOffset.UTC).toInstant()), cfg.getExpirationDate());
    }
}

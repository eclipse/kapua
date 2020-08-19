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
package org.eclipse.kapua.broker.core.plugin.metric;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class PublishMetricTest extends Assert {

    PublishMetric publishMetric;

    @Before
    public void start() {
        publishMetric = PublishMetric.getInstance();
    }

    @Test
    public void gettersTest() {
        assertNotNull(publishMetric.getAllowedMessages());
        assertNotNull(publishMetric.getNotAllowedMessages());
        assertNotNull(publishMetric.getTime());
        assertNotNull(publishMetric.getMessageSizeAllowed());
        assertNotNull(publishMetric.getMessageSizeNotAllowed());
    }
}

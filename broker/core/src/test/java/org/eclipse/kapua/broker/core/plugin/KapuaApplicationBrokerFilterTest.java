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
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.event.ServiceEventBusException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaApplicationBrokerFilterTest extends Assert {

    @Test(expected = ServiceEventBusException.class)
    public void startNullTest() throws Exception {
        KapuaApplicationBrokerFilter filter = new KapuaApplicationBrokerFilter(null);
        filter.start();
    }

    @Test(expected = NullPointerException.class)
    public void stopNullTest() throws Exception {
        KapuaApplicationBrokerFilter filter = new KapuaApplicationBrokerFilter(null);
        filter.stop();
    }

}

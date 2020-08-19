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
package org.eclipse.kapua.broker.core.converter;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class DeviceManagementNotificationConverterTest extends Assert {

    DeviceManagementNotificationConverter converter;

    @Before
    public void start() {
        converter = new DeviceManagementNotificationConverter();
    }

    @Test(expected = NullPointerException.class)
    public void convertToManagementNotificationNullTest() throws KapuaException {
        converter.convertToManagementNotification(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void convertToManagementNotificationOnExceptionNullTest() throws KapuaException {
        converter.convertToManagementNotificationOnException(null, null);
    }

}

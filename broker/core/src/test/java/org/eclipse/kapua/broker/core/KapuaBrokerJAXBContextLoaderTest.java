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
package org.eclipse.kapua.broker.core;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaBrokerJAXBContextLoaderTest extends Assert {

    KapuaBrokerJAXBContextLoader kapuaBrokerJAXBContextLoader;

    @Before
    public void initialize() {
        kapuaBrokerJAXBContextLoader = new KapuaBrokerJAXBContextLoader();
    }

    @Test
    public void initTest() {
        try {
            kapuaBrokerJAXBContextLoader.init();
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void resetTest() {
        try {
            kapuaBrokerJAXBContextLoader.reset();
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }
}
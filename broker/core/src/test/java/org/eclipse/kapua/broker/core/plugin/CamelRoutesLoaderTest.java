/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.KapuaBrokerJAXBContextLoader;
import org.eclipse.kapua.broker.core.router.CamelKapuaDefaultRouter;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class CamelRoutesLoaderTest {

    private KapuaBrokerJAXBContextLoader kapuaBrokerJAXBContextLoader;

    @Before
    public void initJAXBContext() throws KapuaException {
        kapuaBrokerJAXBContextLoader = new KapuaBrokerJAXBContextLoader();
        kapuaBrokerJAXBContextLoader.init();
    }

    @After
    public void resetJAXBContext() {
        kapuaBrokerJAXBContextLoader.reset();
    }

    @Test
    public void testRouteLoad() {
        new CamelKapuaDefaultRouter();
    }
}

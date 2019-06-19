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
package org.eclipse.kapua.locator.internal;

import org.eclipse.kapua.locator.KapuaLocatorException;
import org.eclipse.kapua.locator.guice.ServiceResolver;
import org.eclipse.kapua.locator.internal.guice.FactoryA;
import org.eclipse.kapua.locator.internal.guice.FactoryAImpl;
import org.eclipse.kapua.locator.internal.guice.FactoryC;
import org.eclipse.kapua.locator.internal.guice.ServiceA;
import org.eclipse.kapua.locator.internal.guice.ServiceAImpl;
import org.eclipse.kapua.locator.internal.guice.ServiceC;
import org.eclipse.kapua.locator.internal.guice.ServiceCImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ResolverTest {

    @Test(expected = KapuaLocatorException.class)
    public void testWrongServiceClass() throws KapuaLocatorException {
        ServiceResolver.newInstance(ServiceC.class, ServiceCImpl.class);
    }

    @Test
    public void testCorrectServiceClass() throws KapuaLocatorException {
        ServiceResolver.newInstance(ServiceA.class, ServiceAImpl.class);
    }

    @Test(expected = KapuaLocatorException.class)
    public void testWrongFactoryClass() throws KapuaLocatorException {
        ServiceResolver.newInstance(FactoryC.class, ServiceCImpl.class);
    }

    @Test
    public void testCorrectFactoryClass() throws KapuaLocatorException {
        ServiceResolver.newInstance(FactoryA.class, FactoryAImpl.class);
    }
}

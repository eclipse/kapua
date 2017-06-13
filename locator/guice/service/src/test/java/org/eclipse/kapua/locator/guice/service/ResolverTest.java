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
package org.eclipse.kapua.locator.guice.service;

import org.eclipse.kapua.locator.KapuaLocatorException;
import org.eclipse.kapua.locator.guice.service.ServiceResolver;
import org.eclipse.kapua.locator.guice.service.internal.FactoryA;
import org.eclipse.kapua.locator.guice.service.internal.FactoryAImpl;
import org.eclipse.kapua.locator.guice.service.internal.FactoryC;
import org.eclipse.kapua.locator.guice.service.internal.ServiceA;
import org.eclipse.kapua.locator.guice.service.internal.ServiceAImpl;
import org.eclipse.kapua.locator.guice.service.internal.ServiceC;
import org.eclipse.kapua.locator.guice.service.internal.ServiceCImpl;
import org.junit.Test;

public class ResolverTest {
    
    @Test(expected=KapuaLocatorException.class)
    public void testWrongServiceClass () throws KapuaLocatorException  {
        ServiceResolver.newInstance(ServiceC.class, ServiceCImpl.class);
    }
    
    @Test
    public void testCorrectServiceClass () throws KapuaLocatorException  {
        ServiceResolver.newInstance(ServiceA.class, ServiceAImpl.class);
    }
    
    @Test(expected=KapuaLocatorException.class)
    public void testWrongFactoryClass () throws KapuaLocatorException  {
        ServiceResolver.newInstance(FactoryC.class, ServiceCImpl.class);
    }
    
    @Test
    public void testCorrectFactoryClass () throws KapuaLocatorException  {
        ServiceResolver.newInstance(FactoryA.class, FactoryAImpl.class);
    }
}
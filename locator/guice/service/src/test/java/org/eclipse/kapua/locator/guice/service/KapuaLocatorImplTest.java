/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat - improved tests coverage
 *******************************************************************************/
package org.eclipse.kapua.locator.guice.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.Container;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.guice.service.KapuaLocatorImpl;
import org.eclipse.kapua.locator.guice.service.TestService;
import org.eclipse.kapua.locator.guice.service.internal.FactoryA;
import org.eclipse.kapua.locator.guice.service.internal.FactoryB;
import org.eclipse.kapua.locator.guice.service.internal.ServiceA;
import org.eclipse.kapua.locator.guice.service.internal.ServiceB;
import org.eclipse.kapua.locator.guice.service.internal.ServiceC;
import org.eclipse.kapua.service.KapuaService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class KapuaLocatorImplTest {

    Container container = new Container() {};
    KapuaLocator locator = KapuaLocatorImpl.getInstance();

    @Before
    public void before() {
        container.startup();
    }
    
    @After
    public void after() {
        container.shutdown();
    }
    
    @Ignore
    @Test
    public void shouldThrowKapuaExceptionWhenServiceIsNotAvailable() {
        try {
            locator.getService(MyService.class);
        } catch (KapuaRuntimeException e) {
            assertEquals(KapuaLocatorErrorCodes.SERVICE_UNAVAILABLE.name(), e.getCode().name());
            return;
        }
        fail();
    }

    @Ignore
    @Test
    public void shouldLoadTestService() {
        MyTestableService service = locator.getService(MyTestableService.class);
        Assert.assertTrue(service instanceof TestMyTestableService);
    }

    @Test
    public void shouldProvideServiceA() {
        Assert.assertNotNull(locator.getService(ServiceA.class));
    }

    @Test(expected = KapuaRuntimeException.class)
    public void shouldProvideServiceB() {
        Assert.assertNotNull(locator.getService(ServiceB.class));
    }

    @Test
    public void shouldProvideFactoryA() {
        Assert.assertNotNull(locator.getFactory(FactoryA.class));
    }

    @Test
    public void shouldNotFindFactory() {
        try {
            locator.getFactory(FactoryB.class);
            Assert.fail("getFactory must throw an exception for un-bound factories");
        } catch (KapuaRuntimeException e) {
            Assert.assertEquals(KapuaLocatorErrorCodes.FACTORY_UNAVAILABLE, e.getCode());
        } catch (Throwable e) {
            Assert.fail("Exception must be of type: " + KapuaRuntimeException.class.getName());
        }
    }

    @Test
    public void shouldProvideAll() {
        List<KapuaService> result = locator.getServices();
        Assert.assertEquals(1, result.size());

        {
            KapuaService service = result.get(0);
            Assert.assertTrue(service instanceof ServiceA);
        }
    }
    
    @Test(expected=KapuaRuntimeException.class)
    public void shouldNotLoad () {
        KapuaLocator locator = new KapuaLocatorImpl("locator-1.xml");
        locator.getService(ServiceC.class);
    }

    static interface MyService extends KapuaService {
    }

    interface MyTestableService extends KapuaService {

    }

    public static class MyTestableServiceImpl implements MyTestableService {

    }

    @TestService
    public static class TestMyTestableService implements MyTestableService {

    }

}

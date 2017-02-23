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
 *
 *******************************************************************************/
package org.eclipse.kapua.locator.internal;

import static org.junit.Assert.*;

import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.guice.GuiceLocatorImpl;
import org.eclipse.kapua.locator.guice.TestService;
import org.eclipse.kapua.service.KapuaService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class GuiceLocatorImplTest {

	KapuaLocator locator = GuiceLocatorImpl.getInstance();

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

	@Test
	public void shouldLoadTestService() {
		MyTestableService service = locator.getService(MyTestableService.class);
		Assert.assertTrue(service instanceof TestMyTestableService);
	}

	static interface MyService extends KapuaService {}

	interface MyTestableService extends KapuaService {

	}

	public static class MyTestableServiceImpl implements MyTestableService {

	}

	@TestService
	public static class TestMyTestableService implements MyTestableService {

	}

}

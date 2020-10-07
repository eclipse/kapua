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
package org.eclipse.kapua.transport.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(JUnitTests.class)
public class TransportExceptionTest extends Assert {

    TransportException transportException;
    private static final String KAPUA_ERROR_MESSAGES = "transport-client-error-messages";

    @Before
    public void start() {
        transportException = Mockito.mock(TransportException.class);
        Mockito.doCallRealMethod().when(transportException).getKapuaErrorMessagesBundle();
    }

    @Test
    public void getKapuaErrorMessagesBundleTest() {
        assertEquals(KAPUA_ERROR_MESSAGES, transportException.getKapuaErrorMessagesBundle());
    }
}

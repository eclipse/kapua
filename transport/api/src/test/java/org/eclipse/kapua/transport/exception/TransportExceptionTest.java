/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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

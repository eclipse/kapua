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
package org.eclipse.kapua;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaUnauthenticatedExceptionTest extends Assert {

    @Test
    public void kapuaUnauthenticatedExceptionTest() {
        KapuaUnauthenticatedException kapuaUnauthenticatedException = new KapuaUnauthenticatedException();
        assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.UNAUTHENTICATED, kapuaUnauthenticatedException.getCode());
        assertNull("Null expected.", kapuaUnauthenticatedException.getCause());
        assertEquals("Expected and actual values should be the same.", "No authenticated Subject found in context.", kapuaUnauthenticatedException.getMessage());
    }

    @Test(expected = KapuaUnauthenticatedException.class)
    public void throwingExceptionTest() throws KapuaUnauthenticatedException {
        throw new KapuaUnauthenticatedException();
    }
}
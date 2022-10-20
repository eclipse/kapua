/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class KapuaUnauthenticatedExceptionTest {

    @Test
    public void kapuaUnauthenticatedExceptionTest() {
        KapuaUnauthenticatedException kapuaUnauthenticatedException = new KapuaUnauthenticatedException();
        Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.UNAUTHENTICATED, kapuaUnauthenticatedException.getCode());
        Assert.assertNull("Null expected.", kapuaUnauthenticatedException.getCause());
        Assert.assertEquals("Expected and actual values should be the same.", "No authenticated Subject found in context.", kapuaUnauthenticatedException.getMessage());
    }

    @Test(expected = KapuaUnauthenticatedException.class)
    public void throwingExceptionTest() throws KapuaUnauthenticatedException {
        throw new KapuaUnauthenticatedException();
    }
}

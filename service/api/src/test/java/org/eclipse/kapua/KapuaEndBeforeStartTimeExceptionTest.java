/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
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
public class KapuaEndBeforeStartTimeExceptionTest extends Assert {

    @Test
    public void kapuaEndBeforeStartTimeExceptionTest() {
        KapuaEndBeforeStartTimeException kapuaEndBeforeStartTimeException = new KapuaEndBeforeStartTimeException();
        assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.END_BEFORE_START_TIME_ERROR, kapuaEndBeforeStartTimeException.getCode());
        assertNull("Null expected", kapuaEndBeforeStartTimeException.getCause());
        assertEquals("Expected and actual values should be the same.", "The start time cannot be later than the end time.", kapuaEndBeforeStartTimeException.getMessage());
    }

    @Test(expected = KapuaEndBeforeStartTimeException.class)
    public void throwingExceptionTest() throws KapuaEndBeforeStartTimeException {
        throw new KapuaEndBeforeStartTimeException();
    }
} 

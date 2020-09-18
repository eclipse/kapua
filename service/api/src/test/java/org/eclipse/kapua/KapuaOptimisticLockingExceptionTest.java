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
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaOptimisticLockingExceptionTest extends Assert {

    Exception[] exceptions;

    @Before
    public void initialize() {
        exceptions = new Exception[]{new Exception(), null};
    }

    @Test
    public void kapuaOptimisticLockingExceptionTest() {
        for (Exception exception : exceptions) {
            KapuaOptimisticLockingException kapuaOptimisticLockingException = new KapuaOptimisticLockingException(exception);
            assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.OPTIMISTIC_LOCKING, kapuaOptimisticLockingException.getCode());
            assertEquals("Expected and actual values should be the same.", exception, kapuaOptimisticLockingException.getCause());
            assertEquals("Expected and actual values should be the same.", "The entity is out of state as it has been modified or deleted by another transaction.", kapuaOptimisticLockingException.getMessage());
        }
    }

    @Test(expected = KapuaOptimisticLockingException.class)
    public void throwingExceptionTest() throws KapuaOptimisticLockingException {
        for (Exception exception : exceptions) {
            throw new KapuaOptimisticLockingException(exception);
        }
    }
}  
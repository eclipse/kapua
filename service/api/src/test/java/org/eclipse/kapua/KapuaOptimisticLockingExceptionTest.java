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
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class KapuaOptimisticLockingExceptionTest {

    Exception[] exceptions;

    @Before
    public void initialize() {
        exceptions = new Exception[]{new Exception(), null};
    }

    @Test
    public void kapuaOptimisticLockingExceptionTest() {
        for (Exception exception : exceptions) {
            KapuaOptimisticLockingException kapuaOptimisticLockingException = new KapuaOptimisticLockingException(exception);
            Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.OPTIMISTIC_LOCKING, kapuaOptimisticLockingException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", exception, kapuaOptimisticLockingException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", "The entity is out of state as it has been modified or deleted by another transaction.", kapuaOptimisticLockingException.getMessage());
        }
    }

    @Test(expected = KapuaOptimisticLockingException.class)
    public void throwingExceptionTest() throws KapuaOptimisticLockingException {
        for (Exception exception : exceptions) {
            throw new KapuaOptimisticLockingException(exception);
        }
    }
}  

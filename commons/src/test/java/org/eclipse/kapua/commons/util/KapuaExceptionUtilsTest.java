/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.lang.reflect.Constructor;

import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaExceptionUtilsTest extends Assert {

    @Test
    public void testConstructor() throws Exception {
        Constructor<KapuaExceptionUtils> exceptionUtil = KapuaExceptionUtils.class.getDeclaredConstructor();
        exceptionUtil.setAccessible(true);
        @SuppressWarnings("unused")
        KapuaExceptionUtils exceptionUtilTest = exceptionUtil.newInstance();
    }

    @Test
    public void testConvertPersistenceException() {
        Throwable t = new Throwable();
        Throwable s = new PersistenceException("Persistence Exception", t);
        KapuaException kapExc0 = KapuaException.internalError(null, null);
        KapuaException kapExc1 = KapuaException.internalError(t, "error");
        KapuaException kapExc2 = KapuaException.internalError("string");
        KapuaException kapExc3 = KapuaException.internalError(t);
        KapuaException kapExc4 = KapuaException.internalError(s);
        OptimisticLockException optimisticLock = new OptimisticLockException();
        Throwable q = new Throwable();
        Throwable w = new RollbackException("Rollback exception", q);
        PersistenceException persistExc0 = new PersistenceException(w);
        Throwable e = new Throwable();
        Throwable r = new RollbackException("Rollback exception", e);
        Throwable a = new RollbackException("rollback2", r);
        PersistenceException persistExc1 = new PersistenceException(a);

        Exception[] listOfPermittedExceptions = new Exception[]{kapExc0,kapExc1,kapExc2,kapExc3,
                kapExc4,persistExc0, persistExc1,optimisticLock};
        int sizeOfExceptions = listOfPermittedExceptions.length;
        // positive tests
        for (int i = 0; i < sizeOfExceptions; i++) {
            try {
                KapuaExceptionUtils.convertPersistenceException(listOfPermittedExceptions[i]);
            } catch (Exception ex) {
                // Expected
                fail("No exception expected for: " + listOfPermittedExceptions[i]);
            }
        }
    }
}
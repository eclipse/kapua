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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdStatic;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;

@Category(JUnitTests.class)
public class KapuaEntityExistsExceptionTest extends Assert {

    Throwable[] throwables;
    KapuaId[] ids;

    @Before
    public void initialize() {
        throwables = new Throwable[]{new Throwable(), null};
        ids = new KapuaId[]{new KapuaIdStatic(BigInteger.ONE), null};
    }

    @Test
    public void kapuaEntityExistsExceptionTest() {
        for (Throwable throwable : throwables) {
            for (KapuaId id : ids) {
                KapuaEntityExistsException kapuaEntityExistsException = new KapuaEntityExistsException(throwable, id);
                assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ENTITY_ALREADY_EXISTS, kapuaEntityExistsException.getCode());
                assertEquals("Expected and actual values should be the same.", id, kapuaEntityExistsException.getId());
                assertEquals("Expected and actual values should be the same.", throwable, kapuaEntityExistsException.getCause());
                assertEquals("Expected and actual values should be the same.", "Error: ", kapuaEntityExistsException.getMessage());
            }
        }
    }

    @Test(expected = KapuaEntityExistsException.class)
    public void throwingExceptionTest() {
        for (Throwable throwable : throwables) {
            for (KapuaId id : ids) {
                throw new KapuaEntityExistsException(throwable, id);
            }
        }
    }
}  
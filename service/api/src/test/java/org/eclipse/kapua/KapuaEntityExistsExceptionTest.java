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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;


@Category(JUnitTests.class)
public class KapuaEntityExistsExceptionTest {

    Throwable[] throwables;
    KapuaId[] ids;

    @Before
    public void initialize() {
        throwables = new Throwable[]{new Throwable(), null};
        ids = new KapuaId[]{new KapuaIdImpl(BigInteger.ONE), null};
    }

    @Test
    public void kapuaEntityExistsExceptionTest() {
        for (Throwable throwable : throwables) {
            for (KapuaId id : ids) {
                KapuaEntityExistsException kapuaEntityExistsException = new KapuaEntityExistsException(throwable, id);
                Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ENTITY_ALREADY_EXISTS, kapuaEntityExistsException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", id, kapuaEntityExistsException.getId());
                Assert.assertEquals("Expected and actual values should be the same.", throwable, kapuaEntityExistsException.getCause());
                Assert.assertEquals("Expected and actual values should be the same.", "Error: ", kapuaEntityExistsException.getMessage());
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

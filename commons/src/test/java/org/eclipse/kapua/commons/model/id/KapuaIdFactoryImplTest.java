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
package org.eclipse.kapua.commons.model.id;

import org.eclipse.kapua.commons.util.RandomUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;
import java.util.Random;

@Category(JUnitTests.class)
public class KapuaIdFactoryImplTest extends Assert {

    private final static Random RANDOM = RandomUtils.getInstance();

    @Test
    public void newKapuaIdShortIdTest() {
        KapuaIdFactoryImpl kapuaIdFactoryImpl = new KapuaIdFactoryImpl();
        String shortId = "12";
        KapuaId actualKapuaId = kapuaIdFactoryImpl.newKapuaId(shortId);
        KapuaId expectedKapuaId = KapuaEid.parseCompactId(shortId);

        assertEquals("Actual and expected values are not the same", expectedKapuaId.getId(), actualKapuaId.getId());
    }

    @Test
    public void newKapuaIdBigIntegerTest() {
        KapuaIdFactoryImpl kapuaIdFactoryImpl = new KapuaIdFactoryImpl();
        BigInteger bigInteger = new BigInteger(64, RANDOM);
        BigInteger bigIntegerMaxValue = new BigInteger(String.valueOf(2 ^ (Integer.MAX_VALUE)));
        BigInteger bigIntegerMinValue = new BigInteger(String.valueOf(-2 ^ (Integer.MAX_VALUE)));
        KapuaId actualKapuaId = kapuaIdFactoryImpl.newKapuaId(bigInteger);
        KapuaId actualKapuaIdNull = kapuaIdFactoryImpl.newKapuaId((BigInteger) null);
        KapuaId actualKapuaIdMax = kapuaIdFactoryImpl.newKapuaId(bigIntegerMaxValue);
        KapuaId actualKapuaIdMin = kapuaIdFactoryImpl.newKapuaId(bigIntegerMinValue);

        assertEquals("Actual and expected values are not the same", bigInteger, actualKapuaId.getId());
        assertEquals("Actual and expected values are not the same", bigIntegerMaxValue, actualKapuaIdMax.getId());
        assertEquals("Actual and expected values are not the same", bigIntegerMinValue, actualKapuaIdMin.getId());
        assertNull("Actual value should be null", actualKapuaIdNull.getId());
    }
}

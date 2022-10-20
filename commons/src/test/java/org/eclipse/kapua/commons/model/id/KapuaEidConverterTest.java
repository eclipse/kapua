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
package org.eclipse.kapua.commons.model.id;

import org.eclipse.kapua.commons.util.RandomUtils;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;


@Category(JUnitTests.class)
@RunWith(value = Parameterized.class)
public class KapuaEidConverterTest {

    private final static Random RANDOM = RandomUtils.getInstance();

    private final BigInteger eid;

    public KapuaEidConverterTest(BigInteger eid) {
        this.eid = eid;
    }

    @Parameters
    public static Iterable<Object[]> eids() {
        return Arrays.asList(
                new Object[]{new BigInteger(64, RANDOM)},
                new Object[]{null});
    }

    @Test
    public void convertToDatabaseColumnTest() {
        KapuaEid kapuaEid = new KapuaEid(eid);
        KapuaEidConverter kapuaEidConverter = new KapuaEidConverter();
        Assert.assertEquals("Actual and expected values are not the same", eid, kapuaEidConverter.convertToDatabaseColumn(kapuaEid));
    }

    @Test
    public void convertToEntityAttributeTest() {
        KapuaEidConverter kapuaEidConverter = new KapuaEidConverter();
        KapuaEid kapuaEid = kapuaEidConverter.convertToEntityAttribute(eid);
        Assert.assertEquals("Actual and expected values are not the same", eid, kapuaEid.getId());
    }

}

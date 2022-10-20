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
package org.eclipse.kapua.commons.event;

import com.codahale.metrics.Counter;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;


@Category(JUnitTests.class)
public class RaiseServiceEventInterceptorTest {

    @Before
    public void createInstanceOfClass() {
        raiseServiceEventInterceptor = new RaiseServiceEventInterceptor();
    }

    RaiseServiceEventInterceptor raiseServiceEventInterceptor;

    @Test
    public void checkingPrivateFieldsTest() throws NoSuchFieldException {
        Field privateEnumStringMODULE = RaiseServiceEventInterceptor.class.getDeclaredField("MODULE");
        Field privateEnumStringCOMPONENT = RaiseServiceEventInterceptor.class.getDeclaredField("COMPONENT");
        Field privateEnumStringACTION = RaiseServiceEventInterceptor.class.getDeclaredField("ACTION");
        Field privateEnumStringCOUNT = RaiseServiceEventInterceptor.class.getDeclaredField("COUNT");

        privateEnumStringMODULE.setAccessible(true);
        privateEnumStringCOMPONENT.setAccessible(true);
        privateEnumStringACTION.setAccessible(true);
        privateEnumStringCOUNT.setAccessible(true);

        Assert.assertEquals("The fields are not equal", "private static final java.lang.String org.eclipse.kapua.commons.event.RaiseServiceEventInterceptor.MODULE", privateEnumStringMODULE.toString());
        Assert.assertEquals("The fields are not equal", "private static final java.lang.String org.eclipse.kapua.commons.event.RaiseServiceEventInterceptor.COMPONENT", privateEnumStringCOMPONENT.toString());
        Assert.assertEquals("The fields are not equal", "private static final java.lang.String org.eclipse.kapua.commons.event.RaiseServiceEventInterceptor.ACTION", privateEnumStringACTION.toString());
        Assert.assertEquals("The fields are not equal", "private static final java.lang.String org.eclipse.kapua.commons.event.RaiseServiceEventInterceptor.COUNT", privateEnumStringCOUNT.toString());

    }

    @Test
    public void constructorTest() throws Exception {
        Constructor<RaiseServiceEventInterceptor> constructor = RaiseServiceEventInterceptor.class.getConstructor();
        RaiseServiceEventInterceptor raiseServiceEventInterceptor = constructor.newInstance();

        Field wrongId = RaiseServiceEventInterceptor.class.getDeclaredField("wrongId");
        Field wrongEntity = RaiseServiceEventInterceptor.class.getDeclaredField("wrongEntity");

        wrongId.setAccessible(true);
        wrongEntity.setAccessible(true);

        Object wrongIdValue = wrongId.get(raiseServiceEventInterceptor);
        Object wrongEntityValue = wrongEntity.get(raiseServiceEventInterceptor);

        Assert.assertTrue(wrongIdValue instanceof Counter);
        Assert.assertTrue(wrongEntityValue instanceof Counter);
    }

    @Test(expected = NullPointerException.class)
    public void invokeNullTest() throws Throwable {
        raiseServiceEventInterceptor.invoke(null);
    }
}

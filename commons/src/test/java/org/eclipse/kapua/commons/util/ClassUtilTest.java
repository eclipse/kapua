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
package org.eclipse.kapua.commons.util;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.event.ServiceEntry;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;


@Category(JUnitTests.class)
public class ClassUtilTest {

    String serviceName, address;

    @Test
    public void classUtilTest() throws Exception {
        Constructor<ClassUtil> classUtilConstructor = ClassUtil.class.getDeclaredConstructor();
        classUtilConstructor.setAccessible(true);
        classUtilConstructor.newInstance();
    }

    @Test(expected = NullPointerException.class)
    public void newInstanceNullTest() throws KapuaException {
        Assert.assertNull("The class does not exist.", ClassUtil.newInstance(null, null));
    }

    @Test(expected = KapuaException.class)
    public void newInstanceTest() throws KapuaException {
        ClassUtil.newInstance("org.eclipse.kapua.commons.event.ServiceEntry", ServiceEntry.class);
    }

    @Test(expected = KapuaException.class)
    public void newInstanceClazzNullTest() throws KapuaException {
        ClassUtil.newInstance(null, ServiceEntry.class);
    }

    @Test(expected = KapuaException.class)
    public void newInstanceDefaultInstanceNullTest() throws KapuaException {
        ClassUtil.newInstance("org.eclipse.kapua.commons.event.ServiceEntry", null);
    }

    @Test
    public void newInstanceStringTest() throws KapuaException {
        Assert.assertNotNull("Null not expected.", ClassUtil.newInstance("java.lang.String", String.class));
        Assert.assertThat("Instance of String expected.", ClassUtil.newInstance("java.lang.String", String.class), IsInstanceOf.instanceOf(String.class));
    }

    @Test
    public void extendedNewInstanceTest() throws KapuaException {
        Assert.assertNotNull("The class does not exist.", ClassUtil.newInstance("org.eclipse.kapua.commons.event.ServiceEntry", ServiceEntry.class, new Class<?>[]{String.class, String.class}, new Object[]{serviceName, address}));
    }

    @Test
    public void extendedNewInstanceEmptyClazzTest() throws KapuaException {
        Assert.assertNotNull("The class does not exist.", ClassUtil.newInstance("", ServiceEntry.class, new Class<?>[]{String.class, String.class}, new Object[]{serviceName, address}));
    }

    @Test(expected = KapuaException.class)
    public void extendedNewInstanceParameterTypeNullTest() throws KapuaException {
        Assert.assertNull("The class does not exist.", ClassUtil.newInstance("org.eclipse.kapua.commons.event.ServiceEntry", ServiceEntry.class, null, new Object[]{serviceName, address}));
    }

    @Test(expected = KapuaException.class)
    public void extendedNewInstanceEmptyParametersTypeTest() throws KapuaException {
        ClassUtil.newInstance("org.eclipse.kapua.commons.event.ServiceEntry", ServiceEntry.class, new Class<?>[]{}, new Object[]{serviceName, address});
    }

    @Test(expected = KapuaException.class)
    public void extendedNewInstanceParameterNullTest() throws KapuaException {
        Assert.assertNull("The class does not exist.", ClassUtil.newInstance("org.eclipse.kapua.commons.event.ServiceEntry", ServiceEntry.class, new Class<?>[]{String.class, String.class}, null));
    }

    @Test(expected = KapuaException.class)
    public void extendedNewInstanceParametersNullTest() throws KapuaException {
        Assert.assertNull("The class does not exist.", ClassUtil.newInstance("org.eclipse.kapua.commons.event.ServiceEntry", ServiceEntry.class, null, null));
    }

    @Test(expected = KapuaException.class)
    public void extendedNewInstanceWrongPackageNameTest() throws KapuaException {
        Assert.assertNull("The class does not exist.", ClassUtil.newInstance("org.eclipse.kapua.commons.ServiceEntry", ServiceEntry.class, null, null));
    }

    @Test(expected = KapuaException.class)
    public void extendedNewInstanceWrongParametersTypeTest() throws KapuaException {
        Assert.assertNull("PARAMETER_ERROR_MSG", ClassUtil.newInstance("org.eclipse.kapua.commons.event.ServiceEntry", ServiceEntry.class, new Class<?>[]{String.class, Object.class}, new Object[]{serviceName, address}));
    }

    @Test(expected = KapuaException.class)
    public void extendedNewInstanceWrongParametersTypeNumberTest() throws KapuaException {
        Assert.assertNull("PARAMETER_ERROR_MSG", ClassUtil.newInstance("org.eclipse.kapua.commons.event.ServiceEntry", ServiceEntry.class, new Class<?>[]{String.class, String.class, String.class}, new Object[]{serviceName, address}));
    }

    @Test(expected = NullPointerException.class)
    public void extendedNewInstanceAllNullTest() throws KapuaException {
        Assert.assertNull("The class does not exist.", ClassUtil.newInstance(null, null, null, null));
    }
}

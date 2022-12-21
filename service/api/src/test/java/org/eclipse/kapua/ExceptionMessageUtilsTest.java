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
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.MissingResourceException;


@Category(JUnitTests.class)
public class ExceptionMessageUtilsTest {

    String[] resourceBundleName;
    Locale locale;
    KapuaErrorCode kapuaErrorCode;
    KapuaErrorCode mockedKapuaErrorCode;
    Object[] objectList;
    Object[] emptyObjectList;
    String[] kapuaGenericMessageWithObject;
    String[] kapuaGenericMessage;

    @Before
    public void initialize() {
        resourceBundleName = new String[]{"kapua-service-error-messages", "Resource Bundle Name"};
        locale = Locale.ENGLISH;
        kapuaErrorCode = KapuaErrorCodes.ADMIN_ROLE_DELETED_ERROR;
        mockedKapuaErrorCode = Mockito.mock(KapuaErrorCode.class);
        objectList = new Object[]{0, 10, 100000, "String", 'c', -10, -1000000000, -100000000000L, 10L, 10.0f, null, 10.10d, true, false};
        emptyObjectList = new Object[]{};
        kapuaGenericMessageWithObject = new String[]{"Operation not allowed on admin role.", "Error: 0, 10, 100000, String, c, -10, -1000000000, -100000000000, 10, 10.0, null, 10.1, true, false", "Error: "};
        kapuaGenericMessage = new String[]{"Operation not allowed on admin role.", "Error: "};
    }

    @Test
    public void exceptionMessageUtilsTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ExceptionMessageUtils> exceptionMessageUtils = ExceptionMessageUtils.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(exceptionMessageUtils.getModifiers()));
        exceptionMessageUtils.setAccessible(true);
        exceptionMessageUtils.newInstance();
    }

    @Test
    public void getLocalizedMessageTest() {
        for (int i = 0; i < resourceBundleName.length; i++) {
            Assert.assertEquals("Expected and actual values should be the same.", kapuaGenericMessageWithObject[i], ExceptionMessageUtils.getLocalizedMessage(resourceBundleName[i], locale, kapuaErrorCode, objectList));
        }
    }

    @Test
    public void getLocalizedMessageEmptyObjectListTest() {
        for (int i = 0; i < resourceBundleName.length; i++) {
            Assert.assertEquals("Expected and actual values should be the same.", kapuaGenericMessage[i], ExceptionMessageUtils.getLocalizedMessage(resourceBundleName[i], locale, kapuaErrorCode, emptyObjectList));
        }
    }

    @Test(expected = NullPointerException.class)
    public void getLocalizedMessageNullStringTest() {
        ExceptionMessageUtils.getLocalizedMessage(null, locale, kapuaErrorCode, objectList);
    }

    @Test(expected = NullPointerException.class)
    public void getLocalizedMessageNullLocaleTest() {
        for (String name : resourceBundleName) {
            ExceptionMessageUtils.getLocalizedMessage(name, null, kapuaErrorCode, objectList);
        }
    }

    @Test(expected = MissingResourceException.class)
    public void getLocalizedMessageMissingResourceTest() {
        Mockito.when(mockedKapuaErrorCode.name()).thenThrow(new MissingResourceException("Message", "Class Name", "Key"));
        ExceptionMessageUtils.getLocalizedMessage("kapua-service-error-messages", locale, mockedKapuaErrorCode, objectList);
    }

    @Test
    public void nullErrorCodeLeadsToDefaultErrorMessage() {
        final String got = ExceptionMessageUtils.getLocalizedMessage(resourceBundleName[0], locale, null, objectList);
        Assert.assertEquals("Error: 0, 10, 100000, String, c, -10, -1000000000, -100000000000, 10, 10.0, null, 10.1, true, false", got);
    }

    @Test
    public void getLocalizedMessageNullObjectTest() {
        for (int i = 0; i < resourceBundleName.length; i++) {
            Assert.assertEquals("Expected and actual values should be the same.", kapuaGenericMessage[i], ExceptionMessageUtils.getLocalizedMessage(resourceBundleName[i], locale, kapuaErrorCode, null));
        }
    }

    @Test
    public void getLocalizedMessageNullKapuaErrorCodeSecondTest() {
        Assert.assertEquals("Expected and actual values should be the same.", kapuaGenericMessageWithObject[1], ExceptionMessageUtils.getLocalizedMessage(resourceBundleName[1], locale, null, objectList));
    }
}

/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.storable.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class InvalidValueMappingExceptionTest {

    @Test
    public void newInstanceHardcodesUnsupportedTypeCode() {
        final InvalidValueMappingException instance = new InvalidValueMappingException("arg0", new Object(), Object.class);

        Assert.assertEquals(StorableErrorCodes.INVALID_VALUE, instance.getCode());
    }


    @Test
    public void newInstanceFirstsClassArgumentIsUsedInExceptionMessage() {
        final String arg0 = "argName";
        final InvalidValueMappingException mappingException = new InvalidValueMappingException(arg0, arg0.getClass(), null);

        final String expected = "The value of mapping " + arg0 + " of value";
        Assert.assertTrue(mappingException.getMessage().startsWith(expected));
    }


    @Test
    public void newInstanceNotNullThirdArgumentClassIsUsedInExceptionMessage() {
        final String arg1 = "arg1Value";
        final InvalidValueMappingException mappingException = new InvalidValueMappingException(new Throwable("foo"), "argName", arg1, arg1.getClass());

        final String expected = "of value " + arg1 + " is not compatible with type " + arg1.getClass() + ".";
        Assert.assertTrue(mappingException.getMessage().endsWith(expected));
    }


    @Test
    public void newInstanceNullSecondsArgumentLeadsToNullType() {
        final String arg1 = null;
        final InvalidValueMappingException mappingException = new InvalidValueMappingException(new Throwable("foo"), "argName", arg1, null);

        Assert.assertNull(mappingException.getType());
    }
}

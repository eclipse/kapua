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
public class UnsupportedTypeMappingExceptionTest {

    @Test
    public void newInstanceHardcodesUnsupportedTypeCode() {
        final UnsupportedTypeMappingException instance = new UnsupportedTypeMappingException("nameArg", "valueArg");

        Assert.assertEquals(StorableErrorCodes.UNSUPPORTED_TYPE, instance.getCode());
    }


    @Test
    public void newInstanceFirstsClassArgumentIsUsedInExceptionMessage() {
        final String arg0 = "argName";
        final UnsupportedTypeMappingException mappingException = new UnsupportedTypeMappingException(arg0, null);

        final String expected = "The conversion of mapping " + arg0 + " of value";
        Assert.assertTrue(mappingException.getMessage().startsWith(expected));
    }


    @Test
    public void newInstanceNotNullSecondsArgumentClassIsUsedInExceptionMessage() {
        final String arg1 = "arg1Value";
        final UnsupportedTypeMappingException mappingException = new UnsupportedTypeMappingException("argName", arg1);

        final String expected = "of value " + arg1 + " of type " + arg1.getClass().getSimpleName() + " with is not supported!";
        Assert.assertTrue(mappingException.getMessage().endsWith(expected));
    }


    @Test
    public void newInstanceNullSecondsArgumentClassIsReplacedByPlaceholderInExceptionMessage() {
        final String arg1 = null;
        final UnsupportedTypeMappingException mappingException = new UnsupportedTypeMappingException("argName", arg1);

        final String expected = "of value " + arg1 + " of type " + "null" + " with is not supported!";
        Assert.assertTrue(mappingException.getMessage().endsWith(expected));
    }

    @Test
    public void newInstanceNotNullSecondsArgumentClassIsAssignedToType() {
        final String arg1 = "arg1Value";
        final UnsupportedTypeMappingException mappingException = new UnsupportedTypeMappingException("argName", arg1);

        Assert.assertEquals(arg1.getClass(), mappingException.getType());
    }


    @Test
    public void newInstanceNullSecondsArgumentLeadsToNullType() {
        final String arg1 = null;
        final UnsupportedTypeMappingException mappingException = new UnsupportedTypeMappingException("argName", arg1);

        Assert.assertNull(mappingException.getType());
    }
}

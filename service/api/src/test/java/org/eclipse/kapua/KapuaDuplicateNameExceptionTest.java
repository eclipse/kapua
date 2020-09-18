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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaDuplicateNameExceptionTest extends Assert {

    String[] duplicatedNames;

    @Before
    public void initialize() {
        duplicatedNames = new String[]{"Duplicated Name", null};
    }

    @Test
    public void kapuaDuplicateNameExceptionStringParameterTest() {
        for (String duplicatedName : duplicatedNames) {
            KapuaDuplicateNameException kapuaDuplicateNameExceptions = new KapuaDuplicateNameException(duplicatedName);
            assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.DUPLICATE_NAME, kapuaDuplicateNameExceptions.getCode());
            assertNull("Null expected", kapuaDuplicateNameExceptions.getCause());
            assertEquals("Expected and actual values should be the same.", "An entity with the same name " + duplicatedName + " already exists.", kapuaDuplicateNameExceptions.getMessage());
        }
    }

    @Test
    public void kapuaDuplicateNameExceptionNoParametersTest() {
        KapuaDuplicateNameException kapuaDuplicateNameException = new KapuaDuplicateNameException();

        assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.SCHEDULE_DUPLICATE_NAME, kapuaDuplicateNameException.getCode());
        assertNull("Null expected", kapuaDuplicateNameException.getCause());
        assertEquals("Expected and actual values should be the same.", "An entity with the same value for field already exists here or in another job.", kapuaDuplicateNameException.getMessage());
    }

    @Test(expected = KapuaDuplicateNameException.class)
    public void throwingExceptionWithParameterTest() throws KapuaDuplicateNameException {
        for (String duplicatedName : duplicatedNames) {
            throw new KapuaDuplicateNameException(duplicatedName);
        }
    }

    @Test(expected = KapuaDuplicateNameException.class)
    public void throwingExceptionWithoutParameterTest() throws KapuaDuplicateNameException {
        throw new KapuaDuplicateNameException();
    }
}
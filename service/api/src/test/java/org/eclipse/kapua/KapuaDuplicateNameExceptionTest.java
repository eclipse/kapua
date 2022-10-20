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


@Category(JUnitTests.class)
public class KapuaDuplicateNameExceptionTest {

    String[] duplicatedNames;

    @Before
    public void initialize() {
        duplicatedNames = new String[]{"Duplicated Name", null};
    }

    @Test
    public void kapuaDuplicateNameExceptionStringParameterTest() {
        for (String duplicatedName : duplicatedNames) {
            KapuaDuplicateNameException kapuaDuplicateNameExceptions = new KapuaDuplicateNameException(duplicatedName);
            Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.DUPLICATE_NAME, kapuaDuplicateNameExceptions.getCode());
            Assert.assertNull("Null expected", kapuaDuplicateNameExceptions.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", "An entity with the same name " + duplicatedName + " already exists.", kapuaDuplicateNameExceptions.getMessage());
        }
    }

    @Test(expected = KapuaDuplicateNameException.class)
    public void throwingExceptionWithParameterTest() throws KapuaDuplicateNameException {
        for (String duplicatedName : duplicatedNames) {
            throw new KapuaDuplicateNameException(duplicatedName);
        }
    }
}

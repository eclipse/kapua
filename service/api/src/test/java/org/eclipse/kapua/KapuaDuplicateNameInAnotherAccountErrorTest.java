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
public class KapuaDuplicateNameInAnotherAccountErrorTest extends Assert {

    String[] duplicateNames;

    @Before
    public void initialize() {
        duplicateNames = new String[]{"Duplicate Name", null};
    }

    @Test
    public void kapuaDuplicateNameInAnotherAccountErrorTest() {
        for (String duplicateName : duplicateNames) {
            KapuaDuplicateNameInAnotherAccountError kapuaDuplicateNameInAnotherAccountError = new KapuaDuplicateNameInAnotherAccountError(duplicateName);
            assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ENTITY_ALREADY_EXIST_IN_ANOTHER_ACCOUNT, kapuaDuplicateNameInAnotherAccountError.getCode());
            assertNull("Null expected", kapuaDuplicateNameInAnotherAccountError.getCause());
            assertEquals("Expected and actual values should be the same.", "An entity with the same name " + duplicateName, kapuaDuplicateNameInAnotherAccountError.getMessage());
        }
    }

    @Test(expected = KapuaDuplicateNameInAnotherAccountError.class)
    public void throwingExceptionNullParameterTest() throws KapuaDuplicateNameInAnotherAccountError {
        for (String duplicateName : duplicateNames) {
            throw new KapuaDuplicateNameInAnotherAccountError(duplicateName);
        }
    }
}
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
public class KapuaDuplicateExternalIdInAnotherAccountErrorTest extends Assert {

    String[] duplicateExternalId;

    @Before
    public void initialize() {
        duplicateExternalId = new String[]{"Duplicate External Id", null};
    }

    @Test
    public void kapuaDuplicateExternalIdInAnotherAccountErrorTest() {
        for (String id : duplicateExternalId) {
            KapuaDuplicateExternalIdInAnotherAccountError kapuaDuplicateExternalIdInAnotherAccountError = new KapuaDuplicateExternalIdInAnotherAccountError(id);
            assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.EXTERNAL_ID_ALREADY_EXIST_IN_ANOTHER_ACCOUNT, kapuaDuplicateExternalIdInAnotherAccountError.getCode());
            assertNull("Null expected", kapuaDuplicateExternalIdInAnotherAccountError.getCause());
            assertEquals("Expected and actual values should be the same.", "An entity with the same external id " + id + " already exists in another account.", kapuaDuplicateExternalIdInAnotherAccountError.getMessage());
        }
    }

    @Test(expected = KapuaDuplicateExternalIdInAnotherAccountError.class)
    public void throwingExceptionTest() throws KapuaDuplicateExternalIdInAnotherAccountError {
        for (String id : duplicateExternalId) {
            throw new KapuaDuplicateExternalIdInAnotherAccountError(id);
        }
    }
}

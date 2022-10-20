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
public class KapuaDuplicateExternalIdExceptionTest {

    String[] duplicatedExternalId;

    @Before
    public void initialize() {
        duplicatedExternalId = new String[]{"Duplicated External Id", null};
    }

    @Test
    public void kapuaDuplicateExternalIdExceptionTest() {
        for (String id : duplicatedExternalId) {
            KapuaDuplicateExternalIdException kapuaDuplicateExternalIdException = new KapuaDuplicateExternalIdException(id);
            Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.DUPLICATE_EXTERNAL_ID, kapuaDuplicateExternalIdException.getCode());
            Assert.assertNull("Null expected", kapuaDuplicateExternalIdException.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", "An entity with the same external Id " + id + " already exists.", kapuaDuplicateExternalIdException.getMessage());
        }
    }

    @Test(expected = KapuaDuplicateExternalIdException.class)
    public void throwingExceptionTest() throws KapuaDuplicateExternalIdException {
        for (String id : duplicatedExternalId) {
            throw new KapuaDuplicateExternalIdException(id);
        }
    }
}

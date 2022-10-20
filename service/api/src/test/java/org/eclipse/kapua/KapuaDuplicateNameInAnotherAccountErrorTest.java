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
public class KapuaDuplicateNameInAnotherAccountErrorTest {

    String[] duplicateNames;

    @Before
    public void initialize() {
        duplicateNames = new String[]{"Duplicate Name", null};
    }

    @Test
    public void kapuaDuplicateNameInAnotherAccountErrorTest() {
        for (String duplicateName : duplicateNames) {
            KapuaDuplicateNameInAnotherAccountError kapuaDuplicateNameInAnotherAccountError = new KapuaDuplicateNameInAnotherAccountError(duplicateName);
            Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ENTITY_ALREADY_EXIST_IN_ANOTHER_ACCOUNT, kapuaDuplicateNameInAnotherAccountError.getCode());
            Assert.assertNull("Null expected", kapuaDuplicateNameInAnotherAccountError.getCause());
            Assert.assertEquals("Expected and actual values should be the same.", "An entity with the same name " + duplicateName + " already exists.", kapuaDuplicateNameInAnotherAccountError.getMessage());
        }
    }

    @Test(expected = KapuaDuplicateNameInAnotherAccountError.class)
    public void throwingExceptionNullParameterTest() throws KapuaDuplicateNameInAnotherAccountError {
        for (String duplicateName : duplicateNames) {
            throw new KapuaDuplicateNameInAnotherAccountError(duplicateName);
        }
    }
}

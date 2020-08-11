/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaErrorCodesTest extends Assert {

    @Test
    public void testIdGenerationError() {
        try {
            assertNotNull(KapuaCommonsErrorCodes.valueOf("ID_GENERATION_ERROR"));
        } catch (Exception ex) {
            fail("ID_GENERATION_ERROR Enum does not exist");
        }
    }
}

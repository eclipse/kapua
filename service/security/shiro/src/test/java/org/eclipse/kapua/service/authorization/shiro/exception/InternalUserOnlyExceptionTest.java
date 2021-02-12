/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.shiro.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class InternalUserOnlyExceptionTest extends Assert {

    @Test
    public void internalUserOnlyExceptionTest() {
        InternalUserOnlyException internalUserOnlyException = new InternalUserOnlyException();
        assertEquals("Expected and actual values should be the same.", KapuaAuthorizationErrorCodes.INTERNAL_USER_ONLY, internalUserOnlyException.getCode());
        assertNull("Null expected.", internalUserOnlyException.getCause());
        assertEquals("Expected and actual values should be the same.", "This action can be performed only by internal users.", internalUserOnlyException.getMessage());
        assertEquals("Expected and actual values should be the same.", "kapua-service-error-messages", internalUserOnlyException.getKapuaErrorMessagesBundle());
    }
}

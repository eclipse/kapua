/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
public class SelfManagedOnlyExceptionTest {

    @Test
    public void selfManagedOnlyExceptionTest() {
        SelfManagedOnlyException selfManagedOnlyException = new SelfManagedOnlyException();
        Assert.assertEquals("Expected and actual values should be the same.", KapuaAuthorizationErrorCodes.SELF_MANAGED_ONLY, selfManagedOnlyException.getCode());
        Assert.assertNull("Null expected.", selfManagedOnlyException.getCause());
        Assert.assertEquals("Expected and actual values should be the same.", "User cannot perform this action on behalf of another user. This action can be performed only in self-management.", selfManagedOnlyException.getMessage());
        Assert.assertEquals("Expected and actual values should be the same.", "kapua-service-error-messages", selfManagedOnlyException.getKapuaErrorMessagesBundle());
    }
}
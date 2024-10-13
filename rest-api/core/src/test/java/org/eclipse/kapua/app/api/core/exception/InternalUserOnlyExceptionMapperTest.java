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
package org.eclipse.kapua.app.api.core.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.shiro.exception.InternalUserOnlyException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class InternalUserOnlyExceptionMapperTest extends Assert {

    InternalUserOnlyExceptionMapper internalUserOnlyExceptionMapper;
    InternalUserOnlyException internalUserOnlyException;

    @Before
    public void initialize() {
        internalUserOnlyExceptionMapper = new InternalUserOnlyExceptionMapper();
        internalUserOnlyException = new InternalUserOnlyException();
    }

    @Test
    public void toResponseTest() {
        assertEquals("Expected and actual values should be the same.", 403, internalUserOnlyExceptionMapper.toResponse(internalUserOnlyException).getStatus());
        assertEquals("Expected and actual values should be the same.", "Forbidden", internalUserOnlyExceptionMapper.toResponse(internalUserOnlyException).getStatusInfo().toString());
    }

    @Test(expected = NullPointerException.class)
    public void toResponseNullTest() {
        internalUserOnlyExceptionMapper.toResponse(null).getStatus();
    }
}
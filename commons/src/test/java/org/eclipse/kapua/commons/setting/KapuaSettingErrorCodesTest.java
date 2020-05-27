/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.setting;

import org.junit.Assert;
import org.junit.Test;

public class KapuaSettingErrorCodesTest extends Assert {

    @Test
    public void invalidResourcesNameTest() {
        assertEquals("Expected and actual values should be the same!", "INVALID_RESOURCE_NAME", KapuaSettingErrorCodes.INVALID_RESOURCE_NAME.name());
    }

    @Test
    public void invalidResourcesFileTest() {
        assertEquals("Expected and actual values should be the same!", "INVALID_RESOURCE_FILE", KapuaSettingErrorCodes.INVALID_RESOURCE_FILE.name());
    }

    @Test
    public void resourceNotFoundTest() {
        assertEquals("Expected and actual values should be the same!", "RESOURCE_NOT_FOUND", KapuaSettingErrorCodes.RESOURCE_NOT_FOUND.name());
    }
}

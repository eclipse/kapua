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
package org.eclipse.kapua.commons.setting;

import org.junit.Assert;
import org.junit.Test;

public class KapuaSettingErrorCodesTest {

    @Test
    public void invalidResourcesNameTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "INVALID_RESOURCE_NAME", KapuaSettingErrorCodes.INVALID_RESOURCE_NAME.name());
    }

    @Test
    public void invalidResourcesFileTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "INVALID_RESOURCE_FILE", KapuaSettingErrorCodes.INVALID_RESOURCE_FILE.name());
    }

    @Test
    public void resourceNotFoundTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "RESOURCE_NOT_FOUND", KapuaSettingErrorCodes.RESOURCE_NOT_FOUND.name());
    }
}

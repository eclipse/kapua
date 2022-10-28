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
package org.eclipse.kapua.app.api.core.settings;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class KapuaApiCoreSettingKeysTest {

    @Test
    public void kapuaApiCoreSettingKeysTest(){
        Assert.assertEquals("Expected and actual values should be the same.","api.path.param.scopeId.wildcard",KapuaApiCoreSettingKeys.API_PATH_PARAM_SCOPEID_WILDCARD.key());
        Assert.assertEquals("Expected and actual values should be the same.","api.exception.stacktrace.show",KapuaApiCoreSettingKeys.API_EXCEPTION_STACKTRACE_SHOW.key());
    }
}
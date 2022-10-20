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
package org.eclipse.kapua.integration.misc;

import org.eclipse.kapua.broker.core.plugin.authentication.UserAuthenticationLogic;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;
import java.util.Map;


@Category(JUnitTests.class)
public class UserAuthenticationLogicTest {

    @Test
    public void userAuthenticationLogicTest() {
        Map<String, Object> options = new HashMap<>();
        options.put("address_prefix", "prefix");
        options.put("address_advisory_prefix", "advisory_prefix");

        try {
            new UserAuthenticationLogic(options);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void userAuthenticationLogicEmptyOptionsTest() {
        Map<String, Object> options = new HashMap<>();
        try {
            new UserAuthenticationLogic(options);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void userAuthenticationLogicNullTest() {
        try {
            new UserAuthenticationLogic(null);
            Assert.fail("NullPointerException expected.");
        } catch (Exception e) {
            Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
    }
}
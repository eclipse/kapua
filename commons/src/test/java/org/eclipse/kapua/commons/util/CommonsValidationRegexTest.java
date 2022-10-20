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
package org.eclipse.kapua.commons.util;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class CommonsValidationRegexTest {

    @Test
    public void simpleNameRegexpTest() throws Exception {
            Assert.assertEquals("CommonsvalidationRegex.SIMPLE_NAME_REGEXP.name()", "SIMPLE_NAME_REGEXP", CommonsValidationRegex.SIMPLE_NAME_REGEXP.name());
    }

    @Test
    public void nameRegexpTest() throws Exception {
            Assert.assertEquals("CommonsValidationRegex.NAME_REGEXP.name()", "NAME_REGEXP", CommonsValidationRegex.NAME_REGEXP.name());
        }

    @Test
    public void nameSpaceRegexpTest() throws Exception {
            Assert.assertEquals("CommonsValidationRegex.NAME_SPACE_REGEXP.name()", "NAME_SPACE_REGEXP", CommonsValidationRegex.NAME_SPACE_REGEXP.name());
    }

    @Test
    public void passwordRegexpTest() throws Exception {
            Assert.assertEquals("CommonsValidationRegex.PASSWORD_REGEXP.name()", "PASSWORD_REGEXP", CommonsValidationRegex.PASSWORD_REGEXP.name());
    }

    @Test
    public void emailRegexpTest() throws Exception {
            Assert.assertEquals("CommonsValidationRegex.EMAIL_REGEXP.name()", "EMAIL_REGEXP", CommonsValidationRegex.EMAIL_REGEXP.name());
    }

    @Test
    public void iPAddressRegexpTest() throws Exception {
            Assert.assertEquals("CommonsValidationRegex.IP_ADDRESS_REGEXP.name()", "IP_ADDRESS_REGEXP", CommonsValidationRegex.IP_ADDRESS_REGEXP.name());
    }

    @Test
    public void macAddressRegexpTest() throws Exception {
            Assert.assertEquals("CommonsValidationRegex.MAC_ADDRESS_REGEXP.name()", "MAC_ADDRESS_REGEXP", CommonsValidationRegex.MAC_ADDRESS_REGEXP.name());
    }

    @Test
    public void localIpAddressRegexpTest() throws Exception {
            Assert.assertEquals("CommonsValidationRegex.LOCAL_IP_ADDRESS_REGEXP.name()", "LOCAL_IP_ADDRESS_REGEXP", CommonsValidationRegex.LOCAL_IP_ADDRESS_REGEXP.name());
    }

    @Test
    public void uriSchemeRegexpTest() throws Exception {
            Assert.assertEquals("CommonsValidationRegex.URI_SCHEME.name()", "URI_SCHEME", CommonsValidationRegex.URI_SCHEME.name());
    }

    @Test
    public void uriDnsRegexpTest() throws Exception {
            Assert.assertEquals("CommonsValidationRegex.URI_DNS.name()", "URI_DNS", CommonsValidationRegex.URI_DNS.name());
    }
}

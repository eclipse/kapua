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
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class CredentialServiceImplTest {

    @Test
    public void credentialServiceImplMinimumPasswordLengthTest() {
        System.setProperty("authentication.credential.userpass.password.minlength", "12");
        try {
            new CredentialServiceImpl();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void credentialServiceImplTooShortPasswordLengthTest() {
        System.setProperty("authentication.credential.userpass.password.minlength", "11");
        try {
            new CredentialServiceImpl();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void credentialServiceNegativePasswordLengthTest() {
        System.setProperty("authentication.credential.userpass.password.minlength", "-1");
        try {
            new CredentialServiceImpl();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void credentialServiceZeroPasswordLengthTest() {
        System.setProperty("authentication.credential.userpass.password.minlength", "0");
        try {
            new CredentialServiceImpl();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void credentialServiceImplTest() {
        System.setProperty("authentication.credential.userpass.password.minlength", "20");
        try {
            new CredentialServiceImpl();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void credentialServiceLongPasswordLengthTest() {
        System.setProperty("authentication.credential.userpass.password.minlength", "255");
        try {
            new CredentialServiceImpl();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void credentialServiceTooLongPasswordLengthTest() {
        System.setProperty("authentication.credential.userpass.password.minlength", "256");
        try {
            new CredentialServiceImpl();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }
}
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
package org.eclipse.kapua.service.authentication.shiro.mfa;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.mfa.MfaAuthenticator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;


@Category(JUnitTests.class)
public class MfaAuthenticatorServiceLocatorTest {

    @Test
    public void mfaAuthenticatorServiceLocatorTest() throws Exception {
        Constructor<MfaAuthenticatorServiceLocator> mfaAuthenticatorServiceLocator = MfaAuthenticatorServiceLocator.class.getDeclaredConstructor();
        mfaAuthenticatorServiceLocator.setAccessible(true);
        mfaAuthenticatorServiceLocator.newInstance();
        Assert.assertTrue("True expected.", Modifier.isPrivate(mfaAuthenticatorServiceLocator.getModifiers()));
    }

    @Test
    public void getInstanceNullLocatorTest() {
        Assert.assertTrue("True expected.", MfaAuthenticatorServiceLocator.getInstance() instanceof MfaAuthenticatorServiceLocator);
    }

    @Test
    public void getInstanceTest() {
        MfaAuthenticatorServiceLocator.getInstance();
        Assert.assertTrue("True expected.", MfaAuthenticatorServiceLocator.getInstance() instanceof MfaAuthenticatorServiceLocator);
    }

    @Test
    public void getMfaAuthenticatorTest() {
        Assert.assertTrue("True expected.", MfaAuthenticatorServiceLocator.getInstance().getMfaAuthenticator() instanceof MfaAuthenticator);
    }
}
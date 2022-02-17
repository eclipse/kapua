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
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class MfaOptionEntityManagerFactoryTest extends Assert {

    @Test
    public void mfaOptionEntityManagerFactoryTest() throws Exception {
        Constructor<MfaOptionEntityManagerFactory> mfaOptionEntityManagerFactory = MfaOptionEntityManagerFactory.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(mfaOptionEntityManagerFactory.getModifiers()));
        mfaOptionEntityManagerFactory.setAccessible(true);
        mfaOptionEntityManagerFactory.newInstance();
    }

    @Test
    public void getInstanceTest() {
        assertTrue("Instance of EntityManagerFactory expected.", MfaOptionEntityManagerFactory.getInstance() instanceof EntityManagerFactory);
    }
}
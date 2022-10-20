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
package org.eclipse.kapua.service.authentication.shiro.utils;

import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;


@Category(JUnitTests.class)
public class JwtProcessorsTest {

    @Test
    public void jwtProcessorsTest() throws Exception {
        Constructor<JwtProcessors> jwtProcessors = JwtProcessors.class.getDeclaredConstructor();
        jwtProcessors.setAccessible(true);
        jwtProcessors.newInstance();
        Assert.assertTrue("True expected.", Modifier.isPrivate(jwtProcessors.getModifiers()));
    }

    @Test
    public void createDefaultTest() throws OpenIDException {
        Assert.assertTrue("True expected.", JwtProcessors.createDefault() instanceof JwtProcessor);
    }
}
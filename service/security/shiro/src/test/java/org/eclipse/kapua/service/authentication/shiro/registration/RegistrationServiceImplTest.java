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
package org.eclipse.kapua.service.authentication.shiro.registration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;


@Category(JUnitTests.class)
public class RegistrationServiceImplTest {

    @Test
    public void registrationServiceImplTest() {
        try {
            new RegistrationServiceImpl();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void closeTest() throws Exception {
        RegistrationServiceImpl registrationServiceImpl = new RegistrationServiceImpl();
        try {
            registrationServiceImpl.close();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void isAccountCreationEnabledTrueEmptyProcessorsTest() throws KapuaException {
        System.setProperty("authentication.registration.service.enabled", "true");
        RegistrationServiceImpl registrationService = new RegistrationServiceImpl();
        Assert.assertFalse("False expected.", registrationService.isAccountCreationEnabled());
    }

    @Test
    public void isAccountCreationEnabledFalseTest() throws KapuaException {
        System.setProperty("authentication.registration.service.enabled", "false");
        RegistrationServiceImpl registrationService = new RegistrationServiceImpl();
        Assert.assertFalse("False expected.", registrationService.isAccountCreationEnabled());
    }

    @Test
    public void createAccountCreationNotEnabledTest() throws KapuaException {
        JwtCredentials jwtCredentials = Mockito.mock(JwtCredentials.class);
        Assert.assertFalse("False expected.", new RegistrationServiceImpl().createAccount(jwtCredentials));
    }

    @Test
    public void createAccountNullTest() throws KapuaException {
        Assert.assertFalse("False expected.", new RegistrationServiceImpl().createAccount(null));
    }
}
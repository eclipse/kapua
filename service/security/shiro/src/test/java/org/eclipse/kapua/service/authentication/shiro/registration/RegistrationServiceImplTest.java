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
import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.OpenIDLocator;
import org.eclipse.kapua.plugin.sso.openid.OpenIDService;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.plugin.sso.openid.provider.internal.DisabledJwtProcessor;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.security.registration.RegistrationProcessor;
import org.eclipse.kapua.security.registration.RegistrationProcessorProvider;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;


@Category(JUnitTests.class)
public class RegistrationServiceImplTest {

    private RegistrationServiceImpl createDummyInstance() throws OpenIDException {
        return new RegistrationServiceImpl(new KapuaAuthenticationSetting(), new OpenIDLocator() {
            @Override
            public OpenIDService getService() {
                return null;
            }

            @Override
            public JwtProcessor getProcessor() throws OpenIDException {
                return new DisabledJwtProcessor();
            }
        }, Collections.singleton(new RegistrationProcessorProvider() {
            @Override
            public Collection<? extends RegistrationProcessor> createAll() {
                return Collections.emptyList();
            }
        }));
    }

    @Test
    public void registrationServiceImplTest() {
        try {
            createDummyInstance();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void closeTest() throws Exception {
        RegistrationServiceImpl registrationServiceImpl = createDummyInstance();
        try {
            registrationServiceImpl.close();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void isAccountCreationEnabledTrueEmptyProcessorsTest() throws KapuaException {
        System.setProperty("authentication.registration.service.enabled", "true");
        RegistrationServiceImpl registrationService = createDummyInstance();
        Assert.assertFalse("False expected.", registrationService.isAccountCreationEnabled());
    }

    @Test
    public void isAccountCreationEnabledFalseTest() throws KapuaException {
        System.setProperty("authentication.registration.service.enabled", "false");
        RegistrationServiceImpl registrationService = createDummyInstance();
        Assert.assertFalse("False expected.", registrationService.isAccountCreationEnabled());
    }

    @Test
    public void createAccountCreationNotEnabledTest() throws KapuaException {
        JwtCredentials jwtCredentials = Mockito.mock(JwtCredentials.class);
        Assert.assertFalse("False expected.", createDummyInstance().createAccount(jwtCredentials));
    }

    @Test
    public void createAccountNullTest() throws KapuaException {
        Assert.assertFalse("False expected.", createDummyInstance().createAccount(null));
    }
}
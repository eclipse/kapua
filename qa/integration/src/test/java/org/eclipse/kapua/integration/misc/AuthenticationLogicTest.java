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

import org.eclipse.kapua.broker.core.plugin.KapuaSecurityContext;
import org.eclipse.kapua.broker.core.plugin.authentication.AuthenticationLogic;
import org.eclipse.kapua.broker.core.plugin.authentication.AuthorizationEntry;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;


@Category(JUnitTests.class)
public class AuthenticationLogicTest {

    private class AuthenticationLogicImpl extends AuthenticationLogic {
        /**
         * Default constructor
         *
         * @param addressPrefix     prefix address to prepend to all the addressed when building the ACL list
         * @param addressClassifier address classifier used by the platform messages (not telemetry messages) (if defined by the platform)
         * @param advisoryPrefix    address prefix for the advisory messages (if defined by the platform)
         */
        protected AuthenticationLogicImpl(String addressPrefix, String addressClassifier, String advisoryPrefix) {
            super(addressPrefix, addressClassifier, advisoryPrefix);
        }

        @Override
        public List<AuthorizationEntry> connect(KapuaSecurityContext kapuaSecurityContext) {
            return null;
        }

        @Override
        public boolean disconnect(KapuaSecurityContext kapuaSecurityContext, Throwable error) {
            return false;
        }

        @Override
        protected List<AuthorizationEntry> buildAuthorizationMap(KapuaSecurityContext kapuaSecurityContext) {
            return null;
        }
    }

    @Test
    public void authenticationLogicTest() {
        String[] addressPrefixes = {null, "", "address_Pre,,fix", "addressP..refix-123", "addressPrefix#3", "!address <Prefix>", "#5#"};
        String[] addressClassifiers = {null, "", "add..ress_Classif,ier", "addressClassi,,fier-123", "addressClassifier#3", "!address <Classifier>", "#5#"};
        String[] advisoryPrefixes = {null, "", "advisory_Pre--fix", "ad,.visoryPrefix-123", "adviso,,ryPrefix#3", "!advisory <Prefix>", "#5#"};

        for (String addressPrefix : addressPrefixes) {
            for (String addressClassifier : addressClassifiers) {
                for (String advisoryPrefix : advisoryPrefixes) {
                    try {
                        new AuthenticationLogicImpl(addressPrefix, addressClassifier, advisoryPrefix);
                    } catch (Exception e) {
                        Assert.fail("Exception not expected.");
                    }
                }
            }
        }
    }
}
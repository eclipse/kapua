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
package org.eclipse.kapua.service.authentication.shiro.mfa;

import org.eclipse.kapua.service.authentication.mfa.MfaAuthenticator;

/**
 * The {@link MfaAuthenticator} service locator.
 */
public class MfaAuthenticatorServiceLocator {

    private static MfaAuthenticatorServiceLocator locator;

    private final MfaAuthenticator mfaAuthenticator;

    private MfaAuthenticatorServiceLocator() {
        // for the moment the one implemented in MfaAuthenticatorImpl is the only available authenticator
        mfaAuthenticator = new MfaAuthenticatorImpl();
    }

    public static MfaAuthenticatorServiceLocator getInstance() {
        if (locator == null) {
            synchronized (MfaAuthenticatorServiceLocator.class) {
                if (locator == null) {
                    locator = new MfaAuthenticatorServiceLocator();
                }
            }
        }
        return locator;
    }

    /**
     * Retrieve the {@link MfaAuthenticator} service.
     *
     * @return a {@link MfaAuthenticator} object.
     */
    public MfaAuthenticator getMfaAuthenticator() {
        return mfaAuthenticator;
    }

}

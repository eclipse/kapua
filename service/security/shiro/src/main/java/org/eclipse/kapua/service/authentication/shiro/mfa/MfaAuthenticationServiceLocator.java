/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.mfa;

import org.eclipse.kapua.service.authentication.mfa.MfaAuthenticationService;

/**
 * The MfaAuthenticationService locator.
 */
public class MfaAuthenticationServiceLocator {

    private static MfaAuthenticationServiceLocator locator;

    private final MfaAuthenticationService mfaAuthenticationService;

    private MfaAuthenticationServiceLocator() {
        mfaAuthenticationService = new MfaAuthenticationServiceImpl();
    }

    public static MfaAuthenticationServiceLocator getInstance() {
        if (locator == null) {
            synchronized (MfaAuthenticationServiceLocator.class) {
                if (locator == null) {
                    locator = new MfaAuthenticationServiceLocator();
                }
            }
        }
        return locator;
    }

    /**
     * Retrieve the MfaAuthenticationService.
     *
     * @return a {@link MfaAuthenticationService} object.
     */
    public MfaAuthenticationService getMfaAuthenticationService() {
        return mfaAuthenticationService;
    }

}

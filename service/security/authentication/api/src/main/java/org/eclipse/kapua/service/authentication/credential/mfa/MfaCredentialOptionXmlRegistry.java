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
package org.eclipse.kapua.service.authentication.credential.mfa;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class MfaCredentialOptionXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final MfaCredentialOptionFactory MFA_CREDENTIAL_OPTION_FACTORY = LOCATOR.getFactory(MfaCredentialOptionFactory.class);

    /**
     * Creates a new MfaCredentialOption instance
     *
     * @return
     */
    public MfaCredentialOption newMfaCredentialOption() {
        return MFA_CREDENTIAL_OPTION_FACTORY.newEntity(null);
    }

    /**
     * Creates a new MfaCredentialOption list result instance
     *
     * @return
     */
    public MfaCredentialOptionListResult newMfaCredentialOptionListResult() {
        return MFA_CREDENTIAL_OPTION_FACTORY.newListResult();
    }

    /**
     * Creates a new MfaCredentialOption creator instance
     *
     * @return
     */
    public MfaCredentialOptionCreator newMfaCredentialOptionCreator() {
        return MFA_CREDENTIAL_OPTION_FACTORY.newCreator(null, null, null);
    }

    public MfaCredentialOptionQuery newQuery() {
        return MFA_CREDENTIAL_OPTION_FACTORY.newQuery(null);
    }
}

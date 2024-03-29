/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class CredentialXmlRegistry {

    private final CredentialFactory credentialFactory = KapuaLocator.getInstance().getFactory(CredentialFactory.class);

    /**
     * Creates a new credential instance
     *
     * @return
     */
    public Credential newCredential() {
        return credentialFactory.newEntity(null);
    }

    /**
     * Creates a new credential list result instance
     *
     * @return
     */
    public CredentialListResult newCredentialListResult() {
        return credentialFactory.newListResult();
    }

    /**
     * Creates a new credential creator instance
     *
     * @return
     */
    public CredentialCreator newCredentialCreator() {
        return credentialFactory.newCreator(null, null, null, null, null, null);
    }

    public CredentialQuery newQuery() {
        return credentialFactory.newQuery(null);
    }
}

/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class CredentialXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final CredentialFactory factory = locator.getFactory(CredentialFactory.class);

    /**
     * Creates a new credential instance
     * 
     * @return
     */
    public Credential newCredential() {
        return factory.newEntity(null);
    }

    /**
     * Creates a new credential list result instance
     * 
     * @return
     */
    public CredentialListResult newCredentialListResult() {
        return factory.newListResult();
    }

    /**
     * Creates a new credential creator instance
     * 
     * @return
     */
    public CredentialCreator newCredentialCreator() {
        return factory.newCreator(null, null, null, null);
    }

    public CredentialQuery newQuery() {
        return factory.newQuery(null);
    }
}

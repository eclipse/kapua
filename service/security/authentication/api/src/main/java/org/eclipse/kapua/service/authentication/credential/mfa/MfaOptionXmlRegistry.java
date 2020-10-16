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
public class MfaOptionXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final MfaOptionFactory MFA_OPTION_FACTORY = LOCATOR.getFactory(MfaOptionFactory.class);

    /**
     * Creates a new {@link MfaOption} instance
     *
     * @return
     */
    public MfaOption newMfaOption() {
        return MFA_OPTION_FACTORY.newEntity(null);
    }

    /**
     * Creates a new {@link MfaOption} list result instance
     *
     * @return
     */
    public MfaOptionListResult newMfaOptionListResult() {
        return MFA_OPTION_FACTORY.newListResult();
    }

    /**
     * Creates a new {@link MfaOption} creator instance
     *
     * @return
     */
    public MfaOptionCreator newMfaOptionCreator() {
        return MFA_OPTION_FACTORY.newCreator(null, null, null);
    }

    public MfaOptionQuery newQuery() {
        return MFA_OPTION_FACTORY.newQuery(null);
    }
}

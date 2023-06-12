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
package org.eclipse.kapua.service.authentication.credential.mfa;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class MfaOptionXmlRegistry {

    private final MfaOptionFactory mfaOptionFactory = KapuaLocator.getInstance().getFactory(MfaOptionFactory.class);

    /**
     * Creates a new {@link MfaOption} instance
     *
     * @return
     */
    public MfaOption newMfaOption() {
        return mfaOptionFactory.newEntity(null);
    }

    /**
     * Creates a new {@link MfaOption} list result instance
     *
     * @return
     */
    public MfaOptionListResult newMfaOptionListResult() {
        return mfaOptionFactory.newListResult();
    }

    /**
     * Creates a new {@link MfaOption} creator instance
     *
     * @return
     */
    public MfaOptionCreator newMfaOptionCreator() {
        return mfaOptionFactory.newCreator(null, null, null);
    }

    public MfaOptionQuery newQuery() {
        return mfaOptionFactory.newQuery(null);
    }
}

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
public class ScratchCodeXmlRegistry {

    private final ScratchCodeFactory scratchCodeFactory = KapuaLocator.getInstance().getFactory(ScratchCodeFactory.class);

    /**
     * Creates a new {@link ScratchCode} instance
     *
     * @return
     */
    public ScratchCode newScratchCode() {
        return scratchCodeFactory.newEntity(null);
    }

    /**
     * Creates a new {@link ScratchCodeListResult} instance
     *
     * @return
     */
    public ScratchCodeListResult newScratchCodeListResult() {
        return scratchCodeFactory.newListResult();
    }

    /**
     * Creates a new {@link ScratchCodeCreator} instance
     *
     * @return
     */
    public ScratchCodeCreator newScratchCodeCreator() {
        return scratchCodeFactory.newCreator(null, null, null);
    }

    public ScratchCodeQuery newQuery() {
        return scratchCodeFactory.newQuery(null);
    }
}

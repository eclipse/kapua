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

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final ScratchCodeFactory SCRATCH_CODE_FACTORY = LOCATOR.getFactory(ScratchCodeFactory.class);

    /**
     * Creates a new {@link ScratchCode} instance
     *
     * @return
     */
    public ScratchCode newScratchCode() {
        return SCRATCH_CODE_FACTORY.newEntity(null);
    }

    /**
     * Creates a new {@link ScratchCodeListResult} instance
     *
     * @return
     */
    public ScratchCodeListResult newScratchCodeListResult() {
        return SCRATCH_CODE_FACTORY.newListResult();
    }

    /**
     * Creates a new {@link ScratchCodeCreator} instance
     *
     * @return
     */
    public ScratchCodeCreator newScratchCodeCreator() {
        return SCRATCH_CODE_FACTORY.newCreator(null, null, null);
    }

    public ScratchCodeQuery newQuery() {
        return SCRATCH_CODE_FACTORY.newQuery(null);
    }
}

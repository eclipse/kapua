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
public class ScratchCodeXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final ScratchCodeFactory SCRATCH_CODE_FACTORY = LOCATOR.getFactory(ScratchCodeFactory.class);

    /**
     * Creates a new ScratchCode instance
     *
     * @return
     */
    public ScratchCode newScratchCode() {
        return SCRATCH_CODE_FACTORY.newEntity(null);
    }

    /**
     * Creates a new ScratchCode list result instance
     *
     * @return
     */
    public ScratchCodeListResult newScratchCodeListResult() {
        return SCRATCH_CODE_FACTORY.newListResult();
    }

    /**
     * Creates a new ScratchCode creator instance
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

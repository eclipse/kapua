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
package org.eclipse.kapua.service.authorization.access;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class AccessInfoXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AccessInfoFactory ACCESS_INFO_FACTORY = LOCATOR.getFactory(AccessInfoFactory.class);

    /**
     * Creates a new access info instance
     *
     * @return
     */
    public AccessInfo newAccessInfo() {
        return ACCESS_INFO_FACTORY.newEntity(null);
    }

    /**
     * Creates a new access info creator instance
     *
     * @return
     */
    public AccessInfoCreator newAccessInfoCreator() {
        return ACCESS_INFO_FACTORY.newCreator(null);
    }

    /**
     * Creates a new {@link AccessInfoListResult} instance
     *
     * @return
     */
    public AccessInfoListResult newAccessInfoListResult() {
        return ACCESS_INFO_FACTORY.newListResult();
    }

    public AccessInfoQuery newQuery() {
        return ACCESS_INFO_FACTORY.newQuery(null);
    }
}

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
package org.eclipse.kapua.service.datastore.model.xml;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.ClientInfoFactory;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * Client information xml registry
 *
 * @since 1.0
 */
@XmlRegistry
public class ClientInfoXmlRegistry {

    private final ClientInfoFactory clientInfoFactory = KapuaLocator.getInstance().getFactory(ClientInfoFactory.class);

    /**
     * Creates a {@link ClientInfoListResult} instance
     *
     * @return
     */
    public ClientInfoListResult newListResult() {
        return clientInfoFactory.newListResult();
    }

    /**
     * Creates a {@link ClientInfoQuery} instance.
     *
     * @return
     */
    public ClientInfoQuery newQuery() {
        return clientInfoFactory.newQuery(null);
    }
}

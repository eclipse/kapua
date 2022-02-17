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
import org.eclipse.kapua.service.datastore.ChannelInfoFactory;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * Channel information xml registry
 *
 * @since 1.0
 */
@XmlRegistry
public class ChannelInfoXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final ChannelInfoFactory CHANNEL_INFO_FACTORY = LOCATOR.getFactory(ChannelInfoFactory.class);

    /**
     * Creates a {@link ChannelInfoListResult} instance
     *
     * @return
     */
    public ChannelInfoListResult newListResult() {
        return CHANNEL_INFO_FACTORY.newListResult();
    }

    /**
     * Creates a {@link ChannelInfoQuery} instance.
     *
     * @return
     */
    public ChannelInfoQuery newQuery() {
        return CHANNEL_INFO_FACTORY.newQuery(null);
    }
}

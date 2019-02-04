/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client.transport;

import org.eclipse.kapua.commons.setting.AbstractBaseKapuaSetting;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;

import java.net.InetSocketAddress;
import java.util.List;

/*******************************************************************************
 * This proxy class is only used to access the otherwise package-restricted
 * methods of the Elasticsearch transport client provider class.
 *
 * At the moment the only method that is required is parseAddress().
 *
 *******************************************************************************/

public class EsTransportClientProviderProxy {

    private EsTransportClientProviderProxy() {}

    public static InetSocketAddress parseAddress(String node) {
        return EsTransportClientProvider.parseAddress(node);
    }

    public static List<InetSocketAddress> parseAddresses(AbstractBaseKapuaSetting<ClientSettingsKey> settings) throws ClientUnavailableException {
        return EsTransportClientProvider.parseAddresses(settings);
    }
}

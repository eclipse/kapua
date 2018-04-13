/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client.transport;

import java.net.UnknownHostException;

import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;

import org.assertj.core.api.Assertions;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class EsClientTest {

    @Test
    public void elascticSearchClientShouldDefaultToLocalhost() throws UnknownHostException, ClientUnavailableException {
        // When
        EsTransportClientProvider.init();
        TransportClient client = EsTransportClientProvider.getInstance().getClient();

        // Then
        String host = client.listedNodes().get(0).getHostAddress();
        Assertions.assertThat(host).isEqualTo("127.0.0.1");
    }

}

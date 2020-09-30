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
package org.eclipse.kapua.service.elasticsearch.client.utils;

import com.google.common.collect.Lists;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchNode;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

public class InetAddressParser {

    private InetAddressParser() {
    }

    public static List<InetSocketAddress> parseAddresses(List<ElasticsearchNode> elasticsearchNodes) {
        return elasticsearchNodes
                .stream()
                .map(n -> new InetSocketAddress(n.getAddress(), n.getPort()))
                .collect(Collectors.toList());
    }

    public static InetSocketAddress parseAddresses(ElasticsearchNode elasticsearchNode) {
        return parseAddresses(Lists.newArrayList(elasticsearchNode)).get(0);
    }
}

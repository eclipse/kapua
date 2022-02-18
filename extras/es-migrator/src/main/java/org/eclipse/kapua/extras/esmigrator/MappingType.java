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
package org.eclipse.kapua.extras.esmigrator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.eclipse.kapua.commons.util.ResourceUtils;

import org.apache.commons.io.IOUtils;

public enum MappingType {

    CHANNEL("mappings/channel.json", "channel", "channel", IndexType.DATA_REGISTRY),
    CLIENT("mappings/client.json", "client", "client", IndexType.DATA_REGISTRY),
    METRIC("mappings/metric.json", "metric", "metric", IndexType.DATA_REGISTRY);

    private final String mappingFilePath;
    private final String mappingName;
    private final String newIndexSuffix;
    private final IndexType indexType;

    MappingType(String mappingFilePath, String mappingName, String newIndexSuffix, IndexType indexType) {
        this.mappingFilePath = mappingFilePath;
        this.mappingName = mappingName;
        this.newIndexSuffix = newIndexSuffix;
        this.indexType = indexType;
    }

    public String getMapping() throws IOException {
        return IOUtils.toString(ResourceUtils.getResource(mappingFilePath), StandardCharsets.UTF_8);
    }

    public String getMappingName() {
        return mappingName;
    }

    public String getNewIndexSuffix() {
        return newIndexSuffix;
    }

    public IndexType getIndexType() {
        return indexType;
    }
}


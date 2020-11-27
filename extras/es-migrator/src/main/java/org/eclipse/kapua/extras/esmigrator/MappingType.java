/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.util.ResourceUtils;

import org.apache.commons.io.IOUtils;

public enum MappingType {

    CHANNEL("mappings/channel.json"),
    CLIENT("mappings/client.json"),
    METRIC("mappings/metric.json");

    private final String mappingFilePath;

    MappingType(String mappingFilePath) {
        this.mappingFilePath = mappingFilePath;
    }

    public String getMappingFilePath() {
        return mappingFilePath;
    }

    public String getMapping() throws IOException {
        return IOUtils.toString(ResourceUtils.getResource(mappingFilePath));
    }
}

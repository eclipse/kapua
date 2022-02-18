/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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

public enum IndexType {
    DATA_MESSAGE,
    DATA_REGISTRY;

    public String deriveNewRegistryIndexName(String currentName, MappingType mappingType) {
        return currentName + "-" + mappingType.getNewIndexSuffix();
    }

}

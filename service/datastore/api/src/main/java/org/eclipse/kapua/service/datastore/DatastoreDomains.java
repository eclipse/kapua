/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore;

/**
 * All {@link org.eclipse.kapua.model.domain.Domain}s available for this module
 *
 * @since 1.0.0
 */
public class DatastoreDomains {

    private DatastoreDomains() {
    }

    /**
     * The {@link DatastoreDomain}
     *
     * @since 1.0.0
     */
    public static final DatastoreDomain DATASTORE_DOMAIN = new DatastoreDomain();
}

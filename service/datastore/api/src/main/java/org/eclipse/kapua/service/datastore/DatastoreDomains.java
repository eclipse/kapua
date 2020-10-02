/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

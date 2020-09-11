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
package org.eclipse.kapua.service.datastore.exception;

import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.config.ServiceComponentConfiguration;
import org.eclipse.kapua.service.datastore.MessageStoreService;

/**
 * {@link DatastoreServiceException} to {@code throw} when {@link MessageStoreService#store(KapuaMessage)} is invoked for
 * an {@link Account} which has it disabled from the {@link ServiceComponentConfiguration}
 *
 * @since 1.3.0
 */
public class DatastoreDisabledException extends DatastoreServiceException {

    private final KapuaId scopeId;

    /**
     * Constructor.
     *
     * @param scopeId The ScopeId for with the Datastore is disabled.
     * @since 1.3.0
     */
    public DatastoreDisabledException(KapuaId scopeId) {
        super(DatastoreServiceErrorCodes.DATASTORE_DISABLED_EXCEPTION, scopeId);

        this.scopeId = scopeId;
    }

    /**
     * Gets the ScopeId for with the Datastore is disabled.
     *
     * @return The ScopeId for with the Datastore is disabled.
     * @since 1.3.0
     */
    public KapuaId getScopeId() {
        return scopeId;
    }
}

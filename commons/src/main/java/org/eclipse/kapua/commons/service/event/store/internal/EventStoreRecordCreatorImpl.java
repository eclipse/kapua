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
package org.eclipse.kapua.commons.service.event.store.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecord;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordCreator;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * KapuaEvent creator service implementation.
 *
 * @since 1.0
 *
 */
public class EventStoreRecordCreatorImpl extends AbstractKapuaEntityCreator<EventStoreRecord> implements EventStoreRecordCreator {

    private static final long serialVersionUID = 1048699703033893534L;

    /**
     * Constructor
     *
     * @param scopeId
     */
    public EventStoreRecordCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

}

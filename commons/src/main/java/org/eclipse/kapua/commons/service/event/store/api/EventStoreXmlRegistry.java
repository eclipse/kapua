/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.service.event.store.api;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class EventStoreXmlRegistry {


    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final EventStoreFactory kapuaEventFactory = locator.getFactory(EventStoreFactory.class);

    /**
     * Creates a new kapuaEvent instance
     * 
     * @return
     */
    public EventStoreRecord newEventStoreRecord() {
        return kapuaEventFactory.newEntity(null);
    }

    /**
     * Creates a new kapuaEvent creator instance
     * 
     * @return
     */
    public EventStoreRecordCreator newEventStoreRecordCreator() {
        return kapuaEventFactory.newCreator(null);
    }

    /**
     * Creates a new kapuaEvent list result instance
     * 
     * @return
     */
    public EventStoreRecordListResult newEventStoreRecordListResult() {
        return kapuaEventFactory.newListResult();
    }

    public EventStoreRecordQuery newQuery() {
        return kapuaEventFactory.newQuery(null);
    }

}
/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.service.event.store.internal.EventStoreRecordImpl;

/**
 * Utility to convert event from/to entity event
 * 
 * @since 1.0
 *
 */
public class ServiceEventUtil {

    private ServiceEventUtil() {

    }

    /**
     * Convert the service event entity to the service bus object
     * 
     * @param serviceEventEntity
     * @return
     * @throws KapuaIllegalArgumentException
     */
    public static org.eclipse.kapua.event.ServiceEvent toServiceEventBus(EventStoreRecord serviceEventEntity) throws KapuaIllegalArgumentException {
        org.eclipse.kapua.event.ServiceEvent newEvent = new org.eclipse.kapua.event.ServiceEvent();
        if (serviceEventEntity.getId() == null) {
            throw new KapuaIllegalArgumentException("id", null);
        }
        newEvent.setId(serviceEventEntity.getId().toCompactId());
        newEvent.setContextId(serviceEventEntity.getContextId());
        newEvent.setTimestamp(serviceEventEntity.getTimestamp());
        newEvent.setUserId(serviceEventEntity.getUserId());
        newEvent.setService(serviceEventEntity.getService());
        newEvent.setEntityType(serviceEventEntity.getEntityType());
        newEvent.setScopeId(serviceEventEntity.getScopeId());
        newEvent.setEntityId(serviceEventEntity.getEntityId());
        newEvent.setOperation(serviceEventEntity.getOperation());
        newEvent.setInputs(serviceEventEntity.getInputs());
        newEvent.setOutputs(serviceEventEntity.getOutputs());
        newEvent.setStatus(serviceEventEntity.getStatus());
        newEvent.setNote(serviceEventEntity.getNote());
        return newEvent;
    }

    /**
     * Convert the service bus object to the service event entity. It should be used on a fresh service event bus object (if already persisted please use the
     * {@link #mergeToEntity(EventStoreRecord, org.eclipse.kapua.event.ServiceEvent)} method)
     *
     * @param serviceEventBus
     * @return
     * @throws KapuaIllegalArgumentException
     *             if the service event bus id is not null
     */
    public static EventStoreRecord fromServiceEventBus(org.eclipse.kapua.event.ServiceEvent serviceEventBus) throws KapuaIllegalArgumentException {
        if (serviceEventBus.getId() != null) {
            throw new KapuaIllegalArgumentException("id", serviceEventBus.getId());
        }
        return mergeToEntityInternal(new EventStoreRecordImpl(), serviceEventBus);
    }

    /**
     * 
     * @param serviceEventEntity
     * @param serviceEventBus
     * @return
     * @throws KapuaIllegalArgumentException
     *             if the service event bus id is null or differs to the service event entity
     */
    public static EventStoreRecord mergeToEntity(EventStoreRecord serviceEventEntity, org.eclipse.kapua.event.ServiceEvent serviceEventBus) throws KapuaIllegalArgumentException {
        if (serviceEventEntity.getId() == null) {
            throw new KapuaIllegalArgumentException("id", null);
        }
        if (!serviceEventEntity.getId().toCompactId().equals(serviceEventBus.getId())) {
            throw new KapuaIllegalArgumentException("serviceEventEntity.id - serviceEventBus.id", "not equals");
        }
        return mergeToEntityInternal(serviceEventEntity, serviceEventBus);
    }

    private static EventStoreRecord mergeToEntityInternal(EventStoreRecord serviceEventEntity, org.eclipse.kapua.event.ServiceEvent serviceEventBus) {
        serviceEventEntity.setContextId(serviceEventBus.getContextId());
        serviceEventEntity.setTimestamp(serviceEventBus.getTimestamp());
        serviceEventEntity.setUserId(serviceEventBus.getUserId());
        serviceEventEntity.setService(serviceEventBus.getService());
        serviceEventEntity.setEntityType(serviceEventBus.getEntityType());
        serviceEventEntity.setScopeId(serviceEventBus.getScopeId());
        serviceEventEntity.setEntityId(serviceEventBus.getEntityId());
        serviceEventEntity.setOperation(serviceEventBus.getOperation());
        serviceEventEntity.setInputs(serviceEventBus.getInputs());
        serviceEventEntity.setOutputs(serviceEventBus.getOutputs());
        serviceEventEntity.setStatus(serviceEventBus.getStatus());
        serviceEventEntity.setNote(serviceEventBus.getNote());
        return serviceEventEntity;
    }

}

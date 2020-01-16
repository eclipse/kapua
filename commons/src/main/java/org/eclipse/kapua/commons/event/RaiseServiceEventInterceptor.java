/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.event;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.kapua.commons.core.InterceptorBind;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.service.event.store.api.ServiceEventUtil;
import org.eclipse.kapua.commons.service.event.store.internal.EventStoreDAO;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.event.RaiseServiceEvent;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.event.ServiceEvent.EventStatus;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Event interceptor. It builds the event object and sends it to the event bus.
 *
 * @since 1.0
 */
@KapuaProvider
@InterceptorBind(matchSubclassOf = KapuaService.class, matchAnnotatedWith = RaiseServiceEvent.class)
public class RaiseServiceEventInterceptor implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(RaiseServiceEventInterceptor.class);

    private static final String MODULE = "commons";
    private static final String COMPONENT = "service_event";
    private static final String ACTION = "event_data_filler";
    private static final String COUNT = "count";


    private static final MetricsService METRIC_SERVICE = MetricServiceFactory.getInstance();

    private Counter wrongId;
    private Counter wrongEntity;

    public RaiseServiceEventInterceptor() {
        wrongId = METRIC_SERVICE.getCounter(MODULE, COMPONENT, ACTION, "wrong_id", COUNT);
        wrongEntity = METRIC_SERVICE.getCounter(MODULE, COMPONENT, ACTION, "wrong_entity", COUNT);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object returnObject = null;

        try {
            // if(!create) then the entity id can be set here
            ServiceEvent serviceEvent = ServiceEventScope.begin();

            KapuaSession session = KapuaSecurityUtils.getSession();
            // Context ID is initialized/managed by the EventScope object
            serviceEvent.setTimestamp(new Date());
            serviceEvent.setUserId(session.getUserId());
            serviceEvent.setScopeId(session.getScopeId());
            fillEvent(invocation, serviceEvent);

            // execute the business logic
            returnObject = invocation.proceed();

            // Raise service event if the execution is successful
            try {
                sendEvent(invocation, serviceEvent, returnObject);
            } catch (ServiceEventBusException e) {
                LOG.warn("Error sending event: {}", e.getMessage(), e);
            }

            return returnObject;

        } finally {
            ServiceEventScope.end();
        }
    }

    private void fillEvent(MethodInvocation invocation, ServiceEvent serviceEvent) {
        if (LOG.isDebugEnabled()) {
            logInputParameters(invocation.getMethod());
        }
        StringBuilder inputs = new StringBuilder();
        //find ids and entities
        List<KapuaEntity> entities = new ArrayList<>();
        List<KapuaId> ids = new ArrayList<>();
        Class<?>[] parametersClass = invocation.getMethod().getParameterTypes();
        Object[] arguments = invocation.getArguments();
        for (int i = 0; i<parametersClass.length; i++) {
            Class<?> parameterClass = parametersClass[i];
            LOG.debug("Parameter '{}' type {}", i, parameterClass);
            if (KapuaId.class.isAssignableFrom(parameterClass)) {
                ids.add((KapuaId)arguments[i]);
            }
            else if (KapuaEntity.class.isAssignableFrom(parameterClass)) {
                entities.add((KapuaEntity)arguments[i]);
            }
            // fill the inputs
            inputs.append(arguments[i] != null ? arguments[i].toString() : "null");
            inputs.append(", ");
        }
        if (inputs.length() > 2) {
            inputs.replace(inputs.length() - 2, inputs.length(), "");
        }
        serviceEvent.setInputs(inputs.toString());
        if (LOG.isDebugEnabled()) {
            logFoundEntities(entities, ids);
        }
        if (invocation.getThis() instanceof AbstractKapuaService) {
            // get the service name
            // the service is wrapped by guice so getThis --> getSuperclass() should provide the intercepted class
            // then keep the interface from this object
            serviceEvent.setOperation(invocation.getMethod().getName());
            Class<?> wrappedClass = ((AbstractKapuaService) invocation.getThis()).getClass().getSuperclass(); // this object should be not null
            Class<?>[] implementedClass = wrappedClass.getInterfaces();
            // assuming that the KapuaService implemented is specified by the first implementing interface
            String serviceInterfaceName = implementedClass[0].getName();
            // String splittedServiceInterfaceName[] = serviceInterfaceName.split("\\.");
            // String serviceName = splittedServiceInterfaceName.length > 0 ? splittedServiceInterfaceName[splittedServiceInterfaceName.length-1] : "";
            // String cleanedServiceName = serviceName.substring(0, serviceName.length()-"Service".length()).toLowerCase();
            String cleanedServiceName = serviceInterfaceName;
            LOG.debug("Service name '{}' ", cleanedServiceName);
            serviceEvent.setService(cleanedServiceName);
            if (entities.size()>0) {
                useEntityToFillEvent(serviceEvent, entities);
            }
            else if (ids.size()>0) {
                // otherwise assume that the second identifier is the entity id (and the first the scope id, if there are more than one) or take the first one (if there is one)
                useKapuaIdsToFillEvent(serviceEvent, ids, implementedClass);
            }
        } else {
            Annotation[] annotations = invocation.getMethod().getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (RaiseServiceEvent.class.isAssignableFrom(annotation.annotationType())) {
                    RaiseServiceEvent raiseKapuaEvent = (RaiseServiceEvent) annotation;
                    serviceEvent.setService(raiseKapuaEvent.service());
                    serviceEvent.setEntityType(raiseKapuaEvent.entityType());
                    serviceEvent.setOperation(raiseKapuaEvent.operation());
                    serviceEvent.setNote(raiseKapuaEvent.note());
                    break;
                }
            }
        }
    }

    private void useEntityToFillEvent(ServiceEvent serviceEvent, List<KapuaEntity> entities) {
        if (entities.size()>1) {
            LOG.warn("Found more than one KapuaEntity in the parameters! Assuming to use the first one!");
            wrongEntity.inc();
        }
        KapuaEntity entity = entities.get(0);
        serviceEvent.setEntityType(entity.getClass().getName());
        serviceEvent.setEntityId(entity.getId());
        serviceEvent.setEntityScopeId(entity.getScopeId());
        LOG.info("Entity '{}' with id '{}' and scopeId '{}' found!", entity.getClass().getName(), entity.getId(), entity.getScopeId());
    }

    private void useKapuaIdsToFillEvent(ServiceEvent serviceEvent, List<KapuaId> ids, Class<?>[] implementedClass) {
        if (ids.size()>2) {
            LOG.warn("Found more than two KapuaId in the parameters! Assuming to use the first two!");
            wrongId.inc();
        }
        if (ids.size() >= 2) {
            serviceEvent.setEntityScopeId(ids.get(0));
            serviceEvent.setEntityId(ids.get(1));
        }
        else {
            serviceEvent.setEntityId(ids.get(0));
        }
        String serviceInterface = implementedClass[0].getAnnotatedInterfaces()[0].getType().getTypeName();
        String genericsList = serviceInterface.substring(serviceInterface.indexOf('<') + 1, serviceInterface.indexOf('>'));
        String[] entityClassesToScan = genericsList.replaceAll("\\,", "").split(" ");
        for (String str : entityClassesToScan) {
            try {
                if (KapuaEntity.class.isAssignableFrom(Class.forName(str))) {
                    serviceEvent.setEntityType(str);
                }
            } catch (ClassNotFoundException e) {
                // do nothing
                LOG.warn("Cannot find class {}", str, e);
            }
        }
    }

    private void logInputParameters(Method method) {
        LOG.debug("Event input parameters: ");
        LOG.debug("   Parameter types");
        for (Class<?> tmp : method.getParameterTypes()) {
            LOG.debug("      {}", tmp.getName());
        }
        LOG.info("   Declared annotations");
        for (Annotation tmp : method.getDeclaredAnnotations()) {
            LOG.debug("      {}", tmp.getClass());
        }
        LOG.info("   Parameters");
        for (Parameter tmp : method.getParameters()) {
            LOG.debug("      {} - class: {}", tmp.getName(), tmp.getType());
        }
        LOG.debug("================ END");
    }

    private void logFoundEntities(List<KapuaEntity> entities, List<KapuaId> ids) {
        LOG.debug("Entities found:");
        for (KapuaEntity tmp : entities) {
            LOG.debug("   id: {} - scopeId: {} - type: {}", tmp.getId(), tmp.getScopeId(), tmp.getType());
        }
        LOG.debug("   KapuaIds found:");
        for (KapuaId tmp : ids) {
            LOG.debug("   id: {}", tmp.getId());
        }
    }

    private void sendEvent(MethodInvocation invocation, ServiceEvent serviceEvent, Object returnedValue) throws ServiceEventBusException {
        String address = ServiceMap.getAddress(serviceEvent.getService());
        try {
            ServiceEventBusManager.getInstance().publish(address, serviceEvent);
            LOG.info("SENT event from service {} to {} - entity type {} - entity scope id {} - entity id {} - context id {}",
                    serviceEvent.getService(),
                    address,
                    serviceEvent.getEntityType(),
                    serviceEvent.getEntityScopeId(),
                    serviceEvent.getEntityId(),
                    serviceEvent.getContextId());
            // if message was sent successfully then confirm the event in the event table
            updateEventStatus(invocation, serviceEvent, EventStatus.SENT);
        } catch (ServiceEventBusException e) {
            LOG.warn("Error sending event", e);
            // mark event status as SEND_ERROR
            updateEventStatus(invocation, serviceEvent, EventStatus.SEND_ERROR);
        }
    }

    private void updateEventStatus(MethodInvocation invocation, ServiceEvent serviceEventBus, EventStatus newServiceEventStatus) {
        if (invocation.getThis() instanceof AbstractKapuaService) {
            try {
                serviceEventBus.setStatus(newServiceEventStatus);
                ((AbstractKapuaService) invocation.getThis()).getEntityManagerSession().onTransactedAction(
                        em -> EventStoreDAO.update(em,
                                ServiceEventUtil.mergeToEntity(EventStoreDAO.find(em, serviceEventBus.getScopeId(), KapuaEid.parseCompactId(serviceEventBus.getId())), serviceEventBus)));
            } catch (Throwable t) {
                // this may be a valid condition if the HouseKeeper is doing the update concurrently with this task
                LOG.warn("Error updating event status: {}", t.getMessage(), t);
            }
        }
    }

}

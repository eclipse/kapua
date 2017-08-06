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
package org.eclipse.kapua.commons.event.service;

import java.util.Date;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.kapua.commons.event.bus.EventBusManager;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventBus;
import org.eclipse.kapua.service.event.KapuaEventBusException;
import org.eclipse.kapua.service.event.RaiseKapuaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Event interceptor
 * 
 * @since 1.0
 */
@KapuaProvider
@InterceptorBind(matchSubclassOf = KapuaService.class, matchAnnotatedWith = RaiseKapuaEvent.class)
public class RaiseKapuaEventInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaiseKapuaEventInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object returnObject = null;

        try {
            // if(!create) then the entity id can be set here
            KapuaEvent kapuaEvent = EventScope.begin();

            KapuaSession session = KapuaSecurityUtils.getSession();
            // Context ID is initialized/managed by the EventScope object
            kapuaEvent.setTimestamp(new Date());
            kapuaEvent.setUserId(session.getUserId());
            kapuaEvent.setScopeId(session.getScopeId());
            kapuaEvent.setOperation(invocation.getMethod().getName());
            fillEntityFields(invocation, kapuaEvent);
            fillServiceName(invocation, kapuaEvent);

            // TODO Extract from MethodInvocation and RaiseKapuaEvent annotation attributes
            kapuaEvent.setInputs(" ");

            // event.setProperties(properties);

            //execute the business logic
            returnObject = invocation.proceed();

            // Raise service event if the execution is successful
            sendEvent(invocation, kapuaEvent, returnObject);

            return returnObject;

        } finally {
            EventScope.end();
        }
    }

    private void fillEntityFields(MethodInvocation invocation, KapuaEvent kapuaEvent) {
        Object[] arguments = invocation.getArguments();
        if (arguments!=null) {
            for (Object tmp : arguments) {
                LOGGER.info("Scan for entity. Object: {}", tmp!=null ? tmp.getClass() : "NULL");
                if (tmp instanceof KapuaEntity) {
                    kapuaEvent.setEntityType(tmp.getClass().getName());
                    kapuaEvent.setEntityId(((KapuaEntity) tmp).getId());
                    LOGGER.info("Entity '{}' with id '{}' found!", new Object[]{tmp.getClass().getName(), ((KapuaEntity) tmp).getId()});
                    return;
                }
            }
            //otherwise assume that the second identifier is the entity id (if there are more than one) or take the first one (if there is one)
            int kapuaIdPosition = 0;
            int kapuaIdFound = 0;
            for (int i=0; i<arguments.length; i++) {
                Object tmp = arguments[i];
                if (tmp instanceof KapuaId) {
                    kapuaIdPosition = i;
                    if (++kapuaIdFound > 1) {
                        break;
                    }
                }
            }
            if (kapuaIdFound>0) {
                kapuaEvent.setEntityId(((KapuaId) arguments[kapuaIdPosition]));
                kapuaEvent.setEntityType(extractEntityType(invocation));
                return;
            }
            //send some error???
        }
        else {
            //send some error???
        }
    }

    private void fillServiceName(MethodInvocation invocation, KapuaEvent kapuaEvent) {
        //get the service name
        //the service is wrapped by guice so getThis --> getSuperclass() should provide the intercepted class
        //then keep the interface from this object
        Class<?> wrappedClass = invocation.getThis().getClass().getSuperclass(); //this object should be not null
        Class<?>[] impementedClass = wrappedClass.getInterfaces();
        //assuming that the KapuaService implemented is specified by the first implementing interface
        String serviceInterfaceName = impementedClass[0].getName();
        String tmp[] = serviceInterfaceName.split("\\.");
        String serviceName = tmp[tmp.length-1];
        String cleanedServiceName = serviceName.substring(0, serviceName.length()-"Service".length()).toLowerCase();
        LOGGER.info("Service name '{}' ", cleanedServiceName);
        kapuaEvent.setService(cleanedServiceName);
    }

    private String extractEntityType(MethodInvocation invocation) {
        //the service is wrapped by guice so getThis --> getSuperclass() should provide the intercepted class
        //then keep the interface from this object
        Class<?> wrappedClass = invocation.getThis().getClass().getSuperclass(); //this object should be not null
        Class<?>[] impementedClass = wrappedClass.getInterfaces();
        String serviceInterface = impementedClass[0].getAnnotatedInterfaces()[0].getType().getTypeName();
        String genericsList = serviceInterface.substring(serviceInterface.indexOf('<')+1, serviceInterface.indexOf('>'));
        String[] entityClassesToScan = genericsList.replaceAll("\\,", "").split(" ");
        for (String str : entityClassesToScan) {
            try {
                if (KapuaEntity.class.isAssignableFrom(Class.forName(str))) {
                    return str;
                }
            } catch (ClassNotFoundException e) {
                //do nothing
                LOGGER.warn("Cannon find class {}", str, e);
            }
        }
        return null;
    }

    private void sendEvent(MethodInvocation invocation, KapuaEvent kapuaEvent, Object returnedValue) throws KapuaEventbusException {
        KapuaEventbus eventbus = EventbusProvider.getInstance();

        String address = String.format("%s", kapuaEvent.getService());
        eventbus.publish(address, kapuaEvent);
        LOGGER.info("SENT event from service {} - entity type {} - entity id {} - context id {}", new Object[]{kapuaEvent.getService(), kapuaEvent.getEntityType(), kapuaEvent.getEntityId(), kapuaEvent.getContextId()});
    }

}

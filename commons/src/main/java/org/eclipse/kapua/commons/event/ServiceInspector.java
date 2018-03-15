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
package org.eclipse.kapua.commons.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.event.ListenServiceEvent;
import org.eclipse.kapua.event.RaiseServiceEvent;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceInspector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInspector.class);

    private static final String GUICE_ENHANCER_TAG = "$$EnhancerByGuice$$";

    private ServiceInspector() {}

    public static <T extends KapuaService> List<ServiceEventClientConfiguration> getEventBusClients(KapuaService aService, Class<T> clazz) {

        Class<? extends KapuaService> superclass = null;
        if (clazz.isAssignableFrom(aService.getClass().getSuperclass())) {
            superclass = (Class<T>) aService.getClass().getSuperclass(); // Safe because of the check in the condition
        }

        Method[] methods = aService.getClass().getMethods();
        List<ServiceEventClientConfiguration> configurations = new ArrayList<>();
        for (Method method : methods) {
            RaiseServiceEvent[] raiseAnnotations = method.getAnnotationsByType(RaiseServiceEvent.class);
            ListenServiceEvent[] listenAnnotations = method.getAnnotationsByType(ListenServiceEvent.class);
            if (superclass != null && (isNullOrEmpty(listenAnnotations) || isNullOrEmpty(raiseAnnotations))) {
                Method matchingMethod = null;
                try {
                    matchingMethod = getMatchingMethod(superclass, method);
                    if (isNullOrEmpty(listenAnnotations)) {
                        listenAnnotations = matchingMethod.getAnnotationsByType(ListenServiceEvent.class);
                    }
                    if (isNullOrEmpty(raiseAnnotations)) {
                        raiseAnnotations = matchingMethod.getAnnotationsByType(RaiseServiceEvent.class);
                    }
                } catch (NoSuchMethodException e) {                 
                    LOGGER.debug("Method not found in superclass: {}", method);
                }
            }
            if (!isNullOrEmpty(listenAnnotations) && !isNullOrEmpty(raiseAnnotations)) {
                KapuaRuntimeException.internalError(String.format("A method cannot be annotated with both %s and %s", ListenServiceEvent.class, RaiseServiceEvent.class));
            }
            if (!isNullOrEmpty(listenAnnotations)) {
                Method enhancedMethod = null;
                try {
                    enhancedMethod = aService.getClass().getMethod(method.getName(), method.getParameterTypes());
                    if (!enhancedMethod.getReturnType().equals(Void.class)) {
                        KapuaRuntimeException.internalError("Invalid method signature: return type, expected Void");
                    }
                    int paramsSize = enhancedMethod.getParameterTypes().length;
                    if (paramsSize != 1) {
                        KapuaRuntimeException.internalError(String.format("Invalid method signature: number of parameters %d, expected 1", paramsSize));
                    }
                    Class<?> paramClazz = enhancedMethod.getParameterTypes()[0];
                    if (enhancedMethod.getParameterTypes()[0].equals(ServiceEvent.class)) {
                        KapuaRuntimeException.internalError(String.format("Invalid method signature: type of parameters %s, expected ServiceEvent", paramClazz));
                    }
                } catch (NoSuchMethodException | SecurityException e1) {
                    KapuaRuntimeException.internalError(e1, String.format("Unable to get the annotated method: annotation %s", ListenServiceEvent.class));
                }

                for (ListenServiceEvent listenAnnotation:listenAnnotations) {
                    final Method listenerMethod = enhancedMethod;
                    configurations.add(
                        new ServiceEventClientConfiguration(
                                listenAnnotation.fromAddress(),
                                clazz.getName(),
                                (serviceEvent) -> {
                                    try {
                                        listenerMethod.invoke(aService, new Object[] { serviceEvent });
                                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                                        throw KapuaException.internalError(e, String.format("Error invoking method %s", method.getName()));
                                    }
                                }));
                }
            }
            if (!isNullOrEmpty(raiseAnnotations)) {
                configurations.add(
                        new ServiceEventClientConfiguration(
                                null,
                                clazz.getName(),
                                null));
            }
        }

        return configurations;
    }
//
//    public static List<ServiceEventListenerConfiguration> getEventBusListeners(KapuaService aService, Class<? extends KapuaService> clazz) {
//
//        String subAddress = clazz.getName();
//
//        Method[] methods;
//        boolean isEnhancedClass = isEnhancedClass(aService);
//        if (isEnhancedClass) {
//            methods = getSuperMethods(aService);
//        } else {
//            methods = getMethods(aService);
//        }
//
//        List<ServiceEventListenerConfiguration> configurations = new ArrayList<>();
//        for (Method method : methods) {
//            ListenServiceEvent[] listenAnnotations = method.getAnnotationsByType(ListenServiceEvent.class);
//            if (listenAnnotations != null) {
//
//                Method enhancedMethod = null;
//                try {
//                    enhancedMethod = aService.getClass().getMethod(method.getName(), method.getParameterTypes());
//                    if (!enhancedMethod.getReturnType().equals(Void.class)) {
//                        KapuaRuntimeException.internalError("Invalid method signature: return type, expected Void");
//                    }
//                    int paramsSize = enhancedMethod.getParameterTypes().length;
//                    if (paramsSize != 1) {
//                        KapuaRuntimeException.internalError(String.format("Invalid method signature: number of parameters %d, expected 1", paramsSize));
//                    }
//                    Class<?> paramClazz = enhancedMethod.getParameterTypes()[0];
//                    if (enhancedMethod.getParameterTypes()[0].equals(ServiceEvent.class)) {
//                        KapuaRuntimeException.internalError(String.format("Invalid method signature: type of parameters %s, expected ServiceEvent", paramClazz));
//                    }
//                } catch (NoSuchMethodException | SecurityException e1) {
//                    KapuaRuntimeException.internalError(e1, String.format("Unable to get the annotated method: annotation %s", ListenServiceEvent.class));
//                }
//
//                for (ListenServiceEvent listenAnnotation:listenAnnotations) {
//                    final Method listenerMethod = enhancedMethod;
//                    configurations.add(
//                        new ServiceEventListenerConfiguration(
//                                listenAnnotation.fromAddress(),
//                                subAddress,
//                                (serviceEvent) -> {
//                                    try {
//                                        listenerMethod.invoke(aService, new Object[] { serviceEvent });
//                                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//                                        KapuaException.internalError(e, String.format("Error invoking method %s", method.getName()));
//                                    }
//                                }));
//                }
//            }
//        }
//
//        return configurations;
//    }
//

    private static boolean isEnhancedClass(Object obj) {
        String canonicalName = obj.getClass().getCanonicalName();
        boolean isEnhanced = canonicalName.contains(GUICE_ENHANCER_TAG);
        return isEnhanced;
    }

    private static Class<?> getSuperClass(Object obj) {
        return obj.getClass().getSuperclass();
    }

    private static Method[] getSuperMethods(Object obj) {
        Class<?> superclass = obj.getClass().getSuperclass();
        Method[] superMethod = superclass.getMethods();
        return superMethod;
    }

    private static Method[] getMethods(Object obj) {
        Method[] superMethod = obj.getClass().getMethods();
        return superMethod;
    }

    private static Method getMatchingMethod(Class<?> clazz, Method method) throws NoSuchMethodException {
        Method[] methods = clazz.getMethods();
        if (methods == null || methods.length == 0) {
            return null;
        }

        List<Class<?>> methodParamTypes = Arrays.asList(method.getParameterTypes());

        Method matchingMethod = null;
        for (Method candidate:methods) {
            if (!candidate.getName().equals(method.getName())) {
                continue;
            }
            if (!candidate.getReturnType().equals(method.getReturnType())) {
                continue;
            }

            List<Class<?>> candidateParamTypes = Arrays.asList(method.getParameterTypes());

            if (candidateParamTypes.size() != methodParamTypes.size() || !candidateParamTypes.containsAll(methodParamTypes)) {
                continue;
            }

            matchingMethod = candidate;
            break;
        }

        if (matchingMethod == null) {
            throw new NoSuchMethodException(method.getName());
        }
        return matchingMethod;
    }

    private static boolean isNullOrEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }
}

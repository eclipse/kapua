/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app.annotated;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.kapua.kura.simulator.app.AbstractDefaultApplication;
import org.eclipse.kapua.kura.simulator.app.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AnnotatedApplication extends AbstractDefaultApplication {

    private static final Logger logger = LoggerFactory.getLogger(AnnotatedApplication.class);

    private static interface ResourceHandler {

        public void handle(Request request) throws Exception;
    }

    private static class PlainMethodHandler implements ResourceHandler {

        private final MethodHandle methodHandle;

        public PlainMethodHandler(final MethodHandle methodHandle) {
            this.methodHandle = methodHandle;
        }

        @Override
        public void handle(final Request request) throws Exception {
            try {
                methodHandle.invokeExact(request);
            } catch (final Exception e) {
                throw e;
            } catch (final Throwable e) {
                throw new InvocationTargetException(e);
            }
        }

    }

    private final Map<String, ResourceHandler> handlers;

    private AnnotatedApplication(final String applicationId, final Map<String, ResourceHandler> handlers) {
        super(applicationId);
        this.handlers = handlers;
    }

    @Override
    protected void processRequest(final Request request) throws Exception {
        final String comand = request.getMessage().getTopic().render(0, 2);

        final ResourceHandler handler = handlers.get(comand);
        logger.debug("Mapping request - {} -> {}", comand, handler);
        if (handler == null) {
            request.replyNotFound();
            return;
        }

        handler.handle(request);
    }

    public static org.eclipse.kapua.kura.simulator.app.Application build(final Object applicationInstance)
            throws Exception {
        Objects.requireNonNull(applicationInstance);

        final Class<? extends Object> clazz = applicationInstance.getClass();
        final Application app = clazz.getAnnotation(Application.class);
        if (app == null) {
            throw new IllegalArgumentException(String.format("Application class %s is missing the @%s annotation",
                    clazz.getName(), Application.class.getName()));
        }

        final String appId = app.value();
        if (appId == null || appId.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Application annotation must have a valid application id value"));
        }

        final Map<String, ResourceHandler> handlers = new HashMap<>();

        fillHandlers(handlers, clazz, applicationInstance);

        return new AnnotatedApplication(appId, handlers);
    }

    public static org.eclipse.kapua.kura.simulator.app.Application build(final Class<?> applicationClazz)
            throws Exception {
        try {
            return build(applicationClazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create application instance", e);
        }
    }

    private static void fillHandlers(final Map<String, ResourceHandler> handlers, final Class<? extends Object> clazz,
            final Object applicationInstance) throws Exception {

        for (final Method method : clazz.getMethods()) {
            fillHandlersFromMethod(handlers, clazz, applicationInstance, method);
        }
    }

    private static void fillHandlersFromMethod(final Map<String, ResourceHandler> handlers,
            final Class<? extends Object> clazz, final Object applicationInstance, final Method method)
            throws Exception {

        final Resource r = method.getAnnotation(Resource.class);
        if (r != null) {
            fillHandlersFromResourceMethod(handlers, clazz, applicationInstance, method);
        }

        fillHandlersFromVerbMethod(handlers, clazz, applicationInstance, method, EXECUTE.class, "EXEC");
        fillHandlersFromVerbMethod(handlers, clazz, applicationInstance, method, GET.class, "GET");
        fillHandlersFromVerbMethod(handlers, clazz, applicationInstance, method, PUT.class, "PUT");
        fillHandlersFromVerbMethod(handlers, clazz, applicationInstance, method, POST.class, "POST");
        fillHandlersFromVerbMethod(handlers, clazz, applicationInstance, method, DELETE.class, "DEL");
    }

    private static void fillHandlersFromVerbMethod(final Map<String, ResourceHandler> handlers, final Class<?> clazz,
            final Object applicationInstance, final Method method, final Class<? extends Annotation> annotationClazz,
            final String kuraVerb) throws IllegalAccessException {

        if (!method.isAnnotationPresent(annotationClazz)) {
            return;
        }

        mapMethod(handlers, applicationInstance, method, kuraVerb, method.getName());
    }

    private static void fillHandlersFromResourceMethod(final Map<String, ResourceHandler> handlers,
            final Class<? extends Object> clazz, final Object applicationInstance, final Method method)
            throws Exception {

        final String name = method.getName();

        String verb = null;
        final String resource;

        StringBuilder verbBuilder = new StringBuilder();
        final StringBuilder resourceBuilder = new StringBuilder();

        for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            if (verbBuilder != null) {
                if (Character.isUpperCase(c)) {
                    verb = verbBuilder.toString().toUpperCase();
                    resourceBuilder.append(Character.toLowerCase(c));
                    verbBuilder = null;
                } else {
                    verbBuilder.append(c);
                }
            } else {
                resourceBuilder.append(c);
            }
        }

        resource = resourceBuilder.toString();

        if (verb == null || verb.isEmpty() || resource == null || resource.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Method '%s' of class '%s' has not a valid name", name, clazz.getName()));
        }

        mapMethod(handlers, applicationInstance, method, verb, resource);
    }

    private static void mapMethod(final Map<String, ResourceHandler> handlers, final Object applicationInstance,
            final Method method, String verb, final String resource) throws IllegalAccessException {

        // allow proper verbs
        if ("DELETE".equals(verb)) {
            verb = "DEL";
        } else if ("EXECUTE".equals(verb)) {
            verb = "EXEC";
        }

        logger.debug("Mapping - {} - {}/{}", method.getDeclaringClass().getName(), verb, resource);

        final MethodHandle mh = MethodHandles.lookup().unreflect(method);
        handlers.put(verb + "/" + resource, new PlainMethodHandler(mh.bindTo(applicationInstance)));
    }

}

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
package org.eclipse.kapua.job.engine.jbatch;

import org.eclipse.kapua.locator.KapuaLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KapuaLocatorInjector {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KapuaLocator kapuaLocator;

    public KapuaLocatorInjector(KapuaLocator kapuaLocator) {
        this.kapuaLocator = kapuaLocator;
    }

    public void injectKapuaReferences(Object artifact) {
        if (artifact == null) {
            logger.trace("Null artifact, bailing out");
            return;
        }
        // Go through declared field annotations
        for (final Field field : getAllFields(new ArrayList<>(), artifact.getClass())) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                public Object run() {
                    field.setAccessible(true); // ignore java accessibility
                    return null;
                }
            });

            Inject injectAnnotation = field.getAnnotation(Inject.class);
            if (injectAnnotation == null) {
                logger.trace("{}.{} not annotated for injection, skipping", artifact.getClass().getSimpleName(), field.getName());
                continue;
            }
            try {
                if (field.get(artifact) != null) {
                    logger.trace("{}.{} already set, skipping", artifact.getClass().getSimpleName(), field.getName());
                    continue;
                }
                final Object fromKapua = kapuaLocator.getComponent(field.getType());
                if (fromKapua == null) {
                    logger.trace("{}.{} could not be injected from KapuaLocator, skipping", artifact.getClass().getSimpleName(), field.getName());
                    continue;
                }
                logger.trace("{}.{} injected from KapuaLocator", artifact.getClass().getSimpleName(), field.getName());
                field.set(artifact, fromKapua);
            } catch (IllegalAccessException e) {
                logger.error("Failed to inject {}.{}", artifact.getClass().getSimpleName(), field.getName(), e);
                throw new RuntimeException(e);
            }
        }
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }
}

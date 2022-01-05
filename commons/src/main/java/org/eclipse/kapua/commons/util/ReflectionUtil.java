/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    private static final String CANNOT_LOAD_INSTANCE_ERROR_MSG = "Cannot load instance %s. Please check the configuration file!";
    private static final String DEFAULT_INSTANCE_NULL_ERROR_MSG = "Cannot load instance %s. Default instance class is null!";

    private ReflectionUtil() {
    }

    public static <T> T newInstance(String clazz, Class<T> defaultInstance) throws KapuaException {
        logger.info("Initializing instance...");
        T instance;
        // lazy synchronization
        try {
            if (!StringUtils.isEmpty(clazz)) {
                logger.info("Initializing instance of {}...", clazz);
                @SuppressWarnings("unchecked")
                Class<T> clazzToInstantiate = (Class<T>) Class.forName(clazz);
                instance = clazzToInstantiate.newInstance();
            } else {
                logger.info("Initializing instance of. Instantiate default instance {} ...", defaultInstance);
                if (defaultInstance==null) {
                    throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, String.format(DEFAULT_INSTANCE_NULL_ERROR_MSG, clazz));
                }
                instance = defaultInstance.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e, String.format(CANNOT_LOAD_INSTANCE_ERROR_MSG, clazz));
        }
        logger.info("Initializing broker ip resolver... DONE");
        return instance;
    }

}

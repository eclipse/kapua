/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.entity;

import org.eclipse.kapua.KapuaRuntimeErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;

import java.util.Properties;

/**
 * @since 1.1.0
 */
public class EntityPropertiesWriteException extends KapuaRuntimeException {

    private final String attribute;
    private final Properties properties;

    public EntityPropertiesWriteException(Exception e, String attribute, Properties properties) {
        super(KapuaRuntimeErrorCodes.ENTITY_PROPERTIES_WRITE_ERROR, e, attribute, properties);

        this.attribute = attribute;
        this.properties = properties;
    }

    public String getAttribute() {
        return attribute;
    }

    public Properties getProperties() {
        return properties;
    }
}

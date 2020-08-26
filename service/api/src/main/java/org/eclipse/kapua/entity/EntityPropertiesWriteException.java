/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.entity;

import org.eclipse.kapua.KapuaRuntimeErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;

import java.util.Properties;

public class EntityPropertiesWriteException extends KapuaRuntimeException {

    private final String attribute;
    private final Properties properties;

    public EntityPropertiesWriteException(Exception e, String attribute, Properties properties) {
        super(KapuaRuntimeErrorCodes.ENTITY_PROPERTIES_READ_ERROR, e, attribute, properties);

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

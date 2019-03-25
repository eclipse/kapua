/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
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

public class EntityPropertiesReadException extends KapuaRuntimeException {

    private final String attribute;
    private final String stringProperties;

    public EntityPropertiesReadException(Exception e, String attribute, String stringProperties) {
        super(KapuaRuntimeErrorCodes.ENTITY_PROPERTIES_READ_ERROR, e, attribute, stringProperties);

        this.attribute = attribute;
        this.stringProperties = stringProperties;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getStringProperties() {
        return stringProperties;
    }
}

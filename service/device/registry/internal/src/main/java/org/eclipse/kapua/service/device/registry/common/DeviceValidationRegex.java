/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.common;

import org.eclipse.kapua.commons.util.ValidationRegex;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;

import java.util.regex.Pattern;

public enum DeviceValidationRegex implements ValidationRegex {

    /**
     * Validates value of {@link DeviceQuery#getFetchAttributes()}.
     *
     * @since 1.0.0
     */
    QUERY_FETCH_ATTRIBUTES("(" + DeviceAttributes.CONNECTION + "|" + DeviceAttributes.LAST_EVENT + ")"),

    /**
     * Validates value of {@link DeviceConnectionCreator#getClientId()} and {@link DeviceCreator#getClientId()}
     *
     * @since 1.2.0
     */
    CLIENT_ID("^((?!#|\\+|\\*|&|,|\\?|>|\\/|\\:\\:).)*$");

    private Pattern pattern;

    DeviceValidationRegex(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }
}

/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.message.system;

import java.util.Map;

import org.eclipse.kapua.broker.core.plugin.KapuaSecurityContext;

import com.google.common.base.Splitter;

/**
 * Default system message creator
 *
 * @since 1.0
 */
public class DefaultSystemMessageCreator implements SystemMessageCreator {

    private static final String FIELD_SEPARATOR = ",,";
    private static final String PAIR_SEPARATOR = ",";
    private static final String DEVICE_ID_KEY = "DeviceId";
    private static final String EVENT_KEY = "Event";
    private static final String USERNAME_KEY = "Username";

    @Override
    public String createMessage(SystemMessageType systemMessageType, KapuaSecurityContext kapuaSecurityContext) {
        StringBuilder builder = new StringBuilder();
        builder.append(EVENT_KEY).append(PAIR_SEPARATOR).append(systemMessageType.name());
        builder.append(FIELD_SEPARATOR).append(DEVICE_ID_KEY).append(PAIR_SEPARATOR).append(kapuaSecurityContext.getClientId());
        builder.append(FIELD_SEPARATOR).append(USERNAME_KEY).append(PAIR_SEPARATOR).append(kapuaSecurityContext.getUserName());
        return builder.toString();
    }

    public Map<String, String> convertFrom(String message) {
         return Splitter.on(FIELD_SEPARATOR).withKeyValueSeparator(PAIR_SEPARATOR).split(message);
    }

    public String getDeviceId(Map<String, String> map) {
        return map.get(DEVICE_ID_KEY);
    }

    public String getUsername(Map<String, String> map) {
        return map.get(USERNAME_KEY);
    }

}

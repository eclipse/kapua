/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.consumer.commons.message.system;

import java.util.Map;

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
    public String createMessage(SystemMessageType systemMessageType, Map<Fields, String> parameters) {
        StringBuilder builder = new StringBuilder();
        builder.append(EVENT_KEY).append(PAIR_SEPARATOR).append(systemMessageType.name());
        builder.append(FIELD_SEPARATOR).append(DEVICE_ID_KEY).append(PAIR_SEPARATOR).append(parameters.get(Fields.clientId));
        builder.append(FIELD_SEPARATOR).append(USERNAME_KEY).append(PAIR_SEPARATOR).append(parameters.get(Fields.username));
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

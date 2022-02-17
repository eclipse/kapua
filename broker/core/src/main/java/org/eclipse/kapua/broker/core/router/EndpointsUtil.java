/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.router;

import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.kapua.broker.core.message.MessageConstants;

import org.apache.camel.Exchange;

public class EndpointsUtil {

    private EndpointsUtil() { }

    public static boolean matches(Exchange exchange, Object value, String previous, Map<String, Object> properties, Pattern pattern) {

        if (previous == null) {
            String originaTopic = exchange.getIn().getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class);
            // TODO if pattern is null it is an error so it should be correct to throw an exception or leave the NullPointerException raised by the method?!
            return pattern.matcher(originaTopic).matches();
        } else {
            return false;
        }
    }

}

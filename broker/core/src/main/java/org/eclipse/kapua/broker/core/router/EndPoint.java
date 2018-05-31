/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.router;

import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlJavaTypeAdapter(EndPointAdapter.class)
public interface EndPoint {

    static Logger logger = LoggerFactory.getLogger(EndPoint.class);

    boolean matches(Exchange exchange, Object value, @Header(Exchange.SLIP_ENDPOINT) String previous, @Properties Map<String, Object> properties);

    String getEndpoint(Exchange exchange, Object value, @Header(Exchange.SLIP_ENDPOINT) String previous, @Properties Map<String, Object> properties);

    void toLog(StringBuffer buffer, String prefix);

    static String replacePlaceholder(String regex) {
        try {
            return PlaceholderReplacer.replace(regex);
        } catch (Exception e) {
            logger.error("Cannot replace placeholder '{}'", regex, e);
            return null;
        }
    }

    static Pattern parseRegex(String regex) {
        try {
            return Pattern.compile(regex);
        } catch (Exception e) {
            logger.error("Cannot compile regex '{}'", regex, e);
            return null;
        }
    }

}
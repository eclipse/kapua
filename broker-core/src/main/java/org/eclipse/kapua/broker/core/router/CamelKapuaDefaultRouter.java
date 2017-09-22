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

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Properties;
import org.eclipse.kapua.broker.core.listener.CamelConstants;
import org.eclipse.kapua.broker.core.message.MessageConstants;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default message router
 * 
 * @since 1.0
 */
public class CamelKapuaDefaultRouter {

    private final static Logger logger = LoggerFactory.getLogger(CamelKapuaDefaultRouter.class);

    private final static String REGEX_REPLACEMENT_CHARS = "([\\\\\\.\\)\\]\\}\\(‌​\\[\\{\\*\\+\\?\\^\\$\\|])";

    private final static String ESCAPED_CLASSIFIER = "^" +
            SystemSetting.getInstance().getMessageClassifier().replaceAll(REGEX_REPLACEMENT_CHARS, "\\\\$0") +
            "\\.";
    public static final Pattern CONTROL_TOPIC_PATTERN = Pattern.compile(ESCAPED_CLASSIFIER + ".*");
    public static final Pattern CONTROL_TOPIC_BIRTH_PATTERN = Pattern.compile(ESCAPED_CLASSIFIER + "(.*\\.){2}MQTT\\.BIRTH");
    public static final Pattern CONTROL_TOPIC_DC_PATTERN = Pattern.compile(ESCAPED_CLASSIFIER + "(.*\\.){2}MQTT\\.DC");
    public static final Pattern CONTROL_TOPIC_APPS_PATTERN = Pattern.compile(ESCAPED_CLASSIFIER + "(.*\\.){2}MQTT\\.APPS");
    public static final Pattern CONTROL_TOPIC_LWT_PATTERN = Pattern.compile(ESCAPED_CLASSIFIER + "(.*\\.){2}MQTT\\.LWT");
    public static final Pattern CONTROL_TOPIC_NOTIFY_PATTERN = Pattern.compile(ESCAPED_CLASSIFIER + "(.*\\.){2}MQTT\\.NOTIFY");

    private final static String DESTINATION_PATTERN = "%s";

    private final static String DST_BIRTH = "bean:kapuaLifeCycleConverter?method=convertToBirth,bean:deviceMessageListener?method=processBirthMessage";
    private final static String DST_DC = "bean:kapuaLifeCycleConverter?method=convertToDisconnect,bean:deviceMessageListener?method=processDisconnectMessage";
    private final static String DST_APPS = "bean:kapuaLifeCycleConverter?method=convertToApps,bean:deviceMessageListener?method=processAppsMessage";
    private final static String DST_MISSING = "bean:kapuaLifeCycleConverter?method=convertToMissing,bean:deviceMessageListener?method=processMissingMessage";
    private final static String DST_NOTIFY = "bean:kapuaLifeCycleConverter?method=convertToNotify,bean:deviceMessageListener?method=processNotifyMessage";
    private final static String DST_UNMATCHED = "activemq:queue:lifeCycleUnmatchedMessage";
    private final static String DST_DATA = "bean:kapuaDataConverter?method=convertToData,bean:dataStorageMessageProcessor?method=processMessage";

    public CamelKapuaDefaultRouter() {
    }

    public String defaultRouter(Exchange exchange, Object value, @Header(Exchange.SLIP_ENDPOINT) String previous, @Properties Map<String, Object> properties) {
        logger.trace("Received message on topic {} - Previous slip endpoint {} - id {}",
                new Object[] { exchange.getIn().getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class), previous,
                        exchange.getIn().getHeader(CamelConstants.JMS_CORRELATION_ID) });
        if (previous != null) {
            return null;
        } else {
            String originaTopic = exchange.getIn().getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class);
            if (CONTROL_TOPIC_PATTERN.matcher(originaTopic).matches()) {
                if (CONTROL_TOPIC_BIRTH_PATTERN.matcher(originaTopic).matches()) {
                    return String.format(DESTINATION_PATTERN, DST_BIRTH);
                } else if (CONTROL_TOPIC_DC_PATTERN.matcher(originaTopic).matches()) {
                    return String.format(DESTINATION_PATTERN, DST_DC);
                } else if (CONTROL_TOPIC_APPS_PATTERN.matcher(originaTopic).matches()) {
                    return String.format(DESTINATION_PATTERN, DST_APPS);
                } else if (CONTROL_TOPIC_LWT_PATTERN.matcher(originaTopic).matches()) {
                    return String.format(DESTINATION_PATTERN, DST_MISSING);
                } else if (CONTROL_TOPIC_NOTIFY_PATTERN.matcher(originaTopic).matches()) {
                    return String.format(DESTINATION_PATTERN, DST_NOTIFY);
                } else {
                    return String.format(DESTINATION_PATTERN, DST_UNMATCHED);
                }
            } else {
                // data message
                return String.format(DESTINATION_PATTERN, DST_DATA);
            }
        }
    }

}

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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.eclipse.kapua.broker.core.message.MessageConstants;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaceholderReplacer {

    static Logger logger = LoggerFactory.getLogger(PlaceholderReplacer.class);

    private final static String REGEX_REPLACEMENT_CHARS = "([\\\\\\.\\)\\]\\}\\(‌​\\[\\{\\*\\+\\?\\^\\$\\|])";

    enum CAMEL_ROUTER_PLACEHOLDER {
        CLASSIFIER, ORIGINAL_DESTINATION
    }

    private static HashMap<String, String> replacingMap;

    static {
        replacingMap = new HashMap<>();
        replacingMap.put(CAMEL_ROUTER_PLACEHOLDER.CLASSIFIER.name(),
                SystemSetting.getInstance().getMessageClassifier().replaceAll(REGEX_REPLACEMENT_CHARS, "\\\\$0"));
        replacingMap.put(CAMEL_ROUTER_PLACEHOLDER.ORIGINAL_DESTINATION.name(),
                String.format("${header.%s}", MessageConstants.PROPERTY_ORIGINAL_TOPIC));
    }

    private PlaceholderReplacer() {
    }

    public static String replacePlaceholder(String str) {
        try {
            return StrSubstitutor.replace(str, replacingMap);
        } catch (Exception e) {
            logger.error("Cannot replace placeholder '{}'", str, e);
            return null;
        }
    }

    public static String replacePlaceholder(String str, Map<String, Object> ac) {
        try {
            return StrSubstitutor.replace(StrSubstitutor.replace(str, replacingMap), ac);
        } catch (Exception e) {
            logger.error("Cannot replace placeholder '{}'", str, e);
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

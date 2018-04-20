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

import org.apache.commons.lang3.text.StrSubstitutor;
import org.eclipse.kapua.commons.setting.system.SystemSetting;

public class PlaceholderReplacer {

    private final static String REGEX_REPLACEMENT_CHARS = "([\\\\\\.\\)\\]\\}\\(‌​\\[\\{\\*\\+\\?\\^\\$\\|])";

    enum CAMEL_ROUTER_PLACEHOLDER {
        CLASSIFIER
    }

    private static HashMap<String, String> replacingMap;

    static {
        replacingMap = new HashMap<>();
        replacingMap.put(CAMEL_ROUTER_PLACEHOLDER.CLASSIFIER.name(), "^" +
                SystemSetting.getInstance().getMessageClassifier().replaceAll(REGEX_REPLACEMENT_CHARS, "\\\\$0") + "\\.");
    }

    private PlaceholderReplacer() {
    }

    public static String replace(String str) {
        return StrSubstitutor.replace(str, replacingMap);
    }

}

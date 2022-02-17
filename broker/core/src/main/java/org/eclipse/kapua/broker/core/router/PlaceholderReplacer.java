/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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

import org.apache.commons.lang3.text.StrSubstitutor;
import org.eclipse.kapua.commons.setting.system.SystemSetting;

import java.util.HashMap;

public class PlaceholderReplacer {

    private static final String REGEX_REPLACEMENT_CHARS = "([\\\\\\.\\)\\]\\}\\(‌​\\[\\{\\*\\+\\?\\^\\$\\|])";

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

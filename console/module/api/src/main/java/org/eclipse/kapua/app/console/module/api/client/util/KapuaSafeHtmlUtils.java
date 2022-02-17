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
package org.eclipse.kapua.app.console.module.api.client.util;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class KapuaSafeHtmlUtils {

    private KapuaSafeHtmlUtils() {
    }

    public static String htmlEscape(String unsafeHtml) {
        if (unsafeHtml == null) {
            return null;
        }
        return SafeHtmlUtils.htmlEscape(unsafeHtml);
    }

    public static String htmlUnescape(String safeHtml) {
        if (safeHtml == null) {
            return null;
        }

        if (safeHtml.indexOf("&amp;") != -1) {
            safeHtml = safeHtml.replace("&amp;", "&");
        }
        if (safeHtml.indexOf("amp;") != -1) {
            safeHtml = safeHtml.replace("amp;","");
        }
        if (safeHtml.indexOf("&lt;") != -1) {
            safeHtml = safeHtml.replace("&lt;", "<");
        }
        if (safeHtml.indexOf("&gt;") != -1) {
            safeHtml = safeHtml.replace("&gt;", ">");
        }
        if (safeHtml.indexOf("&quot;") != -1) {
            safeHtml = safeHtml.replace("&quot;", "\"");
        }
        if (safeHtml.indexOf("&#39;") != -1) {
            safeHtml = safeHtml.replace("&#39;", "'");
        }
        return safeHtml;
    }
}

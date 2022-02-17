/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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


public class SplitTooltipStringUtil {

    private SplitTooltipStringUtil() { }

    /**
     * Split long string into multiple rows.
     * Used for correct display of tooltip which contains user name and device client id.
     *
     * @param input long string
     * @param maxLineLength length of single line
     * @return string with line separators
     */
    public static String splitTooltipString(String input, int maxLineLength) {
        StringBuilder multiRowString = new StringBuilder();

        if(input != null) {
            for (int start = 0; start <= input.length(); start += maxLineLength) {
                int end = start;

                if ((end + maxLineLength) <= input.length()) {
                    end += maxLineLength;
                } else {
                    end = input.length();
                }
                multiRowString.append(input.substring(start, end));
                if (end < input.length()) {
                    multiRowString.append("</br>");
                }
            }
        }

        return multiRowString.toString();
    }
}

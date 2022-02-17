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
package org.eclipse.kapua.app.console.module.api.client.util;

public class CssLiterals {

    private CssLiterals() { }

    private static final String TOP = "-top";
    private static final String LEFT = "-left";
    private static final String BOTTOM = "-bottom";
    private static final String RIGHT = "-right";

    public static final String MARGIN = "margin";
    public static final String MARGIN_TOP = MARGIN + TOP;
    public static final String MARGIN_LEFT = MARGIN + LEFT;
    public static final String MARGIN_BOTTOM = MARGIN + BOTTOM;
    public static final String MARGIN_RIGHT = MARGIN + RIGHT;

    public static final String BORDER = "border";
    public static final String BORDER_TOP = BORDER + TOP;
    public static final String BORDER_LEFT = BORDER + LEFT;
    public static final String BORDER_BOTTOM = BORDER + BOTTOM;
    public static final String BORDER_RIGHT = BORDER + RIGHT;

    public static final String PADDING = "padding";

    public static final String BORDER_0PX_NONE = "0px none";
    public static String border1PxSolidRgb(int r, int g, int b) {
        return "1px solid rgb(" + r + ", " + g + ", " + b + ")";
    }

    public static final String WORD_BREAK_BREAK_ALL = "word-break: break-all";

}

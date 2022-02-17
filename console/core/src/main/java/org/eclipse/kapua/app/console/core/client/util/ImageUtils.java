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
package org.eclipse.kapua.app.console.core.client.util;

import com.google.gwt.resources.client.ImageResource;

public class ImageUtils {

    private ImageUtils() {
    }

    public static String toHTML(ImageResource ir, String altText) {
        StringBuilder sb = new StringBuilder();
        sb.append("<img src='")
                .append(ir.getSafeUri().asString())
                .append("' alt='")
                .append(altText)
                .append("' title='")
                .append(altText)
                .append("'/>");
        return sb.toString();
    }

    public static String toHTML(ImageResource ir, String altText, int size) {
        String sizeInt = String.valueOf(size);
        return toHTML(ir, altText, sizeInt, sizeInt);
    }

    public static String toHTML(ImageResource ir, String altText, String size) {
        return toHTML(ir, altText, size, size);
    }

    public static String toHTML(ImageResource ir, String altText, String width, String height) {
        StringBuilder sb = new StringBuilder();
        sb.append("<img src='")
                .append(ir.getSafeUri().asString())
                .append("' alt='")
                .append(altText)
                .append("' title='")
                .append(altText)
                .append("' width='")
                .append(width)
                .append("' height='")
                .append(height)
                .append("'/>");
        return sb.toString();
    }
}

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
package org.eclipse.kapua.app.console.module.data.client.util;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtHeader;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtHeader.GwtHeaderType;

public class HeaderTypeUtils {

    private HeaderTypeUtils() {
    }

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    public static String format(GwtHeader header) {
        GwtHeaderType type = GwtHeaderType.valueOf(header.getType().toUpperCase());
        switch (type) {
        case FLOAT:
            return MSGS.floatType();
        case INT:
            return MSGS.integerType();
        case DOUBLE:
            return MSGS.doubleType();
        case LONG:
            return MSGS.longType();
        case BOOLEAN:
            return MSGS.booleanType();
        case BYTE_ARRAY:
            return MSGS.byteArrayType();
        default:
        case STRING:
            return MSGS.stringType();
        }

    }
}

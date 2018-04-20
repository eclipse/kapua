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
package org.eclipse.kapua.model.type;

import javax.xml.bind.DatatypeConverter;

public class ByteArrayConverter {

    private ByteArrayConverter() {
    }

    public static String toString(Byte[] byteArray) {
        byte[] unboxedByteArray = new byte[byteArray.length];

        for (int i = 0; i < byteArray.length; i++) {
            unboxedByteArray[i] = byteArray[i];
        }

        return toString(unboxedByteArray);
    }

    public static String toString(byte[] byteArray) {
        return DatatypeConverter.printBase64Binary(byteArray);
    }

    public static byte[] fromString(String base64) {
        return DatatypeConverter.parseBase64Binary(base64);
    }
}
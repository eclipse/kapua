/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.model.xml;

import org.eclipse.kapua.model.type.ByteArrayConverter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BinaryXmlAdapter extends XmlAdapter<String, byte[]> {

    @Override
    public String marshal(byte[] binary) {
        return binary != null ? ByteArrayConverter.toString(binary) : null;
    }

    @Override
    public byte[] unmarshal(String binary) {
        return binary != null ? ByteArrayConverter.fromString(binary) : null;
    }
}

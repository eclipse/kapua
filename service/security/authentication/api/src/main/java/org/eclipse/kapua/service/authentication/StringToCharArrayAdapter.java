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
package org.eclipse.kapua.service.authentication;

import javax.xml.bind.annotation.adapters.XmlAdapter;

// FIXME: move to commons
public class StringToCharArrayAdapter extends XmlAdapter<String, char[]> {

    @Override
    public String marshal(char[] v) throws Exception {
        return String.valueOf(v);
    }

    @Override
    public char[] unmarshal(String v) throws Exception {
        return v.toCharArray();
    }
}

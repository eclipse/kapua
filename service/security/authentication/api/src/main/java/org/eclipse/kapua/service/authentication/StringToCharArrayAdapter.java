/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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

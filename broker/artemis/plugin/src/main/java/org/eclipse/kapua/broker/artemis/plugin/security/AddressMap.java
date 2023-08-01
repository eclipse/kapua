/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.artemis.plugin.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AddressMap {

    protected Map<String, Long> accessMap;

    AddressMap() {
        //TODO could be used a "normal" map?
        accessMap = new ConcurrentHashMap<String, Long>();
    }

    public void refresh(String address) {
        accessMap.put(address, System.currentTimeMillis());
    }

    public void remove(String address) {
        accessMap.remove(address);
    }

    public Long get(String address) {
        return accessMap.get(address);
    }

    public int sie() {
        return accessMap.size();
    }
}

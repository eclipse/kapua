/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.commons.message.request;

import org.eclipse.kapua.service.device.management.commons.message.KapuaAppChannelImpl;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestChannel;

public class KapuaRequestChannelImpl extends KapuaAppChannelImpl implements KapuaRequestChannel {

    protected KapuaMethod method;

    @Override
    public KapuaMethod getMethod() {
        return method;
    }

    @Override
    public void setMethod(KapuaMethod method) {
        this.method = method;
    }
}

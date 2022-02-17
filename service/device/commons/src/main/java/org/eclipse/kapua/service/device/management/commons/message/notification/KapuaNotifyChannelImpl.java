/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.commons.message.notification;

import org.eclipse.kapua.service.device.management.commons.message.KapuaAppChannelImpl;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyChannel;

/**
 * {@link KapuaNotifyChannel} implementation.
 *
 * @since 1.0.0
 */
public class KapuaNotifyChannelImpl extends KapuaAppChannelImpl implements KapuaNotifyChannel {

    private static final long serialVersionUID = 8630706854304183497L;

    private String[] resources;

    @Override
    public String[] getResources() {
        return resources;
    }

    @Override
    public void setResources(String[] resources) {
        this.resources = resources;
    }
}

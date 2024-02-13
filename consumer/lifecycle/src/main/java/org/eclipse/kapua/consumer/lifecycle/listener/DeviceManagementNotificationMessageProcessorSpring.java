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
package org.eclipse.kapua.consumer.lifecycle.listener;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.camel.message.CamelKapuaMessage;

/**
 * The sole purpose of this class is to be instantiated by spring in the application context,
 * providing a bridge to the guice's context - which in turn is responsible to instantiate the real {@link DeviceManagementNotificationMessageProcessor}
 */
public class DeviceManagementNotificationMessageProcessorSpring {

    private final DeviceManagementNotificationMessageProcessor inner = KapuaLocator.getInstance().getComponent(DeviceManagementNotificationMessageProcessor.class);

    public void processMessage(CamelKapuaMessage<?> message) throws KapuaException {
        inner.processMessage(message);
    }
}

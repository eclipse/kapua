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
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.event.ServiceEventBusException;

public interface ServiceEventBusDriver {

    public String getType();

    public void start() throws ServiceEventBusException;

    public void stop() throws ServiceEventBusException;

    public ServiceEventBus getEventBus();
}

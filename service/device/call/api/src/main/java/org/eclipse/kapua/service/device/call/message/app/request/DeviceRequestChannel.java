/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.app.request;

import org.eclipse.kapua.service.device.call.DeviceMethod;
import org.eclipse.kapua.service.device.call.message.app.DeviceAppChannel;

public interface DeviceRequestChannel extends DeviceAppChannel
{
    public DeviceMethod getMethod();

    public void setMethod(DeviceMethod method);

    public String[] getResources();

    public void setResources(String[] resources);

    public String getRequestId();

    public void setRequestId(String requestId);

    public String getRequesterClientId();

    public void setRequesterClientId(String requesterClientId);

}

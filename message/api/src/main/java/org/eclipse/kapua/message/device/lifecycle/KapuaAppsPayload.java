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
package org.eclipse.kapua.message.device.lifecycle;

import org.eclipse.kapua.message.KapuaPayload;

public interface KapuaAppsPayload extends KapuaPayload
{
    public String getUptime();

    public String getDisplayName();

    public String getModelName();

    public String getModelId();

    public String getPartNumber();

    public String getSerialNumber();

    public String getFirmware();

    public String getFirmwareVersion();

    public String getBios();

    public String getBiosVersion();

    public String getOs();

    public String getOsVersion();

    public String getJvm();

    public String getJvmVersion();

    public String getJvmProfile();

    public String getContainerFramework();

    public String getContainerFrameworkVersion();

    public String getApplicationFramework();

    public String getApplicationFrameworkVersion();

    public String getConnectionInterface();

    public String getConnectionIp();

    public String getAcceptEncoding();

    public String getApplicationIdentifiers();

    public String getAvailableProcessors();

    public String getTotalMemory();

    public String getOsArch();

    public String getModemImei();

    public String getModemImsi();

    public String getModemIccid();
}

/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.systeminfo.internal;

import org.eclipse.kapua.service.systeminfo.SystemInfo;

public class SystemInfoImpl implements SystemInfo {
    private String version;
    private String buildNumber;


    @Override
    public String getVersion() {
        return version;
    }


    @Override
    public void setVersion(String version) {
        this.version = version;
    }


    @Override
    public String getBuildVersion() {
        return buildNumber;
    }


    @Override
    public void setBuildVersion(String buildVersion) {
        this.buildNumber = buildVersion;
    }
}
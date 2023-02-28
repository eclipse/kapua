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
    private String buildDate;
    private String buildBranch;
    private String buildRevision;


    @Override
    public String getVersion() {
        return version;
    }


    @Override
    public void setVersion(String version) {
        this.version = version;
    }


    @Override
    public String getRevision() {
        return buildRevision;
    }


    @Override
    public void setRevision(String revision) {
        this.buildRevision = revision;
    }


    @Override
    public String getBuildTimestamp() {
        return buildDate;
    }


    @Override
    public void setBuildTimestamp(String buildDate) {
        this.buildDate = buildDate;
    }


    @Override
    public String getBuildNumber() {
        return buildNumber;
    }


    @Override
    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }


    @Override
    public String getBuildBranch() {
        return buildBranch;
    }


    @Override
    public void setBuildBranch(String buildBranch) {
        this.buildBranch = buildBranch;
    }
}
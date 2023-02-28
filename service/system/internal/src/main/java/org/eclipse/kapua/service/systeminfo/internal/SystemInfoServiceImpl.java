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

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.systeminfo.SystemInfo;
import org.eclipse.kapua.service.systeminfo.SystemInfoFactory;
import org.eclipse.kapua.service.systeminfo.SystemInfoService;

@KapuaProvider
public class SystemInfoServiceImpl implements SystemInfoService {
    private final KapuaLocator locator = KapuaLocator.getInstance();


    @Override
    public SystemInfo getSystemInfo() {
        SystemSetting systemSetting = SystemSetting.getInstance();
        String version = systemSetting.getString(SystemSettingKey.VERSION);
        String revision = systemSetting.getString(SystemSettingKey.BUILD_REVISION);
        String branch = systemSetting.getString(SystemSettingKey.BUILD_BRANCH);
        String timestamp = systemSetting.getString(SystemSettingKey.BUILD_TIMESTAMP);
        String buildNumber = systemSetting.getString(SystemSettingKey.BUILD_NUMBER);

        SystemInfoFactory systemInfoFactory = locator.getFactory(SystemInfoFactory.class);
        SystemInfo systemInfo = systemInfoFactory.newSystemInfo();
        systemInfo.setVersion(version);
        systemInfo.setRevision(revision);
        systemInfo.setBuildBranch(branch);
        systemInfo.setBuildTimestamp(timestamp + " UTC");
        systemInfo.setBuildNumber(buildNumber);
        return systemInfo;
    }
}

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
package org.eclipse.kapua.service.systeminfo.steps;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.systeminfo.SystemInfo;
import org.eclipse.kapua.service.systeminfo.SystemInfoFactory;
import org.eclipse.kapua.service.systeminfo.SystemInfoService;
import org.junit.Assert;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SystemInfoSteps extends TestBase {
    private SystemInfoService systemInfoService;
    private SystemInfoFactory systemInfoFactory;


    @Inject
    public SystemInfoSteps(StepData stepData) {
        super(stepData);
    }


    @After(value = "@setup")
    public void setServices() {
        KapuaLocator locator = KapuaLocator.getInstance();
        systemInfoService = locator.getService(SystemInfoService.class);
        systemInfoFactory = locator.getFactory(SystemInfoFactory.class);
    }


    @Given("I retrieve the system info")
    public void configureSystemInfo() {
        SystemInfo systemInfo = systemInfoService.getSystemInfo();
        stepData.put("systemVersion", systemInfo.getVersion());
        stepData.put("systemBuildVersion", systemInfo.getBuildVersion());
    }


    @Then("The version of the system is {string} and the build version of the system is {string}")
    public void theVersionOfTheSystemIsAndTheBuildVersionOfTheSystemIs(String expectedVersion, String expectedBuildVersion) {
        Assert.assertEquals(stepData.get("systemVersion"), expectedVersion);
        Assert.assertEquals(stepData.get("systemBuildVersion"), expectedBuildVersion);
    }
}

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
import org.eclipse.kapua.service.systeminfo.SystemInfoService;
import org.junit.Assert;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SystemInfoSteps extends TestBase {
    private SystemInfoService systemInfoService;


    @Inject
    public SystemInfoSteps(StepData stepData) {
        super(stepData);
    }


    @After(value = "@setup")
    public void setServices() {
        KapuaLocator locator = KapuaLocator.getInstance();
        systemInfoService = locator.getService(SystemInfoService.class);
    }


    @Given("I retrieve the system info")
    public void configureSystemInfo() {
        SystemInfo systemInfo = systemInfoService.getSystemInfo();
        stepData.put("systemVersion", systemInfo.getVersion());
        stepData.put("buildNumber", systemInfo.getBuildNumber());
        stepData.put("buildRevision", systemInfo.getRevision());
        stepData.put("buildTimestamp", systemInfo.getBuildTimestamp());
        stepData.put("buildBranch", systemInfo.getBuildBranch());
    }


    @Then("The version of the system is {string}")
    public void theVersionOfTheSystemIs(String expectedVersion) {
        Assert.assertEquals(stepData.get("systemVersion"), expectedVersion);
    }


    @Then("The build number of the system is {string}")
    public void theBuildNumberOfTheSystemIs(String expectedBuildNumber) {
        Assert.assertEquals(stepData.get("buildNumber"), expectedBuildNumber);
    }


    @Then("The build revision of the system is {string}")
    public void theRevisionOfTheSystemIs(String expectedBuildRevision) {
        Assert.assertEquals(stepData.get("buildRevision"), expectedBuildRevision);
    }


    @Then("The build timestamp of the system is {string}")
    public void theTimestampOfTheSystemIs(String expectedBuildTimestamp) {
        Assert.assertEquals(stepData.get("buildTimestamp"), expectedBuildTimestamp);
    }


    @Then("The build branch of the system is {string}")
    public void theBranchOfTheSystemIs(String expectedBuildBranch) {
        Assert.assertEquals(stepData.get("buildBranch"), expectedBuildBranch);
    }
}

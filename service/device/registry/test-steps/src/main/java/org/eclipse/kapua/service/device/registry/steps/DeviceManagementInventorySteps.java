/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.steps;

import com.google.inject.Singleton;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementService;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.DeviceInventoryBundle;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.DeviceInventoryBundles;
import org.eclipse.kapua.service.device.management.inventory.model.container.DeviceInventoryContainer;
import org.eclipse.kapua.service.device.management.inventory.model.container.DeviceInventoryContainers;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventoryItem;
import org.eclipse.kapua.service.device.management.inventory.model.packages.DeviceInventoryPackage;
import org.eclipse.kapua.service.device.management.inventory.model.packages.DeviceInventoryPackages;
import org.eclipse.kapua.service.device.management.inventory.model.system.DeviceInventorySystemPackage;
import org.eclipse.kapua.service.device.management.inventory.model.system.DeviceInventorySystemPackages;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.junit.Assert;

import javax.inject.Inject;
import java.util.List;

@Singleton
public class DeviceManagementInventorySteps extends TestBase {

    private static final String INVENTORY_ITEMS = "inventory_inventory";
    private static final String INVENTORY_BUNDLES = "inventory_bundles";
    private static final String INVENTORY_CONTAINERS = "inventory_container";
    private static final String INVENTORY_SYSTEM_PACKAGES = "inventory_system_packages";
    private static final String INVENTORY_DEPLOYMENT_PACKAGES = "inventory_deployment_packages";

    private DeviceRegistryService deviceRegistryService;

    private DeviceInventoryManagementService deviceInventoryManagementService;
    private DeviceInventoryManagementFactory deviceInventoryManagementFactory;

    /**
     * Scenario scoped step data.
     */
    @Inject
    public DeviceManagementInventorySteps(StepData stepData) {
        super(stepData);
    }

    @Before
    public void beforeScenario(Scenario scenario) {

        this.scenario = scenario;

        BrokerSetting.resetInstance();

        KapuaLocator locator = KapuaLocator.getInstance();
        deviceRegistryService = locator.getService(DeviceRegistryService.class);
        deviceInventoryManagementService = locator.getService(DeviceInventoryManagementService.class);
        deviceInventoryManagementFactory = locator.getFactory(DeviceInventoryManagementFactory.class);
    }

    @After
    public void afterScenario() {

        List<KuraDevice> kuraDevices = (List<KuraDevice>) stepData.get(KURA_DEVICES);

        if (kuraDevices != null) {
            for (KuraDevice kuraDevice : kuraDevices) {
                if (kuraDevice != null) {
                    kuraDevice.mqttClientDisconnect();
                }
            }
            kuraDevices.clear();
        }

        KapuaSecurityUtils.clearSession();
    }

    //
    // /inventory
    @When("Inventory Items are requested")
    public void inventoryItemsRequested() throws Exception {
        List<KuraDevice> kuraDevices = (List<KuraDevice>) stepData.get(KURA_DEVICES);
        for (KuraDevice kuraDevice : kuraDevices) {
            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
            if (device != null) {
                DeviceInventory deviceInventory = deviceInventoryManagementService.getInventory(device.getScopeId(), device.getId(), null);
                List<DeviceInventoryItem> inventoryItems = deviceInventory.getInventoryItems();
                stepData.put(INVENTORY_ITEMS, inventoryItems);
            }
        }
    }

    @Then("Inventory Items are received")
    public void inventoryItemsReceived() {
        List<DeviceInventoryItem> inventoryItems = (List<DeviceInventoryItem>) stepData.get(INVENTORY_ITEMS);
        Assert.assertNotNull(inventoryItems);
    }

    @Then("Inventory Items are {long}")
    public void inventoryItemsAreSize(long size) {
        List<DeviceInventoryItem> inventoryItems = (List<DeviceInventoryItem>) stepData.get(INVENTORY_ITEMS);
        Assert.assertEquals(size, inventoryItems.size());
    }

    @Then("Inventory Items has Item named {string} is present")
    public void inventoryItemNamedIsPresent(String inventoryItemName) {
        List<DeviceInventoryItem> inventoryItems = (List<DeviceInventoryItem>) stepData.get(INVENTORY_ITEMS);
        DeviceInventoryItem inventoryItem = inventoryItems.stream().filter(i -> i.getName().equals(inventoryItemName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryItem);
    }

    @Then("Inventory Items has Item named {string} has type {string}")
    public void inventoryItemNamedHasType(String inventoryItemNamed, String inventoryItemType) {
        List<DeviceInventoryItem> inventoryItems = (List<DeviceInventoryItem>) stepData.get(INVENTORY_ITEMS);
        DeviceInventoryItem inventoryItem = inventoryItems.stream().filter(i -> i.getName().equals(inventoryItemNamed)).findAny().orElse(null);
        Assert.assertNotNull(inventoryItem);
        Assert.assertEquals(inventoryItemType, inventoryItem.getItemType());
    }

    @Then("Inventory Items has Item named {string} has version {string}")
    public void inventoryItemNamedHasVersion(String inventoryItemName, String version) {
        List<DeviceInventoryItem> inventoryItems = (List<DeviceInventoryItem>) stepData.get(INVENTORY_ITEMS);
        DeviceInventoryItem inventoryItem = inventoryItems.stream().filter(i -> i.getName().equals(inventoryItemName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryItem);
        Assert.assertEquals(version, inventoryItem.getVersion());
    }

    //
    // /bundles
    @When("Inventory Bundles are requested")
    public void inventoryBundlesRequested() throws Exception {
        List<KuraDevice> kuraDevices = (List<KuraDevice>) stepData.get(KURA_DEVICES);
        for (KuraDevice kuraDevice : kuraDevices) {
            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
            if (device != null) {
                DeviceInventoryBundles deviceInventoryBundles = deviceInventoryManagementService.getBundles(device.getScopeId(), device.getId(), null);
                List<DeviceInventoryBundle> inventoryBundles = deviceInventoryBundles.getInventoryBundles();
                stepData.put(INVENTORY_BUNDLES, inventoryBundles);
            }
        }
    }

    @Then("Inventory Bundles are received")
    public void inventoryBundlesReceived() {
        List<DeviceInventoryBundle> inventoryBundles = (List<DeviceInventoryBundle>) stepData.get(INVENTORY_BUNDLES);
        Assert.assertNotNull(inventoryBundles);
    }

    @Then("Inventory Bundles are {long}")
    public void inventoryBundlesAreSize(long size) {
        List<DeviceInventoryBundle> inventoryBundles = (List<DeviceInventoryBundle>) stepData.get(INVENTORY_BUNDLES);
        Assert.assertEquals(size, inventoryBundles.size());
    }

    @Then("Inventory Bundles has Bundle named {string} is present")
    public void inventoryBundleNamedIsPresent(String inventoryBundleName) {
        List<DeviceInventoryBundle> inventoryBundles = (List<DeviceInventoryBundle>) stepData.get(INVENTORY_BUNDLES);
        DeviceInventoryBundle inventoryBundle = inventoryBundles.stream().filter(b -> b.getName().equals(inventoryBundleName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryBundle);
    }

    @Then("Inventory Bundles has Bundle named {string} has id {string}")
    public void inventoryBundleNamedHasId(String inventoryBundleNamed, String inventoryBundleId) {
        List<DeviceInventoryBundle> inventoryBundles = (List<DeviceInventoryBundle>) stepData.get(INVENTORY_BUNDLES);
        DeviceInventoryBundle inventoryBundle = inventoryBundles.stream().filter(b -> b.getName().equals(inventoryBundleNamed)).findAny().orElse(null);
        Assert.assertNotNull(inventoryBundle);
        Assert.assertEquals(inventoryBundleId, inventoryBundle.getId());
    }

    @Then("Inventory Bundles has Bundle named {string} has version {string}")
    public void inventoryBundleNamedHasVersion(String inventoryBundleNamed, String inventoryBundleVersion) {
        List<DeviceInventoryBundle> inventoryBundles = (List<DeviceInventoryBundle>) stepData.get(INVENTORY_BUNDLES);
        DeviceInventoryBundle inventoryBundle = inventoryBundles.stream().filter(b -> b.getName().equals(inventoryBundleNamed)).findAny().orElse(null);
        Assert.assertNotNull(inventoryBundle);
        Assert.assertEquals(inventoryBundleVersion, inventoryBundle.getVersion());
    }

    @Then("Inventory Bundles has Bundle named {string} has status {string}")
    public void inventoryBundleNamedHasStatus(String inventoryBundleNamed, String inventoryBundleStatus) {
        List<DeviceInventoryBundle> inventoryBundles = (List<DeviceInventoryBundle>) stepData.get(INVENTORY_BUNDLES);
        DeviceInventoryBundle inventoryBundle = inventoryBundles.stream().filter(b -> b.getName().equals(inventoryBundleNamed)).findAny().orElse(null);
        Assert.assertNotNull(inventoryBundle);
        Assert.assertEquals(inventoryBundleStatus, inventoryBundle.getStatus());
    }

    @Then("Inventory Bundles has Bundle named {string} is signed {string}")
    public void inventoryBundleNamedIsSigned(String inventoryBundleNamed, String inventoryBundleSigned) {
        List<DeviceInventoryBundle> inventoryBundles = (List<DeviceInventoryBundle>) stepData.get(INVENTORY_BUNDLES);
        DeviceInventoryBundle inventoryBundle = inventoryBundles.stream().filter(b -> b.getName().equals(inventoryBundleNamed)).findAny().orElse(null);
        Assert.assertNotNull(inventoryBundle);
        Assert.assertEquals(Boolean.valueOf(inventoryBundleSigned), inventoryBundle.getSigned());
    }

    //
    // /containers
    @When("Inventory Containers are requested")
    public void inventoryContainersRequested() throws Exception {
        List<KuraDevice> kuraDevices = (List<KuraDevice>) stepData.get(KURA_DEVICES);
        for (KuraDevice kuraDevice : kuraDevices) {
            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
            if (device != null) {
                DeviceInventoryContainers deviceInventoryContainers = deviceInventoryManagementService.getContainers(device.getScopeId(), device.getId(), null);
                List<DeviceInventoryContainer> inventoryContainers = deviceInventoryContainers.getInventoryContainers();
                stepData.put(INVENTORY_CONTAINERS, inventoryContainers);
            }
        }
    }

    @Then("Inventory Containers are received")
    public void inventoryContainersReceived() {
        List<DeviceInventoryContainer> inventoryContainers = (List<DeviceInventoryContainer>) stepData.get(INVENTORY_CONTAINERS);
        Assert.assertNotNull(inventoryContainers);
    }

    @Then("Inventory Containers are {long}")
    public void inventoryContainersAreSize(long size) {
        List<DeviceInventoryContainer> inventoryContainers = (List<DeviceInventoryContainer>) stepData.get(INVENTORY_CONTAINERS);
        Assert.assertEquals(size, inventoryContainers.size());
    }

    @Then("Inventory Containers has Container named {string} is present")
    public void inventoryContainerNamedIsPresent(String inventoryContainerName) {
        List<DeviceInventoryContainer> inventoryContainers = (List<DeviceInventoryContainer>) stepData.get(INVENTORY_CONTAINERS);
        DeviceInventoryContainer inventoryContainer = inventoryContainers.stream().filter(c -> c.getName().equals(inventoryContainerName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryContainer);
    }

    @Then("Inventory Containers has Container named {string} has version {string}")
    public void inventoryContainerNamedHasVersion(String inventoryContainerName, String inventoryContainerVersion) {
        List<DeviceInventoryContainer> inventoryContainers = (List<DeviceInventoryContainer>) stepData.get(INVENTORY_CONTAINERS);
        DeviceInventoryContainer inventoryContainer = inventoryContainers.stream().filter(c -> c.getName().equals(inventoryContainerName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryContainer);
        Assert.assertEquals(inventoryContainerVersion, inventoryContainer.getVersion());
    }

    @Then("Inventory Containers has Container named {string} has type {string}")
    public void inventoryContainerNamedHasType(String inventoryContainerName, String inventoryContainerType) {
        List<DeviceInventoryContainer> inventoryContainers = (List<DeviceInventoryContainer>) stepData.get(INVENTORY_CONTAINERS);
        DeviceInventoryContainer inventoryContainer = inventoryContainers.stream().filter(c -> c.getName().equals(inventoryContainerName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryContainer);
        Assert.assertEquals(inventoryContainerType, inventoryContainer.getType());
    }

    //
    // /systemPackages
    @When("Inventory System Packages are requested")
    public void inventorySystemPackagesRequested() throws Exception {
        List<KuraDevice> kuraDevices = (List<KuraDevice>) stepData.get(KURA_DEVICES);
        for (KuraDevice kuraDevice : kuraDevices) {
            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
            if (device != null) {
                DeviceInventorySystemPackages deviceSystemPackages = deviceInventoryManagementService.getSystemPackages(device.getScopeId(), device.getId(), null);
                List<DeviceInventorySystemPackage> systemPackages = deviceSystemPackages.getSystemPackages();
                stepData.put(INVENTORY_SYSTEM_PACKAGES, systemPackages);
            }
        }
    }

    @Then("Inventory System Packages are received")
    public void inventorySystemPackagesReceived() {
        List<DeviceInventorySystemPackage> inventorySystemPackages = (List<DeviceInventorySystemPackage>) stepData.get(INVENTORY_SYSTEM_PACKAGES);
        Assert.assertNotNull(inventorySystemPackages);
    }

    @Then("Inventory System Packages are {long}")
    public void inventorySystemPackagesAreSize(long size) {
        List<DeviceInventorySystemPackage> inventorySystemPackages = (List<DeviceInventorySystemPackage>) stepData.get(INVENTORY_SYSTEM_PACKAGES);
        Assert.assertEquals(size, inventorySystemPackages.size());
    }

    @Then("Inventory System Packages has System Package named {string} is present")
    public void inventorySystemPackageNamedIsPresent(String inventorySystemPackageName) {
        List<DeviceInventorySystemPackage> inventorySystemPackages = (List<DeviceInventorySystemPackage>) stepData.get(INVENTORY_SYSTEM_PACKAGES);
        DeviceInventorySystemPackage inventorySystemPackage = inventorySystemPackages.stream().filter(k -> k.getName().equals(inventorySystemPackageName)).findAny().orElse(null);
        Assert.assertNotNull(inventorySystemPackage);
    }

    @Then("Inventory System Packages has System Package named {string} has version {string}")
    public void inventorySystemPackageHasVersion(String inventorySystemPackageName, String inventorySystemPackageVersion) {
        List<DeviceInventorySystemPackage> inventorySystemPackages = (List<DeviceInventorySystemPackage>) stepData.get(INVENTORY_SYSTEM_PACKAGES);
        DeviceInventorySystemPackage inventorySystemPackage = inventorySystemPackages.stream().filter(p -> p.getName().equals(inventorySystemPackageName)).findAny().orElse(null);
        Assert.assertNotNull(inventorySystemPackage);
        Assert.assertEquals(inventorySystemPackageVersion, inventorySystemPackage.getVersion());
    }

    @Then("Inventory System Packages has System Package named {string} has type {string}")
    public void inventorySystemPackageHasType(String inventorySystemPackageName, String inventorySystemPackageType) {
        List<DeviceInventorySystemPackage> inventorySystemPackages = (List<DeviceInventorySystemPackage>) stepData.get(INVENTORY_SYSTEM_PACKAGES);
        DeviceInventorySystemPackage inventorySystemPackage = inventorySystemPackages.stream().filter(p -> p.getName().equals(inventorySystemPackageName)).findAny().orElse(null);
        Assert.assertNotNull(inventorySystemPackage);
        Assert.assertEquals(inventorySystemPackageType, inventorySystemPackage.getPackageType());
    }

    //
    // /deploymentPackages
    @When("Inventory Deployment Packages are requested")
    public void inventoryDeploymentPackagesRequested() throws Exception {
        List<KuraDevice> kuraDevices = (List<KuraDevice>) stepData.get(KURA_DEVICES);
        for (KuraDevice kuraDevice : kuraDevices) {
            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
            if (device != null) {
                DeviceInventoryPackages deviceDeploymentPackages = deviceInventoryManagementService.getDeploymentPackages(device.getScopeId(), device.getId(), null);
                List<DeviceInventoryPackage> inventoryPackages = deviceDeploymentPackages.getPackages();
                stepData.put(INVENTORY_DEPLOYMENT_PACKAGES, inventoryPackages);
            }
        }
    }

    @Then("Inventory Deployment Packages are received")
    public void inventoryDeploymentPackagesReceived() {
        List<DeviceInventoryPackage> inventoryPackages = (List<DeviceInventoryPackage>) stepData.get(INVENTORY_DEPLOYMENT_PACKAGES);
        Assert.assertNotNull(inventoryPackages);
    }

    @Then("Inventory Deployment Packages are {long}")
    public void inventoryDeploymentPackagesAreSize(long size) {
        List<DeviceInventoryPackage> inventoryPackages = (List<DeviceInventoryPackage>) stepData.get(INVENTORY_DEPLOYMENT_PACKAGES);
        Assert.assertEquals(size, inventoryPackages.size());
    }

    @Then("Inventory Deployment Packages has Deployment Package named {string} is present")
    public void inventoryDeploymentPackageNamedIsPresent(String inventoryDeploymentPackageName) {
        List<DeviceInventoryPackage> inventoryPackages = (List<DeviceInventoryPackage>) stepData.get(INVENTORY_DEPLOYMENT_PACKAGES);
        DeviceInventoryPackage inventoryPackage = inventoryPackages.stream().filter(d -> d.getName().equals(inventoryDeploymentPackageName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryPackage);
    }

    @Then("Inventory Deployment Packages has Deployment Package named {string} has version {string}")
    public void inventoryDeploymentPackageHasVersion(String inventoryDeploymentPackageName, String inventoryDeploymentPackageVersion) {
        List<DeviceInventoryPackage> inventoryPackages = (List<DeviceInventoryPackage>) stepData.get(INVENTORY_DEPLOYMENT_PACKAGES);
        DeviceInventoryPackage inventoryPackage = inventoryPackages.stream().filter(d -> d.getName().equals(inventoryDeploymentPackageName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryPackage);
        Assert.assertEquals(inventoryDeploymentPackageVersion, inventoryPackage.getVersion());
    }

    @Then("Inventory Deployment Packages has Deployment Package named {string} has {int} bundles")
    public void inventoryDeploymentPackageHasBundles(String inventoryDeploymentPackageName, int inventoryDeploymentPackageBundlesSize) {
        List<DeviceInventoryPackage> inventoryPackages = (List<DeviceInventoryPackage>) stepData.get(INVENTORY_DEPLOYMENT_PACKAGES);
        DeviceInventoryPackage inventoryPackage = inventoryPackages.stream().filter(d -> d.getName().equals(inventoryDeploymentPackageName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryPackage);
        Assert.assertNotNull(inventoryPackage.getPackageBundles());
        Assert.assertEquals(inventoryDeploymentPackageBundlesSize, inventoryPackage.getPackageBundles().size());
    }

    @Then("Inventory Deployment Packages has Deployment Package named {string} has bundle named {string}")
    public void inventoryDeploymentPackageHasBundleNamed(String inventoryDeploymentPackageName, String inventoryDeploymentPackageBundleName) {
        List<DeviceInventoryPackage> inventoryPackages = (List<DeviceInventoryPackage>) stepData.get(INVENTORY_DEPLOYMENT_PACKAGES);
        DeviceInventoryPackage inventoryPackage = inventoryPackages.stream().filter(d -> d.getName().equals(inventoryDeploymentPackageName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryPackage);
        Assert.assertNotNull(inventoryPackage.getPackageBundles());

        DeviceInventoryBundle inventoryBundle = inventoryPackage.getPackageBundles().stream().filter(b -> b.getName().equals(inventoryDeploymentPackageBundleName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryBundle);
    }

    @Then("Inventory Deployment Packages has Deployment Package named {string} has bundle named {string} with id {string}")
    public void inventoryDeploymentPackageHasBundleNamedWithId(String inventoryDeploymentPackageName, String inventoryDeploymentPackageBundleName, String inventoryDeploymentPackageBundleId) {
        List<DeviceInventoryPackage> inventoryPackages = (List<DeviceInventoryPackage>) stepData.get(INVENTORY_DEPLOYMENT_PACKAGES);
        DeviceInventoryPackage inventoryPackage = inventoryPackages.stream().filter(d -> d.getName().equals(inventoryDeploymentPackageName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryPackage);
        Assert.assertNotNull(inventoryPackage.getPackageBundles());

        DeviceInventoryBundle inventoryBundle = inventoryPackage.getPackageBundles().stream().filter(b -> b.getName().equals(inventoryDeploymentPackageBundleName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryBundle);
        Assert.assertEquals(inventoryDeploymentPackageBundleId, inventoryBundle.getId());
    }

    @Then("Inventory Deployment Packages has Deployment Package named {string} has bundle named {string} with version {string}")
    public void inventoryDeploymentPackageHasBundleNamedWithVersion(String inventoryDeploymentPackageName, String inventoryDeploymentPackageBundleName, String inventoryDeploymentPackageBundleVersion) {
        List<DeviceInventoryPackage> inventoryPackages = (List<DeviceInventoryPackage>) stepData.get(INVENTORY_DEPLOYMENT_PACKAGES);
        DeviceInventoryPackage inventoryPackage = inventoryPackages.stream().filter(d -> d.getName().equals(inventoryDeploymentPackageName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryPackage);
        Assert.assertNotNull(inventoryPackage.getPackageBundles());

        DeviceInventoryBundle inventoryBundle = inventoryPackage.getPackageBundles().stream().filter(b -> b.getName().equals(inventoryDeploymentPackageBundleName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryBundle);
        Assert.assertEquals(inventoryDeploymentPackageBundleVersion, inventoryBundle.getVersion());
    }

    @Then("Inventory Deployment Packages has Deployment Package named {string} has bundle named {string} with state {string}")
    public void inventoryDeploymentPackageHasBundleNamedWithState(String inventoryDeploymentPackageName, String inventoryDeploymentPackageBundleName, String inventoryDeploymentPackageBundleState) {
        List<DeviceInventoryPackage> inventoryPackages = (List<DeviceInventoryPackage>) stepData.get(INVENTORY_DEPLOYMENT_PACKAGES);
        DeviceInventoryPackage inventoryPackage = inventoryPackages.stream().filter(d -> d.getName().equals(inventoryDeploymentPackageName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryPackage);
        Assert.assertNotNull(inventoryPackage.getPackageBundles());

        DeviceInventoryBundle inventoryBundle = inventoryPackage.getPackageBundles().stream().filter(b -> b.getName().equals(inventoryDeploymentPackageBundleName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryBundle);
        Assert.assertEquals(inventoryDeploymentPackageBundleState, inventoryBundle.getStatus());
    }

    @Then("Inventory Deployment Packages has Deployment Package named {string} has bundle named {string} with signed {string}")
    public void inventoryDeploymentPackageHasBundleNamedWithSignature(String inventoryDeploymentPackageName, String inventoryDeploymentPackageBundleName, String inventoryDeploymentPackageBundleSigned) {
        List<DeviceInventoryPackage> inventoryPackages = (List<DeviceInventoryPackage>) stepData.get(INVENTORY_DEPLOYMENT_PACKAGES);
        DeviceInventoryPackage inventoryPackage = inventoryPackages.stream().filter(d -> d.getName().equals(inventoryDeploymentPackageName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryPackage);
        Assert.assertNotNull(inventoryPackage.getPackageBundles());

        DeviceInventoryBundle inventoryBundle = inventoryPackage.getPackageBundles().stream().filter(b -> b.getName().equals(inventoryDeploymentPackageBundleName)).findAny().orElse(null);
        Assert.assertNotNull(inventoryBundle);
        Assert.assertEquals(Boolean.valueOf(inventoryDeploymentPackageBundleSigned), inventoryBundle.getSigned());
    }

//
//    @When("All Keystore Items are requested")
//    public void requestKeystoreItems() throws Exception {
//        List<KuraDevice> kuraDevices = (List<KuraDevice>) stepData.get(KURA_DEVICES);
//        for (KuraDevice kuraDevice : kuraDevices) {
//            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
//            if (device != null) {
//                DeviceKeystoreItems keystoreItems = deviceInventoryManagementService.getKeystoreItems(device.getScopeId(), device.getId(), null);
//                List<DeviceKeystoreItem> keystoreItemList = keystoreItems.getKeystoreItems();
//                stepData.put(KEYSTORES_ITEMS, keystoreItemList);
//            }
//        }
//    }
//
//    @When("Keystore Items with alias {string} are requested")
//    public void requestKeystoreItemsByAlias(String alias) throws Exception {
//        List<KuraDevice> kuraDevices = (List<KuraDevice>) stepData.get(KURA_DEVICES);
//        for (KuraDevice kuraDevice : kuraDevices) {
//            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
//            if (device != null) {
//                DeviceKeystoreItemQuery query = deviceInventoryManagementFactory.newDeviceKeystoreItemQuery();
//                query.setAlias(alias);
//                DeviceKeystoreItems keystoreItems = deviceInventoryManagementService.getKeystoreItems(device.getScopeId(), device.getId(), query, null);
//                List<DeviceKeystoreItem> keystoreItemList = keystoreItems.getKeystoreItems();
//                stepData.put(KEYSTORES_ITEMS, keystoreItemList);
//            }
//        }
//    }
//
//    @When("Keystore Items with keystore id {string} are requested")
//    public void requestKeystoreItemsByKeystoreId(String keystoreId) throws Exception {
//        List<KuraDevice> kuraDevices = (List<KuraDevice>) stepData.get(KURA_DEVICES);
//        for (KuraDevice kuraDevice : kuraDevices) {
//            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
//            if (device != null) {
//                DeviceKeystoreItemQuery query = deviceInventoryManagementFactory.newDeviceKeystoreItemQuery();
//                query.setKeystoreId(keystoreId);
//                DeviceKeystoreItems keystoreItems = deviceInventoryManagementService.getKeystoreItems(device.getScopeId(), device.getId(), query, null);
//                List<DeviceKeystoreItem> keystoreItemList = keystoreItems.getKeystoreItems();
//                stepData.put(KEYSTORES_ITEMS, keystoreItemList);
//            }
//        }
//    }
//
//    @Then("Keystore Items are received")
//    public void keystoreItemsReceived() {
//        List<DeviceKeystoreItem> deviceKeystoreItems = (List<DeviceKeystoreItem>) stepData.get(KEYSTORES_ITEMS);
//        Assert.assertNotNull(deviceKeystoreItems);
//    }
//
//    @Then("Keystore Items are {long}")
//    public void keystoreItemsAreSize(long size) {
//        List<DeviceKeystoreItem> deviceKeystoreItems = (List<DeviceKeystoreItem>) stepData.get(KEYSTORES_ITEMS);
//        Assert.assertEquals(size, deviceKeystoreItems.size());
//    }
//
//    @Then("Keystore Items has Item with alias {string} is present")
//    public void keystoreItemsWithAliasIsPresent(String keystoreItemAlias) {
//        List<DeviceKeystoreItem> deviceKeystoreItems = (List<DeviceKeystoreItem>) stepData.get(KEYSTORES_ITEMS);
//        DeviceKeystoreItem keystoreItem = deviceKeystoreItems.stream().filter(k -> k.getAlias().equals(keystoreItemAlias)).findAny().orElse(null);
//        Assert.assertNotNull(keystoreItem);
//    }
//
//    @Then("Keystore Items has Item with alias {string} is not present")
//    public void keystoreItemsWithAliasIsNotPresent(String keystoreItemAlias) {
//        List<DeviceKeystoreItem> deviceKeystoreItems = (List<DeviceKeystoreItem>) stepData.get(KEYSTORES_ITEMS);
//        DeviceKeystoreItem keystoreItem = deviceKeystoreItems.stream().filter(k -> k.getAlias().equals(keystoreItemAlias)).findAny().orElse(null);
//        Assert.assertNull(keystoreItem);
//    }
//
//    @Then("Keystore Items has Item with alias {string} has type {string}")
//    public void keystoreItemsWithAliasHasType(String keystoreName, String keystoreType) {
//        List<DeviceKeystoreItem> keystoreItems = (List<DeviceKeystoreItem>) stepData.get(KEYSTORES_ITEMS);
//        DeviceKeystoreItem keystoreItem = keystoreItems.stream().filter(k -> k.getAlias().equals(keystoreName)).findAny().orElse(null);
//        Assert.assertNotNull(keystoreItem);
//        Assert.assertEquals(keystoreType, keystoreItem.getItemType());
//    }
//
//    @When("Keystore Item with keystore id {string} and alias {string} is requested")
//    public void requestKeystoreItemRequested(String keystoreId, String alias) throws Exception {
//        List<KuraDevice> kuraDevices = (List<KuraDevice>) stepData.get(KURA_DEVICES);
//        for (KuraDevice kuraDevice : kuraDevices) {
//            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
//            if (device != null) {
//                DeviceKeystoreItem keystoreItem = deviceInventoryManagementService.getKeystoreItem(device.getScopeId(), device.getId(), keystoreId, alias, null);
//                stepData.put(KEYSTORES_ITEM, keystoreItem);
//            }
//        }
//    }
//
//    @Then("Keystore Item is received")
//    public void keystoreItemIsReceived() {
//        DeviceKeystoreItem deviceKeystoreItem = (DeviceKeystoreItem) stepData.get(KEYSTORES_ITEM);
//        Assert.assertNotNull(deviceKeystoreItem);
//    }
//
//    @Then("Keystore Item matches expected")
//    public void keystoreItemMatchesExpected() {
//        DeviceKeystoreItem deviceKeystoreItem = (DeviceKeystoreItem) stepData.get(KEYSTORES_ITEM);
//        Assert.assertNotNull(deviceKeystoreItem);
//        Assert.assertEquals("HttpsKeystore", deviceKeystoreItem.getKeystoreId());
//        Assert.assertEquals("localhost", deviceKeystoreItem.getAlias());
//        Assert.assertEquals(new Integer(2048), deviceKeystoreItem.getSize());
//        Assert.assertEquals("PRIVATE_KEY", deviceKeystoreItem.getItemType());
//        Assert.assertNotNull(deviceKeystoreItem.getCertificateChain());
//    }
//
//    @When("I install a Keystore Certificate with alias {string}")
//    public void installKeystoreCertificateWithAlias(String alias) throws Exception {
//        List<KuraDevice> kuraDevices = (List<KuraDevice>) stepData.get(KURA_DEVICES);
//        for (KuraDevice kuraDevice : kuraDevices) {
//            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
//            if (device != null) {
//                DeviceKeystoreCertificate deviceKeystoreCertificate = deviceInventoryManagementFactory.newDeviceKeystoreCertificate();
//                deviceKeystoreCertificate.setAlias(alias);
//                deviceKeystoreCertificate.setKeystoreId("SSLKeystore");
//                deviceKeystoreCertificate.setCertificate("-----BEGIN CERTIFICATE-----\n" +
//                        "MIIFVzCCBD+gAwIBAgISA38CzQctm3+HkSyZPnDL8TFsMA0GCSqGSIb3DQEBCwUA\n" +
//                        "MEoxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1MZXQncyBFbmNyeXB0MSMwIQYDVQQD\n" +
//                        "ExpMZXQncyBFbmNyeXB0IEF1dGhvcml0eSBYMzAeFw0xOTA3MTkxMDIxMTdaFw0x\n" +
//                        "OTEwMTcxMDIxMTdaMBsxGTAXBgNVBAMTEG1xdHQuZWNsaXBzZS5vcmcwggEiMA0G\n" +
//                        "CSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDQnt6ZBEZ/vDG0JLqVB45lO6xlLazt\n" +
//                        "YpEqZlGBket6PtjUGLdE2XivTpjtUkERS1cvPBqT1DH/yEZ1CU7iT/gfZtZotR0c\n" +
//                        "qEMogSGkmrN1sAV6Eb+xGT3sPm1WFeKZqKdzAScdULoweUgwbNXa9kAB1uaSYBTe\n" +
//                        "cq2ynfxBKWL/7bVtoeXUOyyaiIxVPTYz5XgpjSUB+9ML/v/+084XhIKA/avGPOSi\n" +
//                        "RHOB+BsqTGyGhDgAHF+CDrRt8U1preS9AKXUvZ0aQL+djV8Y5nXPQPR8c2wplMwL\n" +
//                        "5W/YMrM/dBm64vclKQLVPyEPqMOLMqcf+LkfQi6WOH+JByJfywAlme6jAgMBAAGj\n" +
//                        "ggJkMIICYDAOBgNVHQ8BAf8EBAMCBaAwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsG\n" +
//                        "AQUFBwMCMAwGA1UdEwEB/wQCMAAwHQYDVR0OBBYEFHc+PmokFlx8Fh/0Lob125ef\n" +
//                        "fLNyMB8GA1UdIwQYMBaAFKhKamMEfd265tE5t6ZFZe/zqOyhMG8GCCsGAQUFBwEB\n" +
//                        "BGMwYTAuBggrBgEFBQcwAYYiaHR0cDovL29jc3AuaW50LXgzLmxldHNlbmNyeXB0\n" +
//                        "Lm9yZzAvBggrBgEFBQcwAoYjaHR0cDovL2NlcnQuaW50LXgzLmxldHNlbmNyeXB0\n" +
//                        "Lm9yZy8wGwYDVR0RBBQwEoIQbXF0dC5lY2xpcHNlLm9yZzBMBgNVHSAERTBDMAgG\n" +
//                        "BmeBDAECATA3BgsrBgEEAYLfEwEBATAoMCYGCCsGAQUFBwIBFhpodHRwOi8vY3Bz\n" +
//                        "LmxldHNlbmNyeXB0Lm9yZzCCAQMGCisGAQQB1nkCBAIEgfQEgfEA7wB2AHR+2oMx\n" +
//                        "rTMQkSGcziVPQnDCv/1eQiAIxjc1eeYQe8xWAAABbAn2/p8AAAQDAEcwRQIhAIBl\n" +
//                        "IZC2ZCMDs7bkBQN79xNO84VFpe7bQcMeaqHsQH9jAiAYV5kdZBgl17M5RB44NQ+y\n" +
//                        "Y/WOF1PWOrNrP3XdeEo7HAB1ACk8UZZUyDlluqpQ/FgH1Ldvv1h6KXLcpMMM9OVF\n" +
//                        "R/R4AAABbAn2/o4AAAQDAEYwRAIgNYxfY0bjRfjhXjjAgyPRSLKq4O5tWTd2W4mn\n" +
//                        "CpE3aCYCIGeKPyuuo9tvHbyVKF4bsoN76FmnOkdsYE0MCKeKkUOkMA0GCSqGSIb3\n" +
//                        "DQEBCwUAA4IBAQCB0ykl1N2U2BMhzFo6dwrECBSFO+ePV2UYGrb+nFunWE4MMKBb\n" +
//                        "dyu7dj3cYRAFCM9A3y0H967IcY+h0u9FgZibmNs+y/959wcbr8F1kvgpVKDb1FGs\n" +
//                        "cuEArADQd3X+4TMM+IeIlqbGVXv3mYPrsP78LmUXkS7ufhMXsD5GSbSc2Zp4/v0o\n" +
//                        "3bsJz6qwzixhqg30tf6siOs9yrpHpPnDnbRrahbwnYTpm6JP0lK53GeFec4ckNi3\n" +
//                        "zT5+hEVOZ4JYPb3xVXkzIjSWmnDVbwC9MFtRaER9MhugKmiAp8SRLbylD0GKOhSB\n" +
//                        "2BDf6JrzhIddKxQ75KgMZE6FQaC3Bz1DFyrj\n" +
//                        "-----END CERTIFICATE-----");
//                deviceInventoryManagementService.createKeystoreCertificate(device.getScopeId(), device.getId(), deviceKeystoreCertificate, null);
//            }
//        }
//    }
//
//    @When("I install a Keystore Keypair with alias {string}")
//    public void installKeystoreKeypairWithAlias(String alias) throws Exception {
//        for (KuraDevice kuraDevice : (List<KuraDevice>) stepData.get(KURA_DEVICES)) {
//            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
//            if (device != null) {
//                DeviceKeystoreKeypair deviceKeystoreKeypair = deviceInventoryManagementFactory.newDeviceKeystoreKeypair();
//                deviceKeystoreKeypair.setAlias(alias);
//                deviceKeystoreKeypair.setKeystoreId("SSLKeystore");
//                deviceKeystoreKeypair.setAlgorithm("RSA");
//                deviceKeystoreKeypair.setSize(4096);
//                deviceKeystoreKeypair.setSignatureAlgorithm("SHA256withRSA");
//                deviceKeystoreKeypair.setAttributes("CN=Let's Encrypt Authority X3,O=Let's Encrypt,C=US");
//                deviceInventoryManagementService.createKeystoreKeypair(device.getScopeId(), device.getId(), deviceKeystoreKeypair, null);
//                stepData.put(DEVICE_KEYSTORE_KEYPAIR, deviceKeystoreKeypair);
//            }
//        }
//    }
//
//    @Then("Keystore Items has Item with alias {string} that matches the installed certificate")
//    public void keystoreItemsWithAliasMatchesCertificate(String keystoreItemAlias) {
//        List<DeviceKeystoreItem> deviceKeystoreItems = (List<DeviceKeystoreItem>) stepData.get(KEYSTORES_ITEMS);
//        DeviceKeystoreItem keystoreItem = deviceKeystoreItems.stream().filter(k -> k.getAlias().equals(keystoreItemAlias)).findAny().orElse(null);
//        Assert.assertNotNull(keystoreItem);
//        Assert.assertEquals("SSLKeystore", keystoreItem.getKeystoreId());
//        Assert.assertEquals("CN=mqtt.eclipse.org", keystoreItem.getSubjectDN());
//        Assert.assertEquals(1, keystoreItem.getSubjectAN().size());
//        Assert.assertEquals("CN=Let's Encrypt Authority X3,O=Let's Encrypt,C=US", keystoreItem.getIssuer());
//        Assert.assertNotNull(keystoreItem.getNotBefore());
//        Assert.assertEquals(1563531677000L, keystoreItem.getNotBefore().getTime());
//        Assert.assertNotNull(keystoreItem.getNotAfter());
//        Assert.assertEquals(1571307677000L, keystoreItem.getNotAfter().getTime());
//        Assert.assertEquals("SHA256withRSA", keystoreItem.getAlgorithm());
//        Assert.assertEquals(new Integer(2048), keystoreItem.getSize());
//        Assert.assertEquals("TRUSTED_CERTIFICATE", keystoreItem.getItemType());
//    }
//
//    @Then("Keystore Items has Item with alias {string} that matches the installed keypair")
//    public void keystoreItemsWithAliasMatchesKeypair(String keystoreItemAlias) {
//        List<DeviceKeystoreItem> deviceKeystoreItems = (List<DeviceKeystoreItem>) stepData.get(KEYSTORES_ITEMS);
//        DeviceKeystoreItem keystoreItem = deviceKeystoreItems.stream().filter(k -> k.getAlias().equals(keystoreItemAlias)).findAny().orElse(null);
//        Assert.assertNotNull(keystoreItem);
//        DeviceKeystoreKeypair keystoreKeypair = (DeviceKeystoreKeypair) stepData.get(DEVICE_KEYSTORE_KEYPAIR);
//        Assert.assertNotNull(keystoreKeypair);
//        Assert.assertEquals(keystoreKeypair.getKeystoreId(), keystoreItem.getKeystoreId());
//        Assert.assertEquals(keystoreKeypair.getAlgorithm(), keystoreItem.getAlgorithm());
//        Assert.assertEquals(keystoreKeypair.getSize(), keystoreItem.getSize());
//        Assert.assertEquals("PRIVATE_KEY", keystoreItem.getItemType());
//    }
//
//    @When("I delete a Keystore Item from keystore {string} with alias {string}")
//    public void deleteKeystoreItemWithAlias(String keystoreId, String alias) throws Exception {
//        List<KuraDevice> kuraDevices = (List<KuraDevice>) stepData.get(KURA_DEVICES);
//        for (KuraDevice kuraDevice : kuraDevices) {
//            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
//            if (device != null) {
//                deviceInventoryManagementService.deleteKeystoreItem(device.getScopeId(), device.getId(), keystoreId, alias, null);
//            }
//        }
//    }
//
//    @When("I send a Certificate Signing Request for Keystore Item with keystore {string} and alias {string}")
//    public void sendCertificateSigningRequestFor(String keystoreId, String alias) throws Exception {
//        List<KuraDevice> kuraDevices = (List<KuraDevice>) stepData.get(KURA_DEVICES);
//        for (KuraDevice kuraDevice : kuraDevices) {
//            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
//            if (device != null) {
//                DeviceKeystoreCSRInfo deviceKeystoreCSRInfo = deviceInventoryManagementFactory.newDeviceKeystoreCSRInfo();
//                deviceKeystoreCSRInfo.setKeystoreId(keystoreId);
//                deviceKeystoreCSRInfo.setAlias(alias);
//                deviceKeystoreCSRInfo.setSignatureAlgorithm("SHA256withRSA");
//                deviceKeystoreCSRInfo.setAttributes("CN=Kura, OU=IoT, O=Eclipse, C=US");
//                DeviceKeystoreCSR deviceKeystoreCSR = deviceInventoryManagementService.createKeystoreCSR(device.getScopeId(), device.getId(), deviceKeystoreCSRInfo, null);
//                stepData.put(DEVICE_KEYSTORE_CSR, deviceKeystoreCSR);
//            }
//        }
//    }
//
//    @Then("The Certificate Signing Request is received")
//    public void keystoreCertificateSigningRequestIsReceived() {
//        DeviceKeystoreCSR deviceKeystoreCSR = (DeviceKeystoreCSR) stepData.get(DEVICE_KEYSTORE_CSR);
//        Assert.assertNotNull(deviceKeystoreCSR);
//    }
//
//    @Then("The Certificate Signing Request matches expected")
//    public void keystoreCertificateSigningRequestMatchesExpected() {
//        DeviceKeystoreCSR deviceKeystoreCSR = (DeviceKeystoreCSR) stepData.get(DEVICE_KEYSTORE_CSR);
//        Assert.assertNotNull(deviceKeystoreCSR);
//        Assert.assertEquals("-----BEGIN CERTIFICATE REQUEST-----\n" +
//                "MIICgTCCAWkCAQAwPDELMAkGA1UEBhMCVVMxEDAOBgNVBAoTB0VjbGlwc2UxDDAK\n" +
//                "BgNVBAsTA0lvVDENMAsGA1UEAxMES3VyYTCCASIwDQYJKoZIhvcNAQEBBQADggEP\n" +
//                "ADCCAQoCggEBAKpmnJeOJ7wczIMj3nUe+qxAtfJaXhUJkGy+bQuEfSEKRhA9QXAT\n" +
//                "bt6N5alSj9mHb0OcOESBdUEr8vt28d5qHyHUUJ3yOJH3qURGO3He8yqLuUmgMgdK\n" +
//                "Dtp5bGFy5ltW/F+ASB8vJlX2jaC/Tybq8KjPTzVeEIilyQ9LDQMLmH7l+WklkpsK\n" +
//                "LZHF+2fATJK7HISijozZiVfk8EFi5JXbGo9VFlKouwTU3V2NVY9f4cIftPb5pNs2\n" +
//                "lEL+ZkAuaPksHzkI0z+bPwR4+tlMTxgcQE25r7fPK3FYEuOugSV8zGghI1dBDAHx\n" +
//                "eHYVpduJPhz7RtdVw3x7eM7I1C2IrmfHaP0CAwEAAaAAMA0GCSqGSIb3DQEBCwUA\n" +
//                "A4IBAQAC8rvMaHZ+7szRm490O0nOj2wC0yngvciyBvCqEiKGmlOjeXxJAVjTG+r6\n" +
//                "tXe6Jce9weIRdbI0HHVWkNVBX7Z0xjuD/SjrXOKjx1gm1DTbkp97OTBXuPhuiNXq\n" +
//                "Ihvy/j0P/yFRAUP+YRkV6N5OE76fUst/VHUvMWbEEnH9qPGYmSwV4yBgsSRiL4km\n" +
//                "84uuNDaILuCuYqTMtfoPSrfcILrKMfmPRvNE5DNDbk/BsR33zyBXCjnd+/P61sKo\n" +
//                "VSn6maFDBHcZP2jkBOBr8QmW8jt3oR9qWX5LXBpEHkmki8cy6FEhUOGZIuPAd8Rj\n" +
//                "PfZ8kKHpraMQuOeg0ZsZcZzlZsa8\n" +
//                "-----END CERTIFICATE REQUEST-----\n", deviceKeystoreCSR.getSigningRequest());
//    }
}

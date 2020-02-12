/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.qa.common.utils.EmbeddedBroker;
import org.eclipse.kapua.service.device.management.asset.internal.DeviceAssetsImpl;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetManagementService;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundle;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleManagementService;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;
import org.eclipse.kapua.service.device.management.command.DeviceCommandFactory;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandManagementService;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotManagementService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Assert;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Steps used in integration scenarios with running MQTT broker and process of
 * registering mocked Kura device registering with Kapua and issuing basic administrative
 * commands on Mocked Kura.
 */
@ScenarioScoped
public class BrokerSteps extends TestBase {

    /**
     * Embedded broker configuration file from classpath resources.
     */
    public static final String ACTIVEMQ_XML = "xbean:activemq.xml";

    /**
     * Device birth topic.
     */
    private String mqttBirth;

    /**
     * Device death topic.
     */
    private String mqttDc;

    /**
     * URI of mqtt broker.
     */
    private static final String BROKER_URI = "tcp://localhost:1883";

    /**
     * Access to device management service.
     */
    private DevicePackageManagementService devicePackageManagementService;

    /**
     * Device registry service for managing devices in DB.
     */
    private DeviceRegistryService deviceRegistryService;

    /**
     * Configuration service for kura devices.
     */
    private DeviceConfigurationManagementService deviceConfiguratiomManagementService;

    /**
     * Kura snapshot management service.
     */
    @SuppressWarnings("unused")
    private DeviceSnapshotManagementService deviceSnapshotManagementService;

    /**
     * Kura bundle management service.
     */
    private DeviceBundleManagementService deviceBundleManagementService;

    /**
     * Service for issuing commnads on Kura device.
     */
    private DeviceCommandManagementService deviceCommandManagementService;

    /**
     * Factory for creating commands sent to Kura.
     */
    private DeviceCommandFactory deviceCommandFactory;

    /**
     * Service for connecting devices.
     */
    private static DeviceConnectionService deviceConnectionService;
    private static DeviceAssetManagementService deviceAssetManagementService;

    /**
     * Client simulating Kura device
     */
    private KuraDevice kuraDevice;
    private ArrayList<KuraDevice> kuraDevices = kuraDevices = new ArrayList<>();

    /**
     * Scenario scoped step data.
     */
//    private StepData stepData;

    @Inject
    public BrokerSteps(/* dependency */ EmbeddedBroker broker, DBHelper database, StepData stepData) {

        this.stepData = stepData;
        this.database = database;
    }

    @Before
    public void beforeScenario(Scenario scenario) {

        this.scenario = scenario;

        BrokerSetting.resetInstance();

        KapuaLocator locator = KapuaLocator.getInstance();
        devicePackageManagementService = locator.getService(DevicePackageManagementService.class);
        deviceRegistryService = locator.getService(DeviceRegistryService.class);
        deviceConfiguratiomManagementService = locator.getService(DeviceConfigurationManagementService.class);
        deviceSnapshotManagementService = locator.getService(DeviceSnapshotManagementService.class);
        deviceBundleManagementService = locator.getService(DeviceBundleManagementService.class);
        deviceCommandManagementService = locator.getService(DeviceCommandManagementService.class);
        deviceCommandFactory = locator.getFactory(DeviceCommandFactory.class);
        deviceConnectionService = locator.getService(DeviceConnectionService.class);
        deviceAssetManagementService = locator.getService(DeviceAssetManagementService.class);

        JAXBContextProvider consoleProvider = new TestJAXBContextProvider();
        XmlUtil.setContextProvider(consoleProvider);
    }

    @After
    public void afterScenario() throws Exception {

        if (kuraDevice != null) {
            this.kuraDevice.mqttClientDisconnect();
        }

        KapuaSecurityUtils.clearSession();

        this.stepData = null;
    }

    @When("^I start the Kura Mock$")
    public void startKuraMock() {

        if (!kuraDevices.isEmpty()) {
            kuraDevices.clear();
        }
        kuraDevice = new KuraDevice();
        kuraDevice.mqttClientConnect();
        kuraDevices.add(kuraDevice);

        stepData.put("KuraDevices", kuraDevices);
    }

    @When("^I restart the Kura Mock$")
    public void restartKuraMock() throws Exception {
        ArrayList<KuraDevice> kuraDevices = (ArrayList<KuraDevice>) stepData.get("KuraDevices");
        List<KuraDevice> restartedKuraDevices = new ArrayList<>();
        if (!kuraDevices.isEmpty()) {
            for (KuraDevice kuraDevice : kuraDevices) {
                kuraDevice.mqttClientSetup();
                kuraDevice.mqttClientConnect();
                restartedKuraDevices.add(kuraDevice);
            }
        }
        stepData.put("KuraDevices", restartedKuraDevices);
    }


    @When("I get the KuraMock device(?:|s)$")
    public void getKuraMockDevice() throws Exception {
        ArrayList<Device> deviceList = new ArrayList<>();
        for (KuraDevice kuraDevice : kuraDevices) {
            Device device = deviceRegistryService.findByClientId(getCurrentScopeId(), kuraDevice.getClientId());
            if (device != null) {
                deviceList.add(device);
                stepData.put("LastDevice", device);
            }
        }
        stepData.put("DeviceList", deviceList);
    }

    @When("^KuraMock is disconnected$")
    public void kuraMockDisconnected() throws Exception {
        ArrayList<KuraDevice> kuraDevices = (ArrayList<KuraDevice>) stepData.get("KuraDevices");
        deviceDeathMessage();

        for (KuraDevice kuraDevice : kuraDevices) {
            kuraDevice.mqttClientDisconnect();
        }
    }

    @When("^Device birth message is sent$")
    public void deviceBirthMessage() throws Exception {
        ArrayList<KuraDevice> kuraDevices = (ArrayList<KuraDevice>) stepData.get("KuraDevices");

        for(KuraDevice kuraDevice : kuraDevices) {
            mqttBirth = "$EDC/kapua-sys/"+kuraDevice.getClientId()+"/MQTT/BIRTH";
            kuraDevice.sendMessageFromFile(mqttBirth, 0, false, "/mqtt/rpione3_MQTT_BIRTH.mqtt");

        }

    }

    @When("^^Device(?:|s) (?:is|are) connected$$")
    public void deviceConnected() throws Exception {
        try {
            deviceBirthMessage();
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^Device death message is sent$")
    public void deviceDeathMessage() throws Exception {
        ArrayList<KuraDevice> kuraDevices = (ArrayList<KuraDevice>) stepData.get("KuraDevices");

        for(KuraDevice kuraDevice : kuraDevices) {
            mqttDc = "$EDC/kapua-sys/"+kuraDevice.getClientId()+"/MQTT/DC";
            kuraDevice.sendMessageFromFile(mqttDc, 0, false, "/mqtt/rpione3_MQTT_DC.mqtt");
        }
    }

    @When("^Device is disconnected$")
    public void deviceDisconnected() throws Exception {

        deviceDeathMessage();
    }

    @When("^Packages are requested$")
    public void requestPackages() throws Exception {

        for(KuraDevice kuraDevice : kuraDevices) {
            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
            if (device != null) {
                DevicePackages deploymentPackages = devicePackageManagementService.getInstalled(device.getScopeId(),
                        device.getId(), null);
                List<DevicePackage> packages = deploymentPackages.getPackages();
                stepData.put("packages", packages);
            }
        }
    }

    @Then("^Packages are received$")
    public void packagesReceived() {

        @SuppressWarnings("unchecked")
        List<DevicePackage> packages = (List<DevicePackage>) stepData.get("packages");
        if (packages != null) {
            assertEquals(1, packages.size());
        }
    }

    @Then("^Number of received packages is (\\d+)$")
    public void checkNumberOfReceivedDevicePackages(long number) {
        @SuppressWarnings("unchecked")
        List<DevicePackage> receivedPackages = (List<DevicePackage>) stepData.get("packages");
        assertEquals(number, receivedPackages.size());
    }

    @Then("Package named (.*) with version (.*) is received$")
    public void assertPackage(final String packageName, final String version) {
        final DevicePackage pkg = findPackageByNameAndVersion(packageName, version);
        Assert.assertEquals(packageName, pkg.getName());
        Assert.assertEquals(version, pkg.getVersion());
    }

    private DevicePackage findPackageByNameAndVersion(final String packageSymbolicName, final String version) {
        List<DevicePackage> savedPackages = (List<DevicePackage>) stepData.get("packages");
        List<DevicePackage> packages = savedPackages.stream()
                .filter(bundle -> bundle.getName().equals(packageSymbolicName))
                .filter(bundle -> bundle.getVersion().equals(version))
                .collect(Collectors.toList());

        if (packages.isEmpty()) {
            Assert.fail(String.format("Package %s/%s is not present", packageSymbolicName, version));
        }
        if (packages.size() > 1) {
            Assert.fail(String.format("There is more than one entry for bundle %s/%s", packageSymbolicName, version));
        }

        return packages.get(0);
    }

    @When("^Bundles are requested$")
    public void requestBundles() throws Exception {
        ArrayList<KuraDevice> kuraDevices = (ArrayList<KuraDevice>) stepData.get("KuraDevices");

        for (KuraDevice kuraDevice : kuraDevices) {
            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
            Assert.assertNotNull(device);
            DeviceBundles deviceBundles = deviceBundleManagementService.get(device.getScopeId(), device.getId(), null);
            List<DeviceBundle> bundles = deviceBundles.getBundles();
            stepData.put("bundles", bundles);
        }
    }

    @Then("^Bundles are received$")
    public void bundlesReceived() {
        @SuppressWarnings("unchecked")
        List<DeviceBundle> bundles = (List<DeviceBundle>) stepData.get("bundles");
        assertEquals(137, bundles.size());
    }

    @When("A bundle named (.*) with id (.*) and version (.*) is present and (.*)$")
    public void bundleIsPresent(String bundleSymbolicName, long id ,String version, String state) {
        DeviceBundle bundle = findBundleByNameAndVersion(bundleSymbolicName, version);
        Assert.assertEquals(id, bundle.getId());
        Assert.assertEquals(state, bundle.getState());
    }

    private DeviceBundle findBundleByNameAndVersion(final String bundleSymbolicName, final String version) {
        List<DeviceBundle> savedBundles = (List<DeviceBundle>) stepData.get("bundles");
        List<DeviceBundle> bundles = savedBundles.stream()
                .filter(bundle -> bundle.getName().equals(bundleSymbolicName))
                .filter(bundle -> bundle.getVersion().equals(version))
                .collect(Collectors.toList());

        if (bundles.isEmpty()) {
            Assert.fail(String.format("Bundle %s/%s is not present", bundleSymbolicName, version));
        }
        if (bundles.size() > 1) {
            Assert.fail(String.format("There is more than one entry for bundle %s/%s", bundleSymbolicName, version));
        }

        return bundles.get(0);
    }

    private DeviceAsset findAssetByName(final String assetSymbolicName) {
        List<DeviceAsset> savedAssets = (List<DeviceAsset>) stepData.get("assets");

        List<DeviceAsset> assets = savedAssets.stream()
                .filter(asset -> asset.getName().equals(assetSymbolicName))
                .collect(Collectors.toList());

        if (assets.isEmpty()) {
            Assert.fail(String.format("Asset %s/%s is not present", assetSymbolicName));
        }
        if (assets.size() > 1) {
            Assert.fail(String.format("There is more than one entry for asset %s/%s", assetSymbolicName));
        }

        return assets.get(0);
    }

    @When("^Configuration is requested$")
    public void requestConfiguration() throws Exception {
        ArrayList<KuraDevice> kuraDevices = (ArrayList<KuraDevice>) stepData.get("KuraDevices");

        for (KuraDevice kuraDevice : kuraDevices) {
            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
            Assert.assertNotNull(device);
            DeviceConfiguration deviceConfiguration = deviceConfiguratiomManagementService.get(device.getScopeId(), device.getId(), null, null, null);
            List<DeviceComponentConfiguration> configurations = deviceConfiguration.getComponentConfigurations();
            stepData.put("configurations", configurations);
        }
    }

    @When("A Configuration named (.*) has property (.*) with value (.*)$")
    public void checkConfiguration(String configurationName, String configurationKey, String configurationValue) {
        DeviceComponentConfiguration configuration = findConfigurationByNameAndValue(configurationName, configurationKey, configurationValue);
        Assert.assertEquals(configurationName, configuration.getDefinition().getId());
        Assert.assertTrue(configuration.getProperties().containsKey(configurationKey));
        Assert.assertTrue(configuration.getProperties().get(configurationKey).toString().equals(configurationValue));
    }

    private DeviceComponentConfiguration findConfigurationByNameAndValue(final String configurationName, final String configurationKey, final String configurationValue) {
        List<DeviceComponentConfiguration> savedConfigurations = (List<DeviceComponentConfiguration>) stepData.get("configurations");
        List<DeviceComponentConfiguration> configurations = savedConfigurations.stream()
                .filter(configuration -> configuration.getDefinition().getId().equals(configurationName))
                .filter(configuration -> configuration.getProperties().containsKey(configurationKey)
                        && configuration.getProperties().get(configurationKey).toString().equals(configurationValue))
                .collect(Collectors.toList());

        if (configurations.isEmpty()) {
            Assert.fail(String.format("Configuration %s with value %s for property %s is not present", configurationName, configurationValue, configurationKey));
        }
        if (configurations.size() > 1) {
            Assert.fail(String.format("There is more than one entry for configuration %s", configurationName));
        }

        return configurations.get(0);
    }

    @Then("^Configuration is received$")
    public void configurationReceived() {
        @SuppressWarnings("unchecked")
        List<DeviceComponentConfiguration> configurations = (List<DeviceComponentConfiguration>) stepData.get("configurations");
        assertEquals(17, configurations.size());
    }

    @When("^Command (.*) is executed$")
    public void executeCommand(String command) throws Exception {

        for(KuraDevice kuraDevice : kuraDevices) {
            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
            DeviceCommandInput commandInput = deviceCommandFactory.newCommandInput();
            commandInput.setCommand(command);
            commandInput.setRunAsynch(false);
            commandInput.setTimeout(0);
            DeviceCommandOutput deviceCommandOutput = deviceCommandManagementService.exec(device.getScopeId(),
                    device.getId(), commandInput, null);
            Integer commandExitCode = deviceCommandOutput.getExitCode();
            stepData.put("commandExitCode", commandExitCode);
        }
    }

    @Then("^Exit code (\\d+) is received$")
    public void configurationReceived(int expectedExitCode) {

        Integer commandExitCode = (Integer) stepData.get("commandExitCode");
        assertEquals(expectedExitCode, commandExitCode.intValue());
    }

    @Then("^Device is connected with \"(.*)\" server ip$")
    public void deviceWithServerIp(String serverIp) {

        DeviceConnection deviceConn = null;
        stepData.put("ExceptionCaught", false);
        try {
            for(KuraDevice kuraDevice : kuraDevices) {
                deviceConn = deviceConnectionService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
            }
        } catch (KapuaException ex) {
            stepData.put("ExceptionCaught", true);
            return;
        }
        assertEquals(serverIp, deviceConn.getServerIp());
    }


    @When("^Client with name \"(.*)\" with client id \"(.*)\" user \"(.*)\" password \"(.*)\" is connected$")
    public void clientConnect(String clientName, String clientId, String user, String password) throws Exception {
        MqttClient mqttClient = null;
        MqttConnectOptions clientOpts = new MqttConnectOptions();

        try {
            mqttClient = new MqttClient(BROKER_URI, clientId,
                    new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }
        clientOpts.setUserName(user);
        clientOpts.setPassword(password.toCharArray());
        try {
            mqttClient.connect(clientOpts);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        if (mqttClient != null) {
            stepData.put(clientName, mqttClient);
        } else {
            throw new Exception("Mqtt test client not connected.");
        }
    }

    @When("^topic \"(.*)\" content \"(.*)\" is published by client named \"(.*)\"$")
    public void publishMessageByClient(String topic, String content, String clientName) throws Exception {
        MqttClient mqttClient = (MqttClient) stepData.get(clientName);
        byte[] payload = Files.readAllBytes(Paths.get(getClass().getResource(content).toURI()));

        if (mqttClient == null) {
            throw new Exception("Mqtt test client not found");
        }
        mqttClient.publish(topic, payload, 0, false);
    }

    @Then("^Client named \"(.*)\" is connected$")
    public void clientConnected(String clientName) throws Exception {
        MqttClient mqttClient = (MqttClient) stepData.get(clientName);
        if (mqttClient == null) {
            throw new Exception("Mqtt test client not found");
        }
        assertEquals(true, mqttClient.isConnected());
    }

    @Then("^Client named \"(.*)\" is not connected$")
    public void clientNotConnected(String clientName) throws Exception {
        MqttClient mqttClient = (MqttClient) stepData.get(clientName);
        if (mqttClient == null) {
            throw new Exception("Mqtt test client not found");
        }
        assertEquals(false, mqttClient.isConnected());
    }

    @Then("^Disconnect client with name \"(.*)\"$")
    public void disconnectClient(String clientName) throws Exception {
        MqttClient mqttClient = (MqttClient) stepData.get(clientName);
        if (mqttClient == null) {
            throw new Exception("Mqtt test client not found");
        }
        try {
            mqttClient.disconnect();
            mqttClient.close();
        } catch (Exception e) {
            // Exception eaten on purpose.
            // Disconnect is for sanity only.
        }
    }

    @Then("^Device(?:|s) status is \"([^\"]*)\"$")
    public void deviceStatusIs(String deviceStatus) throws Exception {
        DeviceConnection deviceConn = null;
        ArrayList<KuraDevice> kuraDevices = (ArrayList<KuraDevice>) stepData.get("KuraDevices");
        try {
            for (KuraDevice kuraDevice : kuraDevices) {
                deviceConn = deviceConnectionService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
            }
        } catch (KapuaException ex) {
            return;
        }
        assertEquals(deviceStatus, deviceConn.getStatus().toString());
    }

    @And("^I add (\\d+) devices to Kura Mock$")
    public void iAddDeviceToKuraMock(int numberOfDevices) {

        if (!kuraDevices.isEmpty()) {
            kuraDevices.clear();
        }
        for (int i = 0; i < numberOfDevices; i++) {
            String clientId = "device" + i;
            kuraDevice = new KuraDevice();
            kuraDevice.addMoreThanOneDeviceToKuraMock(clientId);
            kuraDevice.mqttClientConnect();
            kuraDevices.add(kuraDevice);
        }
        stepData.put("KuraDevices", kuraDevices);
    }

    @And("^Device assets are requested$")
    public void deviceAssetsAreRequested() throws Exception{
        ArrayList<KuraDevice> kuraDevices = (ArrayList<KuraDevice>) stepData.get("KuraDevices");
        DeviceAssets deviceAssets = new DeviceAssetsImpl();

        for (KuraDevice kuraDevice : kuraDevices) {
            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
            Assert.assertNotNull(device);
            DeviceAssets deviceAsset = deviceAssetManagementService.read(device.getScopeId(), device.getId(), deviceAssets ,null);
            List<DeviceAsset> assets = deviceAsset.getAssets();
            stepData.put("assets", assets);
        }
    }

    @And("^Asset with name \"([^\"]*)\" and channel with name \"([^\"]*)\" and value (\\d+) are received$")
    public void assetWithNameAndChannelWithNameAndValueAreReceived(String assetName, String channelName, int channelValue) throws Throwable {
        DeviceAsset asset = findAssetByName(assetName);
        Assert.assertEquals(assetName, asset.getName());
        for (DeviceAssetChannel deviceAssetChannel : asset.getChannels()) {
            assertEquals(channelName, deviceAssetChannel.getName());
            assertEquals(channelValue, deviceAssetChannel.getValue());
        }
    }

    @And("^Packages are requested and (\\d+) package(?:|s) (?:is|are) received$")
    public void packagesAreRequestedAndPackageIsReceived(int numberOfPackages) throws Exception {
        for(KuraDevice kuraDevice : kuraDevices) {
            Device device = deviceRegistryService.findByClientId(SYS_SCOPE_ID, kuraDevice.getClientId());
            if (device != null) {
                DevicePackages deploymentPackages = devicePackageManagementService.getInstalled(device.getScopeId(),
                        device.getId(), null);
                List<DevicePackage> packages = deploymentPackages.getPackages();
                stepData.put("packages", packages);

                assertEquals(numberOfPackages, packages.size());
            }
        }
    }
}

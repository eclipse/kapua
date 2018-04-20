/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.Sanselan;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSettingKeys;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtConfigParameter;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtConfigParameter.GwtConfigParameterType;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtBundleInfo;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeploymentPackage;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceCommandInput;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceCommandOutput;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtSnapshot;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.bundles.GwtBundle;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.packages.GwtPackageDownloadOperation;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.packages.GwtPackageInstallRequest;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.packages.GwtPackageOperation;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.packages.GwtPackageUninstallRequest;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementService;
import org.eclipse.kapua.commons.configuration.metatype.Password;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundle;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleManagementService;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;
import org.eclipse.kapua.service.device.management.command.DeviceCommandFactory;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandManagementService;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationFactory;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.eclipse.kapua.service.device.management.packages.DevicePackageFactory;
import org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfo;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfos;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOperation;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshot;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotManagementService;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;

import javax.xml.namespace.QName;

/**
 * The server side implementation of the Device RPC service.
 */
public class GwtDeviceManagementServiceImpl extends KapuaRemoteServiceServlet implements GwtDeviceManagementService {

    private static Logger logger = LoggerFactory.getLogger(GwtDeviceManagementServiceImpl.class);
    private static final long serialVersionUID = -1391026997499175151L;

    //
    // Packages
    //
    @Override
    public List<GwtDeploymentPackage> findDevicePackages(String scopeShortId, String deviceShortId)
            throws GwtKapuaException {
        List<GwtDeploymentPackage> gwtPkgs = new ArrayList<GwtDeploymentPackage>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DevicePackageManagementService deviceManagementService = locator.getService(DevicePackageManagementService.class);

            KapuaId scopeId = KapuaEid.parseCompactId(scopeShortId);
            KapuaId deviceId = KapuaEid.parseCompactId(deviceShortId);
            DevicePackages deploymentPackages = deviceManagementService.getInstalled(scopeId,
                    deviceId,
                    null);

            for (DevicePackage deploymentPackage : deploymentPackages.getPackages()) {
                GwtDeploymentPackage gwtPkg = new GwtDeploymentPackage();
                gwtPkg.setName(deploymentPackage.getName());
                gwtPkg.setVersion(deploymentPackage.getVersion());

                DevicePackageBundleInfos devicePackageBundleInfos = deploymentPackage.getBundleInfos();

                if (devicePackageBundleInfos != null) {
                    List<GwtBundleInfo> gwtBundleInfos = new ArrayList<GwtBundleInfo>();
                    for (DevicePackageBundleInfo bundleInfo : devicePackageBundleInfos.getBundlesInfos()) {

                        GwtBundleInfo gwtBundleInfo = new GwtBundleInfo();
                        gwtBundleInfo.setName(bundleInfo.getName());
                        gwtBundleInfo.setVersion(bundleInfo.getVersion());

                        gwtBundleInfos.add(gwtBundleInfo);
                    }
                    gwtPkg.setBundleInfos(gwtBundleInfos);
                }
                gwtPkgs.add(gwtPkg);
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtPkgs;
    }

    @Override
    public void installPackage(GwtXSRFToken xsrfToken, GwtPackageInstallRequest gwtPackageInstallRequest) throws GwtKapuaException {
        //
        // Check token
        checkXSRFToken(xsrfToken);

        //
        // Do install
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(gwtPackageInstallRequest.getScopeId());
            KapuaId deviceId = KapuaEid.parseCompactId(gwtPackageInstallRequest.getDeviceId());

            KapuaLocator locator = KapuaLocator.getInstance();
            DevicePackageFactory devicePackageFactory = locator.getFactory(DevicePackageFactory.class);

            DevicePackageDownloadRequest packageDownloadRequest = devicePackageFactory.newPackageDownloadRequest();
            packageDownloadRequest.setUri(new URI(gwtPackageInstallRequest.getPackageURI()));
            packageDownloadRequest.setName(gwtPackageInstallRequest.getPackageName());
            packageDownloadRequest.setVersion(gwtPackageInstallRequest.getPackageVersion());
            packageDownloadRequest.setInstall(true); // Always install
            packageDownloadRequest.setReboot(gwtPackageInstallRequest.isReboot());
            packageDownloadRequest.setRebootDelay(gwtPackageInstallRequest.getRebootDelay());

            DevicePackageManagementService packageManagementService = locator.getService(DevicePackageManagementService.class);
            packageManagementService.downloadExec(scopeId,
                    deviceId,
                    packageDownloadRequest,
                    null);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    public ListLoadResult<GwtPackageOperation> getDownloadOperations(String scopeShortId, String deviceShortId)
            throws GwtKapuaException {
        List<GwtPackageOperation> gwtDeviceOperations = new ArrayList<GwtPackageOperation>();
        try {

            KapuaLocator locator = KapuaLocator.getInstance();
            DevicePackageManagementService deviceManagementService = locator.getService(DevicePackageManagementService.class);

            KapuaId scopeId = KapuaEid.parseCompactId(scopeShortId);
            KapuaId deviceId = KapuaEid.parseCompactId(deviceShortId);
            DevicePackageDownloadOperation downloadOperation = deviceManagementService.downloadStatus(scopeId, deviceId, null);

            GwtPackageDownloadOperation gwtDownloadOperation = new GwtPackageDownloadOperation();

            gwtDownloadOperation.setId(downloadOperation.getId().toCompactId());
            gwtDownloadOperation.setStatus(downloadOperation.getStatus().name());
            gwtDownloadOperation.setSize(downloadOperation.getSize());
            gwtDownloadOperation.setProgress(downloadOperation.getProgress());

            gwtDeviceOperations.add(gwtDownloadOperation);

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtPackageOperation>(gwtDeviceOperations);
    }

    @Override
    public void uninstallPackage(GwtXSRFToken xsrfToken, GwtPackageUninstallRequest gwtPackageUninstallRequest) throws GwtKapuaException {
        //
        // Check token
        checkXSRFToken(xsrfToken);

        //
        // Do install
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(gwtPackageUninstallRequest.getScopeId());
            KapuaId deviceId = KapuaEid.parseCompactId(gwtPackageUninstallRequest.getDeviceId());

            KapuaLocator locator = KapuaLocator.getInstance();
            DevicePackageFactory devicePackageFactory = locator.getFactory(DevicePackageFactory.class);

            DevicePackageUninstallRequest packageUninstallRequest = devicePackageFactory.newPackageUninstallRequest();
            packageUninstallRequest.setName(gwtPackageUninstallRequest.getPackageName());
            packageUninstallRequest.setVersion(gwtPackageUninstallRequest.getPackageVersion());
            packageUninstallRequest.setReboot(gwtPackageUninstallRequest.isReboot());
            packageUninstallRequest.setRebootDelay(gwtPackageUninstallRequest.getRebootDelay());

            DevicePackageManagementService packageManagementService = locator.getService(DevicePackageManagementService.class);
            packageManagementService.uninstallExec(scopeId,
                    deviceId,
                    packageUninstallRequest,
                    null);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

    }

    //
    // Configurations
    //
    @Override
    public List<GwtConfigComponent> findDeviceConfigurations(GwtDevice device)
            throws GwtKapuaException {
        List<GwtConfigComponent> gwtConfigs = new ArrayList<GwtConfigComponent>();
        try {

            // get the configuration
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceConfigurationManagementService deviceConfiguratiomManagementService = locator.getService(DeviceConfigurationManagementService.class);

            KapuaId scopeId = KapuaEid.parseCompactId(device.getScopeId());
            KapuaId deviceId = KapuaEid.parseCompactId(device.getId());
            DeviceConfiguration deviceConfigurations = deviceConfiguratiomManagementService.get(scopeId,
                    deviceId,
                    null,
                    null,
                    null);

            if (deviceConfigurations != null) {

                // sort the list alphabetically by service name
                List<DeviceComponentConfiguration> configs = deviceConfigurations.getComponentConfigurations();
                Collections.sort(configs, new Comparator<DeviceComponentConfiguration>() {

                    @Override
                    public int compare(DeviceComponentConfiguration arg0, DeviceComponentConfiguration arg1) {
                        String name0 = arg0.getId();
                        String name1 = arg1.getId();

                        if (name0.contains(".")) {
                            name0 = name0.substring(name0.lastIndexOf('.'));
                        }
                        if (name1.contains(".")) {
                            name1 = name1.substring(name1.lastIndexOf('.'));
                        }

                        return name0.compareTo(name1);
                    }
                });

                // prepare results
                ConsoleSetting consoleConfig = ConsoleSetting.getInstance();
                List<String> serviceIgnore = consoleConfig.getList(String.class, ConsoleSettingKeys.DEVICE_CONFIGURATION_SERVICE_IGNORE);
                for (DeviceComponentConfiguration config : deviceConfigurations.getComponentConfigurations()) {

                    // ignore items we want to hide
                    if (serviceIgnore != null && serviceIgnore.contains(config.getId())) {
                        continue;
                    }

                    KapuaTocd ocd = config.getDefinition();
                    if (ocd != null) {
                        GwtConfigComponent gwtConfig = new GwtConfigComponent();
                        gwtConfig.setId(config.getId());
                        gwtConfig.setName(ocd.getName());
                        gwtConfig.setDescription(ocd.getDescription());
                        if (ocd.getIcon() != null && ocd.getIcon().size() > 0) {
                            KapuaTicon icon = ocd.getIcon().get(0);

                            checkIconResource(icon);

                            gwtConfig.setComponentIcon(icon.getResource());
                        }

                        List<GwtConfigParameter> gwtParams = new ArrayList<GwtConfigParameter>();
                        gwtConfig.setParameters(gwtParams);
                        for (KapuaTad ad : ocd.getAD()) {
                            if (ad != null) {
                                GwtConfigParameter gwtParam = new GwtConfigParameter();
                                gwtParam.setId(ad.getId());
                                gwtParam.setName(ad.getName());
                                gwtParam.setDescription(ad.getDescription());
                                gwtParam.setType(GwtConfigParameterType.fromString(ad.getType().value()));
                                gwtParam.setRequired(ad.isRequired());
                                gwtParam.setCardinality(ad.getCardinality());
                                if (ad.getOption() != null && ad.getOption().size() > 0) {
                                    Map<String, String> options = new HashMap<String, String>();
                                    for (KapuaToption option : ad.getOption()) {
                                        options.put(option.getLabel(), option.getValue());
                                    }
                                    gwtParam.setOptions(options);
                                }
                                gwtParam.setMin(ad.getMin());
                                gwtParam.setMax(ad.getMax());
                                Map<String, String> gwtEntries = new HashMap<String, String>();
                                for(Entry<QName, String> entry : ad.getOtherAttributes().entrySet()) {
                                    gwtEntries.put(entry.getKey().toString(), entry.getValue());
                                }
                                gwtParam.setOtherAttributes(gwtEntries);

                                if (config.getProperties() != null) {

                                    // handle the value based on the cardinality of the attribute
                                    int cardinality = ad.getCardinality();
                                    Object value = config.getProperties().get(ad.getId());
                                    if (value != null) {

                                        if (cardinality == 0 || cardinality == 1 || cardinality == -1) {
                                            gwtParam.setValue(value.toString());
                                        } else {
                                            // this could be an array value
                                            if (value instanceof Object[]) {
                                                Object[] objValues = (Object[]) value;
                                                List<String> strValues = new ArrayList<String>();
                                                for (Object v : objValues) {
                                                    if (v != null) {
                                                        strValues.add(v.toString());
                                                    }
                                                }
                                                gwtParam.setValues(strValues.toArray(new String[] {}));
                                            }
                                        }
                                    }
                                    gwtParams.add(gwtParam);
                                }
                            }
                        }
                        gwtConfigs.add(gwtConfig);
                    }
                }
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtConfigs;
    }

    @Override
    public void updateComponentConfiguration(GwtXSRFToken xsrfToken,
            GwtDevice gwtDevice,
            GwtConfigComponent gwtCompConfig)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceConfigurationManagementService deviceConfigurationManagementService = locator.getService(DeviceConfigurationManagementService.class);
        DeviceConfigurationFactory deviceConfigurationManagementFactory = locator.getFactory(DeviceConfigurationFactory.class);

        // set name and properties
        DeviceComponentConfiguration compConfig = deviceConfigurationManagementFactory.newComponentConfigurationInstance(gwtCompConfig.getUnescapedComponentId());
        compConfig.setName(gwtCompConfig.getUnescapedComponentName());

        Map<String, Object> compProps = new HashMap<String, Object>();
        for (GwtConfigParameter gwtConfigParam : gwtCompConfig.getParameters()) {

            Object objValue;
            int cardinality = gwtConfigParam.getCardinality();
            if (cardinality == 0 || cardinality == 1 || cardinality == -1) {

                String strValue = gwtConfigParam.getValue();
                objValue = getObjectValue(gwtConfigParam, strValue);
            } else {

                String[] strValues = gwtConfigParam.getValues();
                objValue = getObjectValue(gwtConfigParam, strValues);
            }
            compProps.put(gwtConfigParam.getName(), objValue);
        }
        compConfig.setProperties(compProps);

        // execute the update
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(gwtDevice.getScopeId());
            KapuaId deviceId = KapuaEid.parseCompactId(gwtDevice.getId());
            deviceConfigurationManagementService.put(scopeId,
                    deviceId,
                    compConfig,
                    null);

            //
            // Add an additional delay after the configuration update
            // to give the time to the device to apply the received
            // configuration
            Thread.sleep(1000);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    //
    // Snapshots
    //
    @Override
    public ListLoadResult<GwtSnapshot> findDeviceSnapshots(GwtDevice gwtDevice)
            throws GwtKapuaException {
        List<GwtSnapshot> snapshots = new ArrayList<GwtSnapshot>();
        try {

            // execute the command
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceSnapshotManagementService deviceSnapshotManagementService = locator.getService(DeviceSnapshotManagementService.class);

            KapuaId scopeId = KapuaEid.parseCompactId(gwtDevice.getScopeId());
            KapuaId deviceId = KapuaEid.parseCompactId(gwtDevice.getId());
            DeviceSnapshots snapshotIds = deviceSnapshotManagementService.get(scopeId,
                    deviceId,
                    null);
            // sort them by most recent first

            // sort the list alphabetically by service name
            Collections.sort(snapshotIds.getSnapshots(), new Comparator<DeviceSnapshot>() {

                @Override
                public int compare(DeviceSnapshot arg0,
                        DeviceSnapshot arg1) {
                    DeviceSnapshot snapshotId0 = arg0;
                    DeviceSnapshot snapshotId1 = arg1;
                    return -1 * snapshotId0.getTimestamp().compareTo(snapshotId1.getTimestamp()); // Descending order
                }
            });

            for (DeviceSnapshot snapshot : snapshotIds.getSnapshots()) {
                Long timestamp = snapshot.getTimestamp();
                GwtSnapshot gwtSnapshot = new GwtSnapshot();
                gwtSnapshot.setCreatedOn(new Date(timestamp));
                snapshots.add(gwtSnapshot);
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtSnapshot>(snapshots);
    }

    @Override
    public void rollbackDeviceSnapshot(GwtXSRFToken xsrfToken, GwtDevice gwtDevice, GwtSnapshot snapshot)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceSnapshotManagementService deviceService = locator.getService(DeviceSnapshotManagementService.class);

            KapuaId scopeId = KapuaEid.parseCompactId(gwtDevice.getScopeId());
            KapuaId deviceId = KapuaEid.parseCompactId(gwtDevice.getId());
            deviceService.rollback(scopeId,
                    deviceId,
                    String.valueOf(snapshot.getSnapshotId()),
                    null);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    //
    // Bundles
    //
    @Override
    public ListLoadResult<GwtBundle> findBundles(GwtDevice device)
            throws GwtKapuaException {
        List<GwtBundle> pairs = new ArrayList<GwtBundle>();

        try {

            // get the configuration
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceBundleManagementService deviceBundleManagementService = locator.getService(DeviceBundleManagementService.class);

            KapuaId scopeId = KapuaEid.parseCompactId(device.getScopeId());
            KapuaId id = KapuaEid.parseCompactId(device.getId());
            DeviceBundles bundles = deviceBundleManagementService.get(scopeId,
                    id,
                    null);

            for (DeviceBundle bundle : bundles.getBundles()) {
                GwtBundle pair = new GwtBundle();
                pair.setId(String.valueOf(bundle.getId()));
                pair.setName(bundle.getName());
                pair.setStatus(toStateString(bundle));
                pair.setVersion(bundle.getVersion());

                pairs.add(pair);
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtBundle>(pairs);
    }

    @Override
    public void startBundle(GwtXSRFToken xsrfToken, GwtDevice device, GwtBundle gwtBundle)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceBundleManagementService deviceBundleManagementService = locator.getService(DeviceBundleManagementService.class);

            KapuaId scopeId = KapuaEid.parseCompactId(device.getScopeId());
            KapuaId deviceId = KapuaEid.parseCompactId(device.getId());
            deviceBundleManagementService.start(scopeId,
                    deviceId,
                    String.valueOf(gwtBundle.getId()),
                    null);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public void stopBundle(GwtXSRFToken xsrfToken, GwtDevice device, GwtBundle gwtBundle)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceBundleManagementService deviceBundleManagementService = locator.getService(DeviceBundleManagementService.class);

            KapuaId scopeId = KapuaEid.parseCompactId(device.getScopeId());
            KapuaId deviceId = KapuaEid.parseCompactId(device.getId());
            deviceBundleManagementService.stop(scopeId,
                    deviceId,
                    String.valueOf(gwtBundle.getId()),
                    null);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    //
    // Command
    //
    @Override
    public GwtDeviceCommandOutput executeCommand(GwtXSRFToken xsrfToken, GwtDevice gwtDevice, GwtDeviceCommandInput gwtCommandInput)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        GwtDeviceCommandOutput gwtCommandOutput = new GwtDeviceCommandOutput();
        try {

            // execute the command
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceCommandManagementService deviceCommandManagementService = locator.getService(DeviceCommandManagementService.class);
            DeviceCommandFactory deviceCommandFactory = locator.getFactory(DeviceCommandFactory.class);

            StringTokenizer st = new StringTokenizer(gwtCommandInput.getCommand());
            int count = st.countTokens();

            String command = count > 0 ? st.nextToken() : null;
            String[] args = count > 1 ? new String[count - 1] : null;
            int i = 0;
            while (st.hasMoreTokens()) {
                args[i++] = st.nextToken();
            }

            DeviceCommandInput commandInput = deviceCommandFactory.newCommandInput();
            // commandInput.setArguments(gwtCommandInput.getArguments());
            commandInput.setArguments(args);
            // commandInput.setCommand(gwtCommandInput.getCommand());
            commandInput.setCommand(command);
            commandInput.setEnvironment(gwtCommandInput.getEnvironment());
            commandInput.setRunAsynch(gwtCommandInput.isRunAsynch() != null ? gwtCommandInput.isRunAsynch().booleanValue() : false);
            commandInput.setStdin(gwtCommandInput.getStdin());
            commandInput.setTimeout(gwtCommandInput.getTimeout() != null ? gwtCommandInput.getTimeout().intValue() : 0);
            commandInput.setWorkingDir(gwtCommandInput.getWorkingDir());
            commandInput.setBody(gwtCommandInput.getZipBytes());

            KapuaId scopeId = KapuaEid.parseCompactId(gwtDevice.getScopeId());
            KapuaId deviceId = KapuaEid.parseCompactId(gwtDevice.getId());
            DeviceCommandOutput commandOutput = deviceCommandManagementService.exec(scopeId,
                    deviceId,
                    commandInput, null);

            if (commandOutput.getExceptionMessage() != null) {
                gwtCommandOutput.setExceptionMessage(commandOutput.getExceptionMessage().replace("\n", "<br>"));
            }
            if (commandOutput.getExceptionStack() != null) {
                gwtCommandOutput.setExceptionStack(commandOutput.getExceptionStack().replace("\n", "<br>"));
            }
            gwtCommandOutput.setExitCode(commandOutput.getExitCode());
            if (commandOutput.getStderr() != null) {
                gwtCommandOutput.setStderr(commandOutput.getStderr().replace("\n", "<br>"));
            }
            gwtCommandOutput.setStdout(commandOutput.getStdout());
            gwtCommandOutput.setTimedout(commandOutput.getHasTimedout());
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtCommandOutput;
    }

    //
    // Private methods
    //
    private String toStateString(DeviceBundle bundle) {
        String state = bundle.getState();
        if (state.equals("INSTALLED")) {
            return "bndInstalled";
        } else if (state.equals("RESOLVED")) {
            return "bndResolved";
        } else if (state.equals("STARTING")) {
            return "bndStarting";
        } else if (state.equals("ACTIVE")) {
            return "bndActive";
        } else if (state.equals("STOPPING")) {
            return "bndStopping";
        } else if (state.equals("UNINSTALLED")) {
            return "bndUninstalled";
        } else {
            return "bndUnknown";
        }
    }

    private Object getObjectValue(GwtConfigParameter gwtConfigParam, String strValue) {
        Object objValue = null;
        if (strValue != null) {
            GwtConfigParameterType gwtType = gwtConfigParam.getType();
            switch (gwtType) {
            case LONG:
                objValue = Long.parseLong(strValue);
                break;
            case DOUBLE:
                objValue = Double.parseDouble(strValue);
                break;
            case FLOAT:
                objValue = Float.parseFloat(strValue);
                break;
            case INTEGER:
                objValue = Integer.parseInt(strValue);
                break;
            case SHORT:
                objValue = Short.parseShort(strValue);
                break;
            case BYTE:
                objValue = Byte.parseByte(strValue);
                break;
            case BOOLEAN:
                objValue = Boolean.parseBoolean(strValue);
                break;
            case PASSWORD:
                objValue = new Password(strValue);
                break;
            case CHAR:
                objValue = Character.valueOf(strValue.charAt(0));
                break;
            case STRING:
                objValue = strValue;
                break;
            }
        }
        return objValue;
    }

    private Object[] getObjectValue(GwtConfigParameter gwtConfigParam, String[] defaultValues) {
        List<Object> values = new ArrayList<Object>();
        GwtConfigParameterType type = gwtConfigParam.getType();
        switch (type) {
        case BOOLEAN:
            for (String value : defaultValues) {
                values.add(Boolean.valueOf(value));
            }
            return values.toArray(new Boolean[] {});

        case BYTE:
            for (String value : defaultValues) {
                values.add(Byte.valueOf(value));
            }
            return values.toArray(new Byte[] {});

        case CHAR:
            for (String value : defaultValues) {
                values.add(Character.valueOf(value.charAt(0)));
            }
            return values.toArray(new Character[] {});

        case DOUBLE:
            for (String value : defaultValues) {
                values.add(Double.valueOf(value));
            }
            return values.toArray(new Double[] {});

        case FLOAT:
            for (String value : defaultValues) {
                values.add(Float.valueOf(value));
            }
            return values.toArray(new Float[] {});

        case INTEGER:
            for (String value : defaultValues) {
                values.add(Integer.valueOf(value));
            }
            return values.toArray(new Integer[] {});

        case LONG:
            for (String value : defaultValues) {
                values.add(Long.valueOf(value));
            }
            return values.toArray(new Long[] {});

        case SHORT:
            for (String value : defaultValues) {
                values.add(Short.valueOf(value));
            }
            return values.toArray(new Short[] {});

        case PASSWORD:
            for (String value : defaultValues) {
                values.add(new Password(value));
            }
            return values.toArray(new Password[] {});

        case STRING:
        default:
            return defaultValues;
        }
    }

    /**
     * Checks the source of the icon.
     * The component config icon can be one of the well known icon (i.e. MqttDataTransport icon)
     * as well as an icon loaded from external source with an HTTP link.
     * <p>
     * We need to filter HTTP link to protect the console page and also to have content always served from
     * EC console. Otherwise browsers can alert the user that content is served from domain different from
     * *.everyware-cloud.com and over insicure connection.
     * <p>
     * To avoid this we will download the image locally on the server temporary directory and give back the page
     * a token URL to get the file.
     *
     * @param icon The icon from the OCD of the component configuration.
     */
    private void checkIconResource(KapuaTicon icon) {
        ConsoleSetting config = ConsoleSetting.getInstance();

        String iconResource = icon.getResource();

        //
        // Check if the resource is an HTTP URL or not
        if (iconResource != null &&
                (iconResource.toLowerCase().startsWith("http://") ||
                        iconResource.toLowerCase().startsWith("https://"))) {
            File tmpFile = null;

            try {
                logger.info("Got configuration component icon from URL: {}", iconResource);

                //
                // Tmp file name creation
                String systemTmpDir = System.getProperty("java.io.tmpdir");
                String iconResourcesTmpDir = config.getString(ConsoleSettingKeys.DEVICE_CONFIGURATION_ICON_FOLDER);
                String tmpFileName = Base64.encodeBase64String(MessageDigest.getInstance("MD5").digest(iconResource.getBytes("UTF-8")));

                // Conversions needed got security reasons!
                // On the file servlet we use the regex [0-9A-Za-z]{1,} to validate the given file id.
                // This validation prevents the caller of the file servlet to try to move out of the directory where the icons are stored.
                tmpFileName = tmpFileName.replaceAll("/", "a");
                tmpFileName = tmpFileName.replaceAll("\\+", "m");
                tmpFileName = tmpFileName.replaceAll("=", "z");

                //
                // Tmp dir check and creation
                StringBuilder tmpDirPathSb = new StringBuilder().append(systemTmpDir);
                if (!systemTmpDir.endsWith("/")) {
                    tmpDirPathSb.append("/");
                }
                tmpDirPathSb.append(iconResourcesTmpDir);

                File tmpDir = new File(tmpDirPathSb.toString());
                if (!tmpDir.exists()) {
                    logger.info("Creating tmp dir on path: {}", tmpDir.toString());
                    tmpDir.mkdir();
                }

                //
                // Tmp file check and creation
                tmpDirPathSb.append("/")
                        .append(tmpFileName);
                tmpFile = new File(tmpDirPathSb.toString());

                // Check date of modification to avoid caching forever
                if (tmpFile.exists()) {
                    long lastModifiedDate = tmpFile.lastModified();

                    long maxCacheTime = config.getLong(ConsoleSettingKeys.DEVICE_CONFIGURATION_ICON_CACHE_TIME);

                    if (System.currentTimeMillis() - lastModifiedDate > maxCacheTime) {
                        logger.info("Deleting old cached file: {}", tmpFile.toString());
                        tmpFile.delete();
                    }
                }

                // If file is not cached, download it.
                if (!tmpFile.exists()) {
                    // Url connection
                    URL iconUrl = new URL(iconResource);
                    URLConnection urlConnection = iconUrl.openConnection();
                    urlConnection.setConnectTimeout(2000);
                    urlConnection.setReadTimeout(2000);

                    // Length check
                    String contentLengthString = urlConnection.getHeaderField("Content-Length");

                    long maxLength = config.getLong(ConsoleSettingKeys.DEVICE_CONFIGURATION_ICON_SIZE_MAX);

                    try {
                        Long contentLength = Long.parseLong(contentLengthString);
                        if (contentLength > maxLength) {
                            logger.warn("Content lenght exceeded ({}/{}) for URL: {}",
                                    new Object[] { contentLength, maxLength, iconResource });
                            throw new IOException("Content-Length reported a length of " + contentLength + " which exceeds the maximum allowed size of " + maxLength);
                        }
                    } catch (NumberFormatException nfe) {
                        logger.warn("Cannot get Content-Length header!");
                    }

                    logger.info("Creating file: {}", tmpFile.toString());
                    tmpFile.createNewFile();

                    // Icon download
                    InputStream is = urlConnection.getInputStream();
                    OutputStream os = new FileOutputStream(tmpFile);
                    byte[] buffer = new byte[4096];
                    try {
                        int len;
                        while ((len = is.read(buffer)) > 0) {
                            os.write(buffer, 0, len);

                            maxLength -= len;

                            if (maxLength < 0) {
                                logger.warn("Maximum content lenght exceeded ({}) for URL: {}",
                                        new Object[] { maxLength, iconResource });
                                throw new IOException("Maximum content lenght exceeded (" + maxLength + ") for URL: " + iconResource);
                            }
                        }
                    } finally {
                        os.close();
                    }

                    logger.info("Downloaded file: {}", tmpFile.toString());

                    // Image metadata content checks
                    ImageFormat imgFormat = Sanselan.guessFormat(tmpFile);

                    if (imgFormat.equals(ImageFormat.IMAGE_FORMAT_BMP) ||
                            imgFormat.equals(ImageFormat.IMAGE_FORMAT_GIF) ||
                            imgFormat.equals(ImageFormat.IMAGE_FORMAT_JPEG) ||
                            imgFormat.equals(ImageFormat.IMAGE_FORMAT_PNG)) {
                        logger.info("Detected image format: {}", imgFormat.name);
                    } else if (imgFormat.equals(ImageFormat.IMAGE_FORMAT_UNKNOWN)) {
                        logger.error("Unknown file format for URL: {}", iconResource);
                        throw new IOException("Unknown file format for URL: " + iconResource);
                    } else {
                        logger.error("Usupported file format ({}) for URL: {}", imgFormat, iconResource);
                        throw new IOException("Unknown file format for URL: {}" + iconResource);
                    }

                    logger.info("Image validation passed for URL: {}", iconResource);
                } else {
                    logger.info("Using cached file: {}", tmpFile.toString());
                }

                //
                // Injecting new URL for the icon resource
                String newResourceURL = new StringBuilder().append("img://console/file/icons?id=")
                        .append(tmpFileName)
                        .toString();

                logger.info("Injecting configuration component icon: {}", newResourceURL);
                icon.setResource(newResourceURL);
            } catch (Exception e) {
                if (tmpFile != null &&
                        tmpFile.exists()) {
                    tmpFile.delete();
                }

                icon.setResource("Default");

                logger.error("Error while checking component configuration icon. Using the default plugin icon.", e);
            }
        }
        //
        // If not, all is fine.
    }

}

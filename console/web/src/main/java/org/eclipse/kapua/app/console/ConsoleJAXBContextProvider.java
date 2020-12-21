/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.core.exception.model.CleanJobDataExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.ExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobAlreadyRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobEngineExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobExecutionEnqueuedExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobInvalidTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingStepExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobNotRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobResumingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStartingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStoppingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.ThrowableInfo;
import org.eclipse.kapua.app.api.core.model.job.IsJobRunningResponse;
import org.eclipse.kapua.commons.configuration.metatype.TscalarImpl;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordCreator;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordListResult;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordQuery;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreXmlRegistry;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.job.engine.JobEngineXmlRegistry;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.job.engine.commons.model.JobTargetSublist;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.model.config.metatype.MetatypeXmlRegistry;
import org.eclipse.kapua.service.device.call.kura.model.bundle.KuraBundle;
import org.eclipse.kapua.service.device.call.kura.model.bundle.KuraBundles;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceComponentConfiguration;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceConfiguration;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraBundleInfo;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraDeploymentPackage;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraDeploymentPackages;
import org.eclipse.kapua.service.device.call.kura.model.snapshot.KuraSnapshotIds;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetXmlRegistry;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundle;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobQuery;
import org.eclipse.kapua.service.job.JobXmlRegistry;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;

import javax.xml.bind.JAXBContext;

public class ConsoleJAXBContextProvider implements JAXBContextProvider {

    private JAXBContext context;

    @Override
    public JAXBContext getJAXBContext() throws KapuaException {
        try {
            if (context == null) {
                Map<String, Object> properties = new HashMap<String, Object>(1);
                properties.put(MarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);

                context = JAXBContextFactory.createContext(new Class<?>[]{
                        // REST API exception models
                        ThrowableInfo.class,
                        ExceptionInfo.class,

                        // Jobs Exception Info
                        CleanJobDataExceptionInfo.class,
                        JobAlreadyRunningExceptionInfo.class,
                        JobEngineExceptionInfo.class,
                        JobExecutionEnqueuedExceptionInfo.class,
                        JobInvalidTargetExceptionInfo.class,
                        JobMissingStepExceptionInfo.class,
                        JobMissingTargetExceptionInfo.class,
                        JobNotRunningExceptionInfo.class,
                        JobResumingExceptionInfo.class,
                        JobRunningExceptionInfo.class,
                        JobStartingExceptionInfo.class,
                        JobStoppingExceptionInfo.class,

                        KuraDeviceComponentConfiguration.class,
                        KuraDeviceConfiguration.class,
                        KuraDeploymentPackage.class,
                        KuraDeploymentPackages.class,
                        KuraBundle.class,
                        KuraBundles.class,
                        KuraBundleInfo.class,

                        DeviceAssets.class,

                        DeviceBundle.class,
                        DeviceBundles.class,

                        DeviceCommandInput.class,
                        DeviceCommandOutput.class,

                        DeviceConfiguration.class,
                        DeviceComponentConfiguration.class,

                        DevicePackageDownloadRequest.class,
                        DevicePackageInstallRequest.class,
                        DevicePackageUninstallRequest.class,
                        DevicePackages.class,

                        KuraSnapshotIds.class,
                        DeviceSnapshots.class,
                        KapuaTocd.class,
                        KapuaTad.class,
                        KapuaTicon.class,
                        KapuaToption.class,
                        KapuaTmetadata.class,
                        TscalarImpl.class,
                        MetatypeXmlRegistry.class,

                        // Device Management Assets
                        DeviceAssets.class,
                        DeviceAssetXmlRegistry.class,

                        // Job
                        JobTargetSublist.class,
                        JobStartOptions.class,
                        IsJobRunningResponse.class,
                        JobListResult.class,
                        JobQuery.class,
                        JobXmlRegistry.class,
                        JobEngineXmlRegistry.class,

                        // KapuaEvent
                        ServiceEvent.class,
                        EventStoreRecordCreator.class,
                        EventStoreRecordListResult.class,
                        EventStoreRecordQuery.class,
                        EventStoreXmlRegistry.class,

                }, properties);
            }
            return context;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

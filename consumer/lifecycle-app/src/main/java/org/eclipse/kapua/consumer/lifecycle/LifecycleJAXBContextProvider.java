/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.consumer.lifecycle;

import org.eclipse.kapua.app.api.core.exception.model.CleanJobDataExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.ExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobAlreadyRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobEngineExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobInvalidTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingStepExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobNotRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobResumingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStartingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStoppingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.ThrowableInfo;
import org.eclipse.kapua.commons.configuration.metatype.TscalarImpl;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordCreator;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordListResult;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordQuery;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreXmlRegistry;
import org.eclipse.kapua.commons.util.xml.FallbackMappingJAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.job.engine.commons.model.JobTargetSublist;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTdesignate;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTobject;
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
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundle;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobXmlRegistry;

import java.util.Arrays;
import java.util.List;

/**
 * {@link LifecycleApplication} {@link JAXBContextProvider} implementation.
 * <p>
 * It relies on the {@link FallbackMappingJAXBContextProvider} implementation.
 *
 * @since 2.0.0
 */
public class LifecycleJAXBContextProvider extends FallbackMappingJAXBContextProvider implements JAXBContextProvider {

    private static final List<Class<?>> CLASSES_TO_BOUND = Arrays.asList(
            // Kapua Service Configuration
            KapuaTmetadata.class,
            KapuaTocd.class,
            KapuaTad.class,
            KapuaTicon.class,
            TscalarImpl.class,
            KapuaToption.class,
            KapuaTdesignate.class,
            KapuaTobject.class,
            MetatypeXmlRegistry.class,

            // Kapua Event
            ServiceEvent.class,
            EventStoreRecordCreator.class,
            EventStoreRecordListResult.class,
            EventStoreRecordQuery.class,
            EventStoreXmlRegistry.class,

            // TODO: EXT-CAMEL only for test remove when jobs will be defined in their own container
            // Jobs
            Job.class,
            JobListResult.class,
            JobXmlRegistry.class,

            // Job Engine
            JobStartOptions.class,
            JobTargetSublist.class,

            // Job Engine Exception Info
            ThrowableInfo.class,
            ExceptionInfo.class,
            CleanJobDataExceptionInfo.class,
            JobAlreadyRunningExceptionInfo.class,
            JobEngineExceptionInfo.class,
            JobInvalidTargetExceptionInfo.class,
            JobMissingStepExceptionInfo.class,
            JobMissingTargetExceptionInfo.class,
            JobNotRunningExceptionInfo.class,
            JobResumingExceptionInfo.class,
            JobRunningExceptionInfo.class,
            JobStartingExceptionInfo.class,
            JobStoppingExceptionInfo.class,

            // Device Management Asset
            DeviceAsset.class,
            DeviceAssets.class,

            // Device Management Bundle
            DeviceBundle.class,
            DeviceBundles.class,
            KuraBundle.class,
            KuraBundles.class,
            KuraBundleInfo.class,

            // Device Management Command
            DeviceCommandInput.class,
            DeviceCommandOutput.class,

            // Device Management Configuration + Device Management Snapshot
            DeviceConfiguration.class,
            KuraDeviceComponentConfiguration.class,
            KuraDeviceConfiguration.class,
            KuraSnapshotIds.class,

            // Device Management Packages
            DevicePackage.class,
            DevicePackages.class,
            DevicePackageDownloadRequest.class,
            DevicePackageUninstallRequest.class,
            KuraDeploymentPackage.class,
            KuraDeploymentPackages.class
    );

    @Override
    protected List<Class<?>> getClassesToBound() {
        return CLASSES_TO_BOUND;
    }
}

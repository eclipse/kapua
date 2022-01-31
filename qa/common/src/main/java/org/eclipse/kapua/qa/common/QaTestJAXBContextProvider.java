/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.qa.common;

import org.eclipse.kapua.app.api.core.exception.model.CleanJobDataExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobAlreadyRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobEngineExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobInvalidTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingStepExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobNotRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobResumingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobScopedEngineExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStartingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStoppingExceptionInfo;
import org.eclipse.kapua.app.api.core.model.job.IsJobRunningMultipleResponse;
import org.eclipse.kapua.app.api.core.model.job.IsJobRunningResponse;
import org.eclipse.kapua.app.api.core.model.job.MultipleJobIdRequest;
import org.eclipse.kapua.commons.configuration.metatype.TscalarImpl;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordCreator;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordListResult;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordQuery;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreXmlRegistry;
import org.eclipse.kapua.commons.util.xml.DefaultJAXBContextProvider;
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
import org.eclipse.kapua.service.device.call.kura.model.inventory.KuraInventoryItem;
import org.eclipse.kapua.service.device.call.kura.model.inventory.KuraInventoryItems;
import org.eclipse.kapua.service.device.call.kura.model.inventory.bundles.KuraInventoryBundle;
import org.eclipse.kapua.service.device.call.kura.model.inventory.bundles.KuraInventoryBundles;
import org.eclipse.kapua.service.device.call.kura.model.inventory.containers.KuraInventoryContainer;
import org.eclipse.kapua.service.device.call.kura.model.inventory.containers.KuraInventoryContainers;
import org.eclipse.kapua.service.device.call.kura.model.inventory.packages.KuraInventoryPackage;
import org.eclipse.kapua.service.device.call.kura.model.inventory.packages.KuraInventoryPackages;
import org.eclipse.kapua.service.device.call.kura.model.inventory.system.KuraInventorySystemPackage;
import org.eclipse.kapua.service.device.call.kura.model.inventory.system.KuraInventorySystemPackages;
import org.eclipse.kapua.service.device.call.kura.model.snapshot.KuraSnapshotIds;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundle;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.DeviceInventoryBundle;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.DeviceInventoryBundles;
import org.eclipse.kapua.service.device.management.inventory.model.container.DeviceInventoryContainer;
import org.eclipse.kapua.service.device.management.inventory.model.container.DeviceInventoryContainers;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventoryItem;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventoryXmlRegistry;
import org.eclipse.kapua.service.device.management.inventory.model.packages.DeviceInventoryPackage;
import org.eclipse.kapua.service.device.management.inventory.model.packages.DeviceInventoryPackages;
import org.eclipse.kapua.service.device.management.inventory.model.system.DeviceInventorySystemPackage;
import org.eclipse.kapua.service.device.management.inventory.model.system.DeviceInventorySystemPackages;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystore;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSR;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSRInfo;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCertificate;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItem;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItemQuery;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItems;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreKeypair;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreXmlRegistry;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystores;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobXmlRegistry;

import java.util.Arrays;
import java.util.List;

/**
 * QA Tests {@link JAXBContextProvider} implementation.
 * <p>
 * It relies {@link DefaultJAXBContextProvider} implementation.
 *
 * @since 1.0.0
 */
public class QaTestJAXBContextProvider extends DefaultJAXBContextProvider implements JAXBContextProvider {

    private static final List<Class<?>> CLASSES_TO_BOUND = Arrays.asList(
            // Kapua Service Configuration + Device Management Configuration
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

            // Device Management Configuration + Device Management Snapshots
            DeviceConfiguration.class,
            KuraDeviceComponentConfiguration.class,
            KuraDeviceConfiguration.class,
            KuraSnapshotIds.class,

            // Device Management Inventory
            DeviceInventory.class,
            DeviceInventoryItem.class,
            KuraInventoryItems.class,
            KuraInventoryItem.class,
            DeviceInventoryBundles.class,
            DeviceInventoryBundle.class,
            KuraInventoryBundles.class,
            KuraInventoryBundle.class,
            DeviceInventoryContainers.class,
            DeviceInventoryContainer.class,
            KuraInventoryContainers.class,
            KuraInventoryContainer.class,
            DeviceInventoryPackages.class,
            DeviceInventoryPackage.class,
            KuraInventoryPackages.class,
            KuraInventoryPackage.class,
            DeviceInventorySystemPackages.class,
            DeviceInventorySystemPackage.class,
            KuraInventorySystemPackages.class,
            KuraInventorySystemPackage.class,
            DeviceInventoryXmlRegistry.class,

            // Device Management Keystore
            DeviceKeystore.class,
            DeviceKeystoreCSR.class,
            DeviceKeystoreCSRInfo.class,
            DeviceKeystoreCertificate.class,
            DeviceKeystoreItem.class,
            DeviceKeystoreItemQuery.class,
            DeviceKeystoreItems.class,
            DeviceKeystoreKeypair.class,
            DeviceKeystoreXmlRegistry.class,
            DeviceKeystores.class,

            // Device Management Packages
            DevicePackages.class,
            DevicePackageDownloadRequest.class,
            DevicePackageUninstallRequest.class,
            KuraDeploymentPackage.class,
            KuraDeploymentPackages.class,

            // Jobs
            Job.class,
            JobListResult.class,
            JobXmlRegistry.class,
            DeviceCommandInput.class,
            DeviceCommandOutput.class,

            // Job Engine
            JobStartOptions.class,
            JobTargetSublist.class,

            // Jobs Engine Exception Info
            CleanJobDataExceptionInfo.class,
            JobAlreadyRunningExceptionInfo.class,
            JobEngineExceptionInfo.class,
            JobScopedEngineExceptionInfo.class,
            JobInvalidTargetExceptionInfo.class,
            JobMissingStepExceptionInfo.class,
            JobMissingTargetExceptionInfo.class,
            JobNotRunningExceptionInfo.class,
            JobResumingExceptionInfo.class,
            JobRunningExceptionInfo.class,
            JobStartingExceptionInfo.class,
            JobStoppingExceptionInfo.class,
            IsJobRunningResponse.class,
            IsJobRunningMultipleResponse.class,
            MultipleJobIdRequest.class
    );

    @Override
    protected List<Class<?>> getClassesToBound() {
        return CLASSES_TO_BOUND;
    }
}

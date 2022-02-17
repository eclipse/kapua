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

import org.eclipse.kapua.broker.core.router.EndChainEndPoint;
import org.eclipse.kapua.broker.core.router.EndPointContainer;
import org.eclipse.kapua.broker.core.router.ParentEndPoint;
import org.eclipse.kapua.broker.core.router.SimpleEndPoint;
import org.eclipse.kapua.commons.configuration.metatype.TscalarImpl;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.job.engine.commons.model.JobTargetSublist;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTdesignate;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTobject;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.model.config.metatype.MetatypeXmlRegistry;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.Organization;
import org.eclipse.kapua.service.account.xml.AccountXmlRegistry;
import org.eclipse.kapua.service.authentication.token.AccessToken;
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
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
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
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobXmlRegistry;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagXmlRegistry;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserXmlRegistry;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

import javax.xml.bind.JAXBContext;

/**
 * JAXB context provided for proper (un)marshalling of interface annotated classes.
 * This particular implementation is used only in unit and integration tests.
 * <p>
 * Application and interfaces have their own implementation of provider.
 */
public class TestJAXBContextProvider implements JAXBContextProvider {

    private JAXBContext context;

    @Override
    public JAXBContext getJAXBContext() {
        try {
            if (context == null) {
                context = JAXBContextFactory.createContext(new Class<?>[]{
                        // General
                        ServiceEvent.class,
                        KapuaTmetadata.class,
                        KapuaTocd.class,
                        KapuaTad.class,
                        KapuaTicon.class,
                        TscalarImpl.class,
                        KapuaToption.class,
                        KapuaTdesignate.class,
                        KapuaTobject.class,
                        MetatypeXmlRegistry.class,

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

                        // Device Management Configuration
                        DeviceConfiguration.class,
                        DeviceComponentConfiguration.class,
                        KuraDeviceComponentConfiguration.class,
                        KuraDeviceConfiguration.class,

                        // Device Management Keystore
                        DeviceKeystores.class,
                        DeviceKeystore.class,
                        DeviceKeystoreCertificate.class,
                        DeviceKeystoreItems.class,
                        DeviceKeystoreItem.class,
                        DeviceKeystoreItemQuery.class,
                        DeviceKeystoreCertificate.class,
                        DeviceKeystoreKeypair.class,
                        DeviceKeystoreCSRInfo.class,
                        DeviceKeystoreCSR.class,
                        DeviceKeystoreXmlRegistry.class,

                        // Device Management Package
                        DevicePackages.class,
                        DevicePackageInstallRequest.class,
                        DevicePackageUninstallRequest.class,
                        DevicePackageDownloadRequest.class,
                        KuraDeploymentPackage.class,
                        KuraDeploymentPackages.class,

                        // Device Management Snapshot
                        DeviceSnapshots.class,
                        KuraSnapshotIds.class,

                        // Authorization
                        AccessToken.class,

                        // User
                        User.class,
                        UserCreator.class,
                        UserListResult.class,
                        UserQuery.class,
                        UserXmlRegistry.class,

                        // Account
                        Account.class,
                        AccountCreator.class,
                        AccountListResult.class,
                        Organization.class,
                        AccountXmlRegistry.class,

                        // Tag
                        Tag.class,
                        TagListResult.class,
                        TagXmlRegistry.class,

                        // Jobs
                        Job.class,
                        JobListResult.class,
                        JobXmlRegistry.class,
                        JobTargetSublist.class,

                        // Broker core
                        EndPointContainer.class,
                        SimpleEndPoint.class,
                        ParentEndPoint.class,
                        EndChainEndPoint.class
                }, null);
            }
            return context;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

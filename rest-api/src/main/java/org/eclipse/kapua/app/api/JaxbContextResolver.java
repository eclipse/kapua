/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api;

import java.security.acl.Permission;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import org.eclipse.kapua.app.api.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.v1.resources.model.ErrorBean;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountXmlRegistry;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationXmlRegistry;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenImpl;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.CredentialXmlRegistry;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfoXmlRegistry;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionXmlRegistry;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleXmlRegistry;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionQuery;
import org.eclipse.kapua.service.authorization.role.RolePermissionXmlRegistry;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleXmlRegistry;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceConfiguration;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraDeploymentPackage;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraDeploymentPackages;
import org.eclipse.kapua.service.device.call.kura.model.snapshot.KuraSnapshotIds;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandXmlRegistry;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationXmlRegistry;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfo;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfos;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageXmlRegistry;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshot;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotXmlRegistry;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceXmlRegistry;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionSummary;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionXmlRegistry;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventXmlRegistry;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserXmlRegistry;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

/**
 * Provide a customized JAXBContext that makes the concrete implementations
 * known and available for marshalling
 */
@Provider
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class JaxbContextResolver implements ContextResolver<JAXBContext> {

    private JAXBContext jaxbContext;

    public JaxbContextResolver() {
        try {
            jaxbContext = JAXBContextFactory.createContext(new Class[] {
                    ErrorBean.class,
                    CountResult.class,
                    
                    Account.class,
                    AccountCreator.class,
                    AccountListResult.class,
                    AccountXmlRegistry.class,
                    
                    User.class,
                    UserCreator.class,
                    UserListResult.class,
                    UserQuery.class,
                    UserXmlRegistry.class,
                    
                    Device.class,
                    DeviceCreator.class,
                    DeviceListResult.class,
                    DeviceXmlRegistry.class,
                    DeviceCommandInput.class,
                    DeviceCommandXmlRegistry.class,
                    DeviceCommandOutput.class,
                    DeviceConfiguration.class,
                    DeviceComponentConfiguration.class,
                    DeviceConfigurationXmlRegistry.class,
                    DeviceSnapshot.class,
                    DeviceSnapshots.class,
                    DeviceSnapshotXmlRegistry.class,
                    DeviceEvent.class,
                    DeviceEventListResult.class,
                    DeviceEventXmlRegistry.class,
                    DevicePackage.class,
                    DevicePackages.class,
                    DevicePackageBundleInfo.class,
                    DevicePackageBundleInfos.class,
                    DevicePackageXmlRegistry.class,
                    DevicePackageDownloadRequest.class,
                    DevicePackageUninstallRequest.class,
                    KuraSnapshotIds.class,
                    KuraDeviceConfiguration.class,
                    KuraDeploymentPackages.class,
                    KuraDeploymentPackage.class,
                    KapuaTocd.class,
                    KapuaTad.class,
                    KapuaTicon.class,
                    KapuaToption.class,
                    DeviceConnectionSummary.class,
                    DeviceConnectionXmlRegistry.class,
                    AuthenticationCredentials.class,
                    AuthenticationXmlRegistry.class,
                    AccessTokenImpl.class,
                    ApiKeyCredentials.class,
                    JwtCredentials.class,
                    UsernamePasswordCredentials.class,
                    Credential.class,
                    CredentialListResult.class,
                    CredentialXmlRegistry.class,
                    CredentialCreator.class,
                    CredentialType.class,
                    
                    // Permission
                    Permission.class,

                    // Roles
                    Role.class,
                    RoleListResult.class,
                    RoleCreator.class,
                    RoleQuery.class,
                    RoleXmlRegistry.class,
                    
                    // Role Permissions
                    RolePermission.class,
                    RolePermissionListResult.class,
                    RolePermissionCreator.class,
                    RolePermissionQuery.class,
                    RolePermissionXmlRegistry.class,
                    
                    // Access Info
                    AccessInfo.class,
                    AccessInfoListResult.class,
                    AccessInfoCreator.class,
                    AccessInfoQuery.class,
                    AccessInfoXmlRegistry.class,
                    
                    AccessPermission.class,
                    AccessPermissionListResult.class,
                    AccessPermissionCreator.class,
                    AccessPermissionXmlRegistry.class,
                    AccessRole.class,
                    AccessRoleListResult.class,
                    AccessRoleCreator.class,
                    AccessRoleXmlRegistry.class

            }, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JAXBContext getContext(Class<?> type) {
        return jaxbContext;
    }

}

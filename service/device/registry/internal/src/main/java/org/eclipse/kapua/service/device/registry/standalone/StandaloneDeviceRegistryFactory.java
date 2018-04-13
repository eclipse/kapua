/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.standalone;

import com.google.common.base.MoreObjects;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.ExecutionException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.model.id.KapuaIdFactoryImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationServiceImpl;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryServiceImpl;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.test.MockedLocator;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public class StandaloneDeviceRegistryFactory {

    public DeviceRegistryService create() {
        System.setProperty(KapuaLocator.LOCATOR_CLASS_NAME_SYSTEM_PROPERTY, MockedLocator.class.getName());
        MockedLocator locator = (MockedLocator) KapuaLocator.getInstance();

        XmlUtil.setContextProvider(new RegistryJAXBContextProvider());
        locator.setMockedFactory(KapuaMetatypeFactory.class, new KapuaMetatypeFactoryImpl());
        locator.setMockedFactory(KapuaIdFactory.class, new KapuaIdFactoryImpl());

        System.setProperty(SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER.key(), "H2");
        SystemSetting config = SystemSetting.getInstance();
        String dbUsername = config.getString(SystemSettingKey.DB_USERNAME);
        String dbPassword = config.getString(SystemSettingKey.DB_PASSWORD);
        String schema = MoreObjects.firstNonNull(config.getString(SystemSettingKey.DB_SCHEMA_ENV), config.getString(SystemSettingKey.DB_SCHEMA));
        new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema)).update();

        locator.setMockedService(AuthorizationService.class, new AuthorizationServiceImpl());
        locator.setMockedFactory(PermissionFactory.class, new PermissionFactoryImpl());

        ThreadContext.bind(new Subject() {

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public PrincipalCollection getPrincipals() {
                return null;
            }

            @Override
            public boolean isPermitted(String permission) {
                return false;
            }

            @Override
            public boolean isPermitted(Permission permission) {
                return false;
            }

            @Override
            public boolean[] isPermitted(String... permissions) {
                return new boolean[0];
            }

            @Override
            public boolean[] isPermitted(List<Permission> permissions) {
                return new boolean[0];
            }

            @Override
            public boolean isPermittedAll(String... permissions) {
                return false;
            }

            @Override
            public boolean isPermittedAll(Collection<Permission> permissions) {
                return false;
            }

            @Override
            public void checkPermission(String permission) throws AuthorizationException {
            }

            @Override
            public void checkPermission(Permission permission) throws AuthorizationException {
            }

            @Override
            public void checkPermissions(String... permissions) throws AuthorizationException {
            }

            @Override
            public void checkPermissions(Collection<Permission> permissions) throws AuthorizationException {
            }

            @Override
            public boolean hasRole(String roleIdentifier) {
                return false;
            }

            @Override
            public boolean[] hasRoles(List<String> roleIdentifiers) {
                return new boolean[0];
            }

            @Override
            public boolean hasAllRoles(Collection<String> roleIdentifiers) {
                return false;
            }

            @Override
            public void checkRole(String roleIdentifier) throws AuthorizationException {

            }

            @Override
            public void checkRoles(Collection<String> roleIdentifiers) throws AuthorizationException {

            }

            @Override
            public void checkRoles(String... roleIdentifiers) throws AuthorizationException {

            }

            @Override
            public void login(AuthenticationToken token) throws AuthenticationException {
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public boolean isRemembered() {
                return false;
            }

            @Override
            public Session getSession() {
                return null;
            }

            @Override
            public Session getSession(boolean create) {
                return null;
            }

            @Override
            public void logout() {

            }

            @Override
            public <V> V execute(Callable<V> callable) throws ExecutionException {
                return null;
            }

            @Override
            public void execute(Runnable runnable) {

            }

            @Override
            public <V> Callable<V> associateWith(Callable<V> callable) {
                return null;
            }

            @Override
            public Runnable associateWith(Runnable runnable) {
                return null;
            }

            @Override
            public void runAs(PrincipalCollection principals) throws NullPointerException, IllegalStateException {

            }

            @Override
            public boolean isRunAs() {
                return false;
            }

            @Override
            public PrincipalCollection getPreviousPrincipals() {
                return null;
            }

            @Override
            public PrincipalCollection releaseRunAs() {
                return null;
            }
        });
        KapuaSecurityUtils.setSession(new KapuaSession());
        return new DeviceRegistryServiceImpl();
    }

}

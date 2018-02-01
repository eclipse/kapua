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
package org.eclipse.kapua.app.console.module.authentication.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.api.shared.util.KapuaGwtCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialCreator;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialQuery;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialService;
import org.eclipse.kapua.app.console.module.authentication.shared.util.GwtKapuaAuthenticationModelConverter;
import org.eclipse.kapua.app.console.module.authentication.shared.util.KapuaGwtAuthenticationModelConverter;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserService;

public class GwtCredentialServiceImpl extends KapuaRemoteServiceServlet implements GwtCredentialService {

    private static final long serialVersionUID = 7323313459749361320L;

    @Override
    public PagingLoadResult<GwtCredential> query(PagingLoadConfig loadConfig, final GwtCredentialQuery gwtCredentialQuery) throws GwtKapuaException {
        //
        // Do query
        int totalLength = 0;
        List<GwtCredential> gwtCredentials = new ArrayList<GwtCredential>();

        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            CredentialService credentialService = locator.getService(CredentialService.class);
            final UserService userService = locator.getService(UserService.class);
            final UserFactory userFactory = locator.getFactory(UserFactory.class);
            final HashMap<String, String> usernameMap = new HashMap<String, String>();
            // Convert from GWT entity
            CredentialQuery credentialQuery = GwtKapuaAuthenticationModelConverter.convertCredentialQuery(loadConfig, gwtCredentialQuery);

            // query
            CredentialListResult credentials = credentialService.query(credentialQuery);

            // If there are results
            if (!credentials.isEmpty()) {
                UserListResult userListResult = KapuaSecurityUtils.doPrivileged(new Callable<UserListResult>() {

                    @Override
                    public UserListResult call() throws Exception {
                        return userService.query(userFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialQuery.getScopeId())));
                    }
                });
                for (User user : userListResult.getItems()) {
                    usernameMap.put(user.getId().toCompactId(), user.getName());
                }

                // count
                totalLength = Long.valueOf(credentialService.count(credentialQuery)).intValue();
                // Convert to GWT entity
                for (Credential credential : credentials.getItems()) {
                    GwtCredential gwtCredential = KapuaGwtAuthenticationModelConverter.convertCredential(credential);
                    gwtCredential.setUsername(usernameMap.get(credential.getUserId().toCompactId()));
                    gwtCredential.setCreatedByName(usernameMap.get(credential.getCreatedBy().toCompactId()));
                    gwtCredential.setModifiedByName(usernameMap.get(credential.getModifiedBy().toCompactId()));
                    gwtCredentials.add(gwtCredential);
                }
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtCredential>(gwtCredentials, loadConfig.getOffset(), totalLength);
    }

    @Override
    public void delete(GwtXSRFToken xsrfToken, String stringAccountId, String gwtCredentialId) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            CredentialService credentialService = locator.getService(CredentialService.class);
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(stringAccountId);
            KapuaId credentialId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialId);
            credentialService.delete(scopeId, credentialId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public GwtCredential create(GwtXSRFToken xsrfToken, GwtCredentialCreator gwtCredentialCreator) throws GwtKapuaException {
        //
        // Checking XSRF token
        checkXSRFToken(xsrfToken);

        //
        // Do create
        GwtCredential gwtCredential = null;
        try {
            // Convert from GWT Entity
            CredentialCreator credentialCreator = GwtKapuaAuthenticationModelConverter.convertCredentialCreator(gwtCredentialCreator);

            // Create
            KapuaLocator locator = KapuaLocator.getInstance();
            CredentialService credentialService = locator.getService(CredentialService.class);
            UserService userService = locator.getService(UserService.class);
            Credential credential = credentialService.create(credentialCreator);
            User user = userService.find(GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialCreator.getScopeId()), credential.getUserId());
            // Convert
            gwtCredential = KapuaGwtAuthenticationModelConverter.convertCredential(credential, user);

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        //
        // Return result
        return gwtCredential;
    }

    @Override
    public GwtCredential update(GwtXSRFToken gwtXsrfToken, GwtCredential gwtCredential) throws GwtKapuaException {
        //
        // Checking XSRF token
        checkXSRFToken(gwtXsrfToken);

        //
        // Do update
        GwtCredential gwtCredentialUpdated = null;
        try {
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredential.getScopeId());
            KapuaId credentialId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredential.getId());
            // Update
            KapuaLocator locator = KapuaLocator.getInstance();
            CredentialService credentialService = locator.getService(CredentialService.class);
            UserService userService = locator.getService(UserService.class);

            if (StringUtils.isNotEmpty(StringUtils.strip(gwtCredential.getCredentialKey()))) {
                String encryptedPass = AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, gwtCredential.getCredentialKey());
                gwtCredential.setCredentialKey(encryptedPass);
            } else {
                Credential currentCredential = credentialService.find(scopeId, credentialId);
                gwtCredential.setCredentialKey(currentCredential.getCredentialKey());
            }

            Credential credentialUpdated = credentialService.update(GwtKapuaAuthenticationModelConverter.convertCredential(gwtCredential));
            User user = userService.find(credentialUpdated.getScopeId(), credentialUpdated.getUserId());
            // Convert
            gwtCredentialUpdated = KapuaGwtAuthenticationModelConverter.convertCredential(credentialUpdated, user);

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        //
        // Return result
        return gwtCredentialUpdated;
    }

    @Override
    public ListLoadResult<GwtGroupedNVPair> getCredentialDescription(String scopeShortId, String credentialShortId) throws GwtKapuaException {
        //
        // Do get
        List<GwtGroupedNVPair> gwtCredentialDescription = new ArrayList<GwtGroupedNVPair>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            CredentialService credentialService = locator.getService(CredentialService.class);

            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeShortId);
            KapuaId credentialId = GwtKapuaCommonsModelConverter.convertKapuaId(credentialShortId);

            // Find
            Credential credential = credentialService.find(scopeId, credentialId);

            // If there are results
            if (credential != null) {
                gwtCredentialDescription.add(new GwtGroupedNVPair("Entity", "Scope Id", KapuaGwtCommonsModelConverter.convertKapuaId(credential.getScopeId())));
                gwtCredentialDescription.add(new GwtGroupedNVPair("Entity", "Id", KapuaGwtCommonsModelConverter.convertKapuaId(credential.getId())));
                gwtCredentialDescription.add(new GwtGroupedNVPair("Entity", "Created On", credential.getCreatedOn()));
                gwtCredentialDescription.add(new GwtGroupedNVPair("Entity", "Created By", KapuaGwtCommonsModelConverter.convertKapuaId(credential.getCreatedBy())));
                gwtCredentialDescription.add(new GwtGroupedNVPair("Entity", "Modified On", credential.getModifiedOn()));
                gwtCredentialDescription.add(new GwtGroupedNVPair("Entity", "Modified By", KapuaGwtCommonsModelConverter.convertKapuaId(credential.getModifiedBy())));
                gwtCredentialDescription.add(new GwtGroupedNVPair("Credential", "Credential Type", credential.getCredentialType().toString()));
                gwtCredentialDescription.add(new GwtGroupedNVPair("Credential", "User ID", KapuaGwtCommonsModelConverter.convertKapuaId(credential.getUserId())));
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtGroupedNVPair>(gwtCredentialDescription);
    }

    @Override
    public void changePassword(GwtXSRFToken gwtXsrfToken, String oldPassword, final String newPassword, String stringUserId, String stringScopeId) throws GwtKapuaException {
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
            final UserService userService = locator.getService(UserService.class);
            final CredentialService credentialsService = locator.getService(CredentialService.class);
            final CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);
            final CredentialsFactory credentialsFactory = locator.getFactory(CredentialsFactory.class);

            final KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(stringScopeId);
            final KapuaId userId = GwtKapuaCommonsModelConverter.convertKapuaId(stringUserId);

            User user = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return userService.find(scopeId, userId);
                }
            });
            final String username = user.getName();
            LoginCredentials loginCredentials = credentialsFactory.newUsernamePasswordCredentials(username, oldPassword);

            boolean validPassword = authenticationService.verifyCredentials(loginCredentials);

            if (validPassword) {
                KapuaSecurityUtils.doPrivileged(new Callable<Void>() {

                    @Override
                    public Void call() throws Exception {
                        CredentialListResult credentialsList = credentialsService.findByUserId(scopeId, userId);
                        Credential oldCredential = null;
                        for (Credential credential : credentialsList.getItems()) {
                            if (credential.getCredentialType().equals(CredentialType.PASSWORD)) {
                                oldCredential = credential;
                                break;
                            }
                        }
                        if (oldCredential != null) {
                            credentialsService.delete(scopeId, oldCredential.getId());
                            CredentialCreator newCredentialCreator = credentialFactory
                                    .newCreator(scopeId, userId, CredentialType.PASSWORD, newPassword, oldCredential.getStatus(), oldCredential.getExpirationDate());
                            credentialsService.create(newCredentialCreator);
                        } else {
                        }
                        return null;
                    }
                });
            } else {
                throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS, null, String.format("Wrong existing password for user %s", username));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public void unlock(GwtXSRFToken xsrfToken, String stringScopeId, String gwtCredentialId) throws GwtKapuaException {

        try {
            // //
            // Checking XSRF token
            checkXSRFToken(xsrfToken);

            KapuaLocator locator = KapuaLocator.getInstance();
            CredentialService credentialService = locator.getService(CredentialService.class);
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(stringScopeId);
            KapuaId credentialId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialId);

            credentialService.unlock(scopeId, credentialId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }
}

/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.authentication.server;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialCreator;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialQuery;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialService;
import org.eclipse.kapua.app.console.module.authentication.shared.util.GwtKapuaAuthenticationModelConverter;
import org.eclipse.kapua.app.console.module.authentication.shared.util.KapuaGwtAuthenticationModelConverter;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.CommonsValidationRegex;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.exception.PasswordLengthException;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class GwtCredentialServiceImpl extends KapuaRemoteServiceServlet implements GwtCredentialService {

    private static final long serialVersionUID = 7323313459749361320L;

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthenticationService AUTHENTICATION_SERVICE = LOCATOR.getService(AuthenticationService.class);

    private static final CredentialService CREDENTIAL_SERVICE = LOCATOR.getService(CredentialService.class);
    private static final CredentialFactory CREDENTIAL_FACTORY = LOCATOR.getFactory(CredentialFactory.class);

    private static final CredentialsFactory CREDENTIALS_FACTORY = LOCATOR.getFactory(CredentialsFactory.class);

    private static final UserService USER_SERVICE = LOCATOR.getService(UserService.class);
    private static final UserFactory USER_FACTORY = LOCATOR.getFactory(UserFactory.class);

    // this should be removed due to the refactoring in fixPasswordValidationBypass method
    private static final int SYSTEM_MAXIMUM_PASSWORD_LENGTH = 255;

    @Override
    public PagingLoadResult<GwtCredential> query(PagingLoadConfig loadConfig, final GwtCredentialQuery gwtCredentialQuery) throws GwtKapuaException {
        int totalLength = 0;
        List<GwtCredential> gwtCredentials = new ArrayList<GwtCredential>();
        try {

            // Convert from GWT entity
            CredentialQuery credentialQuery = GwtKapuaAuthenticationModelConverter.convertCredentialQuery(loadConfig, gwtCredentialQuery);

            // query
            CredentialListResult credentials = CREDENTIAL_SERVICE.query(credentialQuery);
            credentials.sort(credentialComparator);
            totalLength = credentials.getTotalCount().intValue();

            // If there are results
            if (!credentials.isEmpty()) {
                UserListResult userListResult = KapuaSecurityUtils.doPrivileged(new Callable<UserListResult>() {

                    @Override
                    public UserListResult call() throws Exception {
                        return USER_SERVICE.query(USER_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialQuery.getScopeId())));
                    }
                });

                HashMap<String, String> usernameMap = new HashMap<String, String>();
                for (User user : userListResult.getItems()) {
                    usernameMap.put(user.getId().toCompactId(), user.getName());
                }

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
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(stringAccountId);
            KapuaId credentialId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialId);

            CREDENTIAL_SERVICE.delete(scopeId, credentialId);
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
            Credential credential = CREDENTIAL_SERVICE.create(credentialCreator);
            User user = USER_SERVICE.find(GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialCreator.getScopeId()), credential.getUserId());

            // Convert
            gwtCredential = KapuaGwtAuthenticationModelConverter.convertCredential(credential, user);
            gwtCredential.setCredentialKey(credential.getCredentialKey());

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

        fixPasswordValidationBypass(gwtCredential);

        //
        // Do update
        GwtCredential gwtCredentialUpdated = null;
        try {
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredential.getScopeId());
            KapuaId credentialId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredential.getId());

            // Update
            if (StringUtils.isNotEmpty(StringUtils.strip(gwtCredential.getCredentialKey()))) {
                String encryptedPass = AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, gwtCredential.getCredentialKey());
                gwtCredential.setCredentialKey(encryptedPass);
            } else {
                Credential currentCredential = CREDENTIAL_SERVICE.find(scopeId, credentialId);
                gwtCredential.setCredentialKey(currentCredential.getCredentialKey());
            }
            Credential credentialUpdated = CREDENTIAL_SERVICE.update(GwtKapuaAuthenticationModelConverter.convertCredential(gwtCredential));
            User user = USER_SERVICE.find(credentialUpdated.getScopeId(), credentialUpdated.getUserId());

            // Convert
            gwtCredentialUpdated = KapuaGwtAuthenticationModelConverter.convertCredential(credentialUpdated, user);

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        //
        // Return result
        return gwtCredentialUpdated;
    }


    /**
     * Validate password, this check should be moved to
     * CredentialServiceImpl. There, this check already exist,
     * but it's useless since it's done on the already encrypted password
     * @param gwtCredential
     * @throws GwtKapuaException
     */
    private void fixPasswordValidationBypass(GwtCredential gwtCredential)
    throws GwtKapuaException {
        Credential credential =
            GwtKapuaAuthenticationModelConverter.convertCredential(
                gwtCredential);
        try {
            // Validate Password length
            int minPasswordLength = CREDENTIAL_SERVICE.getMinimumPasswordLength(
                credential.getScopeId());
            if (gwtCredential.getCredentialKey().length() < minPasswordLength ||
                gwtCredential.getCredentialKey().length() >
                    SYSTEM_MAXIMUM_PASSWORD_LENGTH) {
                throw new PasswordLengthException(
                    minPasswordLength, SYSTEM_MAXIMUM_PASSWORD_LENGTH);
            }

            // Validate Password regex
            ArgumentValidator.match(
                gwtCredential.getCredentialKey(),
                CommonsValidationRegex.PASSWORD_REGEXP,
                "credential.credentialKey"
            );

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }


    @Override
    public void changePassword(GwtXSRFToken gwtXsrfToken, String oldPassword, final String newPassword, String mfaCode, String stringUserId, String stringScopeId) throws GwtKapuaException {
        String username = null;
        try {
            final KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(stringScopeId);
            final KapuaId userId = GwtKapuaCommonsModelConverter.convertKapuaId(stringUserId);

            User user = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return USER_SERVICE.find(scopeId, userId);
                }
            });
            if (user == null) {
                SecurityUtils.getSubject().logout();

                throw new AuthenticationException();
            }
            username = user.getName();
            final String finalUsername = username;
            UsernamePasswordCredentials loginCredentials = CREDENTIALS_FACTORY.newUsernamePasswordCredentials(finalUsername, oldPassword);
            loginCredentials.setAuthenticationCode(mfaCode);
            AUTHENTICATION_SERVICE.verifyCredentials(loginCredentials);

            KapuaSecurityUtils.doPrivileged(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    CredentialListResult credentialsList = CREDENTIAL_SERVICE.findByUserId(scopeId, userId);

                    Credential oldCredential = null;
                    for (Credential credential : credentialsList.getItems()) {
                        if (credential.getCredentialType().equals(CredentialType.PASSWORD)) {
                            oldCredential = credential;
                            break;
                        }
                    }

                    if (oldCredential != null) {
                        CREDENTIAL_SERVICE.delete(scopeId, oldCredential.getId());

                        CredentialCreator newCredentialCreator = CREDENTIAL_FACTORY.newCreator(
                                scopeId,
                                userId,
                                CredentialType.PASSWORD,
                                newPassword,
                                oldCredential.getStatus(),
                                oldCredential.getExpirationDate());

                        CREDENTIAL_SERVICE.create(newCredentialCreator);
                    } else {
                        throw new KapuaEntityNotFoundException(Credential.TYPE, finalUsername);
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
    }

    @Override
    public void unlock(GwtXSRFToken xsrfToken, String stringScopeId, String gwtCredentialId) throws GwtKapuaException {

        try {
            // //
            // Checking XSRF token
            checkXSRFToken(xsrfToken);

            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(stringScopeId);
            KapuaId credentialId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialId);

            CREDENTIAL_SERVICE.unlock(scopeId, credentialId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public Integer getMinPasswordLength(final String scopeId) throws GwtKapuaException {
        try {
            AUTHENTICATION_SERVICE.isAuthenticated();

            // Do privileged because the request may come from a user with no permission who just wants to change his own password
            return KapuaSecurityUtils.doPrivileged(new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    return CREDENTIAL_SERVICE.getMinimumPasswordLength(GwtKapuaCommonsModelConverter.convertKapuaId(scopeId));
                }

            });
        } catch (KapuaException ex) {
            FailureHandler.handle(ex);
            return null;
        }
    }

    Comparator<Credential> credentialComparator = new Comparator<Credential>() {

        @Override
        public int compare(Credential credential1, Credential credential2) {
            return credential1.getId().toString().compareTo(credential2.getId().toString());
        }
    };
}

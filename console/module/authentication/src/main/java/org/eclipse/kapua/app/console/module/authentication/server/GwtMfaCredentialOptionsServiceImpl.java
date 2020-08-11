/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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

import java.util.concurrent.Callable;

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtMfaCredentialOptions;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtMfaCredentialOptionsCreator;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtMfaCredentialOptionsService;
import org.eclipse.kapua.app.console.module.authentication.shared.util.GwtKapuaAuthenticationModelConverter;
import org.eclipse.kapua.app.console.module.authentication.shared.util.KapuaGwtAuthenticationModelConverter;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.ThrowingRunnable;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionService;

public class GwtMfaCredentialOptionsServiceImpl extends KapuaRemoteServiceServlet implements GwtMfaCredentialOptionsService {

    private static final long serialVersionUID = 7323313459749361320L;

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final MfaOptionService MFA_OPTION_SERVICE = LOCATOR.getService(MfaOptionService.class);

    @Override
    public void delete(GwtXSRFToken xsrfToken, String stringAccountId, String gwtMfaCredentialOptionsId, boolean selfManagement) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        try {
            final KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(stringAccountId);
            final KapuaId mfaCredentialOptionsId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtMfaCredentialOptionsId);

            if (selfManagement) {
                KapuaSecurityUtils.doPrivileged(new ThrowingRunnable() {

                    @Override
                    public void run() throws Exception {
                        MFA_OPTION_SERVICE.delete(scopeId, mfaCredentialOptionsId);
                    }

                });
            } else {
                MFA_OPTION_SERVICE.delete(scopeId, mfaCredentialOptionsId);
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public GwtMfaCredentialOptions find(String scopeIdStr, String mfaCredentialOptionsIdStr, boolean selfManagement) throws GwtKapuaException {
        try {
            final KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeIdStr);
            final KapuaId mfaCredentialOptionsId = GwtKapuaCommonsModelConverter.convertKapuaId(mfaCredentialOptionsIdStr);
            MfaOption mfaCredentialOption;
            if (selfManagement) {
                mfaCredentialOption = KapuaSecurityUtils.doPrivileged(new Callable<MfaOption>() {

                    @Override
                    public MfaOption call() throws Exception {
                        return MFA_OPTION_SERVICE.find(scopeId, mfaCredentialOptionsId);
                    }

                });
            } else {
                mfaCredentialOption = MFA_OPTION_SERVICE.find(scopeId, mfaCredentialOptionsId);
            }
            return mfaCredentialOption != null ? KapuaGwtAuthenticationModelConverter.convertMfaCredentialOptions(mfaCredentialOption) : null;
        } catch (Exception ex) {
            KapuaExceptionHandler.handle(ex);
            return null;
        }
    }

    @Override
    public GwtMfaCredentialOptions findByUserId(String scopeIdStr, String userIdStr, boolean selfManagement) throws GwtKapuaException {
        try {
            final KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeIdStr);
            final KapuaId userId = GwtKapuaCommonsModelConverter.convertKapuaId(userIdStr);
            MfaOption mfaCredentialOption;
            if (selfManagement) {
                mfaCredentialOption = KapuaSecurityUtils.doPrivileged(new Callable<MfaOption>() {

                    @Override
                    public MfaOption call() throws Exception {
                        return MFA_OPTION_SERVICE.findByUserId(scopeId, userId);
                    }

                });
            } else {
                mfaCredentialOption = MFA_OPTION_SERVICE.findByUserId(scopeId, userId);
            }
            return mfaCredentialOption != null ? KapuaGwtAuthenticationModelConverter.convertMfaCredentialOptions(mfaCredentialOption) : null;
        } catch (Exception ex) {
            KapuaExceptionHandler.handle(ex);
            return null;
        }
    }

    @Override
    public GwtMfaCredentialOptions create(GwtXSRFToken xsrfToken, GwtMfaCredentialOptionsCreator gwtMfaCredentialOptionsCreator, boolean selfManagement) throws GwtKapuaException {
        //
        // Checking XSRF token
        checkXSRFToken(xsrfToken);

        //
        // Do create
        GwtMfaCredentialOptions gwtMfaCredentialOptions = null;
        try {
            // Convert from GWT Entity
            final MfaOptionCreator mfaCredentialOptionCreator = GwtKapuaAuthenticationModelConverter.convertMfaCredentialOptionsCreator(gwtMfaCredentialOptionsCreator);
            MfaOption mfaCredentialOptions;
            // Create
            if (selfManagement) {
                mfaCredentialOptions = KapuaSecurityUtils.doPrivileged(new Callable<MfaOption>() {

                    @Override
                    public MfaOption call() throws Exception {
                        return MFA_OPTION_SERVICE.create(mfaCredentialOptionCreator);
                    }

                });
            } else {
                mfaCredentialOptions = MFA_OPTION_SERVICE.create(mfaCredentialOptionCreator);
            }
            gwtMfaCredentialOptions = KapuaGwtAuthenticationModelConverter.convertMfaCredentialOptions(mfaCredentialOptions);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        //
        // Return result
        return gwtMfaCredentialOptions;
    }

    @Override
    public void disableTrust(GwtXSRFToken gwtXsrfToken, String scopeIdStr, String mfaCredentialOptionsIdStr, boolean selfManagement) throws GwtKapuaException {
        checkXSRFToken(gwtXsrfToken);

        final KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeIdStr);
        final KapuaId mfaCredentialOptionsId = GwtKapuaCommonsModelConverter.convertKapuaId(mfaCredentialOptionsIdStr);

        try {
            if (selfManagement) {
                KapuaSecurityUtils.doPrivileged(new ThrowingRunnable() {

                    @Override
                    public void run() throws Exception {
                        MFA_OPTION_SERVICE.disableTrust(scopeId, mfaCredentialOptionsId);
                    }

                });
            } else {
                MFA_OPTION_SERVICE.disableTrust(scopeId, mfaCredentialOptionsId);
            }
        } catch (Exception ex) {
            KapuaExceptionHandler.handle(ex);
        }
    }

}

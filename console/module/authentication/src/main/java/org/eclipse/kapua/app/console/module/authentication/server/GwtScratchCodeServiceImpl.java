/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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

import java.util.List;
import java.util.concurrent.Callable;

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtScratchCode;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtScratchCodeService;
import org.eclipse.kapua.app.console.module.authentication.shared.util.KapuaGwtAuthenticationModelConverter;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeFactory;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeService;

public class GwtScratchCodeServiceImpl extends KapuaRemoteServiceServlet implements GwtScratchCodeService {

    private static final long serialVersionUID = 7323313459749361320L;

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final ScratchCodeService SCRATCH_CODE_SERVICE = LOCATOR.getService(ScratchCodeService.class);
    private static final ScratchCodeFactory SCRATCH_CODE_FACTORY = LOCATOR.getFactory(ScratchCodeFactory.class);

    @Override
    public List<GwtScratchCode> findByMfaCredentialOptionsId(String scopeIdStr, String mfaCredentialOptionsIdStr, boolean selfManagement) throws GwtKapuaException {
        try {
            final KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeIdStr);
            final KapuaId mfaCredentialOptionsId = GwtKapuaCommonsModelConverter.convertKapuaId(mfaCredentialOptionsIdStr);
            ScratchCodeListResult scratchCodeListResult;
            if (selfManagement) {
                scratchCodeListResult = KapuaSecurityUtils.doPrivileged(new Callable<ScratchCodeListResult>() {

                    @Override
                    public ScratchCodeListResult call() throws Exception {
                        return SCRATCH_CODE_SERVICE.findByMfaOptionId(scopeId, mfaCredentialOptionsId);
                    }

                });
            } else {
                scratchCodeListResult = SCRATCH_CODE_SERVICE.findByMfaOptionId(scopeId, mfaCredentialOptionsId);
            }
            return scratchCodeListResult != null ? KapuaGwtAuthenticationModelConverter.convertScratchCodeListResult(scratchCodeListResult) : null;
        } catch (Exception ex) {
            KapuaExceptionHandler.handle(ex);
            return null;
        }
    }

    @Override
    public List<GwtScratchCode> createScratchCodes(GwtXSRFToken xsrfToken, String scopeIdStr, String mfaCredentialOptionsIdStr, boolean selfManagement) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        final KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeIdStr);
        final KapuaId mfaCredentialOptionsId = GwtKapuaCommonsModelConverter.convertKapuaId(mfaCredentialOptionsIdStr);
        final ScratchCodeCreator scratchCodeCreator = SCRATCH_CODE_FACTORY.newCreator(scopeId, mfaCredentialOptionsId, null);
        try {
            if (selfManagement) {
                return KapuaGwtAuthenticationModelConverter.convertScratchCodeListResult(KapuaSecurityUtils.doPrivileged(new Callable<ScratchCodeListResult>() {

                    @Override
                    public ScratchCodeListResult call() throws Exception {
                        return SCRATCH_CODE_SERVICE.createAllScratchCodes(scratchCodeCreator);
                    }

                }));
            } else {
                return KapuaGwtAuthenticationModelConverter.convertScratchCodeListResult(SCRATCH_CODE_SERVICE.createAllScratchCodes(scratchCodeCreator));
            }
        } catch (Exception ex) {
            KapuaExceptionHandler.handle(ex);
            return null;
        }
    }

}

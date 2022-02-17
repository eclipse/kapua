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
package org.eclipse.kapua.app.console.module.authentication.shared.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.shared.util.KapuaGwtCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtMfaCredentialOptions;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtSubjectType;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCode;
import org.eclipse.kapua.service.user.User;

public class KapuaGwtAuthenticationModelConverter {

    private KapuaGwtAuthenticationModelConverter() { }

    public static GwtCredential convertCredential(Credential credential, User user) {
        GwtCredential gwtCredential = convertCredential(credential);
        if (user != null) {
            gwtCredential.setUsername(user.getName());
        }
        return gwtCredential;
    }

    public static GwtCredential convertCredential(Credential credential) {
        GwtCredential gwtCredential = new GwtCredential();
        KapuaGwtCommonsModelConverter.convertUpdatableEntity(credential, gwtCredential);
        gwtCredential.setUserId(credential.getUserId().toCompactId());
        gwtCredential.setCredentialType(credential.getCredentialType().toString());
        // DO NOT SET CredentialKey, otherwise it will show up when inspecting network traffic!
        // See https://github.com/eclipse/kapua/issues/2024
        gwtCredential.setCredentialStatus(credential.getStatus().toString());
        gwtCredential.setExpirationDate(credential.getExpirationDate());
        gwtCredential.setLoginFailures(credential.getLoginFailures());
        gwtCredential.setFirstLoginFailure(credential.getFirstLoginFailure());
        gwtCredential.setLoginFailuresReset(credential.getLoginFailuresReset());
        gwtCredential.setLockoutReset(credential.getLockoutReset());
        gwtCredential.setSubjectType(GwtSubjectType.USER.toString());
        return gwtCredential;
    }

    public static GwtMfaCredentialOptions convertMfaCredentialOptions(MfaOption mfaCredentialOptions) {
        GwtMfaCredentialOptions gwtMfaCredentialOptions = new GwtMfaCredentialOptions();
        KapuaGwtCommonsModelConverter.convertEntity(mfaCredentialOptions, gwtMfaCredentialOptions);
        gwtMfaCredentialOptions.setAuthenticationKey(mfaCredentialOptions.getMfaSecretKey());
        gwtMfaCredentialOptions.setTrustKey(mfaCredentialOptions.getTrustKey());
        gwtMfaCredentialOptions.setTrustExpirationDate(mfaCredentialOptions.getTrustExpirationDate());
        gwtMfaCredentialOptions.setQRCodeImage(mfaCredentialOptions.getQRCodeImage());
        gwtMfaCredentialOptions.setScratchCodes(convertScratchCodeListResult(mfaCredentialOptions.getScratchCodes()));
        return gwtMfaCredentialOptions;
    }

    public static List<String> convertScratchCodeListResult(List<ScratchCode> scratchCodes) {
        List<String> scratchCodeList = new ArrayList<String>();
        if (scratchCodes != null && !scratchCodes.isEmpty()) {
            for (ScratchCode scratchCode : scratchCodes) {
                scratchCodeList.add(scratchCode.getCode());
            }
        }
        return scratchCodeList;
    }
}

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
package org.eclipse.kapua.app.console.module.authentication.shared.util;

import org.eclipse.kapua.app.console.module.api.shared.util.KapuaGwtCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtSubjectType;
import org.eclipse.kapua.service.authentication.credential.Credential;
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
        gwtCredential.setCredentialKey(credential.getCredentialKey());
        gwtCredential.setCredentialStatus(credential.getStatus().toString());
        gwtCredential.setExpirationDate(credential.getExpirationDate());
        gwtCredential.setLoginFailures(credential.getLoginFailures());
        gwtCredential.setFirstLoginFailure(credential.getFirstLoginFailure());
        gwtCredential.setLoginFailuresReset(credential.getLoginFailuresReset());
        gwtCredential.setLockoutReset(credential.getLockoutReset());
        gwtCredential.setSubjectType(GwtSubjectType.USER.toString());
        return gwtCredential;
    }
}

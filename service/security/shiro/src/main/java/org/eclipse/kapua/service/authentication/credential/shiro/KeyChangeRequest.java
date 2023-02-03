/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.service.authentication.credential.Credential;

public class KeyChangeRequest {
    private Credential credential;
    private String newCredentialKey;


    public Credential getCredential() {
        return credential;
    }


    public void setCredential(Credential credential) {
        this.credential = credential;
    }


    public String getNewCredentialKey() {
        return newCredentialKey;
    }


    public void setNewCredentialKey(String newCredentialKey) {
        this.newCredentialKey = newCredentialKey;
    }
}

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
package org.eclipse.kapua.service.authentication.user.shiro;

import org.eclipse.kapua.service.authentication.user.PasswordChangeRequest;

public class PasswordChangeRequestImpl implements PasswordChangeRequest {
    private String newPassword;
    private String currentPassword;


    @Override
    public String getCurrentPassword() {
        return currentPassword;
    }


    @Override
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }


    @Override
    public String getNewPassword() {
        return newPassword;
    }


    @Override
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

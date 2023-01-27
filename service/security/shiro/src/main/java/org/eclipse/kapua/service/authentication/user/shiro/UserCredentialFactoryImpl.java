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
import org.eclipse.kapua.service.authentication.user.UserCredentialFactory;

import javax.inject.Singleton;

@Singleton
public class UserCredentialFactoryImpl implements UserCredentialFactory {
    @Override
    public PasswordChangeRequest newPasswordChangeRequest() {
        return new PasswordChangeRequestImpl();
    }
}

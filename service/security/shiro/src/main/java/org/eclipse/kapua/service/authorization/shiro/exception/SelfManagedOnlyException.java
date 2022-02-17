/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.shiro.exception;

import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionService;

/**
 * SelfManagedOnlyException is used by the {@link MfaOptionService}, when a user tries to create an {@link MfaOption} for another user.
 *
 * @since 1.4
 */
public class SelfManagedOnlyException extends KapuaAuthorizationException {

    private static final long serialVersionUID = -3116117876370203330L;

    public SelfManagedOnlyException() {
        super(KapuaAuthorizationErrorCodes.SELF_MANAGED_ONLY);
    }
}

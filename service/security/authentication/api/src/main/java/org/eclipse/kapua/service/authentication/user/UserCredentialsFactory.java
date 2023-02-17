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
package org.eclipse.kapua.service.authentication.user;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link UserCredentialsFactory} definition.
 *
 * @see org.eclipse.kapua.model.KapuaEntityFactory
 * @since 2.0.0
 */
public interface UserCredentialsFactory extends KapuaObjectFactory {
    /**
     * Instantiates a new {@link PasswordChangeRequest}.
     *
     * @return The newly instantiated {@link PasswordChangeRequest}
     * @since 2.0.0
     */
    PasswordChangeRequest newPasswordChangeRequest();


    /**
     * Instantiates a new {@link PasswordResetRequest}.
     *
     * @return The newly instantiated {@link PasswordResetRequest}
     * @since 2.0.0
     */
    PasswordResetRequest newPasswordResetRequest();
}

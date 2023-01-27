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

import com.google.inject.Module;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.authentication.user.UserCredentialService;

/**
 * {@code kapua-security-shiro} {@link Module} implementation.
 *
 * @since 2.0.0
 */
public class UserCredentialModule extends AbstractKapuaModule implements Module {
    @Override
    protected void configureModule() {
        bind(UserCredentialService.class).to(UserCredentialServiceImpl.class);
    }
}

/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.account.internal;

import com.google.inject.Module;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;

/**
 * {@code kapua-account-internal} {@link Module} implementation.
 *
 * @since 2.0.0
 */
public class AccountModule extends AbstractKapuaModule implements Module {

    @Override
    protected void configureModule() {
        bind(AccountService.class).to(AccountServiceImpl.class);
        bind(AccountFactory.class).to(AccountFactoryImpl.class);
    }
}

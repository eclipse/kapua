/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.extras.migrator;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.extras.migrator.encryption.settings.EncryptionMigrationSettings;
import org.eclipse.kapua.extras.migrator.encryption.utils.SecretAttributeMigratorModelUtils;

import javax.inject.Singleton;

public class MigratorModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(EncryptionMigrationSettings.class).in(Singleton.class);
        bind(SecretAttributeMigratorModelUtils.class).in(Singleton.class);
    }
}

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
package org.eclipse.kapua.extras.migrator.encryption.authentication;

import org.eclipse.kapua.extras.migrator.encryption.api.AbstractEntityAttributeMigrator;
import org.eclipse.kapua.extras.migrator.encryption.api.EntitySecretAttributeMigrator;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;

public class MfaOptionAttributeMigrator extends AbstractEntityAttributeMigrator<MfaOption> implements EntitySecretAttributeMigrator<MfaOption> {

    private static final MfaOptionMigratorServiceImpl MFA_OPTION_MIGRATOR_SERVICE = new MfaOptionMigratorServiceImpl();

    public MfaOptionAttributeMigrator() {
        super(MFA_OPTION_MIGRATOR_SERVICE);
    }

    @Override
    public String getEntityName() {
        return MfaOption.TYPE;
    }

    @Override
    protected KapuaQuery newEntityQuery() {
        return new MfaOptionMigratorQueryImpl();
    }
}

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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.extras.migrator.encryption.api.EntitySecretAttributeMigrator;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.query.SortOrder;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;

import java.util.List;

public class MfaOptionAttributeMigrator implements EntitySecretAttributeMigrator<MfaOption> {

    private static final MfaOptionMigratorServiceImpl MFA_OPTION_MIGRATOR_SERVICE = new MfaOptionMigratorServiceImpl();

    @Override
    public String getEntityName() {
        return MfaOption.TYPE;
    }

    @Override
    public void migrate(List<MfaOption> entitiesToMigrate) throws KapuaException {
        for (MfaOption mfaOption : entitiesToMigrate) {
            MFA_OPTION_MIGRATOR_SERVICE.update(mfaOption);
        }
    }

    @Override
    public List<MfaOption> getChunk(int offset, int limit) throws KapuaException {
        MfaOptionMigratorQueryImpl query = new MfaOptionMigratorQueryImpl(null);

        // This is the most stable sorting even if it is not always indexed
        query.setSortCriteria(query.fieldSortCriteria(KapuaEntityAttributes.CREATED_ON, SortOrder.ASCENDING));

        query.setOffset(offset);
        query.setLimit(limit);

        return MFA_OPTION_MIGRATOR_SERVICE.query(query).getItems();
    }

    @Override
    public long getTotalCount() throws KapuaException {
        MfaOptionMigratorQueryImpl query = new MfaOptionMigratorQueryImpl(null);

        return MFA_OPTION_MIGRATOR_SERVICE.count(query);
    }
}

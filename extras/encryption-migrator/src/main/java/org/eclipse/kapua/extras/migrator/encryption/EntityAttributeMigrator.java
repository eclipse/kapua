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
package org.eclipse.kapua.extras.migrator.encryption;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.extras.migrator.encryption.api.EntitySecretAttributeMigrator;
import org.eclipse.kapua.extras.migrator.encryption.authentication.MfaOptionAttributeMigrator;
import org.eclipse.kapua.extras.migrator.encryption.job.JobStepAttributeMigrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class EntityAttributeMigrator {

    private static final Logger LOG = LoggerFactory.getLogger(EntityAttributeMigrator.class);

    private static final int PAGE_SIZE = 50;

    protected EntityAttributeMigrator() {
    }

    public void migrate() throws KapuaException {
        List<EntitySecretAttributeMigrator<?>> entitySecretAttributeMigrators = getEntityMigrators();

        LOG.info("Found {} entity migrators:", entitySecretAttributeMigrators.size());
        for (EntitySecretAttributeMigrator<?> entityAttributeMigrator : entitySecretAttributeMigrators) {
            LOG.info("    + {} for entity {}", entityAttributeMigrator.getClass(), entityAttributeMigrator.getEntityName());
        }
        LOG.info("");

        for (EntitySecretAttributeMigrator<?> entityAttributeMigrator : entitySecretAttributeMigrators) {
            LOG.info("Migrating: {}...", entityAttributeMigrator.getEntityName());

            long totalEntitiesToMigrate = entityAttributeMigrator.getTotalCount();

            LOG.info("    Entities to migrate: {}", totalEntitiesToMigrate);
            for (int i = 0; i < totalEntitiesToMigrate; i = i + PAGE_SIZE) {
                LOG.info("        Migrating entities from {} to {} - total {}", i, i + PAGE_SIZE, totalEntitiesToMigrate);
                List entitiesToMigrate = entityAttributeMigrator.getChunk(i, PAGE_SIZE);

                entityAttributeMigrator.migrate(entitiesToMigrate);
            }
            LOG.info("    Entities migrated: {}", totalEntitiesToMigrate);
            LOG.info("Migrating: {}... DONE!", entityAttributeMigrator.getEntityName());
            LOG.info("");
        }
    }

    public List<EntitySecretAttributeMigrator<?>> getEntityMigrators() {
        return Arrays.asList(
                new JobStepAttributeMigrator(),
                new MfaOptionAttributeMigrator()
        );
    }
}

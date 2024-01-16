/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.domain.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.populators.DataPopulator;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainRepository;
import org.eclipse.kapua.storage.TxManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DomainsPopulator implements DataPopulator {
    private final TxManager txManager;
    private final DomainRepository domainRepository;
    private final Set<Domain> knownDomains;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    public DomainsPopulator(TxManager txManager, DomainRepository domainRepository,
                            Set<Domain> knownDomains) {
        this.txManager = txManager;
        this.domainRepository = domainRepository;
        this.knownDomains = knownDomains;
    }

    @Override
    public void populate() {
        logger.info("Domain alignment commencing. Found {} domain declarations in wiring", knownDomains.size());
        final Map<String, Domain> knownDomainsByName = knownDomains
                .stream()
                .collect(Collectors.toMap(d -> d.getName(), d -> d));
        final List<String> declaredOnlyNames = new ArrayList<>(knownDomainsByName.keySet());
        try {
            KapuaSecurityUtils.doPrivileged(() -> {
                txManager.execute(tx -> {
                    final List<org.eclipse.kapua.service.authorization.domain.Domain> dbDomainEntries = domainRepository.query(tx, new DomainQueryImpl()).getItems();
                    logger.info("Found {} domain declarations in database", dbDomainEntries.size());
                    final List<org.eclipse.kapua.service.authorization.domain.Domain> dbOnlyNames = new ArrayList<>();

                    for (final org.eclipse.kapua.service.authorization.domain.Domain dbDomainEntry : dbDomainEntries) {
                        if (!knownDomainsByName.containsKey(dbDomainEntry.getName())) {
                            dbOnlyNames.add(dbDomainEntry);
                            logger.warn("Domain '{}' is only present in the database but has no current declaration! Details: {}", dbDomainEntry.getName(), dbDomainEntry.getDomain());
                            continue;
                        }
                        declaredOnlyNames.remove(dbDomainEntry.getName());
                        //Trigger fetch from db
                        dbDomainEntry.getActions();
                        if (dbDomainEntry.getDomain().equals(knownDomainsByName.get(dbDomainEntry.getName()))) {
                            logger.debug("Domain '{}' is ok: {}", dbDomainEntry.getName(), dbDomainEntry.getDomain());
                        } else {
                            logger.error("Domain  mismatch for name '{}'! Details:" +
                                    "\n\tDb entry: '{}', " +
                                    "\n\texpected: '{}'", dbDomainEntry.getName(), dbDomainEntry.getDomain(), knownDomainsByName.get(dbDomainEntry.getName()));
                        }
                    }
                    if (declaredOnlyNames.size() > 0) {
                        logger.info("Found {} declared domains that have no counterpart in the database!", declaredOnlyNames.size());
                    }
                    for (final String declaredOnlyName : declaredOnlyNames) {
                        final Domain expected = knownDomainsByName.get(declaredOnlyName);
                        logger.info("To be added: {}", expected);
                        final org.eclipse.kapua.service.authorization.domain.Domain newEntity = new DomainImpl();
                        newEntity.setName(expected.getName());
                        newEntity.setActions(expected.getActions());
                        newEntity.setGroupable(expected.getGroupable());
                        newEntity.setServiceName(expected.getServiceName());
//Conflict with some domains created by liquibase only in certain subprojects, (e.g.: broker domain created only in authorization service app.
// If the populator is run by other components, it creates the domain first and the liquibase script fails
                        //                        domainRepository.create(tx, newEntity);
                    }
                    logger.info("Domain alignment complete!");
                    return null;
                });
            });
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }
}

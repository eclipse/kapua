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
package org.eclipse.kapua.broker.artemis.plugin.security.context;

import java.util.List;

import org.apache.activemq.artemis.core.config.WildcardConfiguration;
import org.apache.activemq.artemis.core.settings.HierarchicalRepository;
import org.apache.activemq.artemis.core.settings.impl.HierarchicalObjectRepository;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.client.security.bean.AuthAcl;
import org.eclipse.kapua.client.security.bean.AuthAcl.Action;
import org.eclipse.kapua.client.security.metric.LoginMetric;
import org.eclipse.kapua.service.authentication.KapuaPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Acl {

    private static Logger logger = LoggerFactory.getLogger(Acl.class);

    private static final WildcardConfiguration WILDCARD_CONFIGURATION;
    //TODO inject!
    private static final LoginMetric LOGIN_METRIC = LoginMetric.getInstance();

    static {
        WILDCARD_CONFIGURATION = new WildcardConfiguration();
        WILDCARD_CONFIGURATION.setSingleWord('+');
        WILDCARD_CONFIGURATION.setAnyWords('#');
        WILDCARD_CONFIGURATION.setDelimiter('/');
    }

    private HierarchicalRepository<KapuaPrincipal> read;
    private HierarchicalRepository<KapuaPrincipal> write;
    private HierarchicalRepository<KapuaPrincipal> admin;

    public Acl(KapuaPrincipal principal, List<AuthAcl> authAcls) throws KapuaIllegalArgumentException {
        if (principal==null) {
            throw new KapuaIllegalArgumentException("principal", null);
        }
        read = new HierarchicalObjectRepository<>(WILDCARD_CONFIGURATION);
        read.setDefault(null);
        write = new HierarchicalObjectRepository<>(WILDCARD_CONFIGURATION);
        write.setDefault(null);
        admin = new HierarchicalObjectRepository<>(WILDCARD_CONFIGURATION);
        admin.setDefault(null);
        StringBuilder aclLog = new StringBuilder();
        if (authAcls!=null) {
            authAcls.forEach((authAcl) -> {
                try {
                    add(principal, authAcl.getMatch(), authAcl.getAction());
                    aclLog.append("\n\t").append(authAcl.getMatch()).append(" - ").append(authAcl.getAction()).append(" - ").
                        append(principal.getName()).append("/").append(principal.getAccountId().toStringId()).append("/").append(principal.getClientId());
                } catch (Exception e) {
                    LOGIN_METRIC.getAclCreationFailure().inc();
                    //no security issue since in case of error no acl is added
                    logger.error("Error adding acl {}", authAcl, e);
                }
            });
        }
        logger.info("Acl: {}", aclLog);
    }

    private void add(KapuaPrincipal principal, String match, Action action) throws KapuaIllegalArgumentException {
        if (action==null) {
            throw new KapuaIllegalArgumentException("action", null);
        }
        if (principal==null) {
            throw new KapuaIllegalArgumentException("principal", null);
        }
        if (match==null || match.trim().length()<=0) {
            throw new KapuaIllegalArgumentException("match", match);
        }
        switch (action) {
        case all:
            read.addMatch(match, principal);
            write.addMatch(match, principal);
            admin.addMatch(match, principal);
            break;
        case read:
            read.addMatch(match, principal);
            break;
        case write:
            write.addMatch(match, principal);
            break;
        case admin:
            admin.addMatch(match, principal);
            break;
        case readAdmin:
            read.addMatch(match, principal);
            admin.addMatch(match, principal);
            break;
        case writeAdmin:
            write.addMatch(match, principal);
            admin.addMatch(match, principal);
            break;
        }
    }

    public boolean canRead(KapuaPrincipal principal, String address) {
        return principal.equals(read.getMatch(address));
    }

    public boolean canWrite(KapuaPrincipal principal, String address) {
        return principal.equals(write.getMatch(address));
    }

    public boolean canManage(KapuaPrincipal principal, String address) {
        return principal.equals(admin.getMatch(address));
    }

}

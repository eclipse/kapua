/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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

import org.apache.activemq.artemis.core.settings.HierarchicalRepository;
import org.apache.activemq.artemis.core.settings.impl.HierarchicalObjectRepository;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.client.security.bean.AuthResponse;
import org.eclipse.kapua.client.security.bean.AuthAcl;
import org.eclipse.kapua.client.security.bean.AuthAcl.Action;
import org.eclipse.kapua.service.authentication.KapuaPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Acl {

    private static Logger logger = LoggerFactory.getLogger(Acl.class);

    private HierarchicalRepository<KapuaPrincipal> read;
    private HierarchicalRepository<KapuaPrincipal> write;
    private HierarchicalRepository<KapuaPrincipal> admin;

    public Acl(KapuaPrincipal principal, AuthResponse authResponse) throws KapuaIllegalArgumentException {
        if (principal==null) {
            throw new KapuaIllegalArgumentException("principal", null);
        }
        read = new HierarchicalObjectRepository<>();
        read.setDefault(null);
        write = new HierarchicalObjectRepository<>();
        write.setDefault(null);
        admin = new HierarchicalObjectRepository<>();
        admin.setDefault(null);
        List<AuthAcl> authAcls = authResponse.getAcls();
        StringBuilder aclLog = new StringBuilder();
        if (authAcls!=null) {
            authAcls.forEach((authAcl) -> {
                try {
                    add(principal, authAcl.getMatch(), authAcl.getAction());
                    aclLog.append(authAcl.getMatch()).append(" - ").append(authAcl.getAction()).append(" - ").append(principal.getName()).append("\n");
                } catch (Exception e) {
                    //TODO add metric
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

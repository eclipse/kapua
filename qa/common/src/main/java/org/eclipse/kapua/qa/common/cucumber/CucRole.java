/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.common.cucumber;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class CucRole {

    private String name;
    private Integer scopeId;
    private String actions;
    private KapuaId id;
    private Set<Actions> actionSet;

    public void doParse() {
        if (scopeId != null) {
            id = new KapuaEid(BigInteger.valueOf(scopeId));
        }
        if (actions != null) {
            String tmpAct = actions.trim().toLowerCase();
            if (tmpAct.length() != 0) {
                actionSet = new HashSet<>();
                String[] tmpList = actions.split(",");

                for (String tmpS : tmpList) {
                    switch (tmpS.trim().toLowerCase()) {
                    case "read":
                        actionSet.add(Actions.read);
                        break;
                    case "write":
                        actionSet.add(Actions.write);
                        break;
                    case "delete":
                        actionSet.add(Actions.delete);
                        break;
                    case "connect":
                        actionSet.add(Actions.connect);
                        break;
                    case "execute":
                        actionSet.add(Actions.execute);
                        break;
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScopeId(KapuaId id) {
        this.id = id;
    }

    public KapuaId getScopeId() {
        return id;
    }

    public Set<Actions> getActions() {
        return actionSet;
    }

    public void setActions(Set<Actions> actions) {
        actionSet = actions;
    }
}

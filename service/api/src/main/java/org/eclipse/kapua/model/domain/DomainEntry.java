/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.model.domain;

import java.util.EnumSet;

public class DomainEntry implements Domain {
    private final String name;
    private final boolean groupable;
    private final EnumSet<Actions> actions;

    public DomainEntry(String name, boolean groupable, Actions action, Actions... actions) {
        this.name = name;
        this.groupable = groupable;
        this.actions = EnumSet.of(action, actions);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean getGroupable() {
        return groupable;
    }

    @Override
    public EnumSet<Actions> getActions() {
        return actions;
    }
}

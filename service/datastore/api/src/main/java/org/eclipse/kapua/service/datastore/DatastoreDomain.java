/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore;

import org.eclipse.kapua.model.domain.AbstractDomain;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link Domain} for:
 *
 * <ul>
 *     <li>{@link ChannelInfoRegistryService}</li>
 *     <li>{@link ClientInfoRegistryService}</li>
 *     <li>{@link MessageStoreService} </li>
 *     <li>{@link MetricInfoRegistryService}</li>
 * </ul>
 *
 * @since 1.0.0
 */
public class DatastoreDomain extends AbstractDomain {

    private String name = "datastore";
    private Set<Actions> actions = new HashSet<>(Arrays.asList(Actions.read, Actions.delete, Actions.write));

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<Actions> getActions() {
        return actions;
    }

    @Override
    public boolean getGroupable() {
        return false;
    }
}

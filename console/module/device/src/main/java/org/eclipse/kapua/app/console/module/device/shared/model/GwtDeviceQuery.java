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
package org.eclipse.kapua.app.console.module.device.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;

public class GwtDeviceQuery extends GwtQuery {

    private static final long serialVersionUID = 7269983474348658584L;

    private GwtDeviceQueryPredicates predicates;

    public GwtDeviceQuery() {
        super();

        setPredicates(new GwtDeviceQueryPredicates());
    }

    public GwtDeviceQuery(String scopeId) {
        this();
        setScopeId(scopeId);
    }

    public GwtDeviceQueryPredicates getPredicates() {
        return predicates;
    }

    public void setPredicates(GwtDeviceQueryPredicates predicates) {
        this.predicates = predicates;
    }
}

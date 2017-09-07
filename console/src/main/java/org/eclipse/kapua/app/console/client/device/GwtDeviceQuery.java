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
package org.eclipse.kapua.app.console.client.device;

import org.eclipse.kapua.app.console.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.shared.model.query.GwtQuery;

public class GwtDeviceQuery extends GwtQuery {

    private static final long serialVersionUID = 7269983474348658584L;

    private GwtDeviceQueryPredicates predicates;

    public GwtDeviceQuery() {
        super();
    }

    public GwtDeviceQuery(String scopeId) {
        super();
        setScopeId(scopeId);
        setPredicates(new GwtDeviceQueryPredicates());
    }

    public GwtDeviceQueryPredicates getPredicates() {
        return predicates;
    }

    public void setPredicates(GwtDeviceQueryPredicates predicates) {
        this.predicates = predicates;
    }
}

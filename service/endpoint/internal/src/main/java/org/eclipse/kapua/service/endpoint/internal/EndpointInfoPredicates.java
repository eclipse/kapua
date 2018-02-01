/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.endpoint.internal;

import org.eclipse.kapua.model.KapuaUpdatableEntityPredicates;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;
import org.eclipse.kapua.service.endpoint.EndpointInfo;

/**
 * {@link KapuaQuery} {@link KapuaPredicate} name for {@link EndpointInfo} entity.
 *
 * @since 1.0.0
 */
public interface EndpointInfoPredicates extends KapuaUpdatableEntityPredicates {

    public static final String SCHEMA = "schema";
    /**
     * {@link EndpointInfo} DNS
     */
    public static final String DNS = "dns";

    public static final String PORT = "port";

    public static final String USAGES = "usages";

    public static final String SECURE = "secure";

}
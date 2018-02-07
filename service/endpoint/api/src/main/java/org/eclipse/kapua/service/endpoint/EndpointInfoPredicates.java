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
package org.eclipse.kapua.service.endpoint;

import org.eclipse.kapua.model.KapuaUpdatableEntityPredicates;

public interface EndpointInfoPredicates extends KapuaUpdatableEntityPredicates {

    public static final String SCHEMA = "schema";
    public static final String DNS = "dns";
    public static final String PORT = "port";
    public static final String SECURE = "secure";
    public static final String USAGES = "usages";

}
